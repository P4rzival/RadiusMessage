package com.github.P4rzival.RadiusMessage;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "postData")
public class drawData
{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userMessageText;
    private float radius;
    private float locationX;
    private float locationY;
    private long messageDuration;

    public drawData(String userMessageText, float radius, float locationX, float locationY, long messageDuration) {
        this.userMessageText = userMessageText;
        this.radius = radius;
        this.locationX = locationX;
        this.locationY = locationY;
        this.messageDuration = messageDuration;
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

    public float getRadius() {
        return radius;
    }

    public float getLocationX() {
        return locationX;
    }

    public float getLocationY() {
        return locationY;
    }

    public long getMessageDuration() {
        return messageDuration;
    }

}
