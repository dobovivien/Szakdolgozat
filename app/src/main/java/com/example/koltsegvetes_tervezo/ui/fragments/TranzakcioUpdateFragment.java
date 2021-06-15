package com.example.koltsegvetes_tervezo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.koltsegvetes_tervezo.R;
import com.example.koltsegvetes_tervezo.ui.entities.AppDatabase;
import com.example.koltsegvetes_tervezo.ui.entities.Tranzakcio;
import com.example.koltsegvetes_tervezo.utils.MainAdapter;

import java.util.ArrayList;
import java.util.List;

public class TranzakcioUpdateFragment extends Fragment {

    private TranzakcioAddViewModel mViewModel;
    View view;
    EditText osszegUpdate;
    TextView datumUpdate;
    EditText megjegyzesUpdate;
    Button datumButton;
    Button tranzakcioUpdateButton;
    List<Tranzakcio> tranzakcioList = new ArrayList<Tranzakcio>();
    LinearLayoutManager linearLayoutManager;
    AppDatabase database;
    MainAdapter mainAdapter;

    public static TranzakcioUpdateFragment newInstance() {
        return new TranzakcioUpdateFragment();
    }

    public TranzakcioUpdateFragment() {
        super(R.layout.tranzakcio_update_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tranzakcio_update_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TranzakcioAddViewModel.class);

        //Initialize variables
        view = getView();
        datumButton = view.findViewById(R.id.datumUpdateButton);
        tranzakcioUpdateButton = view.findViewById(R.id.tranzakcioUpdateButton);
        osszegUpdate = view.findViewById(R.id.osszegUpdateEditText);
        datumUpdate = view.findViewById(R.id.datumUpdateTextView);
        megjegyzesUpdate = view.findViewById(R.id.megjegyzesUpdateEditText);
        datumButton = view.findViewById(R.id.datumButton);



        datumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePicker();
                datePicker.show(getParentFragmentManager(), "date picker");
            }
        });

        tranzakcioUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
