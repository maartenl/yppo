package com.mrbear.yppo.services;

import com.mrbear.yppo.entities.Gallery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Transactional
public class GalleryService
{
    private static final Logger LOGGER = Logger.getLogger(GalleryService.class.getName());

    @PersistenceContext(unitName = "yppo")
    private EntityManager entityManager;

    public List<Gallery> getGalleries()
    {
        TypedQuery<Gallery> query = entityManager.createNamedQuery("Gallery.findAll", Gallery.class);
        return query.getResultList();
    }

    public Optional<Gallery> getGallery(Long id)
    {
        return Optional.ofNullable(entityManager.find(Gallery.class, id));
    }

    public void updateGallery(Long galleryId, String galleryName, String galleryDescription)
    {
        LOGGER.finest(String.format("Changing data on gallery %s", galleryId));
        getGallery(galleryId).ifPresent(gallery ->
        {
            gallery.setName(galleryName);
            gallery.setDescription(galleryDescription);
        });
    }
}
