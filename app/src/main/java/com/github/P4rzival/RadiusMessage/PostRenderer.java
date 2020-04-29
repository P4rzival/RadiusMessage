package com.github.P4rzival.RadiusMessage;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

//https://medium.com/@elye.project/understanding-live-data-made-simple-a820fcd7b4d0
//good intro to the new ways to go about creating an android architecture
//In process of reworking some things, so it is a bit empty.
public class PostRenderer extends ViewModel {

    private drawDataRepository drawRepository;
    private LiveData<List<drawData>> allPostDrawData;
    public ArrayList<Post> postList = new ArrayList<Post>();
    public ArrayList<RadiusPost> radiusPosts = new ArrayList<RadiusPost>();

    public PostRenderer(){
        drawRepository = new drawDataRepository(RadiusMessage.getAppInstance());
        allPostDrawData = drawRepository.getAll();
    }

    //View can use some repo methods when needed
    public void insert(drawData currentDrawData) {
        drawRepository.insert(currentDrawData);
    }

    public void delete(drawData currentDrawData){
        drawRepository.delete(currentDrawData);
    }

    public void deleteAllData(){
        drawRepository.deleteAllDrawData();
    }

    public LiveData<List<drawData>> getAllPostDrawData(){
        return allPostDrawData;
    }

    public void updatePostDataList(){
        allPostDrawData = drawRepository.getAll();
    }


}
