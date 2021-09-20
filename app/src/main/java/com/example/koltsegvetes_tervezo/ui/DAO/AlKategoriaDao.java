package com.example.koltsegvetes_tervezo.ui.DAO;

import androidx.lifecycle.LiveData;
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

    @Query("SELECT AlKategoriaNev FROM AlKategoria WHERE ID = :id LIMIT 1")
    String getAlkategoriaNameById(int id);

    @Query("SELECT * FROM AlKategoria WHERE KategoriaID = :id")
    List<AlKategoria> getAlkategoriaByKategoriaId(int id);

    @Query("SELECT AlKategoriaNev FROM AlKategoria WHERE KategoriaID = :id ORDER BY ID")
    List<String> getAllAlkategoriaNameByID(int id);

    @Insert(onConflict = REPLACE)
    void insert (AlKategoria alKategoria);

    @Query("SELECT * FROM AlKategoria")
    LiveData<List<AlKategoria>> selectAllAlKategoria();
}
