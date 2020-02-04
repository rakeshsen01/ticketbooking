package com.paycraft.model.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.paycraft.model.dao.StationDao;
import com.paycraft.model.beans.Station;

@Database(entities = {Station.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract StationDao stationDao();
}
