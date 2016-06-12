package com.example.arravilar.firstapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
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
    public Button btnRcrdRoute;
    public ToggleButton tglBtnCenter;
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.context;
        setContentView(R.layout.activity_main);

        btnRcrdRoute = (Button) findViewById(R.id.btnRcrdRoute);
        btnSaveRoute = (Button) findViewById(R.id.btnSaveRoute);
        tglBtnCenter = (ToggleButton) findViewById(R.id.centerButton);
        GlobalValues.getInstance().setRecordRoute(false);


        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                mapboxMap.setMyLocationEnabled(true);
                mapboxMap.setMinZoom(5);
                mapboxMap.setMaxZoom(20);

                //set default Start MARKER
                GlobalValues.getInstance().setStart(new LatLng(54.3707, 18.6147));
                GlobalValues.getInstance().setStartMarker(mapboxMap.addMarker(new MarkerOptions()
                        .position(GlobalValues.getInstance().getStart())
                        .title("Start \nLat: "+GlobalValues.getInstance().getStart().getLatitude()
                        +"\nLon: "+GlobalValues.getInstance().getStart().getLongitude())
                    )
                );

                ///Listener dla zmiany KAMERY
                mapboxMap.setOnCameraChangeListener(new MapboxMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition position) {

                    }
                });

                ///Listener dla SCROLLA
                mapboxMap.setOnScrollListener(new MapboxMap.OnScrollListener() {
                    @Override
                    public void onScroll() {
                        if ((tglBtnCenter.isChecked())&&(GlobalValues.getInstance().getKeepCentered())){
                            tglBtnCenter.setChecked(false);
                            GlobalValues.getInstance().setKeepCentered(false);

                        }
                        //Schowaj window MARKERA
                        if (GlobalValues.getInstance().getDestMarker()!=null)
                            GlobalValues.getInstance().getDestMarker().hideInfoWindow();
                        //Log.d("ZOOOOM",Double.toString(mapboxMap.getCameraPosition().zoom));
                    }
                });

                ///Listener dla DLUGI KLICK na mapie
                mapboxMap.setOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {
                                                        @Override
                                                        public void onMapLongClick(@NonNull LatLng point) {
                                                            GlobalValues.getInstance().setDestination(new LatLng(point));

                                                            //Usun jesli juz byl
                                                            if (GlobalValues.getInstance().getDestMarker()!=null) {
                                                                Log.d("MAKRER","Dest Marker != Null");
                                                                mapboxMap.removeMarker(GlobalValues.getInstance().getDestMarker());
                                                            }
                                                            //Dodaj MARKER
                                                            GlobalValues.getInstance().setDestMarker(mapboxMap.addMarker(new MarkerOptions()
                                                                    .position(point)
                                                                    .title("Koniec \nLat: "+GlobalValues.getInstance().getDestination().getLatitude()
                                                                            +"\nLon: "+GlobalValues.getInstance().getDestination().getLongitude())
                                                                    )
                                                            );
                                                            //Show marker Info
                                                            GlobalValues.getInstance().getDestMarker().showInfoWindow(mapboxMap, mapView);
                                                            Log.d("Where is it", point.toString());
                                                        }
                                                    }

                );

                ///Listener dla clicknieca na Marker
                mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                  @Override
                    public boolean onMarkerClick(@NonNull Marker marker){
                      if (marker.isInfoWindowShown()){
                          marker.hideInfoWindow();
                      }
                      else{
                          marker.showInfoWindow(mapboxMap, mapView);
                      }

                      Log.d("MARKER","Kliknieto w marker"+marker.getPosition().toString());
                      return true;
                    }
                });

                ///Listener dla zmiany lokalziacji
                mapboxMap.setOnMyLocationChangeListener(new MapboxMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {

                        Log.d("112", "112");
                        //jesli wlaczone nagrywanie to dodaj punkt do drogi na przez listener
                        if (GlobalValues.getInstance().getRecordRoute()) {
                            Log.d("111", "111");
                            GlobalValues.getInstance().getRouteList().getRoutes().get(GlobalValues.getInstance().getRouteList().getRouteNumber() - 1).addPoint(new LatLng(mapboxMap.getMyLocation()));
                        }
                        if (GlobalValues.getInstance().getKeepCentered()) {
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(new LatLng(mapboxMap.getMyLocation())) // set the camera's center position
                                    .zoom(mapboxMap.getCameraPosition().zoom)  // set the camera's zoom level
                                    .tilt(mapboxMap.getCameraPosition().tilt)  // set the camera's tilt
                                    .build();
                            mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                    }

                });
            }

        });
    }

    public void centerToggleButton(View v) {
        if (tglBtnCenter.isChecked()) {
            GlobalValues.getInstance().setKeepCentered(true);

            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final MapboxMap mapboxMap) {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(mapboxMap.getMyLocation())) // set the camera's center position
                            .zoom(16.5)  // set the camera's zoom level
                            .tilt(mapboxMap.getCameraPosition().tilt)  // set the camera's tilt
                            .build();
                    mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });
        } else GlobalValues.getInstance().setKeepCentered(false);
    }

    public void routeRcrdBtn(View v) {
        if (!GlobalValues.getInstance().getRecordRoute()) {
            //btnSaveRoute.setText("Save recorded points");
            btnRcrdRoute.setVisibility(View.INVISIBLE);
            btnSaveRoute.setVisibility(View.VISIBLE);
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
        }
    }

    public void routeSaveBtn(View v) {

        if (GlobalValues.getInstance().getRecordRoute()) {
            GlobalValues.getInstance().setRecordRoute(false);
            btnRcrdRoute.setVisibility(View.VISIBLE);
            btnSaveRoute.setVisibility(View.INVISIBLE);
            //sprawdz czy ostatnio dodana przecina się z pozostałymi
            Log.d("123", GlobalValues.getInstance().getRouteList().getRoutes().get(GlobalValues.getInstance().getRouteList().getRouteNumber() - 1).getPoint(0).toString());
            Log.d("TNE", Integer.toString(GlobalValues.getInstance().getRouteList().RouteMakeCrossing()));
        }
    }


    public void showWays(View v)
    {
        if (GlobalValues.getInstance().getRouteList() != null)
            for (Route r : GlobalValues.getInstance().getRouteList().getRoutes()) {
                ArrayList<LatLng> points = r.getPoints();
                if (points.size() > 0) {
                    final LatLng[] pointsArray = points.toArray(new LatLng[points.size()]);
                    mapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(final MapboxMap mapboxMap) {
                            mapboxMap.addPolyline(new PolylineOptions()
                                    .add(pointsArray)
                                    .color(Color.parseColor(MyColor.getInstance().getColor()))
                                    //.color(Color.parseColor("#45df67"))
                                    .width(2));
                        }
                    });
                }
            }
        //Log.d("TNE", Integer.toString(GlobalValues.getInstance().getRouteList().RouteMakeCrossing()));
        RoutesToMapConverter rtmc = new RoutesToMapConverter();
        rtmc.convert(GlobalValues.getInstance().getRouteList());
    }


    public void showRoute(View v) {
        MyRoute mr = new MyRoute();
       //LatLng startLocation = new LatLng(54.3707, 18.6147);

        /*mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                startLocation.setLatitude(mapboxMap.getMyLocation().getLatitude());
                startLocation.setLongitude(mapboxMap.getMyLocation().getLongitude());
            }
        });*/

        Log.d("Szukam:", GlobalValues.getInstance().getStart().toString());
        mr.findWay(new LatLng(GlobalValues.getInstance().getStart()), new LatLng(GlobalValues.getInstance().getDestination()));
        
        for (LatLng point:mr.getRoute()) {
            Log.d("PUNKT SCIEZKI-->",point.toString());
        }
    }


    public void clearMap(View v) {

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                mapboxMap.removeAnnotations();
            }
        });
    }

    public void saveToFile(View v) {
        GlobalValues.getInstance().getRouteList().saveList();
    }


    public void loadFromFile(View v) {
        if (GlobalValues.getInstance().getRouteList() == null) {
            Log.d("ladowanie123", "1252123123");
            GlobalValues.getInstance().setRouteList(new RouteList(this));
        }
        Log.d("ladowanie", "1252123123");
        GlobalValues.getInstance().getRouteList().loadList();
        Log.d("TNE", Integer.toString(GlobalValues.getInstance().getRouteList().RouteMakeCrossing()));
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

