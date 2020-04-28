package com.github.P4rzival.RadiusMessage;

import android.location.Location;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;

public class LocationManager {

    private static LocationManager instance;

    public GpsMyLocationProvider userGPSLocationProvider;
    private Location userLocation;
    private GeoPoint userGeoPoint;
    private MapView map = null;

    private LocationManager(){

        userGPSLocationProvider = new GpsMyLocationProvider(RadiusMessage.getAppInstance().getApplicationContext());
    }

    public GeoPoint getCurrentLocationAsGeoPoint(){
        userLocation = userGPSLocationProvider.getLastKnownLocation();

        userGeoPoint = new GeoPoint(userLocation.getLongitude(),userLocation.getLatitude());

        return  userGeoPoint;
    }

    public static synchronized LocationManager getInstance(){

        if (instance == null){
            instance = new LocationManager();
        }
        return instance;
    }
}
