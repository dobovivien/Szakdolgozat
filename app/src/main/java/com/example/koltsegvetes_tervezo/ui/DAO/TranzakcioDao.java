package com.example.koltsegvetes_tervezo.ui.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.koltsegvetes_tervezo.ui.entities.Tranzakcio;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TranzakcioDao {

    //Insert query
    @Insert(onConflict = REPLACE)
    void insert (Tranzakcio tranzakcio);

    //Delete query
    @Delete
    void delete (Tranzakcio tranzakcio);

    //Delete all query
    @Delete
    void reset (List<Tranzakcio> tranzakcioList);

    //Update query
    @Query("UPDATE tranzakcio SET KategoriaID = :sKategoriaID, AlKategoriaID = :sAlKategoriaID, Osszeg = :sOsszeg, Datum = :sDatum, Megjegyzes = :sMegjegyzes WHERE ID = :sID ")
    void update (int sID, int sKategoriaID, int sAlKategoriaID, int sOsszeg, Date sDatum, String sMegjegyzes);

    //Get all data query
    @Query("SELECT * FROM Tranzakcio")
    List<Tranzakcio> getAll();

    @Query("SELECT * FROM Tranzakcio ORDER BY Datum DESC")
    List<Tranzakcio> getAllByDescDate();

    @Query("SELECT * FROM Tranzakcio WHERE ID = :ID")
    List<Tranzakcio> getTransacitonByID (int ID);

    @Query("SELECT * FROM Tranzakcio WHERE KategoriaID = :KategoriaID ORDER BY Datum DESC")
    List<Tranzakcio> getTransactionByCategory (int KategoriaID);

    @Query("SELECT SUM(Osszeg) FROM Tranzakcio WHERE AlKategoriaID = :AlKategoriaID")
    Integer getTransactionByAlCategory (int AlKategoriaID);

    @Query("SELECT SUM(Osszeg) FROM Tranzakcio WHERE AlKategoriaID = :AlKategoriaID AND Datum = :Datum")
    Integer getTransactionByAlCategoryAndDate (int AlKategoriaID, Date Datum);

    @Query("SELECT SUM(Osszeg) FROM Tranzakcio WHERE AlKategoriaID = :AlKategoriaID AND Datum BETWEEN :StartDatum AND :EndDatum")
    Integer getTransactionByAlCategoryAndDateInterval (int AlKategoriaID, Date StartDatum, Date EndDatum);
}
