package com.github.P4rzival.RadiusMessage;

import org.json.JSONObject;

public class DatabaseAccessor {

    public DatabaseAccessor() { }

    public static void databaseFakeRequestApproval(PostRequestSupervisor postRequestSupervisor) {
        postRequestSupervisor.prsContinueTrue();
    }

    public static void databaseRequestApproval(PostRequestSupervisor postRequestSupervisor) {
        databaseFakeRequestApproval(postRequestSupervisor);
    }

}
