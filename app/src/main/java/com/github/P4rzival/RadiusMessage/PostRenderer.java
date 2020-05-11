package com.github.P4rzival.RadiusMessage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class PostRenderer extends ViewModel {

    private DrawDataRepository drawRepository;
    private LiveData<List<drawData>> allPostDrawData;
    public ArrayList<Post> postList = new ArrayList<Post>();
    public ArrayList<RadiusPost> radiusPosts = new ArrayList<RadiusPost>();

    public PostRenderer(){
        drawRepository = new DrawDataRepository(RadiusMessage.getAppInstance());
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
