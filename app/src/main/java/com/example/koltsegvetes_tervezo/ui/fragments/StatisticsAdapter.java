package com.example.koltsegvetes_tervezo.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
import com.example.koltsegvetes_tervezo.ResourceHandler;
import com.example.koltsegvetes_tervezo.ui.entities.AlKategoria;
import com.example.koltsegvetes_tervezo.ui.entities.AppDatabase;
import com.example.koltsegvetes_tervezo.ui.entities.Tranzakcio;
import com.example.koltsegvetes_tervezo.utils.TranzakcioListAdapter;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder> {

    //Initialize variables
    private HashMap<String, Integer> tranzakcioList;
    private final Activity context;
    private AppDatabase database;
    private int sum;


    //constructor
    public StatisticsAdapter(Activity context, HashMap<String, Integer> tranzakcioList) {
        this.context = context;
        this.tranzakcioList = tranzakcioList;
        this.sum = osszegOsszead();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Init view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tranzakciok_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        String alkategoria = tranzakcioList.keySet().toArray()[position].toString();
        final int tranzakcio = tranzakcioList.get(alkategoria);

        database = AppDatabase.getInstance(context);

        holder.osszegTextView.setText(String.valueOf(tranzakcio + " Ft"));
        holder.alKategoriaImageView.setImageResource(ResourceHandler.getResourceID(alkategoria));
        DecimalFormat df = new DecimalFormat("#.#");
        holder.szazalekTextView.setText(String.valueOf(df.format(Double.valueOf(tranzakcio)/sum*100) + "%"));
    }

    @Override
    public int getItemCount() {
        return tranzakcioList.size();
    }

    public int osszegOsszead() {
        int s = 0;
        for (int a: tranzakcioList.values()) {
            s += a;
        }
        System.out.println(s);
        return s;
    }

    public void setTranzakcioList(HashMap<String, Integer> tranzakcioList) {
        this.tranzakcioList = tranzakcioList;
        sum = osszegOsszead();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView alKategoriaImageView;
        TextView osszegTextView;
        TextView szazalekTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            alKategoriaImageView = itemView.findViewById(R.id.alKategoriaImageView);
            osszegTextView = itemView.findViewById(R.id.osszegTextView);
            szazalekTextView = itemView.findViewById(R.id.szazalekTextView);
        }
    }
}

