package com.github.P4rzival.RadiusMessage;

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

    //Async Task
    /*public void createPost(JSONObject newPostJSON)
    {

    }*/
}
