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
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Base64;
import android.view.View;

import android.widget.Button;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;


import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements PostDialog.TextPostDialogListener {

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    public ConstraintLayout parentLayout;

    public PostRenderer postRenderer;
    private Button testPostButton;
    private RadiusPost currentPost;

    public MapActivity mapActivity;

    Bitmap imageToUploadBitmap;


    //May need for later
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context appContext = getApplicationContext();
        org.osmdroid.config.Configuration.getInstance().load(appContext, PreferenceManager.getDefaultSharedPreferences(appContext));
        setContentView(R.layout.activity_main);

        requestPermissionsIfNecessary(new String[]{
                // if you need to show the current location, uncomment the line below
                Manifest.permission.ACCESS_FINE_LOCATION,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        //Need this in main activity for postRenderer to work.
        parentLayout = findViewById(R.id.parentLayout);
        postRenderer = new ViewModelProvider(this).get(PostRenderer.class);
        //postRenderer.deleteAllData();
        mapActivity = new MapActivity(appContext, postRenderer.radiusPosts, parentLayout);


        postRenderer.getAllPostDrawData().observe(this, new Observer<List<drawData>>() {
            @Override
            public void onChanged(List<drawData> drawData) {
                updatePostMap(drawData);
                Toast.makeText(MainActivity.this
                        , "Post Map Updated"
                        , Toast.LENGTH_SHORT).show();
            }

        });
        //End of PostRenderer stuff needed in onCreate

        testPostButton = findViewById(R.id.postButton);
        testPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openDialog();

            }
        });

        new CheckForPostsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void openDialog() {
        PostDialog postDialog = new PostDialog();
        postDialog.show(getSupportFragmentManager(), "Example TextPost");
    }

    @Override
    public void applyTexts(String postText, int postRadius, int postDuration, int postDelay, Bitmap bitmap) throws IOException {

        JSONObject post = new JSONObject();
        GeoPoint messageLocation = UserLocationManager.getInstance().getCurrentLocationAsGeoPoint();
        String decodedImage = "";
        if(bitmap != null){
            decodedImage = BitmapToString(bitmap);
        }
        try {
            post.put("userTextMessage", postText);
            post.put("radius", postRadius);
            post.put("locationX", messageLocation.getLongitude());
            post.put("locationY", messageLocation.getLatitude());
            post.put("messageDuration", postDuration);
            post.put("messageDelay", postDelay);
            post.put("userMessageImage", decodedImage);
        }catch (JSONException e){
            e.printStackTrace();
        }

        RequestGenerator.generateRequest(post);
    }

    public void updatePostMap(List<drawData> currentDrawData) {
        mapActivity.updatePostMapOverlays(currentDrawData);
    }

    public String BitmapToString(Bitmap bitmap){
        ByteArrayOutputStream bitStreamOut = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 24, bitStreamOut);
        byte[] byteImageArray = bitStreamOut.toByteArray();
        String convertedImage = Base64.encodeToString(byteImageArray, Base64.URL_SAFE);
        return convertedImage;
    }


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
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission)
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


            //ActivityCompat.requestPermissions((Activity) RadiusMessage.getAppInstance().getApplicationContext(),
            //        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    // This should be replaced with a Service or something
    private static class CheckForPostsTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            // Wait ten seconds
            while(true) {
                DatabaseAccessor.getPostsFromCurrentLocation();
                waitInThread(10000);
            }
        }

        private void waitInThread(long millis) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                System.out.println("Error In waitInThread: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }


}

