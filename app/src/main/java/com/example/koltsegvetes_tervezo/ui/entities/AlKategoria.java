package com.example.koltsegvetes_tervezo.ui.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import static androidx.room.ForeignKey.CASCADE;

@Entity (foreignKeys = @ForeignKey(entity = Kategoria.class, parentColumns = "ID", childColumns = "KategoriaID", onDelete = CASCADE))
public class AlKategoria {

    @PrimaryKey (autoGenerate = true)
    private int ID;
    private int KategoriaID;
    private String AlKategoriaNev;

    //constructor
    public AlKategoria (int ID, int KategoriaID, String AlKategoriaNev) {
        this.ID = ID;
        this.KategoriaID = KategoriaID;
        this.AlKategoriaNev = AlKategoriaNev;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getKategoriaID() {
        return KategoriaID;
    }

    public void setKategoriaID(int kategoriaID) {
        KategoriaID = kategoriaID;
    }

    public String getAlKategoriaNev() {
        return AlKategoriaNev;
    }

    public void setAlKategoriaNev(String alKategoriaNev) {
        AlKategoriaNev = alKategoriaNev;
    }
}
