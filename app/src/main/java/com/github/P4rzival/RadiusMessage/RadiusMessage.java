package com.github.P4rzival.RadiusMessage;

import android.app.Application;

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
