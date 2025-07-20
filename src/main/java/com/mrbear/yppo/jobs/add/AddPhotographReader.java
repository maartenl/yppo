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

import com.mrbear.yppo.PhotographVisitor;
import com.mrbear.yppo.entities.Location;
import com.mrbear.yppo.services.LocationService;
import jakarta.batch.api.chunk.AbstractItemReader;
import jakarta.batch.operations.JobSecurityException;
import jakarta.batch.operations.NoSuchJobExecutionException;
import jakarta.batch.runtime.BatchRuntime;
import jakarta.batch.runtime.context.JobContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author maartenl
 */
@Named
public class AddPhotographReader extends AbstractItemReader
{

    private static final Logger LOGGER = Logger.getLogger(AddPhotographReader.class.getName());

    private int index = 0;

    @Inject
    private LocationService locationService;

    @Inject
    private JobContext jobContext;

    /**
     * The files that need to be checked.
     */
    private List<Path> files;

    private Location getLocation() throws JobSecurityException, NumberFormatException, RuntimeException, NoSuchJobExecutionException
    {
        Properties jobParameters = BatchRuntime.getJobOperator().getParameters(jobContext.getExecutionId());
        // TODO : Mrbear: make it a "proper" job parameter.
        String locationIdString = (String) jobParameters.get("location");
        Long locationId = locationIdString == null ? 1L : Long.parseLong(locationIdString);
        LOGGER.log(Level.FINEST, "location id={0}", locationId);
        Location location = locationService.getLocation(locationId).orElseThrow(() -> new RuntimeException("Location with id " + locationId + " not found."));
        LOGGER.log(Level.FINEST, "location={0}", location.getFilepath());
        return location;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public void open(Serializable checkpoint) throws Exception
    {
        LOGGER.entering(this.getClass().getName(), "open");
        if (checkpoint == null)
        {
            LOGGER.finest("addPhotographReader open start");
        } else
        {
            LOGGER.finest("addPhotographReader open restart");
        }
        Location location = getLocation();

        // JDK7: Path class and walking the filetree.
        Path startingDir = FileSystems.getDefault().getPath(location.getFilepath());
        PhotographVisitor pf = new PhotographVisitor();
        Files.walkFileTree(startingDir, pf);
        files = pf.getFileList();

        LOGGER.exiting(this.getClass().getName(), "open");
    }

    @Override
    public Object readItem() throws Exception
    {
        LOGGER.finest("addPhotographReader readItem");
        if (files == null || files.isEmpty())
        {
            throw new RuntimeException("No files found.");
        }
        if (index >= files.size())
        {
            return null;
        }
        if (index < 0)
        {
            throw new RuntimeException("Negative index.");
        }
        return files.get(index++);
    }

}
