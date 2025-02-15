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
package com.mrbear.yppo.jobs.add;

import com.mrbear.yppo.entities.LogLevel;
import com.mrbear.yppo.entities.Photograph;
import com.mrbear.yppo.services.LogService;
import jakarta.batch.api.chunk.AbstractItemWriter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maartenl
 */
@Named
public class AddPhotographWriter extends AbstractItemWriter
{

    private static final Logger LOGGER = Logger.getLogger(AddPhotographWriter.class.getName());

    @PersistenceContext(unitName = "yppo")
    private EntityManager entityManager;

    @Inject
    private LogService logService;

    @Override
    public void open(Serializable checkpoint) throws Exception
    {
        if (checkpoint == null)
        {
            LOGGER.finest("addPhotographWriter open start");
        } else
        {
            LOGGER.finest("addPhotographWriter open restart");
        }
    }

    @Override
    public void close() throws Exception
    {
        LOGGER.finest("addPhotographWriter close");
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public void writeItems(List<Object> items) throws Exception
    {
        LOGGER.entering(this.getClass().getName(), "writeItems " + items);
        for (Object i : items)
        {
            LOGGER.log(Level.FINEST, "addPhotographWriter writeItem {0}", i);
            Photograph photograph = (Photograph) i;
            entityManager.persist(photograph);
            logService.createLog("addPhotograph", "Photograph from file " + photograph.getFullPath() + " created.", null, LogLevel.INFO);
        }
        LOGGER.exiting(this.getClass().getName(), "writeItems");
    }

}
