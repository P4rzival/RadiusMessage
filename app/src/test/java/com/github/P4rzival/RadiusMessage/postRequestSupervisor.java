package com.github.P4rzival.RadiusMessage;

import org.json.*;

public class postRequestSupervisor {

    boolean shouldContinue = false;

    public postRequestSupervisor() {
        shouldContinue = false;
    }

    void PRS(JSONObject newPost) {
        while (prsContinue() != true) ;

        postRenderer(newPost, npApprover(newPost));
    }

    private boolean prsContinue() {
        return this.shouldContinue;
    }
}
