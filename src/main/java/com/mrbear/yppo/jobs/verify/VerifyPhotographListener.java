package com.mrbear.yppo.jobs.verify;

import com.mrbear.yppo.jobs.Listener;
import jakarta.inject.Named;

@Named
public class VerifyPhotographListener extends Listener
{

    @Override
    protected String getName()
    {
        return "verifyPhotographListener";
    }
}
