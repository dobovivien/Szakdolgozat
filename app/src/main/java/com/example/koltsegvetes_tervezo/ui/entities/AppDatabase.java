package com.example.koltsegvetes_tervezo.ui.entities;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.koltsegvetes_tervezo.ui.DAO.AlKategoriaDao;
import com.example.koltsegvetes_tervezo.ui.DAO.ErtesitesDao;
import com.example.koltsegvetes_tervezo.ui.DAO.KategoriaDao;
import com.example.koltsegvetes_tervezo.ui.DAO.TranzakcioDao;
import com.example.koltsegvetes_tervezo.ui.DAO.ValutakDao;
import com.example.koltsegvetes_tervezo.utils.Converters;

@Database(entities = {Tranzakcio.class, Kategoria.class, AlKategoria.class, Valutak.class, Ertesites.class}, version = 5, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    //Create database instance
    private static AppDatabase database;

    //Define database name
    private static String DATABASE_NAME = "koltsegvetes_database";

    public synchronized static AppDatabase getInstance(Context context) {
        //Check condition
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME).allowMainThreadQueries().
                    fallbackToDestructiveMigration().build();
        }
        return database;
    }
    //Create DAO
    public abstract TranzakcioDao tranzakcioDao();
    public abstract KategoriaDao kategoriaDao();
    public abstract AlKategoriaDao alKategoriaDao();
    public abstract ErtesitesDao ertesitesDao();
    public abstract ValutakDao valutakDao();


}