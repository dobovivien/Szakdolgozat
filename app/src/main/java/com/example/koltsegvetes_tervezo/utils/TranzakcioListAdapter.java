package com.example.koltsegvetes_tervezo.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koltsegvetes_tervezo.R;
import com.example.koltsegvetes_tervezo.ui.entities.AlKategoria;
import com.example.koltsegvetes_tervezo.ui.entities.AppDatabase;
import com.example.koltsegvetes_tervezo.ui.entities.Tranzakcio;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TranzakcioListAdapter extends RecyclerView.Adapter<TranzakcioListAdapter.ViewHolder> {

    //Initialize variables
    private List<Tranzakcio> tranzakcioList;
    private Activity context;
    private AppDatabase database;
    private static final int SECRET_KEY = 99;


    //constructor
    public TranzakcioListAdapter(Activity context, List<Tranzakcio> tranzakcioList) {
        this.context = context;
        this.tranzakcioList = tranzakcioList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Init view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_main, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Tranzakcio tranzakcio = tranzakcioList.get(position);
        database = AppDatabase.getInstance(context);
        AlKategoria a = database.alKategoriaDao().getAlkategoriaById(tranzakcio.getAlKategoriaID());

        Tranzakcio t = tranzakcioList.get(holder.getAdapterPosition());
        String s = "+" + (t.getOsszeg());
        //set text on textview
        holder.tranzListTextView.setText(String.valueOf(t.getOsszeg()));
        holder.valutaTextView.setText(database.valutakDao().getValutaNameByID(tranzakcio.getValutaID()));
        holder.kategoriaTextView.setText(String.valueOf(a.getAlKategoriaNev()));
        if (t.getKategoriaID() == 1) {
            holder.tranzListTextView.setText(s);
            holder.tranzListTextView.setTextColor(R.color.green);
            holder.valutaTextView.setTextColor(R.color.green);
        }
        String strDate = "";
        if (t.getDatum() != null)
        {
            DateFormat dateFormat = SimpleDateFormat.getDateInstance();
            strDate = dateFormat.format(t.getDatum());
        }
        holder.megjegyzesTextView.setText(tranzakcio.getMegjegyzes());
        holder.datumTextView.setText(strDate);

        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.MyDialogTheme);
                builder.setMessage("Biztosan törölni szeretné?");

                builder.setPositiveButton("Törlés", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Tranzakcio t = tranzakcioList.get(holder.getAdapterPosition());
                        database.tranzakcioDao().delete(t);
                        int position = holder.getAdapterPosition();
                        tranzakcioList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, tranzakcioList.size());
                        Toast.makeText(context, "Tranzakció törölve!", Toast.LENGTH_SHORT).show();
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

        holder.editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tranzakcio t = tranzakcioList.get(holder.getAdapterPosition());
                Intent intent = new Intent(context, com.example.koltsegvetes_tervezo.TranzakcioUpdateActivity.class);
                intent.putExtra("SECRET_KEY", SECRET_KEY);
                Bundle b = new Bundle();
                b.putInt("id", t.getID());
                b.putInt("osszeg", t.getOsszeg());
                b.putString("valuta", database.valutakDao().getValutaNameByID(t.getValutaID()));
                b.putString("kategoria", database.kategoriaDao().getKategoriaNameByID(t.getKategoriaID()));
                b.putString("alkategoria", database.alKategoriaDao().getAlkategoriaById(t.getAlKategoriaID()).getAlKategoriaNev());
                b.putString("datum", t.getDatum().toString());
                b.putString("megjegyzes", t.getMegjegyzes());
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tranzakcioList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        //Init variable
        TextView tranzListTextView;
        TextView valutaTextView;
        ImageView editImageView;
        ImageView deleteImageView;
        TextView megjegyzesTextView;
        TextView kategoriaTextView;
        TextView datumTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Assign variables
            tranzListTextView = itemView.findViewById(R.id.tranzListTextView);
            valutaTextView = itemView.findViewById(R.id.valutaTextView);
            editImageView = itemView.findViewById(R.id.editImageView);
            deleteImageView = itemView.findViewById(R.id.deleteImageView);
            megjegyzesTextView = itemView.findViewById(R.id.megjegyzesTextView);
            kategoriaTextView = itemView.findViewById(R.id.tranzakcioKategoriaListTextView);
            datumTextView = itemView.findViewById(R.id.tranzakcioDatumListTextView);
        }
    }
}
