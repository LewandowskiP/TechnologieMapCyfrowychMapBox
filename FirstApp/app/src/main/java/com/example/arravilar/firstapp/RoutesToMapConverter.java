package com.example.arravilar.firstapp;


import android.util.Log;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Przemysław on 2016-06-07.
 */


public class RoutesToMapConverter {
    public HashMap<LatLng, ArrayList<LatLng>> convert(RouteList routeList) {
        HashMap<LatLng, ArrayList<LatLng>> result = new HashMap<LatLng, ArrayList<LatLng>>();
        double lat = 0;
        double lng = 0;
        int totalNodes = 0;

        for (Route r : routeList.getRoutes()) {
            Log.d("Route", r.toString());
            LatLng last = null;
            ArrayList<LatLng> reversed = new ArrayList<LatLng>(r.getPoints());
            Collections.reverse(reversed);
            for (LatLng l : r.getPoints()) {
                Log.d("RPoint", l.toString());
                lat = MyMath.getInstance().round(l.getLatitude());
                lng = MyMath.getInstance().round(l.getLongitude());
                LatLng toPut = new LatLng(lat, lng, 0);
                Log.d(" Nodes ", toPut.toString());
                ArrayList<LatLng> list;
                if (result.containsKey(toPut)) {
                    list = result.get(toPut);
                    Log.d("RPoint", "piust");
                    if (last != null) {
                        list.add(last);
                        Log.d("RPoint", last.toString());
                    }
                } else {
                    Log.d("RPoint", "buł");
                    list = new ArrayList<LatLng>();
                    list.add(last);
                    result.put(toPut, list);
                    totalNodes++;
                }
                last = toPut;
            }
            for (LatLng l : reversed) {
                Log.d("RPoint", l.toString());
                lat = MyMath.getInstance().round(l.getLatitude());
                lng = MyMath.getInstance().round(l.getLongitude());
                LatLng toPut = new LatLng(lat, lng, 0);
                Log.d(" Nodes ", toPut.toString());
                ArrayList<LatLng> list;
                if (result.containsKey(toPut)) {
                    list = result.get(toPut);
                    Log.d("RPoint", "piust");
                    if (last != null) {
                        list.add(last);
                        Log.d("RPoint", last.toString());
                    }
                } else {
                    Log.d("RPoint", "buł");
                    list = new ArrayList<LatLng>();
                    list.add(last);
                    result.put(toPut, list);
                    totalNodes++;
                }
                last = toPut;
            }
        }

        Log.d(" TotalNodesNumber ", String.valueOf(totalNodes));
        return result;
    }
}
