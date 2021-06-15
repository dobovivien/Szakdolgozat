package com.example.koltsegvetes_tervezo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

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

//import com.getbase.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Animation rotateOpen;
    Animation rotateClose;
    Animation fromBottom;
    Animation toBottom;

    BottomNavigationView bottomNavigationView;
    FloatingActionButton hozzaadButton;
//    FloatingActionButton menuButton;
//    FloatingActionButton celokButton;
//    FloatingActionButton statButton;
//    FloatingActionButton sporolasButton;

    HomeFragment homeFragment = new HomeFragment();
    TranzakcioAddFragment tranzakcioAddFragment = new TranzakcioAddFragment();
    TranzakcioListFragment tranzakcioListFragment = new TranzakcioListFragment();

    ViewPager viewPager;

    private boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);

        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);

        hozzaadButton = findViewById(R.id.hozzaadButton);
//        menuButton = findViewById(R.id.topMenuButton);
//        celokButton = findViewById(R.id.celokButton);
//        statButton = findViewById(R.id.statisztikaButton);
//        sporolasButton = findViewById(R.id.sporolasButon);


        hozzaadButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(1);
            onAddButtonClicked();
        }
    });

//        menuButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onMenuButtonClicked();
//            }
//        });

        BottomBarPagerAdapter bottomBarPagerAdapter = new BottomBarPagerAdapter(getSupportFragmentManager(), 1);
        viewPager = (ViewPager) findViewById(R.id.fragmentHolder);
        bottomBarPagerAdapter.AddFragment(homeFragment);
        bottomBarPagerAdapter.AddFragment(tranzakcioAddFragment);
        bottomBarPagerAdapter.AddFragment(tranzakcioListFragment);

        viewPager.setAdapter(bottomBarPagerAdapter);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.placeholder:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.tranzakcioMenuButton:
                        viewPager.setCurrentItem(2);
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

    private void onMenuButtonClicked() {
//        setVisibiliy(clicked);
//        topMenuAnimation(clicked);
        clicked = !clicked;
    }

    private void onAddButtonClicked() {
        hozzaadButtonAnimation(clicked);
        clicked = !clicked;
    }

//    private void setVisibiliy(boolean clicked) {
//        if (!clicked) {
//            celokButton.setVisibility(View.VISIBLE);
//            statButton.setVisibility(View.VISIBLE);
//            sporolasButton.setVisibility(View.VISIBLE);
//        } else {
//            celokButton.setVisibility(View.INVISIBLE);
//            statButton.setVisibility(View.INVISIBLE);
//            sporolasButton.setVisibility(View.INVISIBLE);
//        }
//    }

    private void hozzaadButtonAnimation(boolean clicked){
        if (!clicked) {
            hozzaadButton.startAnimation(rotateOpen);
        } else {
            hozzaadButton.startAnimation(rotateClose);
            viewPager.setCurrentItem(0);
        }
    }

//    private void topMenuAnimation(boolean clicked) {
//        if (!clicked) {
//            celokButton.startAnimation(fromBottom);
//            statButton.startAnimation(fromBottom);
//            sporolasButton.startAnimation(fromBottom);
//        } else {
//            celokButton.startAnimation(toBottom);
//            statButton.startAnimation(toBottom);
//            sporolasButton.startAnimation(toBottom);
//        }
//    }
}
