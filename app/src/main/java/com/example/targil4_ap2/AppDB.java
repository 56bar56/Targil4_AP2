package com.example.targil4_ap2;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DbObject.class}, version = 1)
public abstract class AppDB extends RoomDatabase {
    public abstract PostDao postDao();
}