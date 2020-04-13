package com.github.P4rzival.RadiusMessage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

import java.lang.Math;

public class MainActivity extends AppCompatActivity {

    public ConstraintLayout parentLayout;
    public PostRenderer postRenderer;
    private Button testPostButton;
    private Post currentPost;

    private float lastTouchX;
    private float lastTouchY;

    //May need for later
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Need this in main activity for postRenderer to work.
        parentLayout = findViewById(R.id.parentLayout);

        postRenderer = new ViewModelProvider(this).get(PostRenderer.class);
        //For the Demo
        postRenderer.deleteAllData();
        postRenderer.getAllPostDrawData().observe(this, new Observer<List<DrawData>>() {
            @Override
            public void onChanged(List<DrawData> drawData) {
                if(drawData != null && drawData.size() > 0)
                {
                    addPostToView(drawData.get(drawData.size()-1));
                    onClickPosts();
                }
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
                JSONObject test = new JSONObject();
                Random rNum = new Random();
                try {
                    //CHNAGE THIS LINE FOR DIFFERENT TEXT!
                    test.put("userTextMessage", String.valueOf(rNum.nextInt(200)));
                    test.put("radius", 15 + (200 - 15) * rNum.nextDouble());
                    test.put("locationX", 1 + (1200 - 1) * rNum.nextDouble());
                    test.put("locationY", 1 + (1300 - 1) * rNum.nextDouble());
                    test.put("messageDuration", 1000);
                }catch (JSONException e){
                    e.printStackTrace();
                }

                PostDrawer postDrawer = new PostDrawer();
                postDrawer.createPost(test);
            }
        });
    }


    public void addPostToView(DrawData newPostDrawData){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Post newPost = new Post(getApplicationContext(), newPostDrawData);
        parentLayout.addView(newPost);
        postRenderer.postList.add(newPost);
    }

    public void onClickPosts(){
        for (int i = 0; i < postRenderer.postList.size(); i++) {
            currentPost = postRenderer.postList.get(i);
            View.OnTouchListener touchListener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        lastTouchX = motionEvent.getX();
                        lastTouchY = motionEvent.getY();
                        Post postToOpen = isInRadius(lastTouchX, lastTouchY);
                        if (postToOpen != null) {
                            openPostMessage(postToOpen);
                        }
                    }
                    return false;
                }
            };
            currentPost.setOnTouchListener(touchListener);
            currentPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public Post isInRadius(float touchX, float touchY){

        Post postToOpen = null;

        float distance;

        for (int i = 0; i < postRenderer.postList.size(); i++){
            DrawData postData = postRenderer.postList.get(i).getPostData();

            distance = (float) (Math.pow(touchX - (float) postData.getLocationX(),2) +
                    Math.pow(touchY - (float) postData.getLocationY(), 2));

            if(Math.sqrt(distance) <= postData.getRadius()){
                postToOpen = postRenderer.postList.get(i);
            }
        }
        return postToOpen;
    }

    public void openPostMessage(Post currentPost){

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newMessagePopup = inflater.inflate(R.layout.post_message, null);

        int width = 920;
        int height = 1400;
        boolean focusable = true;

        final PopupWindow window = new PopupWindow(newMessagePopup, width, height, focusable);
        window.showAtLocation(parentLayout, Gravity.CENTER, 0,0);

        TextView currentText = window
                .getContentView()
                .findViewById(R.id.messageTextView);

        currentText.setText(currentPost.getPostData().getUserMessageText());
        newMessagePopup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                window.dismiss();
                return true;
            }
        });

        Toast.makeText(getApplicationContext()
                , "Post Opened."
                , Toast.LENGTH_SHORT).show();

    }

}
