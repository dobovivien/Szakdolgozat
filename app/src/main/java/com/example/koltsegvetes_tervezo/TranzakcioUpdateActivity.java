package com.example.koltsegvetes_tervezo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koltsegvetes_tervezo.ui.entities.AppDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TranzakcioUpdateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String LOG_TAG = TranzakcioUpdateActivity.class.getName();

    AppDatabase database;
    private static final int SECRET_KEY = 99;

    final Calendar calendar = Calendar.getInstance();

    int id;
    int osszeg;
    CustomItem kategoria;
    CustomItem alKategoria;
    String megjegyzes;
    String date;

    EditText osszegEditText;
    Spinner kategoriaSpinner;
    Spinner alKategoriaSpinner;
    Button datumButton;
    TextView datumTextView;
    EditText megjegyzesEditText;

    ArrayList<CustomItem> kategoriaCustomList;
    ArrayList<CustomItem> alKategoriaCustomList;

    ArrayAdapter<String> kategoriaAdapter;
    ArrayAdapter<String> alKategoriaAdapter;

    MutableLiveData<ArrayList<CustomItem>> kategoriaList = new MutableLiveData<ArrayList<CustomItem>>();
    MutableLiveData<ArrayList<CustomItem>> alKategoriaList = new MutableLiveData<ArrayList<CustomItem>>();

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
        String kategoriaNev = "";
        String alKategoriaNev = "";
        if(b != null) {
            id = b.getInt("id");
            osszeg = b.getInt("osszeg");
            kategoriaNev = b.getString("kategoria");
            alKategoriaNev = b.getString("alkategoria");
            megjegyzes = b.getString("megjegyzes");
            date = b.getString("datum");
        }

        osszegEditText.setText(String.valueOf(osszeg));
        megjegyzesEditText.setText(megjegyzes);
        datumTextView.setText(convert(date));
        kategoriaList.setValue(getKategoriaCustomList());
        alKategoriaList.setValue(getAlKategoriaCustomList());

        //kategoriaAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, kategoriaList.getValue());
//        Observer<ArrayList<CustomItem>> kategoriaObserver = new Observer<ArrayList<CustomItem>>() {
//            @Override
//            public void onChanged(ArrayList<CustomItem> items) {
//                for (CustomItem i:items) {
//                    if (customKategoriaAdapter.getPosition(i) < 0) {
//                        customKategoriaAdapter.add(i);
//                    }
//                }
//                kategoriaSpinner.setAdapter(customKategoriaAdapter);
//                kategoriaSpinner.setSelection(customKategoriaAdapter.getPosition(kategoria));
//                customKategoriaAdapter.setDropDownViewResource(R.layout.custom_dropdown_layout);
//            }
//        };
//
//        kategoriaList.observe(this, kategoriaObserver);
//
//        //alKategoriaAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, alKategoriaList.getValue());
//        Observer<ArrayList<CustomItem>> alKategoriaObserver = new Observer<ArrayList<CustomItem>>() {
//            @Override
//            public void onChanged(ArrayList<CustomItem> items) {
//                for (CustomItem i:items) {
//                    if (customAlKategoriaAdapter.getPosition(i) < 0) {
//                        customAlKategoriaAdapter.add(i);
//                    }
//                }
//                alKategoriaSpinner.setAdapter(customAlKategoriaAdapter);
//                customAlKategoriaAdapter.setDropDownViewResource(R.layout.custom_dropdown_layout);
//            }
//        };
//        alKategoriaList.observe(this, alKategoriaObserver);
        //alKategoriaSpinner.setSelection(customAlKategoriaAdapter.getPosition(alKategoria));

        String finalKategoriaNev = kategoriaNev;
        Observer<ArrayList<CustomItem>> kategoriaObserver = new Observer<ArrayList<CustomItem>>() {
            @Override
            public void onChanged(ArrayList<CustomItem> items) {
                CustomAdapter customKategoriaAdapter = new CustomAdapter(getBaseContext(), kategoriaCustomList);
                kategoriaSpinner.setAdapter(customKategoriaAdapter);
                kategoriaSpinner.setSelection(customKategoriaAdapter.getIndex(finalKategoriaNev));
                kategoriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        CustomItem item = (CustomItem) parent.getSelectedItem();
                        alKategoriaList.setValue(getAlKategoriaCustomList());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                customKategoriaAdapter.setDropDownViewResource(R.layout.custom_dropdown_layout);
            }
        };
        kategoriaList.observe(this, kategoriaObserver);

        String finalAlKategoriaNev = alKategoriaNev;
        Observer<ArrayList<CustomItem>> alKategoriaObserver = new Observer<ArrayList<CustomItem>>() {
            @Override
            public void onChanged(ArrayList<CustomItem> items) {
                CustomAdapter customAlKategoriaAdapter = new CustomAdapter(getBaseContext(), alKategoriaCustomList);
                alKategoriaSpinner.setAdapter(customAlKategoriaAdapter);
                alKategoriaSpinner.setSelection(customAlKategoriaAdapter.getIndex(finalAlKategoriaNev));
                customAlKategoriaAdapter.setDropDownViewResource(R.layout.custom_dropdown_layout);
            }
        };
        alKategoriaList.observe(this, alKategoriaObserver);
    }

    private ArrayList<CustomItem> getKategoriaCustomList() {
        kategoriaCustomList = new ArrayList<>();
        kategoriaCustomList.add(new CustomItem(database.kategoriaDao().getKategoriaNameByID(1), R.drawable.ic_bevetel));
        kategoriaCustomList.add(new CustomItem(database.kategoriaDao().getKategoriaNameByID(2), R.drawable.ic_kiadas));
        return kategoriaCustomList;
    }

    private ArrayList<CustomItem> getAlKategoriaCustomList() {
        alKategoriaCustomList = new ArrayList<>();
        if (kategoriaSpinner.getSelectedItem() == null) {
            return alKategoriaCustomList;
        }
        String s = ((CustomItem) kategoriaSpinner.getSelectedItem()).getSpinnerItemName();
        int katID = database.kategoriaDao().getKategoriaByName(s).getID();
        List<String> alkList = database.alKategoriaDao().getAllAlkategoriaNameByID(katID);
        for (String str : alkList) {
            alKategoriaCustomList.add(new CustomItem(str, ResourceHandler.getResourceID(str)));
        }
        return alKategoriaCustomList;
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
            kategoria = (CustomItem) kategoriaSpinner.getSelectedItem();
            alKategoria = (CustomItem) alKategoriaSpinner.getSelectedItem();

            java.sql.Date sDate = new java.sql.Date(d.getTime());
            database.tranzakcioDao().update(id, database.kategoriaDao().getKategoriaByName(kategoria.getSpinnerItemName()).getID(), database.alKategoriaDao().getAlKategoriaByName(alKategoria.getSpinnerItemName()).getID(), osszeg, sDate, megjegyzes);
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