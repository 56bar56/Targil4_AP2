package com.example.targil4_ap2;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.targil4_ap2.items.logInSave;

@Database(entities = {DbObject.class, logInSave.class}, version = 1)
@TypeConverters(DbObjectConverter.class) // Apply the converter to the database
public abstract class AppDB extends RoomDatabase {
    public abstract PostDao postDao();
    public abstract LogInSaveDao logInSaveDao();
}
