package com.base.e_com;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ProductEntity.class, UserProductMapping.class}, version = 1)
public abstract class ProductDatabase extends RoomDatabase {

    private static final  String dbName = "products";
    private static ProductDatabase INSTANCE;

    public static synchronized ProductDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, ProductDatabase.class, dbName)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
    public abstract ProductDao productDao();
}

