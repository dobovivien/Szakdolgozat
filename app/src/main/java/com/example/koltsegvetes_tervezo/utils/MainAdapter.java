package com.example.koltsegvetes_tervezo.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koltsegvetes_tervezo.R;
import com.example.koltsegvetes_tervezo.ui.DAO.TranzakcioDao;
import com.example.koltsegvetes_tervezo.ui.entities.AlKategoria;
import com.example.koltsegvetes_tervezo.ui.entities.AppDatabase;
import com.example.koltsegvetes_tervezo.ui.entities.Kategoria;
import com.example.koltsegvetes_tervezo.ui.entities.Tranzakcio;
import com.example.koltsegvetes_tervezo.ui.DAO.AlKategoriaDao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    //Initialize variables
    private List<Tranzakcio> tranzakcioList;
    private Activity context;
    private AppDatabase database;
    private static final int SECRET_KEY = 99;


    //constructor
    public MainAdapter(Activity context, List<Tranzakcio> tranzakcioList) {
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
        holder.kategoriaTextView.setText(String.valueOf(a.getAlKategoriaNev()));
        if (t.getKategoriaID() == 1) {
            holder.tranzListTextView.setTextColor(R.color.yellow);
            holder.tranzListTextView.setText(s);
        }
        String strDate = "";
        if (t.getDatum() != null)
        {
            DateFormat dateFormat = SimpleDateFormat.getDateInstance();
            strDate = dateFormat.format(t.getDatum());
        }
        holder.datumTextView.setText(strDate);
        holder.editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //init tranzakcio data
                Tranzakcio t = tranzakcioList.get(holder.getAdapterPosition());
                final int sID = t.getID();
                final int sKategoria = t.getKategoriaID();
                final int sAlKategoroia = t.getAlKategoriaID();
                final int sOsszeg = t.getOsszeg();
                final Date sDatum = t.getDatum();
                final String sMegjegyzes = t.getMegjegyzes();

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.tranzakcio_update_fragment);
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setLayout(width, height);
                dialog.show();
            }
        });

        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tranzakcio t = tranzakcioList.get(holder.getAdapterPosition());
                database.tranzakcioDao().delete(t);
                int position = holder.getAdapterPosition();
                tranzakcioList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, tranzakcioList.size());
                Toast.makeText(context, "Tranzakció törölve!", Toast.LENGTH_SHORT).show();
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
                b.putInt("kategoria", t.getKategoriaID());
                b.putInt("alkategoria", t.getAlKategoriaID());
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
        ImageView editImageView;
        ImageView deleteImageView;
        TextView kategoriaTextView;
        TextView datumTextView;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            //Assign variables
            tranzListTextView = itemView.findViewById(R.id.tranzListTextView);
            editImageView = itemView.findViewById(R.id.editImageView);
            deleteImageView = itemView.findViewById(R.id.deleteImageView);
            kategoriaTextView = itemView.findViewById(R.id.tranzakcioKategoriaListTextView);
            datumTextView = itemView.findViewById(R.id.tranzakcioDatumListTextView);
        }
    }
}
