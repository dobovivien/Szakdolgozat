package com.example.koltsegvetes_tervezo.ui.fragments;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
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
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.koltsegvetes_tervezo.MainActivity;
import com.example.koltsegvetes_tervezo.R;
import com.example.koltsegvetes_tervezo.ui.entities.AppDatabase;
import com.example.koltsegvetes_tervezo.ui.entities.Ertesites;
import com.example.koltsegvetes_tervezo.ui.entities.Tranzakcio;
import com.example.koltsegvetes_tervezo.ui.entities.Valutak;
import com.example.koltsegvetes_tervezo.utils.SettingsAdapter;
import com.example.koltsegvetes_tervezo.utils.TranzakcioListAdapter;
import com.example.koltsegvetes_tervezo.utils.Valuta;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

    View view;
    private SettingsViewModel mViewModel;
    AppDatabase database;
    public int euro = 359;
    public int dollar = 307;
    public int dinar = 3;
    private Button notification;
    private EditText euroEditText;
    private EditText dollarEditText;
    private EditText dinarEditText;
    private Button euroOkButton;
    private Button dollarOkButton;
    private Button dinarOkButton;
    private TextView euroTextView;
    private TextView dollarTextView;
    private TextView dinarTextView;

    RecyclerView ertesitesListaReciclerView;
    ArrayList<Ertesites> ertesitesek = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    SettingsAdapter adapter;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    @SuppressLint({"CutPasteId", "SetTextI18n"})
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        view = getView();

        ertesitesListaReciclerView = view.findViewById(R.id.settingsReciclerView);

        database = AppDatabase.getInstance(getActivity().getApplication());

        ertesitesek = (ArrayList<Ertesites>) database.ertesitesDao().getAll();

        linearLayoutManager = new LinearLayoutManager(getActivity().getApplication());

        ertesitesListaReciclerView.setLayoutManager(linearLayoutManager);

        adapter = new SettingsAdapter(getActivity(), ertesitesek);

        ertesitesListaReciclerView.setAdapter(adapter);
        ertesitesListaReciclerView.setNestedScrollingEnabled(false);

        notification = view.findViewById(R.id.notificationButton);
        euroEditText = view.findViewById(R.id.euroArfolyamEditText);
        dollarEditText = view.findViewById(R.id.dollarArfolyamEditText);
        dinarEditText = view.findViewById(R.id.dinarArfolyamEditText);
        euroOkButton = view.findViewById(R.id.euroOkButton);
        dollarOkButton = view.findViewById(R.id.dollarOkButton);
        dinarOkButton = view.findViewById(R.id.dinarOkButton);
        euroTextView = view.findViewById(R.id.euroTextView);
        dollarTextView = view.findViewById(R.id.dollarTextView);
        dinarTextView = view.findViewById(R.id.dinarTextView);

        euroTextView.setText(database.arfolyamDao().selectArfolyamByValutaName("Euro") + " Ft");
        dollarTextView.setText(database.arfolyamDao().selectArfolyamByValutaName("Dollár") + " Ft");
        dinarTextView.setText(database.arfolyamDao().selectArfolyamByValutaName("Dinár") + " Ft");

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Notification
                CharSequence name = "NAME";
                String description = "DESCRIPTION";

                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(getString(R.string.app_name), name, importance);
                channel.setDescription(description);

                NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);

                Intent intent1 = new Intent(getContext(), MainActivity.class);
                intent1.putExtra("fragmentName", "TranzakcioAddFragment");
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent1, PendingIntent.FLAG_ONE_SHOT);

                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), getString(R.string.app_name));

                builder.setContentTitle("Notification");
                builder.setContentText("Ez egy proba notification.");
                builder.setSmallIcon(R.drawable.ic_notification);
                builder.setSound(uri);
                builder.setAutoCancel(true);
                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                builder.addAction(R.drawable.ic_launcher_foreground, "Yes", pendingIntent);
                builder.setContentIntent(pendingIntent);
                notificationManager.notify(1, builder.build());
            }
        });

        euroOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float e = Float.parseFloat(String.valueOf(euroEditText.getText()));
                euro = Math.round(e);
                database.arfolyamDao().update(euro, "Euro");
                euroTextView.setText(euro + "Ft");
                euroEditText.setText("");
            }
        });

        dollarOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float d = Float.parseFloat(String.valueOf(euroEditText.getText()));
                dollar = Math.round(d);
                database.arfolyamDao().update(dollar, "Dollár");
                dollarTextView.setText(dollar + " Ft");
                dollarEditText.setText("");
            }
        });

        dinarOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float di = Float.parseFloat(String.valueOf(euroEditText.getText()));
                dinar = Math.round(di);
                database.arfolyamDao().update(dinar, "Dinár");
                dinarTextView.setText(dinar + " Ft");
                dinarEditText.setText("");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ertesitesek = (ArrayList<Ertesites>) database.ertesitesDao().getAll();
        adapter = new SettingsAdapter(getActivity(), ertesitesek);
        ertesitesListaReciclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}