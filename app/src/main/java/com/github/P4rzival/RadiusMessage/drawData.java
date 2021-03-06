package com.github.P4rzival.RadiusMessage;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "postData")
public class drawData
{

    @PrimaryKey(autoGenerate = true)
    boolean imageCheck;
    private int id;
    private String userMessageText;
    private double radius;
    private double locationX;
    private double locationY;
    private long messageDuration;
    private long messageDelay;
    private String userMessageImage;
    private boolean isCollectedByUser;
    //Overloading for options when creating drawData or if we need to update one.

    public drawData(String userMessageText, Double radius, Double locationX, Double locationY, long messageDuration, boolean imageCheck) {
        this.userMessageText = userMessageText;
        this.radius = radius;
        this.locationX = locationX;
        this.locationY = locationY;
        this.messageDuration = messageDuration;
        this.isCollectedByUser = false;
        this.imageCheck = imageCheck;
    }
    @Ignore
    public drawData() {
    }

    public boolean getImageCheck(){
        return this.imageCheck;
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

    public String getUserMessageImage() {return userMessageImage;}

    //Used for drawData creation
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

    public void setUserMessageImage(String imageArray){this.userMessageImage = imageArray;}

    public boolean isCollectedByUser() {
        return isCollectedByUser;
    }

    public void setCollectedByUser(boolean collectedByUser) {
        isCollectedByUser = collectedByUser;
    }

    public long getMessageDelay() {
        return messageDelay;
    }

    public void setMessageDelay(long messageDelay) {
        this.messageDelay = messageDelay;
    }
}
