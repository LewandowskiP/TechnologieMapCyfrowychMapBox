package com.example.arravilar.firstapp;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private MapView mapView;
    private Context context;


    public Button btnSaveRoute;
    public Button btnRec;
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            context = this.context;

        setContentView(R.layout.activity_main);

        btnSaveRoute = (Button) findViewById(R.id.btnSaveRoute);
        btnRec = (Button) findViewById(R.id.btnRec);
        GlobalValues.getInstance().setRecordRoute(false);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                mapboxMap.setMyLocationEnabled(true);
                mapboxMap.setMinZoom(5);
                mapboxMap.setMaxZoom(20);
                mapboxMap.setOnMyLocationChangeListener(new MapboxMap.OnMyLocationChangeListener()
                {
                    @Override
                    public void onMyLocationChange(Location location)
                    {
                        Log.d("112","112");
                     if (GlobalValues.getInstance().getRecordRoute() == true) {
                         Log.d("111","111");
                         GlobalValues.getInstance().getRouteList().getRoutes().get(GlobalValues.getInstance().getRouteList().getRouteNumber()-1).addPoint(new LatLng(mapboxMap.getMyLocation()));
                     }
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(mapboxMap.getMyLocation())) // set the camera's center position
                                .zoom(mapboxMap.getCameraPosition().zoom)  // set the camera's zoom level
                                .tilt(mapboxMap.getCameraPosition().tilt)  // set the camera's tilt
                                .build();
                                mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }

                });
            }

        });
    }


    public void routeSaveBtn(View v) { //realy its RECORD
        if (GlobalValues.getInstance().getRecordRoute()==false) {
            btnRec.setText("Save recorded points");
            btnSaveRoute.setVisibility(View.INVISIBLE);
            btnRec.setVisibility(View.VISIBLE);
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
        }
    }

    public void recordBtn(View v) { ///realy its SAVE wtf?!

        if (GlobalValues.getInstance().getRecordRoute()==true){
            //v.getBackground().clearColorFilter();
            btnRec.setText("Record");
            GlobalValues.getInstance().setRecordRoute(false);
            btnSaveRoute.setVisibility(View.VISIBLE);
            btnRec.setVisibility(View.INVISIBLE);
            Log.d("123",GlobalValues.getInstance().getRouteList().getRoutes().get(GlobalValues.getInstance().getRouteList().getRouteNumber()-1).getPoint(0).toString());
        }
    }

    public void showRoute(View v) {
for (int i = 0; i< GlobalValues.getInstance().getRouteList().getRouteNumber(); i++) {
    Route route = GlobalValues.getInstance().getRouteList().getRoutes().get(i);
    ArrayList<LatLng> points = route.getPoints();
    if (points.size() > 0) {
        final LatLng[] pointsArray = points.toArray(new LatLng[points.size()]);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {

                mapboxMap.addPolyline(new PolylineOptions()
                        .add(pointsArray)
                        .color(Color.parseColor("#3bb2d0"))
                        .width(2));
            }
        });
    }
}
    }

    public void saveToFile(View v)
    {
        GlobalValues.getInstance().getRouteList().saveList();
    }


    public void loadFromFile(View v)
    {
        if (GlobalValues.getInstance().getRouteList() == null){
            Log.d("ladowanie123","1252123123");
            GlobalValues.getInstance().setRouteList(new RouteList(this));
        }
        Log.d("ladowanie","1252123123");
        GlobalValues.getInstance().getRouteList().loadList();
    }


    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}

