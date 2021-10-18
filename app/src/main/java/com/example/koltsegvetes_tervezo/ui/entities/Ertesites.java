package com.example.koltsegvetes_tervezo.ui.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.koltsegvetes_tervezo.utils.Converters;

import java.util.Date;
import static androidx.room.ForeignKey.CASCADE;


@Entity (foreignKeys = { @ForeignKey(entity = Kategoria.class, parentColumns = "ID", childColumns = "KategoriaID", onDelete = CASCADE),
                         @ForeignKey(entity = AlKategoria.class, parentColumns = "ID", childColumns = "AlKategoriaID", onDelete = CASCADE),
                         @ForeignKey(entity = Valutak.class, parentColumns = "ID", childColumns = "ValutaID", onDelete = CASCADE)})

public class Ertesites {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    private int KategoriaID;
    private int AlKategoriaID;
    private int Osszeg;
    private int ValutaID;
    private int Datum;
    private String Megjegyzes;
    private boolean Bekapcsolva;

    //constructor
    public Ertesites(int ID, int KategoriaID, int AlKategoriaID, int Osszeg, int ValutaID, int Datum, String Megjegyzes, boolean Bekapcsolva) {
        this.ID = ID;
        this.KategoriaID = KategoriaID;
        this.AlKategoriaID = AlKategoriaID;
        this.Osszeg = Osszeg;
        this.ValutaID = ValutaID;
        this.Datum = Datum;
        this.Megjegyzes = Megjegyzes;
        this.Bekapcsolva = Bekapcsolva;
    }

    public Ertesites() {
        this.ID = 0;
        this.KategoriaID = 0;
        this.AlKategoriaID = 0;
        this.Osszeg = 0;
        this.ValutaID = 0;
        this.Datum = 1;
        this.Megjegyzes = "";
        this.Bekapcsolva = true;
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

    public int getAlKategoriaID() {
        return AlKategoriaID;
    }

    public void setAlKategoriaID(int alKategoriaID) {
        AlKategoriaID = alKategoriaID;
    }

    public int getOsszeg() {
        return Osszeg;
    }

    public void setOsszeg(int osszeg) {
        Osszeg = osszeg;
    }

    public int getDatum() {
        return Datum;
    }

    public void setDatum(int datum) {
        Datum = datum;
    }

    public String getMegjegyzes() {
        return Megjegyzes;
    }

    public void setMegjegyzes(String megjegyzes) {
        Megjegyzes = megjegyzes;
    }

    public int getValutaID() {
        return ValutaID;
    }

    public void setValutaID(int valutaID) {
        ValutaID = valutaID;
    }

    public boolean isBekapcsolva() {
        return Bekapcsolva;
    }

    public void setBekapcsolva(boolean bekapcsolva) {
        Bekapcsolva = bekapcsolva;
    }
}
