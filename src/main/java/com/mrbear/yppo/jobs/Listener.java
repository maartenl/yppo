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
import com.mrbear.yppo.services.LogService;
import jakarta.batch.api.listener.JobListener;
import jakarta.batch.api.listener.StepListener;
import jakarta.inject.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maartenl
 */
public abstract class Listener implements JobListener, StepListener
{

    private static final Logger logger = Logger.getLogger(Listener.class.getName());

    @Inject
    private LogService logBean;

    protected abstract String getName();

    @Override
    public void beforeJob() throws Exception
    {
        logger.log(Level.INFO, "Job Started: {0}", getName());
        logBean.createLog(getName(), "Job Started: " + getName(), null, LogLevel.INFO);
    }

    @Override
    public void afterJob() throws Exception
    {
        logger.log(Level.INFO, "Job Ended: {0}", getName());
        logBean.createLog(getName(), "Job Ended: " + getName(), null, LogLevel.INFO);
    }

    @Override
    public void beforeStep() throws Exception
    {
        logger.log(Level.INFO, "Step Started: {0}", getName());
        logBean.createLog(getName(), "Step Started: " + getName(), null, LogLevel.INFO);
    }

    @Override
    public void afterStep() throws Exception
    {
        logger.log(Level.INFO, "Step Ended: {0}", getName());
        logBean.createLog(getName(), "Step Ended: " + getName(), null, LogLevel.INFO);
    }

}
