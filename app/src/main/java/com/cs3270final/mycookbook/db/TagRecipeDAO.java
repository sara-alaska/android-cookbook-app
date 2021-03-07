package com.cs3270final.mycookbook.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TagRecipeDAO {

    @Insert
    void insert(RecipeTag recipeTag);

    @Delete
    void delete(RecipeTag recipeTag);

}
