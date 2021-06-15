package com.example.koltsegvetes_tervezo.ui.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.koltsegvetes_tervezo.ui.entities.AlKategoria;
import com.example.koltsegvetes_tervezo.ui.entities.Kategoria;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface AlKategoriaDao {

    @Query("SELECT * FROM AlKategoria")
    List<AlKategoria> getAll();

    @Query("SELECT * FROM AlKategoria WHERE AlKategoriaNev = :name LIMIT 1")
    AlKategoria getAlKategoriaByName(String name);

    @Query("SELECT * FROM AlKategoria WHERE ID = :id LIMIT 1")
    AlKategoria getAlkategoriaById(int id);

    @Insert(onConflict = REPLACE)
    void insert (AlKategoria alKategoria);
}
