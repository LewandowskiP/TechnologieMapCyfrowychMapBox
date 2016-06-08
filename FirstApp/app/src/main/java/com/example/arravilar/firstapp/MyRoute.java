package com.example.arravilar.firstapp;

import android.util.Log;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

import hipster.algorithm.Hipster;
import hipster.graph.GraphBuilder;
import hipster.graph.GraphSearchProblem;
import hipster.graph.HipsterDirectedGraph;
import hipster.model.problem.SearchProblem;

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

    private void getMyNeighburs(GraphBuilder<LatLng,Double> gb, LatLng my)
    {
        for (LatLng r : myMap.get(my))
        {
            gb.connect(my).to(r).withEdge(MyMath.getInstance().distance(my,r));
            getMyNeighburs(gb,r);
        }
    }

    public int findWay(LatLng start, LatLng stop) {
        int result;
        LatLng current;
        LatLng startPos = findNearestNode(start);
        current = startPos;
        if (startPos != null) {
            LatLng stopPos = findNearestNode(stop);
            if (stopPos != null) {
                GraphBuilder gb = GraphBuilder.<LatLng, Double>create();
                getMyNeighburs(gb,startPos);

                HipsterDirectedGraph hdg = gb.createDirectedGraph();
                SearchProblem p = GraphSearchProblem.startingFrom(startPos).in(hdg).takeCostsFromEdges().build();
                Log.d("Wynik",Hipster.createAStar(p).search(stopPos).getGoalNodes().toString());
                return 1;
            }

            return 0;
        }
        return 0;
    }
}


