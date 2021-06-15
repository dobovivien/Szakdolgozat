package com.example.koltsegvetes_tervezo.ui.DAO;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.koltsegvetes_tervezo.ui.entities.Valutak;

import java.util.List;

@Dao
public interface ValutakDao {

    @Query("SELECT * FROM Valutak")
    List<Valutak> getAll();
}
