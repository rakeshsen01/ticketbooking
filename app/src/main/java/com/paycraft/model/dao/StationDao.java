package com.paycraft.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.paycraft.model.beans.Station;

import java.util.List;

@Dao
public interface StationDao {
    @Query("SELECT * FROM station")
    List<Station> getAll();

    @Insert
    void insert(Station task);

    @Query("SELECT * from station WHERE stationID= :id")
    List<Station> getItemById(int id);

}
