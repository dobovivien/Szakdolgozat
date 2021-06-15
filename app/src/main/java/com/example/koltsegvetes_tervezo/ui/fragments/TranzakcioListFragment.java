package com.example.koltsegvetes_tervezo.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koltsegvetes_tervezo.MainActivity;
import com.example.koltsegvetes_tervezo.R;
import com.example.koltsegvetes_tervezo.ui.DAO.KategoriaDao;
import com.example.koltsegvetes_tervezo.ui.entities.AppDatabase;
import com.example.koltsegvetes_tervezo.ui.entities.Kategoria;
import com.example.koltsegvetes_tervezo.ui.entities.Tranzakcio;
import com.example.koltsegvetes_tervezo.utils.MainAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class TranzakcioListFragment extends Fragment {

    private TranzakcioAddViewModel mViewModel;
    View view;
    RecyclerView tranzakcioListaRecycleView;
    ArrayList<Tranzakcio> tranzakciok = new ArrayList<Tranzakcio>();
    LinearLayoutManager linearLayoutManager;
    AppDatabase database;
    MainAdapter adapter;
    TextView filter;
    boolean[] selectedCategory;
    ArrayList<Integer> categoryArray = new ArrayList<>();
    String[] categoryList = {"Bevétel", "Kiadás"};


    public static TranzakcioListFragment newInstance() {
        return new TranzakcioListFragment();
    }

    public TranzakcioListFragment() {
        super(R.layout.tranzakcio_list_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tranzakcio_list_fragment, container, false);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TranzakcioAddViewModel.class);
        view = getView();

        tranzakcioListaRecycleView = view.findViewById(R.id.recycler_view);

        database = AppDatabase.getInstance(getActivity().getApplication());

        tranzakciok = (ArrayList<Tranzakcio>) database.tranzakcioDao().getAllByDescDate();

        linearLayoutManager = new LinearLayoutManager(getActivity().getApplication());

        tranzakcioListaRecycleView.setLayoutManager(linearLayoutManager);

        adapter = new MainAdapter(getActivity(), tranzakciok);

        tranzakcioListaRecycleView.setAdapter(adapter);

        filter = view.findViewById(R.id.filterTextView);
        selectedCategory = new boolean[categoryList.length];

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Kategóriák szűrése");
                builder.setCancelable(false);

                builder.setMultiChoiceItems(categoryList, selectedCategory, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i, boolean b) {
                        if (b) {
                            categoryArray.add(i);
                            Collections.sort(categoryArray);
                        } else {
                            categoryArray.remove(categoryArray.indexOf(i));
                        }
                    }
                });

                builder.setPositiveButton("Kiválaszt", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        tranzakciok.clear();
                        StringBuilder stringBuilder = new StringBuilder();
                        if (categoryArray.isEmpty()) {
                            tranzakciok = (ArrayList<Tranzakcio>) database.tranzakcioDao().getAllByDescDate();
                        }
                        for (int j = 0; j < categoryArray.size(); j++) {
                            stringBuilder.append(categoryList[categoryArray.get(j)]);
                            tranzakciok.addAll(database.tranzakcioDao().getTransactionByCategory(categoryArray.get(j)+1));
                            if (j != categoryArray.size()-1) {
                                stringBuilder.append(", ");
                            }
                        }
                        filter.setText(stringBuilder.toString());
                        adapter = new MainAdapter(getActivity(), tranzakciok);

                        tranzakcioListaRecycleView.setAdapter(adapter);
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

    @Override
    public void onResume() {
        super.onResume();
        tranzakciok = (ArrayList<Tranzakcio>) database.tranzakcioDao().getAllByDescDate();
        adapter = new MainAdapter(getActivity(), tranzakciok);
        tranzakcioListaRecycleView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
