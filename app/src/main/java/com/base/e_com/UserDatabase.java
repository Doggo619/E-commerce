package com.base.e_com;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserEntity.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {
    private static final String dbName = "user";
    private static UserDatabase INSTANCE;
    public static UserDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, UserDatabase.class, dbName)
                    .fallbackToDestructiveMigration() // For simplicity (in a real app, use a separate thread)
                    .build();
        }
        return INSTANCE;
    }
    public abstract UserDao userDao();
}

