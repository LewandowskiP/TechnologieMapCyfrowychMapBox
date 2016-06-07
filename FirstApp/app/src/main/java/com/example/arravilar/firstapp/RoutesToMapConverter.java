package com.example.arravilar.firstapp;

import android.util.ArrayMap;
import android.util.Log;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Przemysław on 2016-06-07.
 */
public class RoutesToMapConverter {







    public HashMap<LatLng,ArrayList<LatLng>> convert(){
        HashMap<LatLng,ArrayList<LatLng>> result = new HashMap<LatLng,ArrayList<LatLng>>();
        double lat = 0;
        double lng = 0;

        double exLat = 54.123456789;
        double exLng = 17.123456788;

        lat = MyMath.getInstance().round(exLat);
        lng = MyMath.getInstance().round(exLng);

        Log.d("ddd",String.valueOf(lat));
        Log.d("ccc",String.valueOf(lng));
/*
        for( Route r :GlobalValues.getInstance().getRouteList().getRoutes())
        {

            for ( LatLng l:r.getPoints())
            {
                lat = MyMath.getInstance().round(l.getLatitude());
                lng = MyMath.getInstance().round(l.getLongitude());
            }


        }*/
        return result;
    }

}
