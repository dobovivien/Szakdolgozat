package com.example.koltsegvetes_tervezo.ui.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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

import com.example.koltsegvetes_tervezo.CustomAdapter;
import com.example.koltsegvetes_tervezo.CustomItem;
import com.example.koltsegvetes_tervezo.R;
import com.example.koltsegvetes_tervezo.ResourceHandler;
import com.example.koltsegvetes_tervezo.ui.entities.AppDatabase;
import com.example.koltsegvetes_tervezo.ui.entities.Ertesites;
import com.example.koltsegvetes_tervezo.ui.entities.Tranzakcio;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TranzakcioAddFragment extends Fragment implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    private TranzakcioAddViewModel mViewModel;
    View view;
    EditText osszegEditText;
    TextView datumTextView;
    EditText megjegyzesEditText;
    ImageView datumButton;
    Button tranzakcioHozzaadButton;
    List<Tranzakcio> tranzakcioList = new ArrayList<Tranzakcio>();
    AppDatabase database;
    Spinner kategoriaSpinner;
    Spinner alKategoriaSpinner;
    Spinner valutaSpinner;
    int spinnerDefault = 0;
    ImageView tooltip;
    CheckBox allandoCheckBox;
    ArrayList<CustomItem> kategoriaCustomList;
    ArrayList<CustomItem> alKategoriaCustomList;
    List<String> valutaList;
    MutableLiveData<ArrayList<CustomItem>> kategoriaImg = new MutableLiveData<ArrayList<CustomItem>>();
    MutableLiveData<ArrayList<CustomItem>> alKategoriaImg = new MutableLiveData<ArrayList<CustomItem>>();
    MutableLiveData<ArrayList<String>> valutaMutableLiveData = new MutableLiveData<ArrayList<String>>();

    public static TranzakcioAddFragment newInstance() {
        return new TranzakcioAddFragment();
    }

    public TranzakcioAddFragment() {
        super(R.layout.tranzakcio_add_fragment);
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
        view = getView();
        datumButton = view.findViewById(R.id.datumButton);
        tranzakcioHozzaadButton = view.findViewById(R.id.tranzakcioAddButton);
        osszegEditText = view.findViewById(R.id.osszegEditText);
        datumTextView = view.findViewById(R.id.datumTextView);
        megjegyzesEditText = view.findViewById(R.id.megjegyzesEditText);
        kategoriaSpinner = view.findViewById(R.id.kategoriaSpinner);
        alKategoriaSpinner = view.findViewById(R.id.alKategoriSpinner);
        alKategoriaSpinner = view.findViewById(R.id.alKategoriSpinner);
        valutaSpinner = view.findViewById(R.id.valutaSpinner);
        alKategoriaSpinner.setEnabled(false);
        tooltip = view.findViewById(R.id.allandoTooltip);
        allandoCheckBox = view.findViewById(R.id.allandoCheckBox);
        kategoriaCustomList = getKategoriaCustomList();
        valutaList = database.valutakDao().getAllValutaName();

        NotificationManager manager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();

        tooltip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
                builder.setTitle("Rendszeres tranzakció");
                builder.setMessage("Amennyiben szeretne havi emlékeztetőt kapni a tranzakció rögzítéséről, jelölje be a négyzetet! (Később módosíthatja és törölheti is az emlékeztetőt.)");

                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    });


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
                if (allandoCheckBox.isChecked() && CheckAllFields()) {
                    String kategoria = ((CustomItem) kategoriaSpinner.getSelectedItem()).getSpinnerItemName();
                    String alkategoria = ((CustomItem) alKategoriaSpinner.getSelectedItem()).getSpinnerItemName();
                    int ossz = Integer.parseInt(osszegEditText.getText().toString());
                    String valuta = valutaSpinner.getSelectedItem().toString();
                    String megj = megjegyzesEditText.getText().toString().trim();
                    int nap = 0;

                    Tranzakcio t = new Tranzakcio();
                    t.setKategoriaID(database.kategoriaDao().getKategoriaByName(kategoria).getID());
                    t.setAlKategoriaID(database.alKategoriaDao().getAlKategoriaByName(alkategoria).getID());
                    t.setOsszeg(ossz);
                    t.setValutaID(database.valutakDao().getValutaByName(valuta).getID());
                    t.setMegjegyzes(megj);
                    Ertesites ertesites = new Ertesites();
                    ertesites.setKategoriaID(database.kategoriaDao().getKategoriaByName(kategoria).getID());
                    ertesites.setAlKategoriaID(database.alKategoriaDao().getAlKategoriaByName(alkategoria).getID());
                    ertesites.setOsszeg(ossz);
                    ertesites.setValutaID(database.valutakDao().getValutaByName(valuta).getID());
                    ertesites.setMegjegyzes(megj);
                    ertesites.setBekapcsolva(true);
                    DateFormat dateFormat = SimpleDateFormat.getDateInstance();
                    Date d = null;
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("d");
                    try {
                        d = dateFormat.parse(datumTextView.getText().toString());
                        Date d1 = dateFormat.parse(datumTextView.getText().toString());
                        String date1 = simpleDateFormat1.format(d1);
                        nap = Integer.parseInt(date1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    t.setDatum(d);
                    ertesites.setDatum(nap);
                    database.tranzakcioDao().insert(t);
                    database.ertesitesDao().insert(ertesites);
                    osszegEditText.setText("");
                    megjegyzesEditText.setText("");
                    datumTextView.setText("");
                    tranzakcioList.clear();
                    tranzakcioList.addAll(database.tranzakcioDao().getAll());
                    Toast.makeText(getContext(), "Tranzakció hozzáadva!", Toast.LENGTH_SHORT).show();
                } else if (CheckAllFields()) {
                    String kategoria = ((CustomItem) kategoriaSpinner.getSelectedItem()).getSpinnerItemName();
                    String alkategoria = ((CustomItem) alKategoriaSpinner.getSelectedItem()).getSpinnerItemName();
                    int ossz = Integer.parseInt(osszegEditText.getText().toString());
                    String valuta = valutaSpinner.getSelectedItem().toString();
                    String megj = megjegyzesEditText.getText().toString().trim();
                    Tranzakcio t = new Tranzakcio();
                    t.setKategoriaID(database.kategoriaDao().getKategoriaByName(kategoria).getID());
                    t.setAlKategoriaID(database.alKategoriaDao().getAlKategoriaByName(alkategoria).getID());
                    t.setOsszeg(ossz);
                    t.setValutaID(database.valutakDao().getValutaByName(valuta).getID());
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

        kategoriaImg.setValue(getKategoriaCustomList());

        Observer<ArrayList<CustomItem>> kategoriaObserver = new Observer<ArrayList<CustomItem>>() {
            @Override
            public void onChanged(ArrayList<CustomItem> items) {
                CustomAdapter customAdapter = new CustomAdapter(requireActivity(), items);
                kategoriaSpinner.setAdapter(customAdapter);
                kategoriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        CustomItem item = (CustomItem) parent.getSelectedItem();
                        alKategoriaImg.setValue(getAlKategoriaCustomList());
                        alKategoriaSpinner.setEnabled(true);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                customAdapter.setDropDownViewResource(R.layout.custom_dropdown_layout);

            }
        };
        kategoriaImg.observe(getViewLifecycleOwner(), kategoriaObserver);

        Observer<ArrayList<CustomItem>> alKategoriaObserver = new Observer<ArrayList<CustomItem>>() {
            @Override
            public void onChanged(ArrayList<CustomItem> items) {
                CustomAdapter customAdapter = new CustomAdapter(requireActivity(), items);
                alKategoriaSpinner.setAdapter(customAdapter);
                customAdapter.setDropDownViewResource(R.layout.custom_dropdown_layout);
            }
        };
        alKategoriaImg.observe(getViewLifecycleOwner(), alKategoriaObserver);

        Observer<ArrayList<String>> valutaObserver = new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, valutaList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                valutaSpinner.setAdapter(adapter);
            }
        };
        valutaMutableLiveData.observe(getViewLifecycleOwner(), valutaObserver);
        valutaMutableLiveData.setValue((ArrayList<String>) valutaList);
    }

    private ArrayList<CustomItem> getKategoriaCustomList() {
        kategoriaCustomList = new ArrayList<>();
        kategoriaCustomList.add(new CustomItem(database.kategoriaDao().getKategoriaNameByID(1), R.drawable.ic_bevetel));
        kategoriaCustomList.add(new CustomItem(database.kategoriaDao().getKategoriaNameByID(2), R.drawable.ic_kiadas));
        return kategoriaCustomList;
    }

    private ArrayList<CustomItem> getAlKategoriaCustomList() {
        alKategoriaCustomList = new ArrayList<>();
        String s = ((CustomItem) kategoriaSpinner.getSelectedItem()).getSpinnerItemName();
        int katID = database.kategoriaDao().getKategoriaByName(s).getID();
        List<String> alkList = database.alKategoriaDao().getAllAlkategoriaNameByID(katID);
        for (String str : alkList) {
            alKategoriaCustomList.add(new CustomItem(str, ResourceHandler.getResourceID(str)));
        }
        return alKategoriaCustomList;
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}