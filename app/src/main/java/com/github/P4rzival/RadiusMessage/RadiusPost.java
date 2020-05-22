package com.github.P4rzival.RadiusMessage;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.P4rzival.RadiusMessage.PostDesign.ThemePicker;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polygon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RadiusPost extends Polygon {

    PostImage postImage;
    public Paint paint;
    public drawData postData;
    public GeoPoint postGeoPoint;
    public boolean isPressed = false;
    private ConstraintLayout popupParentLayout;
    private static final float MIN_BRIGHTNESS = 0.8f;

    ImageView imageView;
    ImageView saveButton;

    public RadiusPost(MapView mapView, drawData newDrawData, ConstraintLayout parentLayout) {
        super(mapView);

        postImage = new PostImage();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        postData = newDrawData;
        popupParentLayout = parentLayout;
        GeoPoint messageLocation = new GeoPoint( postData.getLocationY(), postData.getLocationX());
        postGeoPoint = messageLocation;

    }


    public void drawMapPost(MapView mapView){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            paint.setColor(new ThemePicker().getRandomColorFromPalatte());
        }else {
            generateRandomBrightColor();
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            paint.setBlendMode(BlendMode.PLUS);
        }else {
            paint.setColorFilter(new PorterDuffColorFilter(paint.getColor(), PorterDuff.Mode.SRC_ATOP));
            //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));

        }

        this.setPoints(Polygon.pointsAsCircle(postGeoPoint, postData.getRadius()));

        this.setFillColor(paint.getColor());
        this.setStrokeColor(paint.getColor());
        this.setStrokeWidth(1);

        mapView.getOverlays().add(this);
    }

    private void generateRandomBrightColor(){
        Random rNumber = new Random();
        int r = 1 + (255- 200) * rNumber.nextInt();
        int g = 1 + (255- 200) * rNumber.nextInt();
        int b = 1 + (255- 50) * rNumber.nextInt();
        paint.setARGB(1,r,g,b);
        paint.setAlpha(130);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event, MapView mapView){
        //Return true so the little window does not pop up, may be able to use little window in future
        return true;
        //return super.onSingleTapConfirmed(event, mapView);
    }

    public void openPostPopup(){

        LayoutInflater inflater = (LayoutInflater) RadiusMessage.getAppInstance().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newMessagePopup = inflater.inflate(R.layout.post_message, null);
        saveButton = (ImageView) newMessagePopup.findViewById(R.id.saveButton);

        int width = 920;
        int height = 1800;
        boolean focusable = true;

        final PopupWindow window = new PopupWindow(newMessagePopup, width, height, focusable);
        window.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        window.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        window.showAtLocation(popupParentLayout, Gravity.CENTER, 0,0);

        //Populate message content
        if(postImage.StringToBitMap(postData.getUserMessageImage()) != null){
            imageView = window.getContentView().findViewById(R.id.messageImageView);
            imageView.setImageBitmap(postImage.StringToBitMap(postData.getUserMessageImage()));
            saveButton.setImageResource(R.drawable.ic_save);
        }

        TextView currentText = window
                .getContentView()
                .findViewById(R.id.messageTextView);

        currentText.setText(postData.getUserMessageText());


        View.OnClickListener saveButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postImage.savePostImage(postImage.StringToBitMap(postData.getUserMessageImage()));
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
}
