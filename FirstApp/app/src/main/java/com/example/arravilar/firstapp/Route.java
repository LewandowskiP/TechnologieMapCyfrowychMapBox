package com.example.arravilar.firstapp;

import android.location.Location;
import android.util.Log;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;

/**
 * Created by Przemysław on 2016-04-12.
 */
public class Route {
    private ArrayList<LatLng> points = new ArrayList<>();
    private String name;
    private int pointsNum;

    public ArrayList<LatLng> getPoints() {
        return points;
    }

    public Route(String Name)
    {
        name = new String(new String(Name));
        Log.d("Test", "Zrobilem: " + name.toString());
        pointsNum = 0;
    }

    public void addPointI(int index, LatLng loc)
    {
        points.add(index, new LatLng(loc));
        Log.d("Test", "Zrobilem: " + loc.toString());
        pointsNum++;
    }

    public void addPoint(LatLng loc)
    {
        points.add(new LatLng(loc));
        Log.d("Test", "Zrobilem: " + loc.toString());
        pointsNum++;
    }

    public int getPointsNum() {
        return pointsNum;
    }

    public String getName() {
        return name;
    }

    public LatLng getPoint(int num)
    {
        return points.get(num);
    }

}
