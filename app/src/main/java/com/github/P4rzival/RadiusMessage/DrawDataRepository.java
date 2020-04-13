package com.github.P4rzival.RadiusMessage;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DrawDataRepository {

    private DrawDataDatabaseAccessorObject drawDao;
    private LiveData<List<DrawData>> allPosts;

    public DrawDataRepository(Application application){
        DrawDataLocalDatabase localDatabase = DrawDataLocalDatabase.getInstance(application);
        drawDao = localDatabase.drawDao();
        if(localDatabase != null && drawDao != null){
            allPosts = drawDao.getAll();
        }
    }

    public void insert(DrawData currentDrawData){
        new InsertDrawData(drawDao).execute(currentDrawData);
    }

    public void delete(DrawData currentDrawData){
        new DeleteDrawData(drawDao).execute(currentDrawData);
    }

    public void deleteAllDrawData(){
        new DeleteAllDrawData(drawDao).execute();
    }

    public LiveData<List<DrawData>> getAll(){
        return allPosts;
    }

    private static class InsertDrawData extends AsyncTask<DrawData, Void, Void>{
        private DrawDataDatabaseAccessorObject drawDao;

        public InsertDrawData(DrawDataDatabaseAccessorObject drawDao) {
            this.drawDao = drawDao;
        }

        @Override
        protected Void doInBackground(DrawData... drawData) {
            drawDao.insert(drawData[0]);
            return null;
        }
    }

    private static class DeleteDrawData extends AsyncTask<DrawData, Void, Void>{
        private DrawDataDatabaseAccessorObject drawDao;

        public DeleteDrawData(DrawDataDatabaseAccessorObject drawDao) {
            this.drawDao = drawDao;
        }

        @Override
        protected Void doInBackground(DrawData... drawData) {
            drawDao.delete(drawData[0]);
            return null;
        }
    }

    private static class DeleteAllDrawData extends AsyncTask<Void, Void, Void>{
        private DrawDataDatabaseAccessorObject drawDao;

        public DeleteAllDrawData(DrawDataDatabaseAccessorObject drawDao) {
            this.drawDao = drawDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            drawDao.deleteAllDrawData();
            return null;
        }
    }
}
