package com.example.koltsegvetes_tervezo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter {

    public CustomAdapter(@NonNull Context context, ArrayList<CustomItem> customList) {
        super(context, 0, customList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_layout, parent, false);
        }
        CustomItem item = (CustomItem) getItem(position);
        ImageView spinnerIV = convertView.findViewById(R.id.spinnerImageID);
        TextView spinnerTV = convertView.findViewById(R.id.SpinnerTextID);
        if (item != null) {
            spinnerIV.setImageResource(item.getSpinnerItemImage());
            spinnerTV.setText(item.getSpinnerItemName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dropdown_layout, parent, false);
        }
        CustomItem item = (CustomItem) getItem(position);
        ImageView dropdownIV = convertView.findViewById(R.id.DropdownImageID);
        TextView dropdownTV = convertView.findViewById(R.id.DropdownTextID);
        if (item != null) {
            dropdownIV.setImageResource(item.getSpinnerItemImage());
            dropdownTV.setText(item.getSpinnerItemName());
        }
        return convertView;
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return super.getPosition(item);
    }

    public int getIndex (String s) {
        for (int i = 0; i < getCount(); i++) {
            if (((CustomItem) getItem(i)).getSpinnerItemName().equals(s)) {
                return i;
            }
        }
        return 0;
    }
}
