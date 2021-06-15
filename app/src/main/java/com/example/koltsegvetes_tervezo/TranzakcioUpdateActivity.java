package com.example.koltsegvetes_tervezo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koltsegvetes_tervezo.ui.entities.AlKategoria;
import com.example.koltsegvetes_tervezo.ui.entities.AppDatabase;
import com.example.koltsegvetes_tervezo.ui.entities.Kategoria;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;

public class TranzakcioUpdateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String LOG_TAG = TranzakcioUpdateActivity.class.getName();

    AppDatabase database;
    private static final int SECRET_KEY = 99;

    final Calendar calendar = Calendar.getInstance();

    int id;
    int osszeg;
    String kategoria;
    String alKategoria;
    String megjegyzes;
    String date;

    EditText osszegEditText;
    Spinner kategoriaSpinner;
    Spinner alKategoriaSpinner;
    Button datumButton;
    TextView datumTextView;
    EditText megjegyzesEditText;

    ArrayAdapter<String> kategoriaAdapter;
    ArrayAdapter<String> alKategoriaAdapter;

    MutableLiveData<ArrayList<String>> kategoriaList = new MutableLiveData<ArrayList<String>>();
    MutableLiveData<ArrayList<String>> alKategoriaList = new MutableLiveData<ArrayList<String>>();

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tranzakcio_update);

        database = AppDatabase.getInstance(this);

        osszegEditText = findViewById(R.id.osszegUpdateEditText);
        kategoriaSpinner = findViewById(R.id.kategoriaUpdateSpinner);
        alKategoriaSpinner = findViewById(R.id.alKategoriaUpdateSpinner);
        datumButton = findViewById(R.id.datumUpdateButton);
        datumTextView = findViewById(R.id.datumUpdateTextView);
        megjegyzesEditText = findViewById(R.id.megjegyzesUpdateEditText);



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
                new DatePickerDialog(TranzakcioUpdateActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Bundle b = getIntent().getExtras();
        int kategoriaID = 0;
        int alKategoriaID = 0;
        if(b != null) {
            id = b.getInt("id");
            osszeg = b.getInt("osszeg");
            kategoriaID = b.getInt("kategoria");
            alKategoriaID = b.getInt("alkategoria");
            megjegyzes = b.getString("megjegyzes");
            date = b.getString("datum");
        }

        osszegEditText.setText(String.valueOf(osszeg));
        kategoriaSpinner.setSelection(kategoriaID);
        alKategoriaSpinner.setSelection(alKategoriaID);
        megjegyzesEditText.setText(megjegyzes);
        datumTextView.setText(convert(date));
        kategoriaList.setValue(getKategoriaSpinner());
        alKategoriaList.setValue(getAlKategoriaSpinner());

        kategoriaAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, kategoriaList.getValue());
        kategoriaSpinner.setAdapter(kategoriaAdapter);
        Observer<ArrayList<String>> kategoriaObserver = new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                for (String str:strings) {
                    if (kategoriaAdapter.getPosition(str) < 0) {
                        kategoriaAdapter.add(str);
                    }
                }
                kategoriaSpinner.setAdapter(kategoriaAdapter);
                kategoriaSpinner.setSelection(kategoriaAdapter.getPosition(kategoria));
            }
        };

        kategoriaList.observe(this, kategoriaObserver);

        alKategoriaAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, alKategoriaList.getValue());
        Observer<ArrayList<String>> alKategoriaObserver = new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                for (String str:strings) {
                    if (alKategoriaAdapter.getPosition(str) < 0) {
                        alKategoriaAdapter.add(str);
                    }
                }
                alKategoriaSpinner.setAdapter(alKategoriaAdapter);
            }
        };
        alKategoriaList.observe(this, alKategoriaObserver);
        alKategoriaSpinner.setAdapter(alKategoriaAdapter);
        alKategoriaSpinner.setSelection(alKategoriaAdapter.getPosition(alKategoria));
    }

    public ArrayList<String> getKategoriaSpinner() {
        List<Kategoria> kategoria = database.kategoriaDao().getAll();
        ArrayList<String> kategoriaString = new ArrayList<String>();
        for (Kategoria k : kategoria) {
            kategoriaString.add(k.getKategoriaNev());
        }
        return kategoriaString;
    }

    public ArrayList<String> getAlKategoriaSpinner() {
        List<AlKategoria> alKategoria = database.alKategoriaDao().getAll();
        ArrayList<String> kategoriaString = new ArrayList<String>();
        for (AlKategoria k : alKategoria) {
            kategoriaString.add(k.getAlKategoriaNev());
        }
        return kategoriaString;
    }

    @SuppressLint("SimpleDateFormat")
    public void edit(View view) {

        if (CheckAllFields()) {
            DateFormat dateFormat = SimpleDateFormat.getDateInstance();
            Date d = null;
            try {
                d = dateFormat.parse(datumTextView.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            osszeg = Integer.parseInt(osszegEditText.getText().toString());
            megjegyzes = megjegyzesEditText.getText().toString();
            kategoria = kategoriaSpinner.getSelectedItem().toString();
            alKategoria = alKategoriaSpinner.getSelectedItem().toString();

            java.sql.Date sDate = new java.sql.Date(d.getTime());
            database.tranzakcioDao().update(id, database.kategoriaDao().getKategoriaByName(kategoria).getID(), database.alKategoriaDao().getAlKategoriaByName(alKategoria).getID(), osszeg, sDate, megjegyzes);
            finish();
            Toast.makeText(this, "Tranzakció módosítva!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean CheckAllFields() {
        if (osszegEditText.getText().toString().equals("") || !osszegEditText.getText().toString().matches("[0-9]+") || Integer.parseInt(osszegEditText.getText().toString()) <= 0) {
            osszegEditText.requestFocus();
            osszegEditText.setError("A mező kitöltése kötelező, nullánál nagyobb értéket adjon meg!");
            return false;
        }

        if (megjegyzes.length() == 0) {
            megjegyzesEditText.requestFocus();
            megjegyzesEditText.setError("A mező kitöltése kötelező!");
            return false;
        }

        return true;
    }

    public void cancel(View view) {
        finish();
    }

    public String convert(String s) {
        String d = "";
        String[] splitted = {};
        splitted = s.split(" ");
        String year = splitted[5];
        String month = splitted[1];
        String day = splitted[2];
        d = year.concat(". ").concat(monthToInt(month)).concat(". ").concat(cutDay(day)).concat(".");
        return d;
    }
    public String monthToInt(String s) {
        switch (s) {
            case "Jan":
                return "jan";
            case "Feb":
                return "febr";
            case "Mar":
                return "márc";
            case "Apr":
                return "ápr";
            case "May":
                return "máj";
            case "Jun":
                return "jún";
            case "Jul":
                return "júl";
            case "Aug":
                return "aug";
            case "Sep":
                return "szept";
            case "Oct":
                return "okt";
            case "Nov":
                return "nov";
            case "Dec":
                return "dec";
        }
        return s;
    }

    public String cutDay(String s) {
        switch (s) {
            case "01":
                return "1";
            case "02":
                return "2";
            case "03":
                return "3";
            case "04":
                return "4";
            case "05":
                return "5";
            case "06":
                return "6";
            case "07":
                return "7";
            case "08":
                return "8";
            case "09":
                return "9";
        }
        return s;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}