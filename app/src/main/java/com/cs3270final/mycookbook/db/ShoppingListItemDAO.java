package com.cs3270final.mycookbook.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ShoppingListItemDAO {

    @Query("SELECT * FROM ShoppingListItem")
    LiveData<List<ShoppingListItemFull>> selectAll();

    @Insert
    Long insert(ShoppingListItem shoppingListItem);

    @Delete
    void delete(ShoppingListItem shoppingListItem);

}
