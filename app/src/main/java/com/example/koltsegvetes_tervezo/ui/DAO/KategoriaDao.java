package com.example.koltsegvetes_tervezo.ui.DAO;

import androidx.lifecycle.LiveData;
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

    @Query("SELECT KategoriaNev FROM Kategoria WHERE ID = :id LIMIT 1")
    String getKategoriaNameByID(int id);

    @Insert(onConflict = REPLACE)
    void insert (Kategoria kategoria);

    @Query("SELECT * FROM Kategoria")
    LiveData<List<Kategoria>> selectAllKategoria();
}
