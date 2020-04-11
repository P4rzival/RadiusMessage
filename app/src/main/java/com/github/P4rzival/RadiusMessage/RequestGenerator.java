package com.github.P4rzival.RadiusMessage;

import org.json.*;

import java.util.*;

public class RequestGenerator {

    public static JSONObject generateRequest(String userTextMessage, double radius, long postDuration) {
        Random rNum = new Random();
        JSONObject newPost = new JSONObject();

        try {
            newPost.put("userTextMessage", userTextMessage);
            newPost.put("radius", radius);
            newPost.put("locationX", 1 + (1200 - 1) * rNum.nextDouble());
            newPost.put("locationY", 1 + (1200 - 1) * rNum.nextDouble());
            newPost.put("messageDuration", postDuration);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        new PostRequestSupervisor(newPost);
        return newPost;
}
}
