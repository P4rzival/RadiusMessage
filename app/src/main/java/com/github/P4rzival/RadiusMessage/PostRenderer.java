package com.github.P4rzival.RadiusMessage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class PostRenderer extends ViewModel {

    private DrawDataRepository drawRepository;
    private LiveData<List<DrawData>> allPostDrawData;
    public ArrayList<Post> postList = new ArrayList<Post>();

    public PostRenderer(){
        drawRepository = new DrawDataRepository(RadiusMessage.getAppInstance());
        allPostDrawData = drawRepository.getAll();
    }

    //View can use some repo methods when needed
    public void insert(DrawData currentDrawData) {
        drawRepository.insert(currentDrawData);
    }

    public void delete(DrawData currentDrawData){
        drawRepository.delete(currentDrawData);
    }

    public void deleteAllData(){
        drawRepository.deleteAllDrawData();
    }

    public LiveData<List<DrawData>> getAllPostDrawData(){
        return allPostDrawData;
    }

    public void updatePostDataList(){
        allPostDrawData = drawRepository.getAll();
    }


}
