/*
 *  Copyright (C) 2012 maartenl
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
package com.mrbear.yppo.services;

import com.mrbear.yppo.entities.Log;
import com.mrbear.yppo.entities.LogLevel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.logging.Logger;

/**
 * Service for logging to the database.
 *
 * @author maartenl
 */
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class LogService
{

    private static final Logger LOGGER = Logger.getLogger(LogService.class.getName());

    @PersistenceContext(unitName = "yppo")
    private EntityManager entityManager;

    public void createLog(String source, String message, String description, LogLevel logLevel)
    {
        LOGGER.finest("createLog");
        Log log = new Log(source, message, description, logLevel);
        entityManager.persist(log);
    }

    public int deleteAll()
    {
        LOGGER.finest("deleteAll");
        Query query = entityManager.createNamedQuery("Log.deleteAll");
        return query.executeUpdate();
    }

    public List<Log> getLog() {
        TypedQuery<Log> query = entityManager.createNamedQuery("Log.findFirstHundred", Log.class).setMaxResults(100);
        return query.getResultList();
    }
}
