package com.example.arravilar.firstapp;

import android.util.Log;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

import hipster.algorithm.Algorithm;
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
    private ArrayList<LatLng> visited;

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
        result.setLatitude(MyMath.getInstance().round(lat));
        result.setLongitude(MyMath.getInstance().round(lng));
        lat = result.getLatitude();
        lng = result.getLongitude();
        if (myMap.containsKey(result)) return result;
        for (int i = 1; i < 3; i++) {
            for (int j = 0; j < 2 * i; j++) {
                result.setLongitude(lng + i * precision);
                result.setLatitude(lat + j * precision);
                if (myMap.containsKey(result)) return result;
            }
            for (int j = 0; j < 2 * i; j++) {
                result.setLongitude(lng - i * precision);
                result.setLatitude(lat - j * precision);
                if (myMap.containsKey(result)) return result;
            }
            for (int j = 0; j < 2 * i; j++) {
                result.setLongitude(lng + j * precision);
                result.setLatitude(lat - i * precision);
                if (myMap.containsKey(result)) return result;
            }
            for (int j = 0; j < 2 * i; j++) {
                result.setLongitude(lng - j * precision);
                result.setLatitude(lat + i * precision);
                if (myMap.containsKey(result)) return result;
            }
        }

        Log.d("REturning NULL", "a");
        return null;
    }

    private void getMyNeighburs(GraphBuilder<LatLng, Double> gb, LatLng my) {
        Log.d("Sądziedzi 1:", my.toString());
        if (visited.contains(my) == false) {
            for (LatLng r : myMap.get(my)) {

                if (r != null) {
                    if (r != my) {
                        Log.d("Sasiedzie 2:", r.toString());
                        visited.add(my);
                        gb.connect(my).to(r).withEdge( MyMath.getInstance().distance(my, r));
                        getMyNeighburs(gb, r);
                    }
                }
            }
        }
    }

    public int findWay(LatLng start, LatLng stop) {
        int result;
        LatLng current;
        LatLng startPos = findNearestNode(start);
        current = startPos;
        if (startPos != null) {
            Log.d("Start:", startPos.toString());

            Log.d("Stop1:", stop.toString());
            LatLng stopPos = new LatLng(findNearestNode(stop));
            if (stopPos != null) {
                Log.d("Stop:", stopPos.toString());
                visited = new ArrayList<LatLng>();
                GraphBuilder gb = GraphBuilder.<LatLng, Double>create();
                getMyNeighburs(gb, startPos);

                HipsterDirectedGraph hdg = gb.createDirectedGraph();
                Log.d("Graf", hdg.toString());
                SearchProblem p = GraphSearchProblem.startingFrom(startPos).in(hdg).takeCostsFromEdges().build();
                Algorithm.SearchResult sr = Hipster.createAStar(p).search(stopPos);


                Log.d("Wynik", sr.toString());

                return 1;
            }

            return 0;
        }
        return 0;
    }
}


