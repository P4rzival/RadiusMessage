package com.github.P4rzival.RadiusMessage;

import org.json.*;

public class postRequestSupervisor {

    boolean shouldContinue = false;

    public postRequestSupervisor() {
        shouldContinue = false;
    }

    void PRS(JSONObject newPost) {
        while (prsContinue() != true) ;

        PostRenderer(newPost, npApprover(newPost));
    }

    private boolean prsContinue() {
        return this.shouldContinue;
    }

    private boolean prsContinueTrue() {
        return this.shouldContinue = true;
    }

    private boolean prsContinueFalse() {
        return this.shouldContinue = false;
    }

    private boolean prsContinueToggle() {
        return this.shouldContinue = !this.shouldContinue;
    }
}