package com.github.P4rzival.RadiusMessage;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "postData")
public class DrawData
{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userMessageText;
    private double radius;
    private double locationX;
    private double locationY;
    private long messageDuration;

    public DrawData(String userMessageText, float radius, float locationX, float locationY, long messageDuration) {
        this.userMessageText = userMessageText;
        this.radius = radius;
        this.locationX = locationX;
        this.locationY = locationY;
        this.messageDuration = messageDuration;
    }

    public DrawData() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUserMessageText() {
        return userMessageText;
    }

    public double getRadius() {
        return radius;
    }

    public double getLocationX() {
        return locationX;
    }

    public double getLocationY() {
        return locationY;
    }

    public long getMessageDuration() {
        return messageDuration;
    }

    //Used for DrawData creation
    public void setUserMessageText(String userMessageText) {
        this.userMessageText = userMessageText;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setLocationX(double locationX) {
        this.locationX = locationX;
    }

    public void setLocationY(double locationY) {
        this.locationY = locationY;
    }

    public void setMessageDuration(long messageDuration) {
        this.messageDuration = messageDuration;
    }
}