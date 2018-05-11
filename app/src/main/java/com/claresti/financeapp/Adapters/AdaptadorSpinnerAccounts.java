package com.claresti.financeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.claresti.financeapp.Modelos.Cuenta;
import com.claresti.financeapp.R;

import java.util.ArrayList;

public class AdaptadorSpinnerAccounts extends BaseAdapter {

    private LayoutInflater inflator;
    private ArrayList<Cuenta> elementos;

    public AdaptadorSpinnerAccounts(Context context) {
        inflator = LayoutInflater.from(context);
        elementos = new ArrayList<>();
    }

    public void setElementos(ArrayList<Cuenta> elementos) {
        this.elementos = elementos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return elementos.size();
    }

    @Override
    public Object getItem(int position) {
        return elementos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return elementos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflator.inflate(R.layout.stylespinner, null);
        TextView txt_cat = (TextView) convertView.findViewById(R.id.txt_elemento);
        txt_cat.setText(elementos.get(position).getName());
        return convertView;
    }
}
