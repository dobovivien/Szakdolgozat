package com.example.koltsegvetes_tervezo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.koltsegvetes_tervezo.ui.entities.AppDatabase;
import com.example.koltsegvetes_tervezo.ui.entities.Megtakaritas;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MegtakaridasAddActivity extends AppCompatActivity {

    AppDatabase database;
    final Calendar calendar = Calendar.getInstance();

    EditText megnevezesEditText ;
    Button datumButton;
    TextView datumTextView;
    EditText megtakaritasOsszegEditText;
    Button hozzaadButton;
    Button addCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_megtakaridas_add);

        database = AppDatabase.getInstance(this);

        megnevezesEditText = findViewById(R.id.megnevezesEditText);
        datumButton = findViewById(R.id.datumButton);
        datumTextView = findViewById(R.id.datumTextView);
        megtakaritasOsszegEditText = findViewById(R.id.megtakaritasOsszegEditText);
        hozzaadButton = findViewById(R.id.hozzaadButton);
        addCancel = findViewById(R.id.addCancel);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, day);
                String currentDateString = DateFormat.getDateInstance().format(c.getTime());
                datumTextView.setText(currentDateString);
            }
        };

        datumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(MegtakaridasAddActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();
            }
        });

        hozzaadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int osszeg = Integer.parseInt(megtakaritasOsszegEditText.getText().toString());
                String megnevezes = megnevezesEditText.getText().toString();
                DateFormat dateFormat = SimpleDateFormat.getDateInstance();
                Date datum = null;
                try {
                    datum = dateFormat.parse(datumTextView.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Megtakaritas m = new Megtakaritas();
                m.setCelOsszeg(osszeg);
                m.setMegnevezes(megnevezes);
                m.setDatum(datum);
                m.setMegtekintve(false);
                database.megtakaritasDao().insert(m);
                finish();
            }
        });
    }

    public void add(View view) {

    }

    public void cancel(View view) {
        finish();
    }
}