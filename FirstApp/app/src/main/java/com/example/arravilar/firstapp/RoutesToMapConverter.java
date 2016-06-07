package com.example.arravilar.firstapp;

import android.util.ArrayMap;
import android.util.Log;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Przemysław on 2016-06-07.
 */


public class RoutesToMapConverter {
    public HashMap<LatLng, ArrayList<LatLng>> convert(RouteList routeList) {
        HashMap<LatLng, ArrayList<LatLng>> result = new HashMap<LatLng, ArrayList<LatLng>>();
        double lat = 0;
        double lng = 0;
        for (Route r : routeList.getRoutes()) {
            LatLng last = null;
            for (LatLng l : r.getPoints()) {
                lat = MyMath.getInstance().round(l.getLatitude());
                lng = MyMath.getInstance().round(l.getLongitude());
                LatLng toPut = new LatLng(lat, lng);
                ArrayList<LatLng> list;
                if (result.containsKey(toPut)) {
                    list = result.get(toPut);
                    if (last != null) list.add(last);
                } else {
                    list = new ArrayList<LatLng>();
                    list.add(last);
                    result.put(toPut, list);
                }
                last = toPut;
            }
        }
        return result;
    }

}
