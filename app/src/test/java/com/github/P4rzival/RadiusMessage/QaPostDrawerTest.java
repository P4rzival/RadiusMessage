package com.github.P4rzival.RadiusMessage;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class QaPostDrawerTest {

    //More overhead to testing with singleton
    //May look into Factory Object Pattern in future for a possible replacement

    //Set Up singleton for unit testing with Mockito Libraries
    private DrawDataLocalDatabase drawLocalDB;

    @Before
    public void setUpSingleton() throws NoSuchFieldException, IllegalAccessException {
        drawLocalDB = mock(DrawDataLocalDatabase.class);
        setMock(drawLocalDB);
    }


    @After
    public void resetSingleton() throws NoSuchFieldException, IllegalAccessException {
        Field instance = DrawDataLocalDatabase.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null,null);
    }

    //Mockito will allow for unit tests to "mock" certain application runtime functionality
    private void setMock(DrawDataLocalDatabase mock) throws NoSuchFieldException, IllegalAccessException {

        try {
            Field instance = DrawDataLocalDatabase.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(instance, mock);
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    @Test
    public void constructor_ObjectNotNull(){

        PostDrawer postDrawer = new PostDrawer();
        assertNotNull(postDrawer);
    }

    @Test
    public void constructor_RepoNotNull(){

        PostDrawer postDrawer = new PostDrawer();
        assertNotNull(postDrawer.getDrawRepo());
    }

    @Test
    public void parseJSON_NewDrawDataNotNull() throws JSONException {

        PostDrawer postDrawer = new PostDrawer();

        JSONObject mockJSON = new JSONObject();

        assertNotNull(postDrawer.parsePostJSON(mockJSON));
    }

    @Test
    public void parseJSON_JSONToDrawData() throws JSONException {

        double delta = 0.00001d;

        PostDrawer postDrawer = new PostDrawer();

        JSONObject mockJSON = new JSONObject();

        mockJSON.put("userTextMessage", "This is a test message!");

        mockJSON.put("radius", 10);

        mockJSON.put("locationX", 20);

        mockJSON.put("locationY", 100);

        mockJSON.put("messageDuration", 1000);

        assertEquals("This is a test message!"
                , postDrawer.parsePostJSON(mockJSON).getUserMessageText());

        assertEquals(10
                , postDrawer.parsePostJSON(mockJSON).getRadius(), delta);

        assertEquals(20
                , postDrawer.parsePostJSON(mockJSON).getLocationX(), delta);

        assertEquals(100
                , postDrawer.parsePostJSON(mockJSON).getLocationY(), delta);

        assertEquals(1000
                , postDrawer.parsePostJSON(mockJSON).getMessageDuration());
    }

}
