package com.github.P4rzival.RadiusMessage;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class drawDataRepository {

    private drawDataDao drawDao;
    private LiveData<List<drawData>> allPosts;

    public drawDataRepository(Application application){
        drawDataLocalDatabase localDatabase = drawDataLocalDatabase.getInstance(application);
        drawDao = localDatabase.drawDao();
        if(localDatabase != null && drawDao != null){
            allPosts = drawDao.getAll();
        }
    }

    //These are local database operations for the posts
    //have to run these in a background thread(Async) or else
    //it has the potential to freeze up the application
    public void insert(drawData currentDrawData){
        new InsertDrawData(drawDao).execute(currentDrawData);
    }

    public void delete(drawData currentDrawData){
        new DeleteDrawData(drawDao).execute(currentDrawData);
    }

    public void deleteAllDrawData(){
        new DeleteAllDrawData(drawDao).execute();
    }

    public LiveData<List<drawData>> getAll(){
        return allPosts;
    }

    //AysncTasks for Repo, Room will autogenerate actual command code that goes in them
    //PostDrawer will have a more fleshed out AsyncTask usage
    private static class InsertDrawData extends AsyncTask<drawData, Void, Void>{
        private drawDataDao drawDao;

        public InsertDrawData(drawDataDao drawDao) {
            this.drawDao = drawDao;
        }

        @Override
        protected Void doInBackground(drawData... drawData) {
            drawDao.insert(drawData[0]);
            return null;
        }
    }

    private static class DeleteDrawData extends AsyncTask<drawData, Void, Void>{
        private drawDataDao drawDao;

        public DeleteDrawData(drawDataDao drawDao) {
            this.drawDao = drawDao;
        }

        @Override
        protected Void doInBackground(drawData... drawData) {
            drawDao.delete(drawData[0]);
            return null;
        }
    }

    private static class DeleteAllDrawData extends AsyncTask<Void, Void, Void>{
        private drawDataDao drawDao;

        public DeleteAllDrawData(drawDataDao drawDao) {
            this.drawDao = drawDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            drawDao.deleteAllDrawData();
            return null;
        }
    }
}
