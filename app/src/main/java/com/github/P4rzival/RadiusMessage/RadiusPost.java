package com.github.P4rzival.RadiusMessage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

import java.util.Random;

public class RadiusPost extends Marker {

    private Paint paint;
    public boolean isPressed = false;
    private drawData postData;

    public RadiusPost(MapView mapView, drawData newDrawData) {
        super(mapView);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        postData = newDrawData;

        Random rNumber = new Random();
        int a = 50 + (105 - 1) * rNumber.nextInt();
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

    @Override
    public void draw(Canvas canvas, MapView map, boolean shadow){
        if(!isEnabled()) return;


        canvas.drawCircle((float) 0
                , (float)0
                , (float) postData.getRadius(),
                paint);
    }
}
