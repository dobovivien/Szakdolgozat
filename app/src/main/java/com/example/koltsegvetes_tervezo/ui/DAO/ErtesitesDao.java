package com.example.koltsegvetes_tervezo.ui.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.koltsegvetes_tervezo.ui.entities.Ertesites;
import com.example.koltsegvetes_tervezo.ui.entities.Kategoria;
import com.example.koltsegvetes_tervezo.ui.entities.Tranzakcio;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ErtesitesDao {

    //Insert query
    @Insert(onConflict = REPLACE)
    void insert(Ertesites ertesites);

    //Get all data query
    @Query("SELECT * FROM Ertesites")
    List<Ertesites> getAll();

    @Query("SELECT * FROM Ertesites")
    LiveData<List<Ertesites>> selectAllErtesites();
}
