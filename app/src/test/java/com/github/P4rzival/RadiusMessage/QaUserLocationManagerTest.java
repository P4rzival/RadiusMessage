package com.github.P4rzival.RadiusMessage;

import android.content.Context;
import android.location.LocationManager;
import android.location.LocationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;

import java.lang.reflect.Field;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class QaUserLocationManagerTest {

    private UserLocationManager myUserLocationManager;

    @Before
    public void setUpSingleton() throws NoSuchFieldException, IllegalAccessException {

        myUserLocationManager = mock(UserLocationManager.class);
        setMock(myUserLocationManager);
    }

    @After
    public void resetSingleton() throws NoSuchFieldException, IllegalAccessException {
        Field instance = UserLocationManager.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null,null);
    }

    //Mockito will allow for unit tests to "mock" certain application runtime functionality
    private void setMock(UserLocationManager mock) throws NoSuchFieldException, IllegalAccessException {

        try {
            Field instance = UserLocationManager.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(instance, mock);
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }


    @Test
    public void UserLocationManager_UserLocationManagerNotNull(){

        UserLocationManager singleton = UserLocationManager.getInstance();
        assertNotNull(singleton);
    }

}
