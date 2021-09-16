package com.example.koltsegvetes_tervezo.ui.fragments;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.koltsegvetes_tervezo.CustomItem;
import com.example.koltsegvetes_tervezo.R;
import com.example.koltsegvetes_tervezo.ResourceHandler;
import com.example.koltsegvetes_tervezo.ui.entities.AlKategoria;
import com.example.koltsegvetes_tervezo.ui.entities.AppDatabase;
import com.example.koltsegvetes_tervezo.ui.entities.Kategoria;
import com.example.koltsegvetes_tervezo.ui.entities.Tranzakcio;
import com.example.koltsegvetes_tervezo.utils.TranzakcioListAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private StatisticsViewModel mViewModel;
    View view;
    ImageView alKategoriaImageView;
    TextView osszegTextView;
    TextView szazalekTextView;
    TextView kategoriaTextView;
    RecyclerView alkategoriaRecyclerView;
    StatisticsAdapter adapter;
    Button kordGomb;
    Button oszlopdGomb;
    Button vonaldGomb;
    Button valtasGomb;
    BarChart barChart;
    PieChart pieChart;
    LineChart lineChart;
    BarData barData;
    ArrayList<IBarDataSet> dataSets;


    int katID = 2;
    String katText = "Kiadásaim";
    HashMap<String, Integer> tranzakcioList = new HashMap<String, Integer>();
    List<Kategoria> kategoriaList = new ArrayList<Kategoria>();
    AppDatabase database;
    LinearLayoutManager linearLayoutManager;

    public static StatisticsFragment newInstance() {
        return new StatisticsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.statisctics_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StatisticsViewModel.class);

        view = getView();
        alKategoriaImageView = view.findViewById(R.id.alKategoriaImageView);
        osszegTextView = view.findViewById(R.id.osszegTextView);
        szazalekTextView = view.findViewById(R.id.szazalekTextView);
        alkategoriaRecyclerView = view.findViewById(R.id.tranzakciokRecyclerview);
        kategoriaTextView = view.findViewById(R.id.categoryTextView);
        kategoriaTextView.setText("Kiadásaim");
        valtasGomb = view.findViewById(R.id.sourceChangeButton);

        barChart = view.findViewById(R.id.barChart);
        pieChart = view.findViewById(R.id.pieChart);
        lineChart = view.findViewById(R.id.lineChart);

        database = AppDatabase.getInstance(getActivity().getApplication());

        barData = new BarData(getDataSet());
        barChart.setData(barData);
        barChart.animateXY(2000, 2000);
        barChart.getDescription().setEnabled(false);
        barChart.invalidate();
        barChart.getLegend().setEnabled(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);

        XAxis topAxis = barChart.getXAxis();
        topAxis.setDrawGridLines(false);
        topAxis.setDrawLabels(false);

        linearLayoutManager = new LinearLayoutManager(getActivity().getApplication());
        alkategoriaRecyclerView.setLayoutManager(linearLayoutManager);
        kategoriaList = database.kategoriaDao().getAll();
        tranzakcioList = osszegzesAlkategorianket();
        kategoriaTextView.setText(katText);
        adapter = new StatisticsAdapter(getActivity(), tranzakcioList);

        alkategoriaRecyclerView.setAdapter(adapter);


        valtasGomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeKatID();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private ArrayList<IBarDataSet> getDataSet() {
        dataSets = new ArrayList<>();

        ArrayList<BarEntry> valueSet = new ArrayList();
        tranzakcioList = osszegzesAlkategorianket();
        int i = 0;
        for (String alkategoria: tranzakcioList.keySet()) {
            BarEntry barEntry = new BarEntry(i, tranzakcioList.get(alkategoria));
            valueSet.add(barEntry);
            i++;
        }

        BarDataSet barDataSet = new BarDataSet(valueSet, "");
        dataSets.add(barDataSet);

        return dataSets;
    }

    public void changeKatID () {
        if (katID == 2) {
            katID = 1;
            katText = "Bevételeim";
            kategoriaTextView.setText(katText);
        } else {
            katID = 2;
            katText = "Kiadásaim";
            kategoriaTextView.setText(katText);
        }
        tranzakcioList = osszegzesAlkategorianket();
        adapter.setTranzakcioList(tranzakcioList);
    }

    public HashMap<String, Integer> osszegzesAlkategorianket() {
        HashMap<String, Integer> tranzDict = new HashMap<String, Integer>();
        List<AlKategoria> alkategoriak = database.alKategoriaDao().getAlkategoriaByKategoriaId(katID);
        for (int i = 0; i < alkategoriak.size(); i++) {
            Integer test = database.tranzakcioDao().getTransactionByAlCategory(alkategoriak.get(i).getID());
            if (test != null) {
                //int tranzakcioOsszeg = database.tranzakcioDao().getTransactionByAlCategory(alkategoriak.get(i).getID());
                tranzDict.put(alkategoriak.get(i).getAlKategoriaNev(), test);
            }
        }
        return tranzDict;
    }

    @Override
    public void onResume() {
        super.onResume();
        tranzakcioList = osszegzesAlkategorianket();
        adapter = new StatisticsAdapter(getActivity(), tranzakcioList);
        alkategoriaRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}