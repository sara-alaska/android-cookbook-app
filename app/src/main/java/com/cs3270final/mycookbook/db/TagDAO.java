package com.cs3270final.mycookbook.db;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface TagDAO {

    @Insert
    Long insert(Tag tag);

}
