package com.example.koltsegvetes_tervezo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.koltsegvetes_tervezo.ui.entities.AlKategoria;
import com.example.koltsegvetes_tervezo.ui.entities.AppDatabase;
import com.example.koltsegvetes_tervezo.ui.entities.Arfolyam;
import com.example.koltsegvetes_tervezo.ui.entities.Ertesites;
import com.example.koltsegvetes_tervezo.ui.entities.Kategoria;
import com.example.koltsegvetes_tervezo.ui.entities.Valutak;
import com.example.koltsegvetes_tervezo.utils.BottomBarPagerAdapter;
import com.example.koltsegvetes_tervezo.utils.Valuta;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.koltsegvetes_tervezo.ui.fragments.*;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Animation rotateOpen;
    Animation rotateClose;
    Animation fromBottom;
    Animation toBottom;

    AppDatabase database;

    BottomNavigationView bottomNavigationView;
    FloatingActionButton hozzaadButton;

    HomeFragment homeFragment = new HomeFragment();
    TranzakcioAddFragment tranzakcioAddFragment = new TranzakcioAddFragment();
    TranzakcioListFragment tranzakcioListFragment = new TranzakcioListFragment();
    StatisticsFragment statisticsFragment = new StatisticsFragment();
    SettingsFragment settingsFragment = new SettingsFragment();

    ViewPager viewPager;

    private boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = AppDatabase.getInstance(this.getApplication());
        checkIfDatabeseIsEmpty();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);

        View view = findViewById(R.id.main_layout);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);

        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);

        hozzaadButton = findViewById(R.id.hozzaadButton);
        hozzaadButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(2);
            onAddButtonClicked();
        }});

        BottomBarPagerAdapter bottomBarPagerAdapter = new BottomBarPagerAdapter(getSupportFragmentManager(), 1);
        viewPager = (ViewPager) findViewById(R.id.fragmentHolder);
        bottomBarPagerAdapter.AddFragment(homeFragment);
        bottomBarPagerAdapter.AddFragment(tranzakcioListFragment);
        bottomBarPagerAdapter.AddFragment(tranzakcioAddFragment);
        bottomBarPagerAdapter.AddFragment(statisticsFragment);
        bottomBarPagerAdapter.AddFragment(settingsFragment);

        viewPager.setAdapter(bottomBarPagerAdapter);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.tranzakcioMenuButton:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.placeholder:
                        viewPager.setCurrentItem(2);
                        return true;
                    case R.id.statistics:
                        viewPager.setCurrentItem(3);
                        return true;
                    case R.id.settings:
                        viewPager.setCurrentItem(4);
                        return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(0);

        String fragmentName = getIntent().getStringExtra("fragmentName");

        if (fragmentName != null) {
            if (fragmentName.equals("TranzakcioAddFragment")) {
                viewPager.setCurrentItem(2);
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDate = DateFormat.getDateInstance().format(c.getTime());

        TextView datumTextView = findViewById(R.id.datumTextView);
        datumTextView.setText(currentDate);
    }

    private void onAddButtonClicked() {
        hozzaadButtonAnimation(clicked);
        clicked = !clicked;
    }

    private void hozzaadButtonAnimation(boolean clicked){
        if (!clicked) {
            hozzaadButton.startAnimation(rotateOpen);
        } else {
            hozzaadButton.startAnimation(rotateClose);
            viewPager.setCurrentItem(0);
        }
    }

    private void checkIfDatabeseIsEmpty() {
        Kategoria bevetel = new Kategoria(1, "Bevétel");
        Kategoria kiadas = new Kategoria(2, "Kiadás");

        AlKategoria fizetes = new AlKategoria(1,1,"Fizetés");
        AlKategoria ajandek = new AlKategoria(2,1,"Ajándék");
        AlKategoria juttatas = new AlKategoria(3,1,"Juttatás");
        AlKategoria eladas = new AlKategoria(4,1,"Eladás");
        AlKategoria jovairas = new AlKategoria(5,1,"Jóváírás");
        AlKategoria megtakaritas = new AlKategoria(6,1,"Megtakarítás");
        AlKategoria befektetesjov = new AlKategoria(7,1,"Jövedelem befektetséből");
        AlKategoria lakber = new AlKategoria(8,2,"Lakbér");
        AlKategoria rezsi = new AlKategoria(9,2,"Rezsi");
        AlKategoria elelmiszer = new AlKategoria(10,2,"Élelmiszer");
        AlKategoria ruhazat = new AlKategoria(11,2,"Ruházat");
        AlKategoria sport = new AlKategoria(12,2,"Sport");
        AlKategoria egeszseg = new AlKategoria(13,2,"Egészség");
        AlKategoria szepsegapolas = new AlKategoria(14,2,"Szépségápolás");
        AlKategoria haztartas = new AlKategoria(15,2,"Háztartás");
        AlKategoria szorakozas = new AlKategoria(16,2,"Szórakozás");
        AlKategoria etterem = new AlKategoria(17,2,"Étterem/kávézó");
        AlKategoria utazas = new AlKategoria(18,2,"Utazás");
        AlKategoria baba = new AlKategoria(19,2,"Baba/gyerekek");
        AlKategoria auto = new AlKategoria(20,2,"Autó");
        AlKategoria oktatas = new AlKategoria(21,2,"Oktatás");
        AlKategoria haziallat = new AlKategoria(22,2,"Háziállat");
        AlKategoria kert = new AlKategoria(23,2,"Kert");
        AlKategoria elektornika = new AlKategoria(24,2,"Elektronikus eszközök");
        AlKategoria karbantartas = new AlKategoria(25,2,"Karbantartás");
        AlKategoria hitel = new AlKategoria(26,2,"Hitel");
        AlKategoria befektetes = new AlKategoria(27,2,"Befektetés");
        AlKategoria biztositas = new AlKategoria(28,2,"Biztosítás");
        AlKategoria kolcson = new AlKategoria(29,1,"Kölcsön");
        AlKategoria ajandek2 = new AlKategoria(30,2,"Ajándék");
        AlKategoria egyeb = new AlKategoria(31,2,"Egyéb");
        AlKategoria egyeb2 = new AlKategoria(32,1,"Egyéb");

        Valutak ft = new Valutak(1, "Ft");
        Valutak eur = new Valutak(2, "Eur");
        Valutak usd = new Valutak(3, "Usd");
        Valutak din = new Valutak(4, "Din");

        Arfolyam euro = new Arfolyam("Euro", 359);
        Arfolyam dollar = new Arfolyam("Dollár", 307);
        Arfolyam dinar = new Arfolyam("Dinár", 3);

        List<Kategoria> kategoriaData = database.kategoriaDao().getAll();
        List<AlKategoria> alKategoriaData = database.alKategoriaDao().getAll();
        List<Valutak> valutaData = database.valutakDao().getAll();
        List<Arfolyam> arfolyamData = database.arfolyamDao().getAll();

        if (kategoriaData.isEmpty()) {
            database.kategoriaDao().insert(bevetel);
            database.kategoriaDao().insert(kiadas);
        }

        if (alKategoriaData.isEmpty()) {
            database.alKategoriaDao().insert(fizetes);
            database.alKategoriaDao().insert(ajandek);
            database.alKategoriaDao().insert(juttatas);
            database.alKategoriaDao().insert(eladas);
            database.alKategoriaDao().insert(jovairas);
            database.alKategoriaDao().insert(megtakaritas);
            database.alKategoriaDao().insert(befektetesjov);
            database.alKategoriaDao().insert(lakber);
            database.alKategoriaDao().insert(rezsi);
            database.alKategoriaDao().insert(elelmiszer);
            database.alKategoriaDao().insert(ruhazat);
            database.alKategoriaDao().insert(sport);
            database.alKategoriaDao().insert(egeszseg);
            database.alKategoriaDao().insert(szepsegapolas);
            database.alKategoriaDao().insert(haztartas);
            database.alKategoriaDao().insert(szorakozas);
            database.alKategoriaDao().insert(etterem);
            database.alKategoriaDao().insert(utazas);
            database.alKategoriaDao().insert(baba);
            database.alKategoriaDao().insert(auto);
            database.alKategoriaDao().insert(oktatas);
            database.alKategoriaDao().insert(haziallat);
            database.alKategoriaDao().insert(kert);
            database.alKategoriaDao().insert(elektornika);
            database.alKategoriaDao().insert(karbantartas);
            database.alKategoriaDao().insert(hitel);
            database.alKategoriaDao().insert(befektetes);
            database.alKategoriaDao().insert(biztositas);
            database.alKategoriaDao().insert(kolcson);
            database.alKategoriaDao().insert(ajandek2);
            database.alKategoriaDao().insert(egyeb);
            database.alKategoriaDao().insert(egyeb2);
        }

        if (valutaData.isEmpty()) {
            database.valutakDao().insert(ft);
            database.valutakDao().insert(eur);
            database.valutakDao().insert(usd);
            database.valutakDao().insert(din);
        }

        if (arfolyamData.isEmpty()) {
            database.arfolyamDao().insert(euro);
            database.arfolyamDao().insert(dollar);
            database.arfolyamDao().insert(dinar);
        }
    }
}
