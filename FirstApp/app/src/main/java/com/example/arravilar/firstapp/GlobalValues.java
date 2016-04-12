package com.example.arravilar.firstapp;

/**
 * Created by Przemysław on 2016-04-12.
 */
public class GlobalValues {
    private static GlobalValues globalValues;
    private boolean recordRoute;
    private GlobalValues()
    {

    }
    public GlobalValues getInstance()
    {
        if (globalValues == null)
            globalValues = new GlobalValues();
        return globalValues;
    }
    public boolean getRecordRoute()
    {
        return recordRoute;
    }
    public void setRecordRoute(boolean state)
    {
        recordRoute = state;
    }
}
