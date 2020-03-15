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

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public ConstraintLayout parentLayout;
    public PostRenderer postRenderer;
    private Button testPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Need the parent layout
        parentLayout = findViewById(R.id.parentLayout);

        //Keep this here for updating the posts
        postRenderer = new ViewModelProvider(this).get(PostRenderer.class);
        postRenderer.getAllPostDrawData().observe(this, new Observer<List<drawData>>() {
            @Override
            public void onChanged(List<drawData> drawData) {
                if(drawData != null)
                {
                    addPostToView(drawData.get(drawData.size()-1));
                }
                Toast.makeText(MainActivity.this
                        , "Post Map Updated"
                        , Toast.LENGTH_SHORT).show();
            }
        });

        testPostButton = findViewById(R.id.postButton);
        testPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject test = new JSONObject();
                PostDrawer postDrawer = new PostDrawer();
                postDrawer.createPost(test);
            }
        });
    }

    public void addPostToView(drawData newPostDrawData){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Post newPost = new Post(getApplicationContext(), newPostDrawData);
        parentLayout.addView(newPost);
    }
}
