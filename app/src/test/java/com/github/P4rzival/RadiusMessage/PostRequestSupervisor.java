package com.github.P4rzival.RadiusMessage;

import org.json.*;

public class PostRequestSupervisor {

    private boolean shouldContinue = false;

    public PostRequestSupervisor() {
        prsContinueFalse();
    }

    void PRS(JSONObject newPost) {
        while (prsContinue() != true) ;

        PostRenderer(newPost, npApprover(newPost));
    }

    boolean prsContinue() {
        return this.shouldContinue;
    }

    boolean prsContinueTrue() {
        return this.shouldContinue = true;
    }

    boolean prsContinueFalse() {
        return this.shouldContinue = false;
    }

    boolean prsContinueToggle() {
        return this.shouldContinue = !this.shouldContinue;
    }
}
