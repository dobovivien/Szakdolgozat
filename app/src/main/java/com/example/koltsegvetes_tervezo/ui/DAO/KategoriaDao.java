package com.example.koltsegvetes_tervezo.ui.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.koltsegvetes_tervezo.ui.entities.Kategoria;
import com.example.koltsegvetes_tervezo.ui.entities.Tranzakcio;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface KategoriaDao {

    @Query("SELECT * FROM Kategoria")
    List<Kategoria> getAll();

    @Query("SELECT * FROM Kategoria WHERE KategoriaNev = :name LIMIT 1")
    Kategoria getKategoriaByName(String name);

    @Insert(onConflict = REPLACE)
    void insert (Kategoria kategoria);
}
