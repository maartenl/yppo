package com.mrbear.yppo.jobs.delete;

import jakarta.batch.api.chunk.ItemProcessor;
import jakarta.inject.Named;

import java.util.logging.Logger;

@Named("deletePhotographProcessor")
public class Processor implements ItemProcessor
{
    private static final Logger LOGGER = Logger.getLogger(Processor.class.getName());

    @Override
    public Object processItem(Object item) throws Exception
    {
        LOGGER.severe("processItem " + item);
        return null;
    }
}