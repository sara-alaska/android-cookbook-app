package com.cs3270final.mycookbook.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public interface IngredientDAO {

    @Insert
    void insert(Ingredient ingredient);

    @Update
    void update(Ingredient ingredient);

}

