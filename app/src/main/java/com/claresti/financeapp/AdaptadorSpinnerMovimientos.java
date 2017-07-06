package com.claresti.financeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorSpinnerMovimientos extends BaseAdapter {

    private LayoutInflater inflator;
    private ArrayList<String> elementos;

    public AdaptadorSpinnerMovimientos(Context context, ArrayList<String> elementos) {
        inflator = LayoutInflater.from(context);
        this.elementos = elementos;
    }

    @Override
    public int getCount() {
        return elementos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflator.inflate(R.layout.stylespinner, null);
        TextView txt_cat = (TextView) convertView.findViewById(R.id.txt_elemento);
        txt_cat.setText(elementos.get(position));
        return convertView;
    }
}
