package com.example.koltsegvetes_tervezo.ui.fragments;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.koltsegvetes_tervezo.MainActivity;
import com.example.koltsegvetes_tervezo.R;
import com.example.koltsegvetes_tervezo.ui.entities.AppDatabase;
import com.example.koltsegvetes_tervezo.ui.entities.Ertesites;
import com.example.koltsegvetes_tervezo.ui.entities.Valutak;
import com.example.koltsegvetes_tervezo.utils.Valuta;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

    private SettingsViewModel mViewModel;
    AppDatabase database;
    private Button notification;
    private FloatingActionButton valutaValtas;
    private TextView valasztottValuta;
    private Switch allandoValutaSwitch;
    private String[] valutakList = {"Ft", "Eur", "Usd", "Din"};
    private List<Valutak> valutak = new ArrayList<>();
    int defaultSelectedPosition = 0;
    int valutaSelect;
    View view;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        // TODO: Use the ViewModel

        view = getView();
        notification = view.findViewById(R.id.notificationButton);
        valutaValtas = view.findViewById(R.id.valutaChangeButton);
        valasztottValuta = view.findViewById(R.id.valutaChangeTextView);
        allandoValutaSwitch = view.findViewById(R.id.allandoValutaSwitch);

//        if (!allandoValutaSwitch.isChecked()) {
//            valutaValtas.setEnabled(false);
//        } else {
//            valutaValtas.setEnabled(true);
//        }

        database = AppDatabase.getInstance(getActivity().getApplication());

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

        valutaValtas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
                builder.setTitle("Valuta kiválasztása");
                builder.setCancelable(false);

                builder.setSingleChoiceItems(valutakList,  defaultSelectedPosition, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        defaultSelectedPosition = which;
                    }
                });

                builder.setPositiveButton("Kiválaszt", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        valasztottValuta.setText(valutakList[defaultSelectedPosition]);
                        Valuta.valuta = valutakList[defaultSelectedPosition];
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
}