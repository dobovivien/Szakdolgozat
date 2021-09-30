package com.example.koltsegvetes_tervezo.ui.entities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity

public class Arfolyam {

    @PrimaryKey
    @NonNull
    private String valutaRovidNev;
    private String valutaNev;
    private int valutaArfolyam;

    //constructor
    public Arfolyam(@NonNull String valutaRovidNev, String valutaNev, int valutaArfolyam) {
       this.valutaRovidNev = valutaRovidNev;
        this.valutaNev = valutaNev;
        this.valutaArfolyam = valutaArfolyam;
    }

    //getter & setter
    @NonNull
    public String getValutaRovidNev() {
        return valutaRovidNev;
    }

    public void setValutaRovidNev(@NonNull String valutaRovidNev) {
        this.valutaRovidNev = valutaRovidNev;
    }

    public String getValutaNev() {
        return valutaNev;
    }

    public void setValutaNev(@NonNull String valutaNev) {
        this.valutaNev = valutaNev;
    }

    public int getValutaArfolyam() {
        return valutaArfolyam;
    }

    public void setValutaArfolyam(int valutaArfolyam) {
        this.valutaArfolyam = valutaArfolyam;
    }
}
