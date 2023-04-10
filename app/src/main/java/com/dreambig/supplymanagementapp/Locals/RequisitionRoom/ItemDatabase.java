package com.dreambig.supplymanagementapp.Locals.RequisitionRoom;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dreambig.supplymanagementapp.Models.ItemModel;

@Database(
        entities = {ItemModel.class},
        version = 2,
        autoMigrations = {
                @AutoMigration( from = 1, to = 2)
        })
public abstract class ItemDatabase extends RoomDatabase {
    public abstract RequistionDAO requistionDAO();
}
