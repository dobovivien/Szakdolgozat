package com.example.koltsegvetes_tervezo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.koltsegvetes_tervezo.ui.entities.AppDatabase;
import com.example.koltsegvetes_tervezo.ui.entities.Ertesites;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    AppDatabase database = AppDatabase.getInstance(this);
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    Timer timer;
    TimerTask timerTask;
    ArrayList<Ertesites> ertesitesList = (ArrayList<Ertesites>) database.ertesitesDao().getAll();
    LocalDateTime now = LocalDateTime.now();
    int day = now.getDayOfMonth();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy () {
        stopTimerTask();
        super.onDestroy();
    }

    final Handler handler = new Handler();
    public void startTimer () {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule( timerTask, 2 * 60 * 60 * 1000, 24 * 60 * 60 * 1000); //24 orankent lefut es az inditast koveto masodik oraban kuld ertesitest ha kell
    }

    public void stopTimerTask () {
        if (timer != null) {
            timer .cancel();
            timer = null;
        }
    }
    public void initializeTimerTask () {
        timerTask = new TimerTask() {
            public void run () {
                handler .post( new Runnable() {
                    public void run () {
                        for (int i = 0; i < ertesitesList.size(); i++) {
                            if (ertesitesList.get(i).getDatum() == day && ertesitesList.get(i).isBekapcsolva()) {
                                createNotification(ertesitesList.get(i));
                            }
                        }
                    }
                });
            }
        };
    }

    private void createNotification (Ertesites ertesites) {
        Intent intent1 = new Intent(this, MainActivity.class);
        intent1.putExtra("fragmentName", "TranzakcioAddFragment");
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int alkatID = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_ONE_SHOT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        alkatID = ertesites.getAlKategoriaID();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.app_name));
        builder.setContentTitle("Értesítés");
        builder.setContentText("Ne feledkezz meg a(z) " + database.ertesitesDao().getAlkategoria(alkatID) + " rögzítéséről!");
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setSound(uri);
        builder.addAction(R.drawable.ic_launcher_foreground, "Rögzítem most", pendingIntent);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"NOTIFICATION_CHANNEL_NAME", importance);
            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
        assert notificationManager != null;
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
