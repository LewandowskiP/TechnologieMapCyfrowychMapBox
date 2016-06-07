package com.example.arravilar.firstapp;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * Created by Przemysław on 2016-06-07.
 */
public class MyMath {
    private int precision  = 10000;
    private static MyMath myMath = null;
    private MyMath()
    {
    }
    public int mod(int x, int y)
    {
        int result = x % y;
        if (result < 0)
            result += y;
        return result;
    }
    public double round(double d) {
        double result = d * precision;
        result = Math.round(result);
        result = result / precision;
    return result;
    }
    public double round(double d,int precision) {
        double result = d * precision;
        result = Math.round(result);
        result = result / precision;
        return result;
    }
    public double distance(LatLng a, LatLng b)
    {
        double r = 6371;
        double lat = Math.toRadians(Math.abs(a.getLatitude()-b.getLatitude()));
        double lng = Math.toRadians(Math.abs(a.getLongitude()-b.getLongitude()));
        double step1 = Math.sin(lat/2)*Math.sin(lat/2)+ Math.cos(Math.toRadians(a.getLatitude())) * Math.cos(Math.toRadians(b.getLatitude())) * Math.sin(lng/2) * Math.sin(lng/2);
        double result = r * 2 * Math.atan2(Math.sqrt(step1),Math.sqrt(1-step1));

        return result;
    }
    public static MyMath getInstance()
    {
        if (myMath == null) {
            myMath = new MyMath();
        }
        return myMath;
    }

}
