package com.mrbear.yppo.jobs.delete;

import jakarta.batch.api.chunk.listener.ItemProcessListener;
import jakarta.batch.api.chunk.listener.ItemReadListener;
import jakarta.batch.api.chunk.listener.ItemWriteListener;
import jakarta.batch.api.listener.JobListener;
import jakarta.batch.api.listener.StepListener;
import jakarta.inject.Named;

import java.util.List;
import java.util.logging.Logger;

@Named("deletePhotographListener")
public class DeleteListener implements JobListener, StepListener, ItemProcessListener, ItemReadListener, ItemWriteListener
{

    private static final Logger LOGGER = Logger.getLogger(DeleteListener.class.getName());

    @Override
    public void beforeJob() throws Exception
    {
        LOGGER.severe("beforeJob " );
    }

    @Override
    public void afterJob() throws Exception
    {
        LOGGER.severe("afterJob " );

    }

    @Override
    public void beforeStep() throws Exception
    {
        LOGGER.severe("beforeStep " );

    }

    @Override
    public void afterStep() throws Exception
    {
        LOGGER.severe("afterStep " );

    }

    @Override
    public void beforeProcess(Object item) throws Exception
    {
        LOGGER.severe("beforeProcess " );
    }

    @Override
    public void afterProcess(Object item, Object result) throws Exception
    {
        LOGGER.severe("afterProcess " );
    }

    @Override
    public void onProcessError(Object item, Exception ex) throws Exception
    {
        LOGGER.throwing("listener", "onProcessError " , ex);
    }

    @Override
    public void beforeRead() throws Exception
    {
        LOGGER.severe("beforeRead " );
    }

    @Override
    public void afterRead(Object item) throws Exception
    {
        LOGGER.severe("afterRead " );
    }

    @Override
    public void onReadError(Exception ex) throws Exception
    {
        LOGGER.throwing("listener", "onReadError " , ex);
    }

    @Override
    public void beforeWrite(List<Object> items) throws Exception
    {
        LOGGER.severe("beforeWrite " );

    }

    @Override
    public void afterWrite(List<Object> items) throws Exception
    {
        LOGGER.severe("afterWrite " );

    }

    @Override
    public void onWriteError(List<Object> items, Exception ex) throws Exception
    {
        LOGGER.throwing("listener", "onWriteError " , ex);

    }
}
