package com.example.targil4_ap2;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {DbObject.class}, version = 1)
@TypeConverters(DbObjectConverter.class) // Apply the converter to the database
public abstract class AppDB extends RoomDatabase {
    public abstract PostDao postDao();
}
