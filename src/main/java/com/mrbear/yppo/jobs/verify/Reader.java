package com.mrbear.yppo.jobs.verify;


import jakarta.batch.api.chunk.AbstractItemReader;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.logging.Logger;

@Named("verifyPhotographReader")
public class Reader extends AbstractItemReader
{

    private static final Logger LOGGER = Logger.getLogger(Reader.class.getName());

    @Override
    public void open(Serializable checkpoint) throws Exception
    {
        LOGGER.severe("open " + checkpoint );
    }

    @Override
    public Object readItem() throws Exception
    {
        LOGGER.severe("readItem " );
        return null;
    }

    @Override
    public void close() throws Exception
    {
        LOGGER.severe("close ");
        super.close();
    }
}
