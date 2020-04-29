package com.github.P4rzival.RadiusMessage;

import android.app.Application;

//Class to the application, rather would pass the context
// through the hierarchy of modules but for sprint 1
// this will have to do. Temporary Solution
public class RadiusMessage extends Application {

    private static RadiusMessage AppInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        AppInstance = this;
    }

    public static RadiusMessage getAppInstance(){
        return RadiusMessage.AppInstance;
    }
}
