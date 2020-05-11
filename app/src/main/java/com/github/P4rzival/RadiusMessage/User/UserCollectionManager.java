package com.github.P4rzival.RadiusMessage.User;

import androidx.lifecycle.LiveData;

import com.github.P4rzival.RadiusMessage.RadiusMessage;
import com.github.P4rzival.RadiusMessage.RadiusPost;
import com.github.P4rzival.RadiusMessage.UserLocationManager;
import com.github.P4rzival.RadiusMessage.drawData;
import com.github.P4rzival.RadiusMessage.drawDataRepository;

import java.util.ArrayList;
import java.util.List;

public class UserCollectionManager {

    private static UserCollectionManager instance;
    private UserLocationManager userLocationManager;
    private boolean postCollection;
    private drawDataRepository drawRepo;
    public ArrayList<RadiusPost> radiusPosts = new ArrayList<RadiusPost>();

    private UserCollectionManager(){
        userLocationManager = UserLocationManager.getInstance();
        drawRepo = new drawDataRepository(RadiusMessage.getAppInstance());
        postCollection = false;
    }

    public static synchronized UserCollectionManager getInstance(){

        if (instance == null){
            instance = new UserCollectionManager();
        }
        return instance;
    }

    public void togglePostCollection(){
        postCollection = !postCollection;
    }

    public void collectPosts(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (postCollection){

                    try{
                        Thread.sleep(10000);
                    }catch (InterruptedException ignored){
                        //Do nothing for now!
                    }

                    if(radiusPosts.size() > 0){

                        for (int i = 0; i < radiusPosts.size(); i++){

                            if(radiusPosts.get(i).postData.isCollectedByUser() == false){

                                if(userLocationManager.isInGeoRadius(radiusPosts.get(i).postGeoPoint
                                        , userLocationManager.getCurrentLocationAsGeoPoint()
                                        , radiusPosts.get(i).postData.getRadius())){

                                    radiusPosts.get(i).postData.setCollectedByUser(true);
                                }
                            }
                        }
                    }

                }
            }
        }).start();
    }
}
