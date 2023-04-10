package com.dreambig.supplymanagementapp.Locals.RequisitionRoom;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dreambig.supplymanagementapp.Models.ItemModel;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RequistionDAO {
    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    void insertItems(ItemModel itemModel);

    @Delete
    void deleteItems(ItemModel... itemModel);

    @Query("DELETE FROM item_tbl WHERE user_id = :user_id")
    void deleteAll(String user_id);

    @Query("Select * FROM item_tbl WHERE user_id = :user_id")
    LiveData<List<ItemModel>> loadAddedItems(String user_id);

}
