package com.example.koltsegvetes_tervezo.ui.DAO;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.koltsegvetes_tervezo.ui.entities.Arfolyam;
import com.example.koltsegvetes_tervezo.ui.entities.Valutak;

import java.sql.Date;
import java.util.List;

@Dao
public interface ArfolyamDao {

    //Insert query
    @Insert(onConflict = REPLACE)
    void insert(Arfolyam arfolyam);

    @Query("SELECT * FROM Arfolyam")
    List<Arfolyam> getAll();

    @Query("SELECT valutaArfolyam FROM Arfolyam WHERE valutaNev = :valutaNev")
    int selectArfolyamByValutaName(String valutaNev);

    @Query("UPDATE Arfolyam SET valutaArfolyam = :valutaArfolyam WHERE valutaNev = :valutanev")
    void update (int valutaArfolyam, String valutanev);
}

