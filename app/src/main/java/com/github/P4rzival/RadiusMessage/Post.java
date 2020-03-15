package com.github.P4rzival.RadiusMessage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;

import java.util.Random;

public class Post extends View {

    private Paint paint;
    private Color color;
    public boolean isPressed = false;
    private drawData postData;

    public Post(Context context, drawData newDrawData) {
        super(context);

        paint = new Paint();
        postData = newDrawData;

        Random rNumber = new Random();
        int a = 100;
        int r = rNumber.nextInt(256);
        int g = rNumber.nextInt(256);
        int b = rNumber.nextInt(256);

        color = new Color();
        color.alpha(a);
        color.red(r);
        color.green(g);
        color.blue(b);

        //Check version to see if we need to use an old way of getting the custom color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            paint.setColor(color.toArgb());
        }
        else {
                paint.setColor(Color.parseColor(color.toString()));
            }
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawCircle((float) postData.getLocationX()
                , (float) postData.getLocationY()
                , (float) postData.getRadius(),
                paint);
    }
}
