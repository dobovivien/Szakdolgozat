package com.example.koltsegvetes_tervezo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.koltsegvetes_tervezo.utils.BottomBarPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.koltsegvetes_tervezo.ui.fragments.*;

import java.text.DateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Animation rotateOpen;
    Animation rotateClose;
    Animation fromBottom;
    Animation toBottom;

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
        }
    });


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
}
