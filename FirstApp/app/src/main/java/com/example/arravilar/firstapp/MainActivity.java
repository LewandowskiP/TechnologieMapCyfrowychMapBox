package com.example.arravilar.firstapp;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import android.graphics.PorterDuffColorFilter;

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
                     if (GlobalValues.getInstance().getRecordRoute() == true) {
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


    public void routeSaveBtn(View v) {
        if (GlobalValues.getInstance().getRecordRoute()==true) { //if really recording
            GlobalValues.getInstance().setRecordRoute(false); //stop recording
            btnRec.setText("Record");
            btnSaveRoute.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(this, Main2Activity.class);
            String routeName = "Nowa trasa 1";
            intent.putExtra(EXTRA_MESSAGE, routeName);
            startActivity(intent);
        }
    }

    public void recordBtn(View v) {

        if (GlobalValues.getInstance().getRecordRoute()==true){
            //v.getBackground().clearColorFilter();
            btnRec.setText("Record");
            GlobalValues.getInstance().setRecordRoute(false);
            btnSaveRoute.setVisibility(View.INVISIBLE);
        }
        else{
            //v.getBackground().setColorFilter(0xff4444, null);
            btnRec.setText("Recording");
            GlobalValues.getInstance().setRecordRoute(true);
            btnSaveRoute.setVisibility(View.VISIBLE);
        }
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

