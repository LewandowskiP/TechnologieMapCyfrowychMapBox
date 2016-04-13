package com.example.arravilar.firstapp;

/**
 * Created by Przemysław on 2016-04-12.
 */
public class GlobalValues {
    private static GlobalValues globalValues;
    private boolean recordRoute;
    private RouteList routeList = null;
    private GlobalValues()
    {

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
