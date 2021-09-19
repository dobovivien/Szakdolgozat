package com.example.koltsegvetes_tervezo.ui.fragments;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
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

import com.example.koltsegvetes_tervezo.MainActivity;
import com.example.koltsegvetes_tervezo.R;

public class SettingsFragment extends Fragment {

    private SettingsViewModel mViewModel;
    private Button notification;
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

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence name = "NAMEEE";
                String description = "DESCRIPTIOON";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(getString(R.string.app_name), name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
//                NotificationManager notificationManager = (NotificationManager)
//                        getContext().getSystemService(Context.NOTIFICATION_SERVICE);
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
    }

}