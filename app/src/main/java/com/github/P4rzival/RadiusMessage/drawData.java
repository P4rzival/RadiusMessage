package com.github.P4rzival.RadiusMessage;

import java.io.Serializable;

public class drawData implements Serializable
{
    //If Serializable is too slow we can swtich to Parcelable for inbetween objects
    public drawData(String message, float messageRadius, long timestamp)
    {
        userMessageText = message;
        radius = messageRadius;
        messageDuration = timestamp;
    }

    public String getUserMessageText() {
        return userMessageText;
    }

    public void setUserMessageText(String userMessageText) {
        this.userMessageText = userMessageText;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public long getMessageDuration() {
        return messageDuration;
    }

    public void setMessageDuration(long messageDuration) {
        this.messageDuration = messageDuration;
    }

    //Small object class to pass data to activities
    private String userMessageText;
    private float radius;
    //Assuming duration time will be stored as a long, subject to change
    private long messageDuration;



}
