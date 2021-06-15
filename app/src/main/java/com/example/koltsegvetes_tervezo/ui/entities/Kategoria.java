package com.example.koltsegvetes_tervezo.ui.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class Kategoria {

    @PrimaryKey (autoGenerate = true)
    private int ID;
    private String KategoriaNev;

    //constructor
    public Kategoria(int ID, String KategoriaNev) {
        this.ID = ID;
        this.KategoriaNev = KategoriaNev;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getKategoriaNev() {
        return KategoriaNev;
    }

    public void setKategoriaNev(String kategoriaNev) {
        KategoriaNev = kategoriaNev;
    }
}
