package com.github.P4rzival.RadiusMessage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DrawDataDatabaseAccessorObject {

    @Query("SELECT * FROM postData")
    LiveData<List<DrawData>> getAll();

    @Insert
    void insert(DrawData newDrawData);

    @Delete
    void delete(DrawData oldDrawData);

    @Query("DELETE FROM postData")
    void deleteAllDrawData();
}
