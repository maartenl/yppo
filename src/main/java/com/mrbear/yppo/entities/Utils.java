package com.mrbear.yppo.entities;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Utils
{
    public static final String NULL_STRING = "null";

    private Utils()
    {
        // create a private constructor to hide the public one.
    }

    public static String getValue(String value)
    {
        if (value != null && (value.isBlank() || value.equalsIgnoreCase(NULL_STRING)))
        {
            return null;
        }
        return value;
    }

    public static Long getValueAsLong(String value)
    {
        if (value != null && (value.isBlank() || value.equalsIgnoreCase(NULL_STRING)))
        {
            return null;
        }
        return Long.valueOf(value);
    }

    public static String getStacktrace(Throwable e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return e.toString();
    }
}
