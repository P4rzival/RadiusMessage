package com.github.P4rzival.RadiusMessage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;

import java.util.List;

@Dao
public interface drawDataDao {

    @Query("SELECT * FROM postData")
    List<drawData> getAll();

    @Delete
    void delete(drawData olDrawData);
}
