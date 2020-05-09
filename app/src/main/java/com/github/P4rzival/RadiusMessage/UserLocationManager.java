package com.github.P4rzival.RadiusMessage;

import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class UserLocationManager {

    private static UserLocationManager instance;

    public GpsMyLocationProvider userGPSLocationProvider;
    private MyLocationNewOverlay locationNewOverlay;
    private LocationManager myLocationManager;
    private LocationProvider myLocationProvider;
    private Location userLocation;
    private GeoPoint userGeoPoint;
    private MapView map = null;

    private UserLocationManager(){
        userGPSLocationProvider = new GpsMyLocationProvider(RadiusMessage.getAppInstance().getApplicationContext());
        myLocationManager = (LocationManager) RadiusMessage.getAppInstance().getSystemService(RadiusMessage.getAppInstance().getApplicationContext().LOCATION_SERVICE);
        myLocationProvider = myLocationManager.getProvider(myLocationManager.GPS_PROVIDER);

    }

    public void StartLocationTracking(MapView currentMap){
        if(map == null){
            map = currentMap;
            userGPSLocationProvider = new GpsMyLocationProvider(RadiusMessage.getAppInstance().getApplicationContext());
            locationNewOverlay = new MyLocationNewOverlay(userGPSLocationProvider, map);
            //userGPSLocationProvider.startLocationProvider(locationNewOverlay);
            locationNewOverlay.enableMyLocation(userGPSLocationProvider);
            locationNewOverlay.enableFollowLocation();
            map.getOverlays().add(locationNewOverlay);
            map.invalidate();
        }
    }

    public GeoPoint getCurrentLocationAsGeoPoint(){
        userLocation = userGPSLocationProvider.getLastKnownLocation();
        userGeoPoint = new GeoPoint(userLocation.getLatitude(),userLocation.getLongitude());
        return  userGeoPoint;
    }

    public static synchronized UserLocationManager getInstance(){

        if (instance == null){
            instance = new UserLocationManager();
        }
        return instance;
    }

    public boolean isInGeoRadius(GeoPoint tapPoint, GeoPoint postPoint, double postRadius){

        Location tapLocation = new Location("tap");
        tapLocation.setLatitude(tapPoint.getLatitude());
        tapLocation.setLongitude(tapPoint.getLongitude());

        Location postLocation = new Location("post");
        postLocation.setLatitude(postPoint.getLatitude());
        postLocation.setLongitude(postPoint.getLongitude());

        double distanceInMeters = tapLocation.distanceTo(postLocation);

        if(distanceInMeters <= postRadius){
            return true;
        }
        return false;
    }

}
