package com.example.koltsegvetes_tervezo.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koltsegvetes_tervezo.MainActivity;
import com.example.koltsegvetes_tervezo.R;
import com.example.koltsegvetes_tervezo.ResourceHandler;
import com.example.koltsegvetes_tervezo.ui.entities.AlKategoria;
import com.example.koltsegvetes_tervezo.ui.entities.AppDatabase;
import com.example.koltsegvetes_tervezo.ui.entities.Ertesites;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder>{

    private final List<Ertesites> ertesitesList;
    private Activity context;
    private AppDatabase database;
    private static final int SECRET_KEY = 99;


    //constructor
    public SettingsAdapter(Activity context, List<Ertesites> ertesitesList) {
        this.context = context;
        this.ertesitesList = ertesitesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SettingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Init view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ertesites_cardview, parent, false);
        return new SettingsAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Ertesites ertesites = ertesitesList.get(position);
        database = AppDatabase.getInstance(context);

        Ertesites ert = ertesitesList.get(holder.getAdapterPosition());
        holder.alkategoriaSettingTextView.setText(database.alKategoriaDao().getAlkategoriaNameById(ertesites.getAlKategoriaID()));
        holder.alKategoriaSettingImageView.setImageResource(ResourceHandler.getResourceID(database.alKategoriaDao().getAlkategoriaNameById(ertesites.getAlKategoriaID())));
        holder.ertesitesIdejeTextView.setText("Minden hónap " + database.ertesitesDao().getErtesitesDatum(ertesites.getID()) + ". napján.");
        holder.kikapcsolSwitch.setChecked(ertesites.isBekapcsolva());
        holder.deleteSettingImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.MyDialogTheme);
                builder.setMessage("Biztosan törölni szeretné?");

                builder.setPositiveButton("Törlés", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Ertesites ertesites1 = ertesitesList.get(holder.getAdapterPosition());
                        database.ertesitesDao().delete(ertesites1);
                        int position = holder.getAdapterPosition();
                        ertesitesList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, ertesitesList.size());
                        dialog.dismiss();
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


        holder.editSettingImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ertesites ertesites = ertesitesList.get(holder.getAdapterPosition());

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.MyDialogTheme);
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.settings_ertesites_modosit, null);
                builder.setView(dialogView);
                builder.setTitle("Értesítés napjának kiválasztása");
                NumberPicker dayNumberPicker = dialogView.findViewById(R.id.dayNumberPicker);
                dayNumberPicker.setMinValue(1);
                dayNumberPicker.setMaxValue(31);

                builder.setPositiveButton("Módosít", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int pickedNumber = dayNumberPicker.getValue();
                        holder.ertesitesIdejeTextView.setText("Minden hónap " + pickedNumber + ". napján.");
                        database.ertesitesDao().updateDate(pickedNumber, ertesites.getID());
                    }
                });

                builder.setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        holder.kikapcsolSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                database.ertesitesDao().updateBekapcsolva(isChecked, ertesites.getID());
                if (isChecked) {
                    holder.editSettingImageView.setEnabled(true);
                    holder.ertesitesIdejeTextView.setEnabled(true);
                    CharSequence name = "NAME";
                    String description = "DESCRIPTION";

                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(String.valueOf(R.string.app_name), name, importance);
                    channel.setDescription(description);

                    NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);

                    Intent intent1 = new Intent(context, MainActivity.class);
                    intent1.putExtra("fragmentName", "TranzakcioAddFragment");
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_ONE_SHOT);

                    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, String.valueOf(R.string.app_name));

                    builder.setContentTitle("Ertesites");
                    builder.setContentText("Ez egy proba notification.");
                    builder.setSmallIcon(R.drawable.ic_notification);
                    builder.setSound(uri);
                    builder.setAutoCancel(true);
                    builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    builder.addAction(R.drawable.ic_launcher_foreground, "Yes", pendingIntent);
                    builder.setContentIntent(pendingIntent);
                    notificationManager.notify(1, builder.build());
                } else {
                    holder.editSettingImageView.setEnabled(false);
                    holder.ertesitesIdejeTextView.setEnabled(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ertesitesList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView alKategoriaSettingImageView;
        TextView alkategoriaSettingTextView;
        ImageView editSettingImageView;
        ImageView deleteSettingImageView;
        TextView ertesitesIdejeTextView;
        Switch kikapcsolSwitch;
        CardView ertesitesCardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            alKategoriaSettingImageView = itemView.findViewById(R.id.alKategoriaSettingImageView);
            alkategoriaSettingTextView = itemView.findViewById(R.id.alkategoriaSettingTextView);
            editSettingImageView = itemView.findViewById(R.id.editSettingImageView);
            deleteSettingImageView = itemView.findViewById(R.id.deleteSettingImageView);
            ertesitesIdejeTextView = itemView.findViewById(R.id.ertesitesIdejeTextView);
            kikapcsolSwitch = itemView.findViewById(R.id.kikapcsolSwitch);
            ertesitesCardView = itemView.findViewById(R.id.ertesitesCardView);
        }
    }
}
