package com.claresti.financeapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.claresti.financeapp.MainDenarius;
import com.claresti.financeapp.Modelos.Categoria;
import com.claresti.financeapp.Modelos.Cuenta;
import com.claresti.financeapp.Modelos.Movimiento;
import com.claresti.financeapp.R;
import com.claresti.financeapp.Tools.CatalogosUsuario;
import com.claresti.financeapp.Tools.Comunicaciones;
import com.claresti.financeapp.Tools.Urls;
import com.claresti.financeapp.Tools.UserSessionManager;
import com.google.gson.Gson;
import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Splash extends AppCompatActivity {

    private UserSessionManager sessionManager;
    private boolean flagAccounts = false, flagCategories = false;

    private final static String TAG = "Splash";

    private Comunicaciones.ResultadosInterface listenerComunicacionesAccounts = new Comunicaciones.ResultadosInterface() {
        @Override
        public void mostrarDatos(JSONObject json) {
            if(json.has("accounts")){
                try {
                    Gson gson = new Gson();
                    final ArrayList<Cuenta> cuentasArrayList = new ArrayList<Cuenta>(Arrays.asList(gson.fromJson(json.getString("accounts"), Cuenta[].class)));
                    for(Cuenta cuenta: cuentasArrayList) {
                        cuenta.save();
                    }
                } catch(JSONException jsone) {
                    Log.e(TAG, jsone.getMessage());
                }
            }
            flagAccounts = true;
            terminado();
        }

        @Override
        public void setError(String mensaje) {
            Log.e(TAG, mensaje);
            flagAccounts = true;
            terminado();
        }
    };

    private Comunicaciones.ResultadosInterface listenerComunicacionesCategories = new Comunicaciones.ResultadosInterface() {
        @Override
        public void mostrarDatos(JSONObject json) {
            if(json.has("categories")){
                try {
                    Gson gson = new Gson();
                    final ArrayList<Categoria> categoriasArrayList = new ArrayList<Categoria>(Arrays.asList(gson.fromJson(json.getString("categories"), Categoria[].class)));
                    for(Categoria categoria: categoriasArrayList) {
                        categoria.save();
                    }
                } catch(JSONException jsone) {
                    Log.e(TAG, jsone.getMessage());
                }
            }
            flagCategories = true;
            terminado();
        }

        @Override
        public void setError(String mensaje) {
            Log.e(TAG, mensaje);
            flagCategories = true;
            terminado();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sessionManager = new UserSessionManager(this);
        Comunicaciones comAcc = new Comunicaciones(this, listenerComunicacionesAccounts);
        Comunicaciones comCat = new Comunicaciones(this, listenerComunicacionesCategories);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        comAcc.peticionJSON(
                Urls.VIEWUSERACCOUNTS + sessionManager.getUserDetails().get(UserSessionManager.KEY_ID),
                Request.Method.GET,
                new JSONObject(),
                headers
        );
        comCat.peticionJSON(
                Urls.VIEWUSERCATEGORIES + sessionManager.getUserDetails().get(UserSessionManager.KEY_ID),
                Request.Method.GET,
                new JSONObject(),
                headers
        );
    }

    private void terminado(){
        if(flagAccounts && flagCategories){
            CatalogosUsuario.getInstance(Splash.this);
            Intent i = new Intent(getApplicationContext(), MainDenarius.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
    }

}
