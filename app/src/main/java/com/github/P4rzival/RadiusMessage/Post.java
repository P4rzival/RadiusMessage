package com.github.P4rzival.RadiusMessage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

public class Post extends View implements View.OnClickListener {

    private Paint paint;
    public boolean isPressed = false;
    private drawData postData;
    private OnClickListener listener;

    public Post(Context context, drawData newDrawData) {
        super(context);

        setOnClickListener(this);
        setClickable(true);

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
    protected void onDraw(Canvas canvas){
        canvas.drawCircle((float) postData.getLocationX()
                , (float) postData.getLocationY()
                , (float) postData.getRadius(),
                paint);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), "On click.", Toast.LENGTH_SHORT).show();
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public drawData getPostData() {
        return postData;
    }
}
