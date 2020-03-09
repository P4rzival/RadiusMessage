package com.github.P4rzival.RadiusMessage;

import org.json.*;

import java.io.FileWriter;

public class requestGenerator {

    void rGen(String postText, int postRadius, int postExpDelay)
    {
        try {
            JSONObject newPost = new JSONObject();
            newPost.put("postText", postText);
            newPost.put("postRadius", postRadius);
            newPost.put("postExpirationDelay", postExpDelay);
        }
        catch (JSONException e) {
        e.printStackTrace();
    }
    }
}
