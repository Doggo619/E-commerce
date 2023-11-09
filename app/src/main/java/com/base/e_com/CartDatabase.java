package com.base.e_com;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CartEntity.class}, version = 2)
public abstract class CartDatabase extends RoomDatabase {
    private static final  String dbName = "carts";
    private static CartDatabase INSTANCE;

    public static synchronized CartDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, CartDatabase.class, dbName)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
    public abstract CartDao cartDao();
}

