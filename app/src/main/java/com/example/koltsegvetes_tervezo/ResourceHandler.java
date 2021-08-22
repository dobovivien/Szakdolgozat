package com.example.koltsegvetes_tervezo;

import java.util.Dictionary;
import java.util.Hashtable;

public class ResourceHandler {

    private static Dictionary<String, Integer> resourceDictionary = new Hashtable<String, Integer>() {{
            put("Fizetés", R.drawable.ic_fizetes);
            put("Ajándék", R.drawable.ic_ajandek);
            put("Juttatás", R.drawable.ic_juttatas);
            put("Eladás", R.drawable.ic_eladas);
            put("Jóváírás", R.drawable.ic_jovairas);
            put("Megtakarítás", R.drawable.ic_megtakaritas);
            put("Jövedelem befektetséből", R.drawable.ic_befektetes);
            put("Lakbér", R.drawable.ic_lakber);
            put("Rezsi", R.drawable.ic_rezsi);
            put("Élelmiszer", R.drawable.ic_elelmiszer);
            put("Ruházat", R.drawable.ic_ruhazat);
            put("Sport", R.drawable.ic_sport);
            put("Egészség", R.drawable.ic_egeszseg);
            put("Szépségápolás", R.drawable.ic_szepsegapolas);
            put("Háztartás", R.drawable.ic_haztartas);
            put("Szórakozás", R.drawable.ic_szorakozas);
            put("Étterem/kávézó", R.drawable.ic_etterem);
            put("Utazás", R.drawable.ic_utazas);
            put("Baba/gyerekek", R.drawable.ic_baba);
            put("Autó", R.drawable.ic_auto);
            put("Oktatás", R.drawable.ic_oktatas);
            put("Háziállat", R.drawable.ic_haziallat);
            put("Kert", R.drawable.ic_kert);
            put("Elektronikus eszközök", R.drawable.ic_elektronika);
            put("Karbantartás", R.drawable.ic_karbantartas);
            put("Hitel", R.drawable.ic_hitel);
            put("Befektetés", R.drawable.ic_befektetes);
            put("Biztosítás", R.drawable.ic_biztositas);
            put("Kölcsön", R.drawable.ic_kolcson);
            put("Egyéb", R.drawable.ic_egyeb);
    }};

    public static int getResourceID (String s) {
        return resourceDictionary.get(s);
    }
}
