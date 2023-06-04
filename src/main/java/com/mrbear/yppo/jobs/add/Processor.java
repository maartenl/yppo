/*
 * Copyright (C) 2014 maartenl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mrbear.yppo.jobs.add;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import com.mrbear.yppo.FileOperations;
import com.mrbear.yppo.entities.Location;
import com.mrbear.yppo.entities.LogLevel;
import com.mrbear.yppo.entities.Photograph;
import com.mrbear.yppo.enums.ImageAngle;
import com.mrbear.yppo.images.ImageOperations;
import com.mrbear.yppo.services.LogService;
import jakarta.batch.api.chunk.ItemProcessor;
import jakarta.batch.operations.JobSecurityException;
import jakarta.batch.operations.NoSuchJobExecutionException;
import jakarta.batch.runtime.BatchRuntime;
import jakarta.batch.runtime.context.JobContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * <p>
 * Processes a new photograph, by retrieving its data and adding
 * it to the database.</p>
 */
@Named("addPhotographProcessor")
public class Processor implements ItemProcessor
{

    private static final Logger LOGGER = Logger.getLogger(Processor.class.getName());

    @Inject
    private LogService logService;

    @Inject
    private JobContext jobContext;

    @PersistenceContext(unitName = "yppo")
    private EntityManager entityManager;

    private Location getLocation() throws JobSecurityException, NumberFormatException, RuntimeException, NoSuchJobExecutionException
    {
        Properties jobParameters = BatchRuntime.getJobOperator().getParameters(jobContext.getExecutionId());
        // TODO : Mrbear: make it a "proper" job parameter.
        String locationIdString = (String) jobParameters.get("location");
        Long locationId = locationIdString == null ? 1L : Long.parseLong(locationIdString);
        LOGGER.log(Level.FINEST, "location id={0}", locationId);
        Optional<Location> location = Optional.ofNullable(entityManager.find(Location.class, locationId));
        if (location.isEmpty())
        {
            throw new RuntimeException("Location with id " + locationId + " not found.");
        }
        LOGGER.log(Level.FINEST, "location={0}", location.get().getFilepath());
        return location.get();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public Object processItem(Object item) throws Exception
    {
        LOGGER.entering(this.getClass().getName(), "addPhotographProcessor processItem " + item);
        // return null; > do not process item
        if (item == null)
        {
            return null;
        }
        Path path = (Path) item;
        Location location = getLocation();
        try
        {
            final Photograph photograph = processPhoto(location, path);
            return photograph;

        } catch (ImageProcessingException | MetadataException | IOException | NoSuchAlgorithmException e)
        {
            StringWriter stackTrace = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stackTrace);
            e.printStackTrace(printWriter);
            log("Unable to add photograph "
                    + item
                    + " to location "
                    + location.getId()
                    + ", exception " + e.getClass().getName() + " caught.", stackTrace.toString(), LogLevel.ERROR);
        }
        return null;
    }

    /**
     * @param location
     * @param path
     * @return
     * @throws NoSuchAlgorithmException            if unable to create a hash using the
     *                                             algorithm.
     * @throws ImageProcessingException            when unable to verify the image.
     * @throws com.drew.metadata.MetadataException
     */
    private Photograph processPhoto(Location location, Path path) throws NoSuchAlgorithmException, IOException, ImageProcessingException, MetadataException
    {
        LOGGER.entering(this.getClass().getName(), "processPhoto");
        if (path == null)
        {
            throw new NullPointerException();
        }
        LOGGER.log(Level.FINE, "processPhoto {0} {1}", new Object[]
                {
                        location.getFilepath(), path.toString()
                });
        Path filename = path.getFileName();
        if (filename.toString().endsWith(".mp4")) {
            LOGGER.severe(filename.toString());
        }
        Path locationPath = FileSystems.getDefault().getPath(location.getFilepath());
        Path relativePath = locationPath.relativize(path).getParent();

        // check if photo already exists in database
        Query query = entityManager.createNamedQuery("Photograph.findByFilename");
        query.setParameter("filename", filename.toString());
        query.setParameter("relativepath", relativePath.toString());
        @SuppressWarnings("unchecked")
        List<Photograph> listByFilename = query.getResultList();
        if (listByFilename != null && !listByFilename.isEmpty())
        {
            LOGGER.log(Level.FINE, "{0} already exists.", path.toString());
            log("Photograph " + path + " already exists.", null, LogLevel.INFO);
            return null;
        }
        // check if hash and filesize already exist in database
        File file = path.toFile();
        String computeHash = FileOperations.computeHash(file);
        Long size = file.length();
        query = entityManager.createNamedQuery("Photograph.findByStats");
        query.setParameter("hashstring", computeHash);
        query.setParameter("filesize", size);
        @SuppressWarnings("unchecked")
        List<Photograph> listByStats = query.getResultList();
        if (listByStats != null && !listByStats.isEmpty())
        {
            Photograph alreadyPhoto = (Photograph) listByStats.get(0);
            String result = "File with filename " + relativePath.toString() + ":" + filename.toString() + " with hash " + computeHash + " already exists with id " + alreadyPhoto.getId() + ".";
            LOGGER.warning(result);
            log(result, null, LogLevel.WARNING);
            File alreadyPhotoFile = new File(alreadyPhoto.getFullPath());
            if (!alreadyPhotoFile.exists())
            {
                String errormessage = "Photograph with id " + alreadyPhoto.getId() + " with path " + alreadyPhoto.getFullPath() + " has moved to " + path + ".";
                LOGGER.warning(errormessage);
                log(errormessage, null, LogLevel.WARNING);
                alreadyPhoto.setFilename(filename.toString());
                alreadyPhoto.setRelativepath(relativePath.toString());
                alreadyPhoto.setLocation(location);
            }
            return null;
        }
        // JDK7: lots of nio.Path calls
        Instant taken = null;
        ImageAngle angle = null;

        if (ImageOperations.isImage(path))
        {
            taken = ImageOperations.getDateTimeTaken(file).orElse(null);
            angle = ImageOperations.getAngle(file).orElse(null);
        }
        Photograph photo = new Photograph();
        photo.setFilesize(size);
        photo.setHashstring(computeHash);
        photo.setLocation(location);
        photo.setTaken(taken);
        photo.setFilename(filename.toString());
        photo.setAngle(angle);
        photo.setRelativepath(relativePath.toString());
        if (taken != null && taken.isBefore(Instant.EPOCH))
        {
            photo.setTaken(null);
            if (LOGGER.isLoggable(Level.FINE))
            {
                LOGGER.log(Level.FINE, "processPhoto cannot determine date/time! {0} {1} {2} {3}", new Object[]
                        {
                                photo.getFilename(), photo.getFilesize(), photo.getHashstring(), taken
                        });
            }
            log("Cannot determine date/time of photograph " + path, null, LogLevel.WARNING);

        } else
        {
            if (LOGGER.isLoggable(Level.FINE))
            {
                LOGGER.log(Level.FINE, "processPhoto {0} {1} {2} {3}", new Object[]
                        {
                                photo.getFilename(), photo.getFilesize(), photo.getHashstring(), photo.getTaken()
                        });
            }
            log("processPhoto " + photo.getFilename(), String.format("%s, %s, %s, %s", photo.getFilename(), photo.getFilesize(), photo.getHashstring(), photo.getTaken()), LogLevel.WARNING);
        }
        return photo;
    }

    private void log(String errormessage, String description, LogLevel logLevel)
    {
        logService.createLog("addPhotograph", errormessage, description, logLevel);
    }
}
