package com.mrbear.yppo.services;

import com.mrbear.yppo.entities.Comment;
import com.mrbear.yppo.entities.Gallery;
import com.mrbear.yppo.entities.GalleryPhotograph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.logging.Logger;

@Transactional
public class CommentService
{

  private static final Logger LOGGER = Logger.getLogger(GalleryService.class.getName());

  @PersistenceContext(unitName = "yppo")
  private EntityManager entityManager;

  public List<Comment> getComments(GalleryPhotograph galleryPhotograph)
  {
    TypedQuery<Comment> query = entityManager.createNamedQuery("Comment.findByPhotograph", Comment.class).setParameter("galleryphotographid", galleryPhotograph.getId());
    return query.getResultList();
  }
}
