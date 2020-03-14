package com.github.P4rzival.RadiusMessage;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class drawDataRepository {

    private drawDataDao drawDao;
    private LiveData<List<drawData>> allPosts;

    public drawDataRepository(Application application){
        drawDataLocalDatabase localDatabase = drawDataLocalDatabase.getInstance(application);
        drawDao = localDatabase.drawDao();
        //allPosts = drawDao.getAll();
    }

    //These are local database operations for the posts
    //have to run these in a background thread(Async) or else
    //it has the potential to freeze up the application
    public void insert(drawData currentDrawData){

    }

    public void delete(drawData currentDrawData){

    }

    public void deleteAllDrawData(){

    }

    public LiveData<List<drawData>> getAll(){
        return allPosts;
    }
}
