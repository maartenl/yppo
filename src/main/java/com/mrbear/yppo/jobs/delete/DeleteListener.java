package com.mrbear.yppo.jobs.delete;

import com.mrbear.yppo.jobs.Listener;
import jakarta.batch.api.chunk.listener.ItemProcessListener;
import jakarta.batch.api.chunk.listener.ItemReadListener;
import jakarta.batch.api.chunk.listener.ItemWriteListener;
import jakarta.batch.api.listener.JobListener;
import jakarta.batch.api.listener.StepListener;
import jakarta.inject.Named;

import java.util.List;
import java.util.logging.Logger;

@Named("deletePhotographListener")
public class DeleteListener extends Listener
{

    @Override
    protected String getName()
    {
        return "deletePhotographListener";
    }
}
