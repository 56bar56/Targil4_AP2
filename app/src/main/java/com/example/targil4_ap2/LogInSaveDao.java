package com.example.targil4_ap2;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.targil4_ap2.items.logInSave;

import java.util.List;

@Dao
public interface LogInSaveDao {
    @Query("SELECT * FROM login_saves")
    List<logInSave> index();

    @Query("DELETE FROM login_saves")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(logInSave logInSave);
}