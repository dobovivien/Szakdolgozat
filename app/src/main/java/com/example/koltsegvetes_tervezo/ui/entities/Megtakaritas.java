package com.example.koltsegvetes_tervezo.ui.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Megtakaritas {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String Megnevezes;
    private int CelOsszeg;
    private int JelenlegiOsszeg;
    private Date Datum;
    private Boolean Megtekintve;

    public Megtakaritas(int ID, String Megnevezes, int CelOsszeg, Date Datum) {
        this.ID = ID;
        this.Megnevezes = Megnevezes;
        this.CelOsszeg = CelOsszeg;
        this.JelenlegiOsszeg = 0;
        this.Datum = Datum;
        this.Megtekintve = false;
    }

    public Megtakaritas() {
        this.ID = 0;
        this.Megnevezes = "";
        this.CelOsszeg = 0;
        this.JelenlegiOsszeg = 0;
        this.Datum = null;
        this.Megtekintve = false;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMegnevezes() {
        return Megnevezes;
    }

    public void setMegnevezes(String megnevezes) {
        Megnevezes = megnevezes;
    }

    public int getCelOsszeg() {
        return CelOsszeg;
    }

    public void setCelOsszeg(int celOsszeg) {
        this.CelOsszeg = celOsszeg;
    }

    public int getJelenlegiOsszeg() {
        return JelenlegiOsszeg;
    }

    public void setJelenlegiOsszeg(int jelenlegiOsszeg) {
        this.JelenlegiOsszeg = jelenlegiOsszeg;
    }

    public Date getDatum() {
        return Datum;
    }

    public void setDatum(Date datum) {
        Datum = datum;
    }

    public Boolean getMegtekintve() {
        return Megtekintve;
    }

    public void setMegtekintve(Boolean megtekintve) {
        Megtekintve = megtekintve;
    }
}
