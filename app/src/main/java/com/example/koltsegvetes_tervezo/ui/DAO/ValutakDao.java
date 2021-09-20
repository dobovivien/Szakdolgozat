package com.example.koltsegvetes_tervezo.ui.DAO;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.koltsegvetes_tervezo.ui.entities.AlKategoria;
import com.example.koltsegvetes_tervezo.ui.entities.Kategoria;
import com.example.koltsegvetes_tervezo.ui.entities.Valutak;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ValutakDao {

    @Query("SELECT * FROM Valutak")
    List<Valutak> getAll();

    @Query("SELECT ValutaNev FROM Valutak")
    List<String> getAllValutaName();

    @Query("SELECT * FROM Valutak WHERE ValutaNev = :name LIMIT 1")
    Kategoria getValutaByName(String name);

    @Query("SELECT * FROM Valutak")
    LiveData<List<Valutak>> selectAllValuta();

    @Insert(onConflict = REPLACE)
    void insert (Valutak valuta);
}
