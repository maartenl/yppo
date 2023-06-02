package com.mrbear.yppo.entities;

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
}
