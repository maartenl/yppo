package com.mrbear.yppo.jobs.delete;

import jakarta.batch.api.chunk.AbstractItemWriter;
import jakarta.inject.Named;

import java.util.List;
import java.util.logging.Logger;

@Named("deletePhotographWriter")
public class Writer extends AbstractItemWriter
{

    private static final Logger LOGGER = Logger.getLogger(Writer.class.getName());

    @Override
    public void writeItems(List<Object> items) throws Exception
    {
        LOGGER.severe("writeItems " + items);
    }
}
