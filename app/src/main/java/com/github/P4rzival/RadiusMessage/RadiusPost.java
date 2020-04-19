package com.github.P4rzival.RadiusMessage;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import org.osmdroid.views.overlay.Polygon;

import java.util.Random;

public class RadiusPost extends Polygon {

    public Paint paint;
    public boolean isPressed = false;
    private drawData postData;
    private ConstraintLayout popupParentLayout;

    public RadiusPost(MapView mapView, drawData newDrawData, ConstraintLayout parentLayout) {
        super(mapView);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        postData = newDrawData;
        popupParentLayout = parentLayout;

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

    public drawData getPostData() {
        return postData;
    }

    public void drawMapPost(GeoPoint messageLocation, MapView mapView){
        this.setPoints(Polygon.pointsAsCircle(messageLocation, postData.getRadius()));
        this.setFillColor(paint.getColor());
        this.setStrokeColor(paint.getColor());
        this.setStrokeWidth(1);
        mapView.getOverlays().add(this);
        mapView.invalidate();
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event, MapView mapView){
        if(contains(event)){
           openPostPopup();
            return true;
        }

        return super.onSingleTapConfirmed(event, mapView);
    }

    public void openPostPopup(){

        LayoutInflater inflater = (LayoutInflater) RadiusMessage.getAppInstance().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newMessagePopup = inflater.inflate(R.layout.post_message, null);

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
