package com.paycraft.model.beans;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Station implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "stationID")
    public int stationID;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "Latitude")
    public double latitude;

    @ColumnInfo(name = "Longitude")
    public double longitude;

    public Station() {
    }

    public Station(int stationID, String name, double latitude, double longitude) {
        this.stationID = stationID;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", stationID=" + stationID +
                ", name='" + name + '\'' +
                ", Latitude=" + latitude +
                ", Longitude=" + longitude +
                '}';
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getStationID() {
        return stationID;
    }


    public String getName() {
        return name;
    }

}
