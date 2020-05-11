package com.github.P4rzival.RadiusMessage;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlendMode;
import android.graphics.Paint;
import android.location.Location;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polygon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RadiusPost extends Polygon {

    ImageView imageDownloaded;
    ImageView saveButton;

    public Paint paint;
    public drawData postData;
    public GeoPoint postGeoPoint;
    public boolean isPressed = false;
    private ConstraintLayout popupParentLayout;


    public RadiusPost(MapView mapView, drawData newDrawData, ConstraintLayout parentLayout) {
        super(mapView);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        postData = newDrawData;
        popupParentLayout = parentLayout;
        GeoPoint messageLocation = new GeoPoint( postData.getLocationY(), postData.getLocationX());
        postGeoPoint = messageLocation;

        Random rNumber = new Random();
        int a = 1;
        int r = 1 + (255- 1) * rNumber.nextInt();
        int g = 1 + (255- 1) * rNumber.nextInt();
        int b = 1 + (255- 1) * rNumber.nextInt();


        //Check version to see if we need to use an old way of getting the custom color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            paint.setARGB(a,r,g,b);
        }
        else {
            paint.setARGB(a,r,g,b);
        }
    }

    public void drawMapPost(MapView mapView){

        paint.setAlpha(130);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            paint.setBlendMode(BlendMode.PLUS);
        }
        this.setPoints(Polygon.pointsAsCircle(postGeoPoint, postData.getRadius()));

        this.setFillColor(paint.getColor());
        this.setStrokeColor(paint.getColor());
        this.setStrokeWidth(1);

        mapView.getOverlays().add(this);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event, MapView mapView){
        //Return true so the little window does not pop up, may be able to use little window in future
        return true;
        //return super.onSingleTapConfirmed(event, mapView);
    }
    //TODO use map event on main activity and cycle through each open first popup
    public boolean isInRadius(MotionEvent event, MapView mapView)
    {
        Projection projection = mapView.getProjection();
        GeoPoint eventPoint = (GeoPoint) projection.fromPixels((int) event.getX(), (int) event.getY());

        Location eventLocation = new Location("");
        eventLocation.setLongitude(eventPoint.getLongitude());
        eventLocation.setLatitude(eventPoint.getLatitude());

        Location postLocation = new Location("");
        postLocation.setLongitude(postGeoPoint.getLongitude());
        postLocation.setLatitude(postGeoPoint.getLatitude());

        double distanceInMeters = eventLocation.distanceTo(postLocation);

        if(distanceInMeters <= postData.getRadius()){
            return true;
        }

        return false;
    }

    public void openPostPopup() {
        LayoutInflater inflater = (LayoutInflater) RadiusMessage.getAppInstance().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View newMessagePopup = inflater.inflate(R.layout.post_message, null);
        imageDownloaded = (ImageView) newMessagePopup.findViewById(R.id.imageDownloaded);
        saveButton = (ImageView) newMessagePopup.findViewById(R.id.saveButton);

        if (postData.getImage().compareTo("") != 0) {
//            saveButton.setImageResource(R.drawable.ic_save);
            imageDownloaded.setImageBitmap(getBitmapFromString(postData.getImage()));
        }

        int width = 920;
        int height = 1800;
        boolean focusable = true;

        final PopupWindow window = new PopupWindow(newMessagePopup, width, height, focusable);
        window.showAtLocation(popupParentLayout, Gravity.CENTER, 0,0);

        //Add text to popup from user input
        TextView currentText = window
                .getContentView()
                .findViewById(R.id.messageTextView);

        currentText.setText(postData.getUserMessageText());

        View.OnClickListener saveButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File path = Environment.getExternalStorageDirectory();
                File dir = new File(path+"/RadiusMessage/");
                dir.mkdirs();

                File file = new File(dir, "new.png");

                OutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                    getBitmapFromString(postData.getImage()).compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText( RadiusMessage.getAppInstance().getApplicationContext()
                        , path.toString()
                        , Toast.LENGTH_SHORT).show();
            }
        };
        saveButton.setOnClickListener(saveButtonListener);

        newMessagePopup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                window.dismiss();

                return true;
            }
        });

        Toast.makeText( RadiusMessage.getAppInstance().getApplicationContext()
                , "Post Opened."
                , Toast.LENGTH_SHORT).show();
    }

    private Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

}
