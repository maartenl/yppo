package com.mrbear.yppo.entities;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Utils
{
    private Utils()
    {
        // create a private constructor to hide the public one.
    }

    public static String getValue(String value)
    {
        if (value != null && (value.isBlank() || value.equals("null")))
        {
            return null;
        }
        return value;
    }

    public static String getStacktrace(Throwable e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return e.toString();
    }
}
