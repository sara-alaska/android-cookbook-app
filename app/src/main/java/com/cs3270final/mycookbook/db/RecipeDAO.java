package com.cs3270final.mycookbook.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecipeDAO {

    @Query("SELECT * FROM Recipe ORDER BY date_created DESC")
    LiveData<List<RecipeFull>> selectAllByDateDesc();

    @Update
    void update(Recipe recipe);

    @Insert
    Long insert(Recipe recipe);

}
