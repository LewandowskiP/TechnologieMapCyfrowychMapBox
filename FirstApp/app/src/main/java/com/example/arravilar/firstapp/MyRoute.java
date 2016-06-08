package com.example.arravilar.firstapp;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

import hipster.graph.GraphBuilder;
import hipster.graph.HipsterDirectedGraph;

/**
 * Created by Przemysław on 2016-06-07.
 */


public class MyRoute {
    private double distance;
    private ArrayList<LatLng> route;
    private HashMap<LatLng, ArrayList<LatLng>> myMap;


    public MyRoute() {
        distance = 0;
        route = new ArrayList<LatLng>();
        RoutesToMapConverter rtmc = new RoutesToMapConverter();
        myMap = rtmc.convert(GlobalValues.getInstance().getRouteList());
    }

    public ArrayList<LatLng> getRoute() {
        return route;
    }


    private LatLng findNearestNode(LatLng start) {
        double precision = 0.0001;
        double lat = start.getLatitude();
        double lng = start.getLongitude();
        LatLng result = new LatLng();

        if (myMap.containsKey(start)) return start;
        for (int i = 1; i < 3; i++) {
            for (int j = 0; j < 2 * i; j++) {
                result.setLongitude(lng + i * precision);
                result.setLatitude(lat + j * precision + i * precision);
                if (myMap.containsKey(result)) return result;
            }
            for (int j = 0; j < 2 * i; j++) {
                result.setLongitude(lng + i * precision);
                result.setLatitude(lat - j * precision + i * precision);
                if (myMap.containsKey(result)) return result;
            }
            for (int j = 0; j < 2 * i; j++) {
                result.setLongitude(lng + i * precision + j * precision);
                result.setLatitude(lat + i * precision);
                if (myMap.containsKey(result)) return result;
            }
            for (int j = 0; j < 2 * i; j++) {
                result.setLongitude(lng + i * precision - j * precision);
                result.setLatitude(lat + i * precision);
                if (myMap.containsKey(result)) return result;
            }
        }
        return null;
    }


    public int findWay(LatLng start, LatLng stop) {
        int result;
        LatLng startPos = findNearestNode(start);
        if (startPos != null) {
            LatLng stopPos = findNearestNode(stop);
            if (stopPos != null) {
                GraphBuilder gb = GraphBuilder.<LatLng, Double>create();
           /* for (LatLng :)*/


            }

            return 0;
        }
        return 0;
    }
}


