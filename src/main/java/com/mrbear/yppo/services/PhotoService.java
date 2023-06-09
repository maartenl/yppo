package com.mrbear.yppo.services;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import com.mrbear.yppo.entities.GalleryPhotograph;
import com.mrbear.yppo.entities.LogLevel;
import com.mrbear.yppo.entities.Photograph;
import com.mrbear.yppo.enums.ImageAngle;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Transactional
public class PhotoService
{

  private static final Logger LOGGER = Logger.getLogger(PhotoService.class.getName());

  @PersistenceContext(unitName = "yppo")
  private EntityManager entityManager;

  @Inject
  private LogService logService;

  public List<GalleryPhotograph> getGalleryPhotographs(Long galleryId)
  {
    TypedQuery<GalleryPhotograph> query = entityManager.createNamedQuery("GalleryPhotograph.findAll",
            GalleryPhotograph.class).setParameter("galleryId", galleryId);
    return query.getResultList();
  }

  public Optional<Photograph> getPhotograph(Long id)
  {
    return Optional.ofNullable(entityManager.find(Photograph.class, id));
  }

  public Optional<GalleryPhotograph> getGalleryPhotograph(Long id)
  {
    return Optional.ofNullable(entityManager.find(GalleryPhotograph.class, id));
  }

  /**
   * @param id id of a photograph
   * @return a file, if found.
   */
  public Optional<File> getFile(Long id)
  {
    Optional<Photograph> photoOpt = getPhotograph(id);
    if (photoOpt.isEmpty())
    {
      return Optional.empty();
    }
    Photograph photo = photoOpt.get();
    java.nio.file.Path newPath = FileSystems.getDefault().getPath(photo.getLocation().getFilepath(), photo.getRelativepath(), photo.getFilename());
    File file = newPath.toFile();
    return Optional.of(file);
  }

  public Optional<ImageAngle> getAngle(Long id) throws ImageProcessingException, IOException, MetadataException
  {
    Logger.getLogger(PhotoService.class.getName()).log(Level.FINE, "getAngle {0}", id);
    Photograph photo = getPhotograph(id).orElseThrow(() -> new WebApplicationException("Photograph " + id + " not found."));
    Logger.getLogger(PhotoService.class.getName()).log(Level.FINE, "getAngle returns {0}", photo.getAngle());
    return photo.getAngle();
  }

  public void updateGalleryPhotograph(Long galleryPhotographId, String galleryPhotographName, String galleryPhotographDescription, ImageAngle galleryPhotographAngle)
  {
    String logmessage = String.format("Changing data on galleryPhotograph %s to %s, %s, %s, %s",
            galleryPhotographId, galleryPhotographId, galleryPhotographName, galleryPhotographDescription, galleryPhotographAngle);
    LOGGER.finest(logmessage);
    logService.createLog("PhotoService", logmessage, null, LogLevel.INFO);
    Optional.ofNullable(entityManager.find(GalleryPhotograph.class, galleryPhotographId))
            .ifPresent(galleryPhotograph ->
            {
              galleryPhotograph.setName(galleryPhotographName);
              galleryPhotograph.setDescription(galleryPhotographDescription);
              if (galleryPhotographAngle != null)
              {
                galleryPhotograph.getPhotograph().setAngle(galleryPhotographAngle);
              }
            });
  }

  public List<Long> getAllPrimaryKeysOfPhotographs()
  {
    TypedQuery<Long> query = entityManager.createNamedQuery("Photograph.findAllPks",
            Long.class);
    return query.getResultList();
  }

  public List<GalleryPhotograph> getGalleryPhotographsUsed(Photograph photograph)
  {
    TypedQuery<GalleryPhotograph> query = entityManager.createNamedQuery("GalleryPhotograph.findByPhotograph",
            GalleryPhotograph.class).setParameter("photograph", photograph);
    return query.getResultList();
  }
}

