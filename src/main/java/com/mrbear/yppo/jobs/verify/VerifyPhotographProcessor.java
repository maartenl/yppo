package com.mrbear.yppo.jobs.verify;

import com.mrbear.yppo.FileOperations;
import com.mrbear.yppo.entities.LogLevel;
import com.mrbear.yppo.entities.Photograph;
import com.mrbear.yppo.services.LogService;
import com.mrbear.yppo.services.PhotoService;
import jakarta.batch.api.chunk.ItemProcessor;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.logging.Logger;

@Named
public class VerifyPhotographProcessor implements ItemProcessor
{
  private static final Logger LOGGER = Logger.getLogger(VerifyPhotographProcessor.class.getName());

  @Inject
  private PhotoService photoService;

  @Inject
  private LogService logService;

  @Transactional(Transactional.TxType.REQUIRES_NEW)
  @Override
  public Object processItem(Object item) throws Exception
  {
    Long pk = (Long) item;
    Photograph photograph = photoService.getPhotograph(pk).orElseThrow(() -> new RuntimeException("Photograph " + item + " not found."));
      // assemble full path
      Path path = FileSystems.getDefault().getPath(photograph.getFullPath());
      // verify that the file exists
      if (!path.toFile().exists())
      {
          logService.createLog("verifyPhotograph", "Photograph " + photograph.getId() + ": File " + path + " does not exist.", null, LogLevel.WARNING);
          return null;
      }
      // verify if file is a file
      if (!path.toFile().isFile())
      {
          logService.createLog("verifyPhotograph", "Photograph " + photograph.getId() + ": File " + path + " is not a file.", null, LogLevel.WARNING);
          return null;
      }
      // verify if file is readable
      if (!path.toFile().canRead())
      {
          logService.createLog("verifyPhotograph", "Photograph " + photograph.getId() + ": File " + path + " cannot be read.", null, LogLevel.WARNING);
          return null;
      }
      // verify the same file fileSize
      final Long fileSize = path.toFile().length();
      final Long databaseSize = photograph.getFilesize();
      if (!fileSize.equals(databaseSize))
      {
          logService.createLog("verifyPhotograph", "Photograph " + photograph.getId() + ": File " + path + " wrong size.", "File has size " + fileSize + ", but we were expecting a size of " + databaseSize, LogLevel.WARNING);
          return null;
      }
      // verify the hash
      final String fileHash = FileOperations.computeHash(path.toFile());
      final String databaseHash = photograph.getHashstring();
      if (!fileHash.equals(databaseHash))
      {
          logService.createLog("verifyPhotograph", "Photograph " + photograph.getId() + ": File " + path + " wrong hash.", "File has hash " + fileHash + ", but we were expecting the hash " + databaseHash, LogLevel.WARNING);
          return null;
      }
    // I don't do anything with the writer, the database does not change.
    // any problems with the photos should be rectified on the filesystem by the user.
    return null;
  }
}
