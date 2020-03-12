package com.github.P4rzival.RadiusMessage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface drawDataDao {

    @Query("SELECT * FROM postData")
    LiveData<List<drawData>> getAll();

    @Insert
    void insert(drawData newDrawData);

    @Delete
    void delete(drawData oldDrawData);

    @Query("DELETE FROM postData")
    void deleteAllDrawData();
}
