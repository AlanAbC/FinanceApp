package com.claresti.financeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AgregarCuenta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_agregar_cuenta);
    }
}
