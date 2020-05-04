package com.github.P4rzival.RadiusMessage;

import org.json.*;

public class RequestGenerator {

    public static void generateRequest(JSONObject jsonObject) {
        new PostRequestSupervisor(jsonObject);
    }
}
