package com.example.arravilar.firstapp;

/**
 * Created by Przemysław on 2016-04-12.
 */
public class GlobalValues  {

    private static GlobalValues globalValues =null;
    private boolean recordRoute;
    private static RouteList routeList;

    private GlobalValues()
    {
        recordRoute = false;
        routeList = null;
    }
    public static GlobalValues getInstance()
    {
        if (globalValues == null) {
            globalValues = new GlobalValues();
        }
        return globalValues;
    }
    public boolean getRecordRoute()
    {
        return recordRoute;
    }
    public RouteList getRouteList(){
        return routeList;
    }
    public void setRouteList(RouteList routeList)
    {
        this.routeList=routeList;
    }
    public void setRecordRoute(boolean state)
    {
        recordRoute = state;
    }
}
