package com.mrbear.yppo.jobs.verify;

import com.mrbear.yppo.jobs.Listener;
import jakarta.inject.Named;

@Named("verifyPhotographListener")
public class VerifyListener extends Listener
{

    @Override
    protected String getName()
    {
        return "verifyPhotographListener";
    }
}
