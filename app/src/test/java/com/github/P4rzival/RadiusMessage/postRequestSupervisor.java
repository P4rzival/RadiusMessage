package com.github.P4rzival.RadiusMessage;

import org.json.*;

public class postRequestSupervisor {
    void PRS(JSONObject newPost) {
        while (PRSContinue() != true) ;

        postRenderer(newPost, npApprover(newPost));
    }
}
