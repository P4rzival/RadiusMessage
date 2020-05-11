package com.github.P4rzival.RadiusMessage;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.P4rzival.RadiusMessage.User.UserCollectionManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements MapEventsReceiver {

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private final int INITIAL_ZOOM = 19;

    private ConstraintLayout parentLayout;
    private Context appContext;

    public MapView map = null;
    private IMapController mapController;
    private MyLocationNewOverlay locationNewOverlay;
    private MapEventsOverlay mapEventsOverlay;
    private Location userLocation;

    private UserLocationManager userLocationManager;
    private UserCollectionManager userCollectionManager;

    public ArrayList<RadiusPost> listOfPosts;
    private RadiusPost currentPost;

    public MapActivity(Context parentContext, ArrayList<RadiusPost> rendererList, ConstraintLayout mainLayout){
        appContext = parentContext;
        listOfPosts = rendererList;
        parentLayout = mainLayout;

        map = parentLayout.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        requestPermissionsIfNecessary(new String[] {
                // if you need to show the current location, uncomment the line below
                Manifest.permission.ACCESS_FINE_LOCATION,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        initMapControl();

        mapEventsOverlay = new MapEventsOverlay(this,this);
        map.getOverlays().add(mapEventsOverlay);

        userLocationManager = UserLocationManager.getInstance();
        //userLocationManager.StartLocationTracking(map);

        locationNewOverlay = new MyLocationNewOverlay(userLocationManager.userGPSLocationProvider, map);
        locationNewOverlay.enableMyLocation(userLocationManager.userGPSLocationProvider);
        locationNewOverlay.enableFollowLocation();
        map.getOverlays().add(locationNewOverlay);
        map.invalidate();

        userCollectionManager = UserCollectionManager.getInstance();
        userCollectionManager.togglePostCollection();
        userCollectionManager.collectPosts();

    }

    public void initMapControl(){

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        mapController = map.getController();
        mapController.setZoom(INITIAL_ZOOM);
    }

    public void updatePostList(ArrayList<RadiusPost> newListOfPosts){
        listOfPosts = newListOfPosts;
    }

    public GeoPoint getMyLocationOnMap(){
        return locationNewOverlay.getMyLocation();
    }

    public void updatePostMapOverlays(List<drawData> currentDrawData) {
        map.getOverlays().remove(locationNewOverlay);
        map.getOverlays().remove(mapEventsOverlay);
        for (int i = 0; i <currentDrawData.size(); i++){
            RadiusPost newPost = new RadiusPost(map, currentDrawData.get(i),parentLayout);
            newPost.drawMapPost(map);

            if(listOfPosts.contains(newPost) == false){
                listOfPosts.add(newPost);
            }

        }
        map.getOverlays().add(locationNewOverlay);
        map.getOverlays().add(mapEventsOverlay);
        userCollectionManager.radiusPosts = listOfPosts;
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        currentPost = getPostToOpen(p);
        if(currentPost != null && userLocationManager.isInGeoRadius(getMyLocationOnMap(), currentPost.postGeoPoint, currentPost.postData.getRadius())){
            currentPost.openPostPopup();
        }
        else if(currentPost != null && userLocationManager.isInGeoRadius(getMyLocationOnMap(), currentPost.postGeoPoint, currentPost.postData.getRadius() ) == false){
            //Open a popup that shows the distance to the selected post
        }
        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    getParent(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(appContext, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    getParent(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    public RadiusPost getPostToOpen(GeoPoint tapPoint) {

        RadiusPost postToOpen = null;

        List<RadiusPost> postsInRange = new ArrayList<>();

        //First find all posts in range of tap
        if (listOfPosts.size() >= 1) {
            for (int i = 0; i < listOfPosts.size(); i++) {
                RadiusPost postToTest = listOfPosts.get(i);
                if (UserLocationManager.getInstance().isInGeoRadius(tapPoint, postToTest.postGeoPoint, postToTest.postData.getRadius())) {
                    postsInRange.add(listOfPosts.get(i));
                }
            }
            //Find the smallest after that
            if (postsInRange.size() >= 1) {
                postToOpen = findSmallestPostInRange(postsInRange);
            }
        }

        return postToOpen;
    }

    public RadiusPost findSmallestPostInRange(List<RadiusPost> inRangePosts){
        RadiusPost smallestPost =  inRangePosts.get(0);
        for (int i = 0; i < inRangePosts.size(); i++){
            if(inRangePosts.get(i).postData.getRadius() <= smallestPost.postData.getRadius()){
                smallestPost = inRangePosts.get(i);
            }
        }
        return smallestPost;
    }
}
