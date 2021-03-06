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

    private class PointDouble{
        private double A, B;

        public PointDouble(){

        }
        public PointDouble(double a, double b){
            A = a;
            B = b;
        }
        public void setA (double a){
            A = a;
        }
        public void setB (double b){
            B = b;
        }
        public double getA(){
            return A;
        }
        public double getB() {
            return B;
        }
    }
    //wspolczynniki A i B dla lini przechodzacej przez 2 punkty
    private  PointDouble getLineCoefficients(LatLng P1, LatLng P2){
        PointDouble punkt = new PointDouble();

        //Log.d("DEBUG //// KOORD P1Lat", Double.toString(P1.getLatitude()));
        //Log.d("DEBUG //// KOORD P1Lng", Double.toString(P1.getLongitude()));
        //Log.d("DEBUG //// KOORD P2Lat", Double.toString(P2.getLatitude()));
        //Log.d("DEBUG //// KOORD P2Lng", Double.toString(P2.getLongitude()));

        punkt.setA((P1.getLatitude()-P2.getLatitude())/(P1.getLongitude()-P2.getLongitude()));
        punkt.setB(P2.getLatitude()-P2.getLongitude()*punkt.getA());
        return punkt;
    }


    private int checkIfCommon(LatLng S1P1, LatLng S1P2, LatLng S2P1, LatLng S2P2){

        if ((S1P1.getLatitude() == S1P2.getLatitude())&&(S1P1.getLongitude()== S1P2.getLongitude())) return 0;
        else if ((S2P1.getLatitude() == S2P2.getLatitude())&&(S2P1.getLongitude()== S2P2.getLongitude())) return 0;
            else if ((S1P1.getLatitude() == S2P1.getLatitude())&&(S1P1.getLongitude()== S2P1.getLongitude())) return 0;
                else if ((S1P1.getLatitude() == S2P2.getLatitude())&&(S1P1.getLongitude()== S2P2.getLongitude())) return 0;
                    else if((S1P2.getLatitude() == S2P1.getLatitude())&&(S1P2.getLongitude()== S2P1.getLongitude())) return 0;
                        else if((S1P2.getLatitude() == S2P2.getLatitude())&&(S1P2.getLongitude()== S2P2.getLongitude())) return 0;
                            else return 1;
    }

    private int  ifLiesOnSection(LatLng xPoint, LatLng P1, LatLng P2, double aFactor){
        if ((aFactor>0)&&(P1.getLongitude()<P2.getLongitude())){
            if ((P1.getLongitude()<=xPoint.getLongitude())&&(xPoint.getLongitude()<=P2.getLongitude())
                &&(P1.getLatitude()<=xPoint.getLatitude())&&(xPoint.getLatitude()<=P2.getLatitude())) {
                //Log.d("LEZZYYY:", "A>0 x1<x2");
                return 1;
            }

        }else if ((aFactor>0)&&(P2.getLongitude()<P1.getLongitude())){
            if ((P2.getLongitude()<=xPoint.getLongitude())&&(xPoint.getLongitude()<=P1.getLongitude())
                    &&(P2.getLatitude()<=xPoint.getLatitude())&&(xPoint.getLatitude()<=P1.getLatitude())){
                //Log.d("LEZZYYY:", "A>0 x2<x1");
                return 1;
            }

        }else if ((aFactor<=0)&&(P1.getLongitude()<P2.getLongitude())){
            if ((P1.getLongitude()<=xPoint.getLongitude())&&(xPoint.getLongitude()<=P2.getLongitude())
                    &&(P2.getLatitude()<=xPoint.getLatitude())&&(xPoint.getLatitude()<=P1.getLatitude())){
                //Log.d("LEZZYYY:", "A<=0 x1<x2");
                return 1;
            }

        }else if ((aFactor<=0)&&(P2.getLongitude()<P1.getLongitude())){
            if ((P2.getLongitude()<=xPoint.getLongitude())&&(xPoint.getLongitude()<=P1.getLongitude())
                    &&(P1.getLatitude()<=xPoint.getLatitude())&&(xPoint.getLatitude()<=P2.getLatitude())){
                //Log.d("LEZZYYY:", "A<=0 x2<x1");
                return 1;
            }
        }
        return 0;
    }

    //punkt przeciecia dwoch odcinkow
    private LatLng sectionCrossingPoint(LatLng S1P1, LatLng S1P2, LatLng S2P1, LatLng S2P2) {
        //współczynniki A i B odcinkow
        double S1A, S1B, S2A, S2B;
        LatLng temp = new LatLng();
        LatLng crossingPoint = new LatLng();

        ///sprawdz czy punkty wspolne itd.
        if (checkIfCommon(S1P1, S1P2, S2P1, S2P2) == 0) {
            //Log.d("GOOOOOOWNO", "KAAAU");
            return null;

        } else {
            S1A = getLineCoefficients(S1P1, S1P2).getA();
            S2A = getLineCoefficients(S2P1, S2P2).getA();

            if (S1A == S2A) {   //jesli odcinki są równoległe
                return null;
            } else {     //jeśli się przecinają
                //Log.d("Debug Gdzie sie inają", "fffff");
                S1B = getLineCoefficients(S1P1, S1P2).getB();
                S2B = getLineCoefficients(S2P1, S2P2).getB();
                //Log.d("DEBUG LICZBY///: S1A", Double.toString(S1A));
                //Log.d("DEBUG LICZBY///: S1B", Double.toString(S1B));
                //Log.d("DEBUG LICZBY///: S2A", Double.toString(S2A));
                //Log.d("DEBUG LICZBY///: S2B", Double.toString(S2B));

                crossingPoint.setLongitude((S2B - S1B) / (S1A - S2A)); //find Longitude - X
                crossingPoint.setLatitude(S1A * crossingPoint.getLongitude() + S1B); //find Latitude - Y

                ///check if NaN
                if (Double.isNaN(crossingPoint.getLongitude()) || (Double.isNaN(crossingPoint.getLatitude()))) {
                    return null;
                }

                // Log.d("Debug Punkkt 1", S1P1.toString());
                // Log.d("Debug Punkkt 2", S1P2.toString());
                // Log.d("Debug Punkkt 3", S2P1.toString());
                // Log.d("Debug Punkkt 4", S2P2.toString());
                //Log.d("DEBUG punkt CROSS", crossingPoint.toString());

                //check if point is a part of two sections

                if ((ifLiesOnSection(crossingPoint,S1P1,S1P2,S1A)==1)&&(ifLiesOnSection(crossingPoint,S2P1,S2P2,S2A)==1)){
                    return crossingPoint;
                }
                else return null;
            }
        }
    }

    public int RouteMakeCrossing(boolean checkAll){
        int routesCount = getRouteNumber();
        int pointsAdded = 0;
        int startIndex;
        //dwie petle bo kazda z kazdym
        //jedna petla bo sprawdzam jedna nowo dodaną droge z pozostalymi
        if (!checkAll) startIndex = routesCount-1;
        else startIndex = 0;
        //START
        for (int i = startIndex; i<routesCount; i++)
            for (int j=0; j<routesCount; j++) {
                //Log.d("Debug Routes Count", Integer.toString(routesCount));
                //Log.d("Debug i", Integer.toString(i));
                //Log.d("Debug j", Integer.toString(j));
                //Log.d("Debug k", Integer.toString(routes.get(i).getPointsNum()-1));
                //Log.d("Debug l", Integer.toString(routes.get(j).getPointsNum()-1));
                //latitude -90 - +90 - Y
                //longitude -180 - +180 - X
                //sprawdz zeby nie sprawdzac drogi samej z soba
                if (i != j) {
                    //po punktach ze scieżki pierwszej
                    for (int k = 0; k < routes.get(i).getPointsNum() - 1; k++)
                        //po punktach ze ścieżki drugiej
                        for (int l = 0; l < routes.get(j).getPointsNum() - 1; l++) {
                            //jeśli ścieżki się przecinają
                            if (sectionCrossingPoint(routes.get(i).getPoint(k), routes.get(i).getPoint(k + 1), routes.get(j).getPoint(l), routes.get(j).getPoint(l + 1)) != null) {
                                LatLng crossPoint;

                                //wstaw obliczony punkt do obydwu sciezek
                                crossPoint = sectionCrossingPoint(routes.get(i).getPoint(k), routes.get(i).getPoint(k + 1), routes.get(j).getPoint(l), routes.get(j).getPoint(l + 1));
                                //Log.d("Debug PUNKT", crossPoint.toString());

                                routes.get(i).addPointI(k+1, crossPoint);
                                routes.get(j).addPointI(l+1, crossPoint);

                                l++; //przejście do początku kolejnego odcinka
                                pointsAdded++; //liczba oddanych punktow
                            }
                        }
                 }
            }
        return pointsAdded; //crossing done
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
            //Log.d("Test", "Zrobilem: " + main.toString());
            outputStreamWriter.write(main.toString());
            outputStreamWriter.close();
        }
        catch (Exception e)
        {
            Log.e(null, "Exception create GeoJSON: " + e.toString());
        }
    }


    public ArrayList<Route> loadList(boolean example) {
        String revicedString = "";
        if (example){
            revicedString = appContext.getResources().getString(R.string.geojsonFile);
            //Log.d("ddddddd",revicedString);
        }else {
            try {
                // Load GeoJSON file
                InputStream inputStream = appContext.openFileInput("Rotues");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                while ((revicedString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(revicedString);
                }
                revicedString = stringBuilder.toString();

                //Log.d("Test", "Zrobilem: " + revicedString);
                inputStream.close();

            } catch (Exception e) {
                //Log.e(null, "Exception Loading GeoJSON: " + e.toString());
            }
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


