package com.github.P4rzival.RadiusMessage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = drawData.class, version = 1, exportSchema = false)
public abstract class drawDataLocalDatabase extends RoomDatabase {

    //This is a singleton, there will be only one instance of this class
    //running as the app is running.
    private static drawDataLocalDatabase instance;

    //DAO = "database accessor object"
    public abstract drawDataDao drawDao();

    //synchronized so only one thread can create an instance of this database
    //as this class is to act as a singleton object.
    public static synchronized drawDataLocalDatabase getInstance(Context context) {

        //if we haven't created the singleton yet, then we make one.
        //Room will build use a new database instance and will handle some
        //database code for us.
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), drawDataLocalDatabase.class, "drawData_local_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
