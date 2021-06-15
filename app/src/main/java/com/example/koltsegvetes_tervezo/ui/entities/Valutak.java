package com.example.koltsegvetes_tervezo.ui.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class Valutak {

    @PrimaryKey (autoGenerate = true)
    private int ID;
    private String ValutaNev;

    //constructor
    public Valutak(int ID, String ValutaNev) {
        this.ID = ID;
        this.ValutaNev = ValutaNev;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getValutaNev() {
        return ValutaNev;
    }

    public void setValutaNev(String valutaNev) {
        ValutaNev = valutaNev;
    }
}
