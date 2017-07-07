package com.claresti.financeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterListViewMovimientos extends BaseAdapter {

    LayoutInflater inflator;
    ArrayList<ObjMovimiento> listaMovimientos;
    Context context;


    public AdapterListViewMovimientos(Context context, ArrayList<ObjMovimiento> listaMovimientos) {
        inflator = LayoutInflater.from(context);
        this.listaMovimientos = listaMovimientos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listaMovimientos.size();
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
        convertView = inflator.inflate(R.layout.movimiento, null);
        //Asignacion de variables del layout
        ImageView imgMov = (ImageView)convertView.findViewById(R.id.tipoMovimiento);
        TextView txtTipMov = (TextView)convertView.findViewById(R.id.txt_tipoMovimiento);
        TextView txtMonMov = (TextView)convertView.findViewById(R.id.txt_MontoMovimiento);
        TextView txtFecMov = (TextView)convertView.findViewById(R.id.txt_fechaMovimiento);
        if(listaMovimientos.get(position).getTipo().equals("1")){
            imgMov.setImageResource(R.drawable.arrow_up);
            txtTipMov.setText("Ingreso");
        }else{
            imgMov.setImageResource(R.drawable.arrow_down);
            txtTipMov.setText("Egreso");
        }
        txtMonMov.setText(listaMovimientos.get(position).getMonto());
        txtFecMov.setText(listaMovimientos.get(position).getFecha());
        return convertView;
    }
}
