package com.example.koltsegvetes_tervezo.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koltsegvetes_tervezo.MainActivity;
import com.example.koltsegvetes_tervezo.R;
import com.example.koltsegvetes_tervezo.ui.entities.AppDatabase;
import com.example.koltsegvetes_tervezo.ui.entities.Ertesites;
import com.example.koltsegvetes_tervezo.ui.entities.Megtakaritas;
import com.example.koltsegvetes_tervezo.ui.entities.Tranzakcio;
import com.example.koltsegvetes_tervezo.utils.TranzakcioListAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MegtakaritasAdapter extends RecyclerView.Adapter<MegtakaritasAdapter.ViewHolder> {

    private List<Megtakaritas> megtakaritasList;
    private Activity context;
    private AppDatabase database;

    //constructor
    public MegtakaritasAdapter(Activity context, ArrayList<Megtakaritas> megtakaritasList) {
        this.context = context;
        this.megtakaritasList = megtakaritasList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MegtakaritasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.megtakaritas_card_view, parent, false);
        return new MegtakaritasAdapter.ViewHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        database = AppDatabase.getInstance(context);

        final Megtakaritas megtakaritas = megtakaritasList.get(holder.getAbsoluteAdapterPosition());

        int bevetel = 0;
        int kiadas = 0;

        if (database.tranzakcioDao().getOsszegByCategory(1) != null) {
            bevetel = database.tranzakcioDao().getOsszegByCategory(1);
        }

        if (database.tranzakcioDao().getOsszegByCategory(2) != null) {
            kiadas = database.tranzakcioDao().getOsszegByCategory(2);
        }

        int egyenleg = bevetel - kiadas;

        holder.megnevezesTextView.setText(megtakaritas.getMegnevezes());
        holder.progressBar.setMax(megtakaritas.getCelOsszeg());
        holder.progressBar.setMin(0);
        holder.progressBar.setProgress(megtakaritas.getJelenlegiOsszeg(), true);
        holder.minMaxTextView.setText(megtakaritas.getJelenlegiOsszeg() + "/" + megtakaritas.getCelOsszeg());
        String strDate = "";
        if (megtakaritas.getDatum() != null)
        {
            DateFormat dateFormat = SimpleDateFormat.getDateInstance();
            strDate = dateFormat.format(megtakaritas.getDatum());
        }
        holder.celDatumTextView.setText(strDate);

        holder.osszegHozzaadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Megtakaritas megtakaritas = megtakaritasList.get(holder.getAdapterPosition());
                Intent intent = new Intent(context, MainActivity.class);


                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.MyDialogTheme);
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.megtakaritas_kezeles, null);
                builder.setView(dialogView);
                builder.setTitle("Megtakarított összeg hozzáadása:");
                EditText editTextNumber = dialogView.findViewById(R.id.editTextNumber);

                builder.setPositiveButton("Módosít", null);
                builder.setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button modosit = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        modosit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (editTextNumber.getText().toString().equals("")) {
                                    dialog.dismiss();
                                } else {
                                    int osszeg = Integer.parseInt(editTextNumber.getText().toString());
                                    intent.putExtra("hozzaad", osszeg);
                                    if (osszeg > egyenleg) {
                                        editTextNumber.requestFocus();
                                        editTextNumber.setError("Nem áll rendelkezésre az egyenlegétől több forrás!");
                                    } else {
                                        if (megtakaritas.getJelenlegiOsszeg() + osszeg > megtakaritas.getCelOsszeg()) {
                                            editTextNumber.requestFocus();
                                            editTextNumber.setError("Legfeljebb " + (megtakaritas.getCelOsszeg() - megtakaritas.getJelenlegiOsszeg()) + " Ft  összeget adhat meg!");
                                        } else {
                                            holder.progressBar.setMin(megtakaritas.getJelenlegiOsszeg() + osszeg);
                                            database.megtakaritasDao().update(megtakaritas.getJelenlegiOsszeg()+osszeg);
                                            updateMegtakaritasList();
                                            dialog.dismiss();
                                        }
                                    }
                                }
                            }
                        });
                    }
                });
                alertDialog.show();
            }
        });

        holder.osszegLevonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Megtakaritas megtakaritas = megtakaritasList.get(holder.getAdapterPosition());
                Intent intent = new Intent(context, MainActivity.class);

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.MyDialogTheme);
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.megtakaritas_kezeles, null);
                builder.setView(dialogView);
                builder.setTitle("Megtakarított összeg levonása:");
                EditText editTextNumber = dialogView.findViewById(R.id.editTextNumber);

                builder.setPositiveButton("Módosít", null);
                builder.setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                     public void onShow(DialogInterface dialog) {
                         Button modosit = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                          modosit.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              if (editTextNumber.getText().toString().equals("")) {
                                  dialog.dismiss();
                              } else {
                                  int osszeg = Integer.parseInt(editTextNumber.getText().toString());
                                  intent.putExtra("kivon", osszeg);
                                  if (megtakaritas.getJelenlegiOsszeg() - osszeg < 0) {
                                      editTextNumber.requestFocus();
                                      editTextNumber.setError("Legfeljebb " + (megtakaritas.getJelenlegiOsszeg()) + " Ft összeget adhat meg!");
                                  } else {
                                      holder.progressBar.setMin(megtakaritas.getJelenlegiOsszeg() - osszeg);
                                      database.megtakaritasDao().update(megtakaritas.getJelenlegiOsszeg() - osszeg);
                                      updateMegtakaritasList();
                                      dialog.dismiss();
                                  }
                              }
                          }
                      });
                    }
                });
                alertDialog.show();
            }
        });

        holder.megtakaritasImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.MyDialogTheme);
                builder.setMessage("Biztosan eltávolítja?");

                builder.setPositiveButton("Törlés", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Megtakaritas megtakaritas = megtakaritasList.get(holder.getAdapterPosition());
                        database.megtakaritasDao().delete(megtakaritas);
                        int position = holder.getAdapterPosition();
                        megtakaritasList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, megtakaritasList.size());
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

        if (megtakaritas.getMegtekintve()) {
            holder.megtakaritasImageView.setImageResource(R.drawable.ic_check_circle_outline);
        }

        if (megtakaritas.getJelenlegiOsszeg() == megtakaritas.getCelOsszeg() && !megtakaritas.getMegtekintve()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.sikeres_teljesites, null);
            builder.setView(dialogView);

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            final Handler handler  = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                }
            };
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    handler.removeCallbacks(runnable);
                }
            });

            handler.postDelayed(runnable, 3000);
            database.megtakaritasDao().updateMegtekintes(true);
        }
    }

    public void updateMegtakaritasList() {
        megtakaritasList = (ArrayList<Megtakaritas>) database.megtakaritasDao().getAll();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return megtakaritasList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView megnevezesTextView;
        TextView celDatumTextView;
        ProgressBar progressBar;
        TextView minMaxTextView;
        Button osszegHozzaadButton;
        Button osszegLevonButton;
        ImageView megtakaritasImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            megnevezesTextView = itemView.findViewById(R.id.megnevezesTextView);
            celDatumTextView = itemView.findViewById(R.id.celDatumTextView);
            progressBar = itemView.findViewById(R.id.progressBar);
            minMaxTextView = itemView.findViewById(R.id.minMaxTextView);
            osszegHozzaadButton = itemView.findViewById(R.id.osszegHozzaadButton);
            osszegLevonButton = itemView.findViewById(R.id.osszegLevonButton);
            megtakaritasImageView = itemView.findViewById(R.id.megtakaritasImageView);

        }
    }
}


