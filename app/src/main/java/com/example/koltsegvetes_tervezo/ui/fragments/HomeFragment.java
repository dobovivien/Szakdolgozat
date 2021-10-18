package com.example.koltsegvetes_tervezo.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.example.koltsegvetes_tervezo.R;
import com.example.koltsegvetes_tervezo.ui.entities.AppDatabase;
import com.example.koltsegvetes_tervezo.ui.entities.Ertesites;
import com.example.koltsegvetes_tervezo.ui.entities.Megtakaritas;
import com.example.koltsegvetes_tervezo.ui.entities.Tranzakcio;
import com.example.koltsegvetes_tervezo.utils.SettingsAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    View view;
    AppDatabase database;
    private int egyenleg;
    private RecyclerView megtakaritasRecycler_view;
    ArrayList<Megtakaritas> megtakaritasok = new ArrayList<Megtakaritas>();
    LinearLayoutManager linearLayoutManager;
    private MegtakaritasAdapter adapter;
    private TextView egyenlegTextView;
    private TextView egynelegMegtakaritassalTextView;
    private Button sporolasButton;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public HomeFragment() {
        super(R.layout.home_fragment);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        View view = getView();
        database = AppDatabase.getInstance(getActivity().getApplication());

        megtakaritasRecycler_view = view.findViewById(R.id.megtakaritasRecycler_view);

        megtakaritasok = (ArrayList<Megtakaritas>) database.megtakaritasDao().getAll();

        linearLayoutManager = new LinearLayoutManager(getActivity().getApplication());

        megtakaritasRecycler_view.setLayoutManager(linearLayoutManager);

        adapter = new MegtakaritasAdapter(getActivity(), megtakaritasok);

        megtakaritasRecycler_view.setAdapter(adapter);
        megtakaritasRecycler_view.setNestedScrollingEnabled(false);
        adapter.notifyDataSetChanged();

        egyenlegTextView = view.findViewById(R.id.egyenlegTextView);
        egynelegMegtakaritassalTextView = view.findViewById(R.id.egynelegMegtakaritassalTextView);
        sporolasButton = view.findViewById(R.id.sporolasButton);

        egyenlegTextView.setText(getEgyenleg() + " Ft");
        egynelegMegtakaritassalTextView.setText((getEgyenleg() - database.megtakaritasDao().eddigFelretett()) + " Ft");



        sporolasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), com.example.koltsegvetes_tervezo.MegtakaridasAddActivity.class);
                getContext().startActivity(intent);
            }
        });
    }

    public int getEgyenleg() {
        int bevetel = 0;
        int kiadas = 0;
        List<Tranzakcio> tranzakciokBevetelek = database.tranzakcioDao().getTransactionByCategory(1);
        for (int j = 0; j < tranzakciokBevetelek.size(); j++) {
            if (tranzakciokBevetelek.get(j).getValutaID() != 1) {
                int valutaID = tranzakciokBevetelek.get(j).getValutaID();
                String valutaNev = database.valutakDao().getValutaNameByID(valutaID);
                int arfolyam = database.arfolyamDao().selectArfolyamByVvalutaRovidNev(valutaNev);
                bevetel += tranzakciokBevetelek.get(j).getOsszeg() * arfolyam;
            } else {
                bevetel += tranzakciokBevetelek.get(j).getOsszeg();
            }
        }

        List<Tranzakcio> tranzakciokKiadasok = database.tranzakcioDao().getTransactionByCategory(2);
        for (int j = 0; j < tranzakciokKiadasok.size(); j++) {
            if (tranzakciokKiadasok.get(j).getValutaID() != 1) {
                int valutaID = tranzakciokKiadasok.get(j).getValutaID();
                String valutaNev = database.valutakDao().getValutaNameByID(valutaID);
                int arfolyam = database.arfolyamDao().selectArfolyamByVvalutaRovidNev(valutaNev);
                kiadas += tranzakciokKiadasok.get(j).getOsszeg() * arfolyam;
            } else {
                kiadas += tranzakciokKiadasok.get(j).getOsszeg();
            }
        }
        return bevetel - kiadas;
    }

    public void updateMegtakaritasList() {
        megtakaritasok = (ArrayList<Megtakaritas>) database.megtakaritasDao().getAll();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        megtakaritasok = (ArrayList<Megtakaritas>) database.megtakaritasDao().getAll();
        adapter = new MegtakaritasAdapter(getActivity(), megtakaritasok);
        megtakaritasRecycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        updateMegtakaritasList();
    }

}