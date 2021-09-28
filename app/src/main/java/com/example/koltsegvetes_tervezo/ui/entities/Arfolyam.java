package com.example.koltsegvetes_tervezo.ui.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Arfolyam {

    @PrimaryKey
    @NonNull
    private String valutaNev;
    private int valutaArfolyam;

    //constructor
    public Arfolyam(@NonNull String valutaNev, int valutaArfolyam) {
        this.valutaNev = valutaNev;
        this.valutaArfolyam = valutaArfolyam;
    }

    //getter & setter
    @NonNull
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
