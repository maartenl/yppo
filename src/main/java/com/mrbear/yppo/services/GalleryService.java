package com.mrbear.yppo.services;

import com.mrbear.yppo.entities.Gallery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
public class GalleryService {

  @PersistenceContext(unitName = "yppo")
  private EntityManager entityManager;

  public List<Gallery> getGalleries() {
    TypedQuery<Gallery> query = entityManager.createNamedQuery("Gallery.findAll", Gallery.class);
    return query.getResultList();
  }
}
