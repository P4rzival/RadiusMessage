package com.github.P4rzival.RadiusMessage;

import org.json.*;

public class RequestGenerator {

    public static void generateRequest(String postText, int postRadius, int postExpDelay) {
        JSONObject newPost = new JSONObject();

        try {
            newPost.put("postText", postText);
            newPost.put("postRadius", postRadius);
            newPost.put("postExpirationDelay", postExpDelay);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        new PostRequestSupervisor(newPost);
    }
}
