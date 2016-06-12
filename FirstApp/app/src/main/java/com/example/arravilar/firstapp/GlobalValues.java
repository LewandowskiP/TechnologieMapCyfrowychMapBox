package com.example.arravilar.firstapp;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;

/**
 * Created by Przemysław on 2016-04-12.
 */
public class GlobalValues  {

    private static GlobalValues globalValues =null;
    private boolean recordRoute;
    private boolean keepCentered;
    private static RouteList routeList;
    private LatLng destination;
    private LatLng start;
    private LatLng lastLocation;
    private Marker startMarker;
    private Marker destMarker;
    private ArrayList<LatLng> shortestWay;

    private GlobalValues()
    {
        recordRoute = false;
        keepCentered = true;
        routeList = null;
    }
    public static GlobalValues getInstance()
    {
        if (globalValues == null) {
            globalValues = new GlobalValues();
        }
        return globalValues;
    }

    public void setShortestWay(ArrayList<LatLng> way){shortestWay = way;}
    public ArrayList<LatLng> getShortestWay() {return shortestWay;}
    public Marker getStartMarker() {return  startMarker;}
    public void setStartMarker(Marker marker) {startMarker = marker; }
    public Marker getDestMarker(){return destMarker;}
    public void setDestMarker(Marker marker) {destMarker = marker; }
    public LatLng getDestination() { return destination;}
    public LatLng getStart() {return start;}
    public void setStart(LatLng start) {this.start = start;}
    public void setDestination(LatLng destination) {this.destination = destination;}
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
    public void setKeepCentered(boolean state){
        keepCentered = state;
    }
    public boolean getKeepCentered(){
        return keepCentered;
    }
}
