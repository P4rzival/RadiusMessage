package com.github.P4rzival.RadiusMessage;

import org.json.JSONObject;

public class DatabaseAccessor {

    public DatabaseAccessor() { }

    public static void databaseFakeRequestApproval(JSONObject jsonObject) {
        postRequestSupervisor.PRSContinueTrue();
    }

    public static void databaseRequestApproval(JSONObject jsonObject) {
        databaseFakeRequestApproval(jsonObject);
    }

}
