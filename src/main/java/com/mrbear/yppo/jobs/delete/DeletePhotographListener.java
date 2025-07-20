package com.mrbear.yppo.jobs.delete;

import com.mrbear.yppo.jobs.Listener;
import jakarta.inject.Named;

@Named
public class DeletePhotographListener extends Listener
{

    @Override
    protected String getName()
    {
        return "deletePhotographListener";
    }
}
