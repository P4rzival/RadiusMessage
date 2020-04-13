package com.github.P4rzival.RadiusMessage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//Database implemented as Singleton, only can access through the DrawDataRepository
@Database(entities = DrawData.class, version = 1, exportSchema = false)
public abstract class DrawDataLocalDatabase extends RoomDatabase {

    private static DrawDataLocalDatabase instance;

    public abstract DrawDataDatabaseAccessorObject drawDao();

    public static synchronized DrawDataLocalDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), DrawDataLocalDatabase.class, "drawData_local_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
