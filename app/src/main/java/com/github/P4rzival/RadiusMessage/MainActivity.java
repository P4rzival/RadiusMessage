package com.github.P4rzival.RadiusMessage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public ConstraintLayout parentLayout;
    public PostRenderer postRenderer;
    private Button testPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Need this in main activity for postRenderer to work.
        //Need the parent layout
        parentLayout = findViewById(R.id.parentLayout);

        //Keep this here for updating the posts
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
                JSONObject test = new JSONObject();
                Random rNum = new Random();
                try {
                    test.put("userTextMessage", "ITs WORKING!");
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

    //Need this in main activity for now
    public void addPostToView(drawData newPostDrawData){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Post newPost = new Post(getApplicationContext(), newPostDrawData);
        parentLayout.addView(newPost);
    }
}
