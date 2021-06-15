package com.example.koltsegvetes_tervezo.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.koltsegvetes_tervezo.R;
import com.example.koltsegvetes_tervezo.ui.entities.AlKategoria;
import com.example.koltsegvetes_tervezo.ui.entities.AppDatabase;
import com.example.koltsegvetes_tervezo.ui.entities.Kategoria;
import com.example.koltsegvetes_tervezo.ui.entities.Tranzakcio;
import com.example.koltsegvetes_tervezo.utils.MainAdapter;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TranzakcioAddFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private TranzakcioAddViewModel mViewModel;
    View view;
    EditText osszegEditText;
    TextView datumTextView;
    EditText megjegyzesEditText;
    Button datumButton;
    Button tranzakcioHozzaadButton;
    List<Tranzakcio> tranzakcioList = new ArrayList<Tranzakcio>();
    AppDatabase database;
    MainAdapter mainAdapter;
    Spinner kategoriaSpinner;
    Spinner alKategoriaSpinner;
    MutableLiveData<ArrayList<String>> kategoriaList = new MutableLiveData<ArrayList<String>>();
    MutableLiveData<ArrayList<String>> alKategoriaList = new MutableLiveData<ArrayList<String>>();

    public static TranzakcioAddFragment newInstance() {
        return new TranzakcioAddFragment();
    }

    public TranzakcioAddFragment() {
        super(R.layout.tranzakcio_add_fragment);
        //database = AppDatabase.getInstance(get)
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tranzakcio_add_fragment, container, false);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TranzakcioAddViewModel.class);

        database = AppDatabase.getInstance(requireActivity().getApplication());

        //Initialize variables
        view = getView();
        datumButton = view.findViewById(R.id.datumButton);
        tranzakcioHozzaadButton = view.findViewById(R.id.tranzakcioAddButton);
        osszegEditText = view.findViewById(R.id.osszegEditText);
        datumTextView = view.findViewById(R.id.datumTextView);
        megjegyzesEditText = view.findViewById(R.id.megjegyzesEditText);
        kategoriaSpinner = view.findViewById(R.id.kategoriaSpinner);
        alKategoriaSpinner = view.findViewById(R.id.alKategoriSpinner);

        datumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePicker();
                datePicker.show(getParentFragmentManager(), "date picker");
            }
        });

        tranzakcioHozzaadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckAllFields()) {
                    String kategoria = kategoriaSpinner.getSelectedItem().toString();
                    String alkategoria = alKategoriaSpinner.getSelectedItem().toString();
                    int ossz = Integer.parseInt(osszegEditText.getText().toString());
                    String megj = megjegyzesEditText.getText().toString().trim();

                    Tranzakcio t = new Tranzakcio();
                    t.setKategoriaID(database.kategoriaDao().getKategoriaByName(kategoria).getID());
                    t.setAlKategoriaID(database.alKategoriaDao().getAlKategoriaByName(alkategoria).getID());
                    t.setOsszeg(ossz);
                    t.setMegjegyzes(megj);
                    DateFormat dateFormat = SimpleDateFormat.getDateInstance();
                    Date d = null;
                    try {
                        d = dateFormat.parse(datumTextView.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    t.setDatum(d);
                    database.tranzakcioDao().insert(t);
                    osszegEditText.setText("");
                    megjegyzesEditText.setText("");
                    datumTextView.setText("");
                    tranzakcioList.clear();
                    tranzakcioList.addAll(database.tranzakcioDao().getAll());
                    Toast.makeText(getContext(), "Tranzakció hozzáadva!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        kategoriaList.setValue(getKategoriaSpinner());
        alKategoriaList.setValue(getAlKategoriaSpinner());

        Observer<ArrayList<String>> kategoriaObserver = new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireActivity(), R.layout.support_simple_spinner_dropdown_item, strings);
                kategoriaSpinner.setAdapter(adapter);

            }
        };
        kategoriaList.observe(getViewLifecycleOwner(), kategoriaObserver);

        Observer<ArrayList<String>> alKategoriaObserver = new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireActivity(), R.layout.support_simple_spinner_dropdown_item, strings);
                alKategoriaSpinner.setAdapter(adapter);

            }
        };
        alKategoriaList.observe(getViewLifecycleOwner(), alKategoriaObserver);
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



    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance().format(c.getTime());
        datumTextView.setText(currentDateString);
    }

    public boolean CheckAllFields() {
        if (osszegEditText.getText().toString().equals("") || !osszegEditText.getText().toString().matches("[0-9]+") || Integer.parseInt(osszegEditText.getText().toString()) <= 0) {
            osszegEditText.requestFocus();
            osszegEditText.setError("A mező kitöltése kötelező, nullánál nagyobb értéket adjon meg!");
            return false;
        }

        if (megjegyzesEditText.getText().toString().equals("")) {
            megjegyzesEditText.requestFocus();
            megjegyzesEditText.setError("A mező kitöltése kötelező!");
            return false;
        }

        if (datumTextView.getText().toString().equals("")) {
            datumTextView.requestFocus();
            datumTextView.setError("A mező kitöltése kötelező!");
            return false;
        }
        return true;
    }
}