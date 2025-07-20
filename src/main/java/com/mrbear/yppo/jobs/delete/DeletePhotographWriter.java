package com.mrbear.yppo.jobs.delete;

import com.mrbear.yppo.entities.GalleryPhotograph;
import com.mrbear.yppo.entities.LogLevel;
import com.mrbear.yppo.entities.Photograph;
import com.mrbear.yppo.services.LogService;
import com.mrbear.yppo.services.PhotoService;
import jakarta.batch.api.chunk.AbstractItemWriter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.logging.Logger;

@Named
public class DeletePhotographWriter extends AbstractItemWriter
{

    private static final Logger LOGGER = Logger.getLogger(DeletePhotographWriter.class.getName());

    @PersistenceContext(unitName = "yppo")
    private EntityManager entityManager;

    @Inject
    private PhotoService photoService;

    @Inject
    private LogService logService;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public void writeItems(List<Object> items) throws Exception
    {
        items.forEach(item -> removePhotograph((Long) item));
    }

    private void removePhotograph(Long pk)
    {
        Photograph photograph = photoService.getPhotograph(pk).orElseThrow(() -> new RuntimeException("Photograph " + pk + " not found."));
        for (GalleryPhotograph galleryPhotograph : photoService.getGalleryPhotographsUsed(photograph))
        {
            String format = String.format("Removing GalleryPhotograph %s (refers to photograph %s that no longer exists)", galleryPhotograph.getId(), photograph.getId());
            LOGGER.severe(format);
            logService.createLog("deletePhotograph", format, null, LogLevel.WARNING);
            entityManager.remove(galleryPhotograph);
        }
        String format = String.format("Removing Photograph %s that no longer exists (at path %s)", photograph.getId(), photograph.getFullPath());
        LOGGER.severe(format);
        logService.createLog("deletePhotograph", format, null, LogLevel.WARNING);
        entityManager.remove(photograph);
    }
}
