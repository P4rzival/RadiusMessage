package com.github.P4rzival.RadiusMessage;

import org.json.*;

public class RequestGenerator {

    public static void generateRequest(String userTextMessage,
                                       double radius,
                                       long messageDuration) {

        JSONObject newPost = new JSONObject();

        try {
            newPost.put("userTextMessage", userTextMessage);
            newPost.put("radius", radius);
            newPost.put("locationX", null);
            newPost.put("locationY", null);
            newPost.put("messageDuration", messageDuration);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        new PostRequestSupervisor(newPost);
    }
}
