package com.example.koltsegvetes_tervezo.ui.DAO;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.koltsegvetes_tervezo.ui.entities.Megtakaritas;

import java.util.Date;
import java.util.List;

@Dao
public interface MegtakaritasDao {

    @Query("SELECT * FROM Megtakaritas ORDER BY Datum ASC")
    List<Megtakaritas> getAll();

    @Insert(onConflict = REPLACE)
    void insert (Megtakaritas megtakaritas);

    @Query("UPDATE megtakaritas SET Megnevezes = :megnevezes, CelOsszeg = :celosszeg, JelenlegiOsszeg = :jelenlegiosszeg, Datum = :datum")
    void update (String megnevezes, int celosszeg, int jelenlegiosszeg, Date datum);

    @Query("UPDATE megtakaritas SET JelenlegiOsszeg = :jelenlegiosszeg WHERE ID = :id")
    void update (int id, int jelenlegiosszeg);

    @Query("UPDATE megtakaritas SET Megtekintve = :megtekintve WHERE ID = :id")
    void updateMegtekintes (int id, boolean megtekintve);

    @Delete
    void delete (Megtakaritas megtakaritas);

    @Query("SELECT SUM(JelenlegiOsszeg) FROM Megtakaritas")
    int eddigFelretett();
}
