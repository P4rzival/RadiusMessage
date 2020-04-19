package com.github.P4rzival.RadiusMessage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.lang.Math;

import org.osmdroid.config.*;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MainActivity extends AppCompatActivity implements TextPostDialog.TextPostDialogListener {

    public ConstraintLayout parentLayout;
    public PostRenderer postRenderer;
    private Button testPostButton;
    private RadiusPost currentPost;

    private float lastTouchX;
    private float lastTouchY;

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private MyLocationNewOverlay locationNewOverlay;
    private Location user_location;

    //May need for later
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context appContext = getApplicationContext();
        org.osmdroid.config.Configuration.getInstance().load(appContext, PreferenceManager.getDefaultSharedPreferences(appContext));
        setContentView(R.layout.activity_main);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        requestPermissionsIfNecessary(new String[] {
                // if you need to show the current location, uncomment the line below
                Manifest.permission.ACCESS_FINE_LOCATION,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(19);

        locationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(appContext), map);
        locationNewOverlay.enableFollowLocation();
        map.getOverlays().add(locationNewOverlay);

        //Need this in main activity for postRenderer to work.
        parentLayout = findViewById(R.id.parentLayout);
        postRenderer = new ViewModelProvider(this).get(PostRenderer.class);
        //For the Demo
        postRenderer.deleteAllData();
        postRenderer.getAllPostDrawData().observe(this, new Observer<List<drawData>>() {
            @Override
            public void onChanged(List<drawData> drawData) {
                if(drawData != null && drawData.size() > 0)
                {
                    addPostToView(drawData.get(drawData.size()-1));
                }
                Toast.makeText(MainActivity.this
                        , "Post Map Updated"
                        , Toast.LENGTH_SHORT).show();
            }
        });
        //End of PostRenderer stuff needed in onCreate


        //This is just a back up button that makes a random post
        //Can replace this with Maddy's stuff if we are able to!
        testPostButton = findViewById(R.id.postButton);
        testPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openDialog();

            }
        });
    }

    public void openDialog() {
        TextPostDialog textPostDialog = new TextPostDialog();
        textPostDialog.show(getSupportFragmentManager(), "Example TextPost");
    }

    @Override
    public void applyTexts(String postText, int postRadius, int postDuration) {
        JSONObject test = new JSONObject();
        Random rNum = new Random();
        GeoPoint messageLocation = locationNewOverlay.getMyLocation();
        try {
            test.put("userTextMessage", postText);
            test.put("radius", postRadius);
            test.put("locationX", messageLocation.getLongitude());
            test.put("locationY", messageLocation.getLatitude());
            test.put("messageDuration", postDuration);
        }catch (JSONException e){
            e.printStackTrace();
        }

        PostDrawer postDrawer = new PostDrawer();
        postDrawer.createPost(test);
    }


    public void addPostToView(drawData newPostDrawData){
        RadiusPost newPost = new RadiusPost(map, newPostDrawData,parentLayout);
        GeoPoint messageLocation = locationNewOverlay.getMyLocation();
        newPost.drawMapPost(messageLocation, map);
        postRenderer.radiusPosts.add(newPost);
    }

    //OPEN STREET MAPS FUNCTIONS
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        map.onPause();
    }

    public void addMapToView(Context applicationContext){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //map = new MapView(inflater.getContext(),, applicationContext);
    }


}
