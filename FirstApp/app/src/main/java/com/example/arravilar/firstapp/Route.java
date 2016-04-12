package com.example.arravilar.firstapp;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by Przemysław on 2016-04-12.
 */
public class Route {
    private ArrayList<Location> points = new ArrayList<>();
    private String name;
    private int pointsNum;

    public Route(String Name)
    {
        name = new String(Name);
        pointsNum = 0;
    }
    public void addPoint(Location loc)
    {
        points.add(loc);
        pointsNum++;
    }

    public int getPointsNum() {
        return pointsNum;
    }

    public String getName() {
        return name;
    }

    public Location getPoint(int num)
    {
        return points.get(num);
    }

}
