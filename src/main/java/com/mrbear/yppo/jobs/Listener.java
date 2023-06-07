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
package com.mrbear.yppo.jobs;

import com.mrbear.yppo.entities.LogLevel;
import com.mrbear.yppo.entities.Utils;
import com.mrbear.yppo.services.LogService;
import jakarta.batch.api.chunk.listener.ItemProcessListener;
import jakarta.batch.api.chunk.listener.ItemReadListener;
import jakarta.batch.api.chunk.listener.ItemWriteListener;
import jakarta.batch.api.listener.JobListener;
import jakarta.batch.api.listener.StepListener;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author maartenl
 */
public abstract class Listener implements JobListener, StepListener, ItemProcessListener, ItemReadListener, ItemWriteListener
{

  private static final Logger LOGGER = Logger.getLogger(Listener.class.getName());

  @Inject
  private LogService logService;

  protected abstract String getName();

  @Override
  public void beforeJob() throws Exception
  {
    LOGGER.log(Level.INFO, "Job Started: {0}", getName());
    logService.createLog(getName(), "Job started", null, LogLevel.INFO);
  }

  @Override
  public void afterJob() throws Exception
  {
    LOGGER.log(Level.INFO, "Job Ended: {0}", getName());
    logService.createLog(getName(), "Job ended", null, LogLevel.INFO);
  }

  @Override
  public void beforeStep() throws Exception
  {
    LOGGER.log(Level.INFO, "Step Started: {0}", getName());
    logService.createLog(getName(), "Step started", null, LogLevel.INFO);
  }

  @Override
  public void afterStep() throws Exception
  {
    LOGGER.log(Level.INFO, "Step Ended: {0}", getName());
    logService.createLog(getName(), "Step ended", null, LogLevel.INFO);
  }

  @Override
  public void beforeProcess(Object item) throws Exception
  {
    LOGGER.log(Level.INFO, "beforeProcess {0}", item);
    logService.createLog(getName(), "beforeProcess " + item, null, LogLevel.INFO);
  }

  @Override
  public void afterProcess(Object item, Object result) throws Exception
  {
    LOGGER.log(Level.INFO, "afterProcess {0} {1}", new Object[]{item, result});
    logService.createLog(getName(), "afterProcess " + item + " " + result, null, LogLevel.INFO);
  }

  @Override
  public void onProcessError(Object item, Exception ex) throws Exception
  {
    LOGGER.throwing("listener", "onProcessError ", ex);
    logService.createLog(getName(), "onProcessError " + item, Utils.getStacktrace(ex), LogLevel.INFO);
  }

  @Override
  public void beforeRead() throws Exception
  {
    LOGGER.log(Level.INFO, "beforeRead ");
    logService.createLog(getName(), "beforeRead ", null, LogLevel.INFO);
  }

  @Override
  public void afterRead(Object item) throws Exception
  {
    LOGGER.log(Level.INFO, "afterRead {0}", item);
    logService.createLog(getName(), "afterRead " + item, null, LogLevel.INFO);
  }

  @Override
  public void onReadError(Exception ex) throws Exception
  {
    LOGGER.throwing("listener", "onReadError ", ex);
    logService.createLog(getName(), "onReadError ", Utils.getStacktrace(ex), LogLevel.INFO);
  }

  @Override
  public void beforeWrite(List<Object> items) throws Exception
  {
    LOGGER.log(Level.INFO, "beforeWrite {0}", items);
    logService.createLog(getName(), "beforeWrite " + items, null, LogLevel.INFO);
  }

  @Override
  public void afterWrite(List<Object> items) throws Exception
  {
    LOGGER.log(Level.INFO, "afterWrite {0}", items);
    logService.createLog(getName(), "afterWrite " + items, null, LogLevel.INFO);
  }

  @Override
  public void onWriteError(List<Object> items, Exception ex) throws Exception
  {
    LOGGER.throwing("listener", "onWriteError ", ex);
    logService.createLog(getName(), "onWriteError " + items, Utils.getStacktrace(ex), LogLevel.INFO);
  }

}
