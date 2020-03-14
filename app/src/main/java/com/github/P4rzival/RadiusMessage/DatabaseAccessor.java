package com.github.P4rzival.RadiusMessage;

import org.json.JSONObject;

public class DatabaseAccessor {

    public DatabaseAccessor(JSONObject jsonObject) { }

    public static void databaseFakeRequestApproval() {
        postRequestSupervisor.PRSContinueTrue();
    }

    public static void databaseRequestApproval() {

    }

}
