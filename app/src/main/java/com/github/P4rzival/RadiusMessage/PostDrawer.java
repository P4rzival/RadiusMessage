package com.github.P4rzival.RadiusMessage;

import org.json.JSONException;
import org.json.JSONObject;


//simple class to run a task in background in order to
//parse a JSON object, sends new drawData to the drawRepo
//Probably should change to intentService in another sprint!!
//or better pass application context from the input activity
//all the way back to the requestSupervisor for a much better
// way of doing things!
public class PostDrawer {

    private drawDataRepository drawRepo;

    public PostDrawer() {
        drawRepo = new drawDataRepository(RadiusMessage.getAppInstance());
    }

    public drawDataRepository getDrawRepo() {
        return drawRepo;
    }

    //Async Task
    /*public void createPost(JSONObject newPostJSON)
    {

    }*/

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
}
