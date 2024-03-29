package com.example.koltsegvetes_tervezo.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koltsegvetes_tervezo.R;
import com.example.koltsegvetes_tervezo.ResourceHandler;
import com.example.koltsegvetes_tervezo.ui.entities.AppDatabase;
import com.example.koltsegvetes_tervezo.ui.entities.Ertesites;

import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder>{

    private final List<Ertesites> ertesitesList;
    private Activity context;
    private AppDatabase database;

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
