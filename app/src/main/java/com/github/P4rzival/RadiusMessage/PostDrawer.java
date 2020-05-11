package com.github.P4rzival.RadiusMessage;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


//simple class to run a task in background in order to
//parse a JSON object, sends new drawData to the drawRepo
//Probably should change to intentService in another sprint!!
//or better pass application context from the input activity
//all the way back to the requestSupervisor for a much better
// way of doing things!
public class PostDrawer {

    private drawDataRepository drawRepo;
    private drawData currentDrawData;

    public PostDrawer() {

        drawRepo = new drawDataRepository(RadiusMessage.getAppInstance());
    }

    public drawDataRepository getDrawRepo() {
        return drawRepo;
    }

    public void setCurrentDrawData(drawData currentDrawData) {
        this.currentDrawData = currentDrawData;
    }

    public void clearPostList(){
        new clearPostListAsync().execute(drawRepo);
    }

    //Async Task Call in createPost
    public void createPost(JSONObject newPostJSON)
    {
        new parsePostJSONAsync().execute(newPostJSON);
    }

    //Seperated out the doInbackground logic from the AsyncTask so I could Unit Test it
    public drawData parsePostJSON(JSONObject newPostJSON) throws JSONException {

        drawData newData = new drawData();

        String message = newPostJSON.getString("userTextMessage");
        double radius = newPostJSON.getDouble("radius");
        double locX = newPostJSON.getDouble("locationX");
        double locY = newPostJSON.getDouble("locationY");
        long messageDur = newPostJSON.getLong("messageDuration");

        newData.setUserMessageText(message);
        newData.setRadius(radius);
        newData.setLocationX(locX);
        newData.setLocationY(locY);
        newData.setMessageDuration(messageDur);

        return newData;
    }

    //Use when refreshing server
    public void parsePostJSONList(List<JSONObject> serverPostList) throws JSONException{

        clearPostList();

        for (int i = 0; i <= serverPostList.size(); i++){

            parsePostJSON(serverPostList.get(i));
        }
    }

    //The AsynTask class acts as sort of a function object
    //I made mine private so that the public createPost method was the only way to call this.
    //It takes in a JSONObject, No updates so third type is Void, Returns a drawData
    private class parsePostJSONAsync extends AsyncTask<JSONObject, Void, drawData>{



        //Had to surrond with try and catch block, but usually you might not need to
        //The stuff you need to do in the background goes in here.
        //You can add a constructor or preExecute method
        //to setup anything before doing the background thread work.
        @Override
        protected drawData doInBackground(JSONObject... jsonObjects) {
            drawData newDrawData;
            try {
                newDrawData = parsePostJSON(jsonObjects[0]);
                //Figure this fix out later
                setCurrentDrawData(newDrawData);
                drawRepo.insert(newDrawData);
                return newDrawData;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        //Different ways to return data, I just set private drawData variable but
        //there is a way to use a public AsyncTask Class and an interface to have
        //return when and where the AsyncTask class was used.
        //This page here explains it:
        //https://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a


        @Override
        protected void onPostExecute(drawData drawData) {
            //setCurrentDrawData(drawData);
        }
    }

    private class clearPostListAsync extends  AsyncTask<drawDataRepository, Void, Void>{

        @Override
        protected Void doInBackground(drawDataRepository... drawDataRepositories){
            drawDataRepositories[0].deleteAllDrawData();
            return null;
        }
    }
}
