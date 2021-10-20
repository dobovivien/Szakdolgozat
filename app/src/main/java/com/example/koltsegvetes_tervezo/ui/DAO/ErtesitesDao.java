package com.example.koltsegvetes_tervezo.ui.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.koltsegvetes_tervezo.ui.entities.Ertesites;
import com.example.koltsegvetes_tervezo.ui.entities.Kategoria;
import com.example.koltsegvetes_tervezo.ui.entities.Tranzakcio;

import java.sql.Date;
import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ErtesitesDao {

    //Delete query
    @Delete
    void delete (Ertesites ertesites);

    //Update query
    @Query("UPDATE ertesites SET Datum = :sDatum WHERE ID = :sID ")
    void updateDate (int sDatum, int sID);

    @Query("UPDATE ertesites SET Bekapcsolva = :sBekapcsolva WHERE ID = :sID ")
    void updateBekapcsolva (boolean sBekapcsolva, int sID);

    //Insert query
    @Insert(onConflict = REPLACE)
    void insert(Ertesites ertesites);

    //Get all data query
    @Query("SELECT * FROM Ertesites")
    List<Ertesites> getAll();

    @Query("SELECT Datum FROM Ertesites")
    List<Integer> getAllErtesitesDatum();

    @Query("SELECT Datum FROM Ertesites WHERE ID = :sID")
    int getErtesitesDatum(int sID);

    @Query("SELECT AlKategoriaNev FROM Ertesites, AlKategoria WHERE AlKategoriaID = :alKategoriaID")
    String getAlkategoria(int alKategoriaID);
}
