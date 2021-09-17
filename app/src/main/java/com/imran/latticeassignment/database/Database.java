package com.imran.latticeassignment.database;

import androidx.room.RoomDatabase;

import com.imran.latticeassignment.model.User;

@androidx.room.Database(entities ={User.class},version = 1)
public abstract class Database extends RoomDatabase {

    public abstract MyDao getUserDao();
}
