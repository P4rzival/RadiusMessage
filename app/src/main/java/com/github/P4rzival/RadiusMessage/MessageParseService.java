package com.github.P4rzival.RadiusMessage;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;


public class MessageParseService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MessageParseService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent messageIntent)
    {

    }
}
