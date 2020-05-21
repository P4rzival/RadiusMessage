package com.github.P4rzival.RadiusMessage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.annotation.SuppressLint;
import android.content.Context;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Base64InputStream;
import android.view.View;

import android.widget.Button;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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


}
