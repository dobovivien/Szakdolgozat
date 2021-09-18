package com.example.koltsegvetes_tervezo.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koltsegvetes_tervezo.R;
import com.example.koltsegvetes_tervezo.ui.entities.AlKategoria;
import com.example.koltsegvetes_tervezo.ui.entities.AppDatabase;
import com.example.koltsegvetes_tervezo.ui.entities.Kategoria;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
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
    PieData pieData;
    LineData lineData;
    ArrayList<IBarDataSet> barDataSets;
    PieDataSet piedataSets;
    ArrayList<ILineDataSet> lineDataSets;
    TextView dateFilter;
    String[] dateFilterItems;


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
        kordGomb = view.findViewById(R.id.pieChartButton);
        oszlopdGomb = view.findViewById(R.id.barGraphButton);
        vonaldGomb = view.findViewById(R.id.lineGraphButton);
        valtasGomb = view.findViewById(R.id.sourceChangeButton);
        dateFilter = view.findViewById(R.id.dateFilterTextView);
        dateFilterItems = new String[] {"Aktuális hónap", "Aktuális hét", "Előző hét"};

        barChart = view.findViewById(R.id.barChart);
        pieChart = view.findViewById(R.id.pieChart);
        lineChart = view.findViewById(R.id.lineChart);

        database = AppDatabase.getInstance(getActivity().getApplication());

        barChartLetrehoz();
        pieChartLetrehoz();
        lineChartLetrehoz();

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
                barChartLetrehoz();
                pieChartLetrehoz();
                lineChartLetrehoz();
            }
        });

        kordGomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieChartLetrehoz();
                pieChart.setVisibility(View.VISIBLE);
                barChart.setVisibility(View.INVISIBLE);
                lineChart.setVisibility(View.INVISIBLE);
            }
        });

        oszlopdGomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barChartLetrehoz();
                barChart.setVisibility(View.VISIBLE);
                pieChart.setVisibility(View.INVISIBLE);
                lineChart.setVisibility(View.INVISIBLE);
            }
        });

        vonaldGomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineChartLetrehoz();
                lineChart.setVisibility(View.VISIBLE);
                barChart.setVisibility(View.INVISIBLE);
                pieChart.setVisibility(View.INVISIBLE);
            }
        });

        dateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
                builder.setTitle("Szűrés dátum szerint");
                builder.setCancelable(false);

                builder.setSingleChoiceItems(dateFilterItems, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                System.out.println("elso");
                                break;
                            case 1:
                                System.out.println("masodik");
                                break;
                            case 2:
                                System.out.println("harmadik");
                                break;
                        }
                    }
                });

                builder.setPositiveButton("Kiválaszt", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                });

                builder.setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    private ArrayList<IBarDataSet> getBarDataSet() {
        barDataSets = new ArrayList<>();

        ArrayList<BarEntry> valueSet = new ArrayList();
        tranzakcioList = osszegzesAlkategorianket();
        int i = 0;
        for (String alkategoria: tranzakcioList.keySet()) {
            BarEntry barEntry = new BarEntry(i, tranzakcioList.get(alkategoria));
            valueSet.add(barEntry);
            i++;
        }

        BarDataSet barDataSet = new BarDataSet(valueSet, "");
        barDataSets.add(barDataSet);

        return barDataSets;
    }

    private PieDataSet getPieDataSet() {
        ArrayList<PieEntry> valueSet = new ArrayList();
        tranzakcioList = osszegzesAlkategorianket();
        int i = 0;
        for (String alkategoria: tranzakcioList.keySet()) {
            PieEntry pieEntry = new PieEntry(tranzakcioList.get(alkategoria), i);
            valueSet.add(pieEntry);
            i++;
            System.out.println(pieEntry);
        }

        piedataSets = new PieDataSet (valueSet, "");
//        piedataSets.setColors(R.color.turkiz1, R.color.turkiz2, R.color.turkiz3, R.color.turkiz4);
        return piedataSets;
    }

    private ArrayList<ILineDataSet> getLineDataSet() {
        lineDataSets = new ArrayList<>();

        ArrayList<Entry> valueSet = new ArrayList();
        tranzakcioList = osszegzesAlkategorianket();
        int i = 0;
        for (String alkategoria: tranzakcioList.keySet()) {
            Entry lineEntry = new Entry(i, tranzakcioList.get(alkategoria));
            valueSet.add(lineEntry);
            i++;
        }

        LineDataSet lineDataSet = new LineDataSet(valueSet, "");
        lineDataSets.add(lineDataSet);

        return lineDataSets;
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

    public void barChartLetrehoz() {
        barData = new BarData(getBarDataSet());
        barChart.setData(barData);
        barChart.animateXY(2000, 2000);
        barChart.getDescription().setEnabled(false);
        barChart.notifyDataSetChanged();
        barChart.getLegend().setEnabled(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);

        XAxis topAxis = barChart.getXAxis();
        topAxis.setDrawGridLines(false);
        topAxis.setDrawLabels(false);

        barChart.invalidate();
    }

    public void pieChartLetrehoz() {
        pieData = new PieData(getPieDataSet());
        pieChart.setData(pieData);
        pieChart.setTransparentCircleRadius(100);
        pieChart.setTransparentCircleColor(21849);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(21849);
        pieChart.animateXY(2000, 2000);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.invalidate();
    }

    public void lineChartLetrehoz() {
        lineData = new LineData(getLineDataSet());
        lineChart.setData(lineData);
        lineChart.animateXY(2000, 2000);
        lineChart.getDescription().setEnabled(false);
        lineChart.notifyDataSetChanged();
        lineChart.getLegend().setEnabled(false);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);

        XAxis topAxis = lineChart.getXAxis();
        topAxis.setDrawGridLines(false);
        topAxis.setDrawLabels(false);

        lineChart.invalidate();
    }

    public HashMap<String, Integer> osszegzesAlkategorianket() {
        HashMap<String, Integer> tranzDict = new HashMap<String, Integer>();
        List<AlKategoria> alkategoriak = database.alKategoriaDao().getAlkategoriaByKategoriaId(katID);
        for (int i = 0; i < alkategoriak.size(); i++) {
            Integer test = database.tranzakcioDao().getTransactionByAlCategory(alkategoriak.get(i).getID());
            if (test != null) {
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