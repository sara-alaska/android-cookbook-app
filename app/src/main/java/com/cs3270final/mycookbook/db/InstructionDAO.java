package com.cs3270final.mycookbook.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public interface InstructionDAO {

    @Insert
    void insert(Instruction instruction);

    @Update
    void update(Instruction instruction);

}
