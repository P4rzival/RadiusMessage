package com.github.P4rzival.RadiusMessage;

import android.content.Context;
import android.os.Environment;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class FileIOTest {


    // App must be run and permissions must be granted beforehand
    @Test
    public void createDirectory() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        File directoryToTest =
                new File(externalStorageDirectory.getPath() + "/TestDirectory");

        directoryToTest.mkdir();
        assertTrue(directoryToTest.exists());

        directoryToTest.delete();
    }

}
