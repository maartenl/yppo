package com.mrbear.yppo.jobs.verify;


import jakarta.batch.api.chunk.AbstractItemWriter;
import jakarta.inject.Named;

import java.util.List;

@Named
public class VerifyPhotographWriter extends AbstractItemWriter
{

    @Override
    public void writeItems(List<Object> list) throws Exception
    {
        //empty, because it doesn't need to do anything, but it fails if the writer is not present.
    }

}
