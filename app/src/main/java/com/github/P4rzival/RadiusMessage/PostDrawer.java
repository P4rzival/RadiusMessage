package com.github.P4rzival.RadiusMessage;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;


//TODO possibly change to intentService if time permits
public class PostDrawer {

    private DrawDataRepository drawRepo;
    private drawData currentDrawData;

    public PostDrawer() {

        drawRepo = new DrawDataRepository(RadiusMessage.getAppInstance());
    }

    public DrawDataRepository getDrawRepo() {
        return drawRepo;
    }

    public void setCurrentDrawData(drawData currentDrawData) {
        this.currentDrawData = currentDrawData;
    }

    public void createPost(JSONObject newPostJSON)
    {
        new parsePostJSONAsync().execute(newPostJSON);
    }

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



    private class parsePostJSONAsync extends AsyncTask<JSONObject, Void, drawData>{

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
    }
}
