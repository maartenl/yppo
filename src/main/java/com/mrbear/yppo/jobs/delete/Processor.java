package com.mrbear.yppo.jobs.delete;

import com.mrbear.yppo.entities.LogLevel;
import com.mrbear.yppo.entities.Photograph;
import com.mrbear.yppo.services.LogService;
import com.mrbear.yppo.services.PhotoService;
import jakarta.batch.api.chunk.ItemProcessor;
import jakarta.batch.runtime.context.JobContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

@Named("deletePhotographProcessor")
public class Processor implements ItemProcessor
{
  private static final Logger LOGGER = Logger.getLogger(Processor.class.getName());

  @PersistenceContext(unitName = "yppo")
  private EntityManager entityManager;

  @Inject
  private JobContext jobContext;

  @Inject
  private PhotoService photoService;

  @Inject
  private LogService logService;

  private Properties jobProperties;
  private boolean dryrun;

  @Transactional(Transactional.TxType.REQUIRES_NEW)
  @Override
  public Object processItem(Object item) throws Exception
  {
    if (jobProperties == null)
    {
      jobProperties = jobContext.getProperties();
      dryrun = Objects.equals(jobProperties.getProperty("dryrun"), "true");
      String format = String.format("Dryrun = %s", dryrun);
      LOGGER.severe(format);
    }

    Long pk = (Long) item;
    Photograph photograph = photoService.getPhotograph(pk).orElseThrow(() -> new RuntimeException("Photograph " + item + " not found."));
    if (photograph.doesFileExist())
    {
      return null;
    }
    String format = String.format("File %s of photograph %s does not exist.", photograph.getFullPath(), photograph.getId());
    LOGGER.severe(format);
    logService.createLog("deletePhotograph", format, null, LogLevel.WARNING);
    if (dryrun)
    {
      return null;
    }
    return pk;
  }
}
