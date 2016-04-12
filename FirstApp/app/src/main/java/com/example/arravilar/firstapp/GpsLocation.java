package com.example.arravilar.firstapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Przemysław on 2016-04-12.
 */
public class GpsLocation implements LocationListener{

    Context mContext;
    TextView gpsStatusTextView;
    LocationManager locationManager;
    boolean isGPSEnabled;
    boolean isNetworkEnabled;
    boolean canGetLocation;

    Location location;



    public GpsLocation(Context mContext, TextView gpsStatusTextView) {
        this.mContext = mContext;
        this.gpsStatusTextView = gpsStatusTextView;
        getLocation();
    }

    @Override
    public void onLocationChanged(Location location) {
        //Gererated method needed to be override TODO
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //Gererated method needed to be override TODO
    }

    @Override
    public void onProviderEnabled(String provider) {
        //Gererated method needed to be override TODO
    }

    @Override
    public void onProviderDisabled(String provider) {
        //Gererated method needed to be override TODO
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            Log.d("1", "1");
            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.d("2", "2");
            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Log.d("3", "3");

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                Log.d("4", "4");
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == 0 &&
                            ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == 0);
                    Log.d("5", "5");

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                    Log.d("Network", "Network");

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            return location;
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, this);
                        Log.d("GPS Enabled", "GPS Enabled");

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                return location;
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

}
