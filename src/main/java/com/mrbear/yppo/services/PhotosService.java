package com.mrbear.yppo.services;

import com.mrbear.yppo.entities.GalleryPhotograph;
import com.mrbear.yppo.entities.Photograph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class PhotosService {

  @PersistenceContext(unitName = "yppo")
  private EntityManager entityManager;

  public List<GalleryPhotograph> getGalleryPhotographs(Long galleryId) {
    TypedQuery<GalleryPhotograph> query = entityManager.createNamedQuery("GalleryPhotograph.findAll",
        GalleryPhotograph.class).setParameter("galleryId", galleryId);
    return query.getResultList();
  }

  public Optional<Photograph> getPhotograph(GalleryPhotograph galleryPhotograph) {
    return Optional.ofNullable(entityManager.find(Photograph.class, galleryPhotograph.getPhotographId()));
  }
}
