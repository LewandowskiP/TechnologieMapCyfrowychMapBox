package com.example.arravilar.firstapp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.util.ArrayList;

/**
 * Created by Przemysław on 2016-04-12.
 */

public class RouteList {

    private ArrayList<Route> routes;

    public int getRouteNumber() {
        return routeNumber;
    }

    private int routeNumber;
    Context appContext;

    public RouteList(Context context) {
        routeNumber = 0;
        routes = new ArrayList<Route>();
        appContext = context;
    }


    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void addRoute(String name){
        routes.add(new Route(name));
        routeNumber++;
        Log.d("Test", "Dodałem ścieżkę: " + name.toString());
    }

    public void saveList() {
        //Construct GeoJSON file
        JSONObject main=null;
        try {
            main = new JSONObject();
            // type : "FeatureCollection"
            main.put("type", "FeatureCollection");

            // features :
            JSONArray features = new JSONArray();
            for (int routeIterator = 0; routeIterator < routeNumber; routeIterator++ ) {

                JSONObject feature = new JSONObject();
                // type : feature
                feature.put("type", "Feature");
                /*"properties": {
                "name": "name"
                },*/

                JSONObject properties = new JSONObject();
                properties.put("name", routes.get(routeIterator).getName());
                feature.put("properties", properties);

                JSONObject geometry = new JSONObject();
                geometry.put("type", "LineString");
                JSONArray coordinates = new JSONArray();
                for (int pointInterator = 0; pointInterator < routes.get(routeIterator).getPointsNum(); pointInterator++) {
                    JSONArray coord = new JSONArray();
                    coord.put(routes.get(routeIterator).getPoint(pointInterator).getLongitude());
                    coord.put(routes.get(routeIterator).getPoint(pointInterator).getLatitude());
                    coordinates.put(coord);
                }
                // "geometry": ...
                geometry.put("coordinates",coordinates);
                feature.put("geometry",geometry);
                features.put(feature);
            }
            main.put("features",features);
        }
        catch (Exception e)
        {
            Log.e(null, "Exception create GeoJSON: " + e.toString());
        }


        //Save to file
        try{
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(appContext.openFileOutput("Rotues", Context.MODE_PRIVATE));
            Log.d("Test", "Zrobilem: " + main.toString());
            outputStreamWriter.write(main.toString());
            outputStreamWriter.close();
        }
        catch (Exception e)
        {
            Log.e(null, "Exception create GeoJSON: " + e.toString());
        }
    }


    public ArrayList<Route> loadList() {
        String revicedString = "";
        Log.d("Test1", "Zrobilem: " + revicedString.toString());
        try {
            // Load GeoJSON file
            InputStream inputStream = appContext.openFileInput("Rotues");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            while ((revicedString = bufferedReader.readLine()) != null) {
                stringBuilder.append(revicedString);
            }
            Log.d("Test", "Zrobilem: " + revicedString);
            revicedString = stringBuilder.toString();
            inputStream.close();
        } catch (Exception e) {
            Log.e(null, "Exception Loading GeoJSON: " + e.toString());
        }
            try {
                // Parse GeoJSON
                JSONObject main = new JSONObject(revicedString.toString());
                JSONArray features = main.getJSONArray("features");
                routeNumber = features.length();
                for (int routeIterator = 0; routeIterator < routeNumber; routeIterator++) {
                    JSONObject feature = features.getJSONObject(routeIterator);
                    JSONObject properties = feature.getJSONObject("properties");
                    String name = properties.getString("name");
                    routes.add(new Route(name));
                    JSONObject geometry = feature.getJSONObject("geometry");
                    if (geometry != null) {
                        String type = geometry.getString("type");
                        // Our GeoJSON only has one feature: a line string
                        if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("LineString")) {
                            // Get the Coordinates
                            JSONArray coordinates = geometry.getJSONArray("coordinates");
                            for (int pointIterator = 0; pointIterator < coordinates.length(); pointIterator++) {
                                JSONArray coord = coordinates.getJSONArray(pointIterator);
                                LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
                                routes.get(routeIterator).addPoint(new LatLng(latLng));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(null, "Exception Loading GeoJSON: " + e.toString());
            }
            return routes;
        }
    }


