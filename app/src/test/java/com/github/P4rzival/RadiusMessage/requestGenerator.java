package com.github.P4rzival.RadiusMessage;

import org.json.*;

public class requestGenerator {

    public JSONObject newPost = new JSONObject();

    void rGen(String postText, int postRadius, int postExpDelay) {
        try {
            newPost.put("postText", postText);
            newPost.put("postRadius", postRadius);
            newPost.put("postExpirationDelay", postExpDelay);
        }
        catch (JSONException e) {
        e.printStackTrace();
        }

        postRequestSupervisor postRequestSupervisor = new postRequestSupervisor();
        PRS(newPost);
        DatabaseAccessor.databaseRequestApproval(newPost);
    }
}
