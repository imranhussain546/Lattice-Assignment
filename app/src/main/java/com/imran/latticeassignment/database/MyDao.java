package com.imran.latticeassignment.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.imran.latticeassignment.model.User;

@Dao
public interface MyDao {

    @Insert
    void insert(User user);

    @Query("select * from User")
    User getUser();
}
