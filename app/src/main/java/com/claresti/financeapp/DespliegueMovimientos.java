package com.claresti.financeapp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DespliegueMovimientos extends AppCompatActivity {

    // Declaracion de variables en el layout
    private RelativeLayout ventana;
    private RelativeLayout contCuenta;
    private RelativeLayout contCategoria;
    private RelativeLayout contMovimiento;
    private Button btnFechaInicial;
    private Button btnFechaFinal;
    private ListView listMovimientos;
    private Spinner spinnerFiltro;
    private TextView txtFiltro;
    private ProgressBar progreso;
    private Spinner spinerCategoria;
    private Spinner spinerCuenta;
    private Spinner spinerTipo;
    private TextView txtCategoria;
    private TextView txtCuenta;
    private TextView txtTipo;

    // Declaracion de variables de clases
    private BD bd;
    private ObjUsuario usuario;
    private Urls urls;

    //Menu, Declaracion de variables
    private DrawerLayout drawerLayout;
    final List<MenuItem> items = new ArrayList<>();
    private Menu menu;
    private ImageView btnMenu;
    private NavigationView nav;

    // Declaracion variables para el datapikerdialog
    private int ano;
    private int mes;
    private int dia;

    // banderas y variables extra
    private ObjCategoria[] arrayCategoria;
    private ObjCuenta[] arrayCuenta;
    private int flagCategoria;
    private int flagCuenta;
    private int flagFiltro;
    private int flagTipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_despliegue_movimientos);

        //Cambiar el color en la barra de notificaciones (Solo funciona de lollipop hacia arriba)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.top));
        }

        // Asignacion variables layout
        ventana = (RelativeLayout)findViewById(R.id.l_ventana);
        listMovimientos = (ListView)findViewById(R.id.lis_movimientos);
        spinnerFiltro = (Spinner)findViewById(R.id.spin_filtro);
        txtFiltro = (TextView)findViewById(R.id.txt_filtro);
        contCategoria = (RelativeLayout)findViewById(R.id.cont_filtroCategoria);
        contMovimiento = (RelativeLayout)findViewById(R.id.cont_filtroTipo);
        contCuenta = (RelativeLayout)findViewById(R.id.cont_filtroCuenta);
        btnFechaInicial = (Button)findViewById(R.id.btn_fechaInicio);
        btnFechaFinal = (Button)findViewById(R.id.btn_fechaFin);
        progreso = (ProgressBar)findViewById(R.id.progress);
        spinerCategoria = (Spinner)findViewById(R.id.spin_categoria);
        spinerCuenta = (Spinner)findViewById(R.id.spin_cuenta);
        spinerTipo = (Spinner)findViewById(R.id.spin_tipo);
        txtCategoria = (TextView)findViewById(R.id.txt_categoria);
        txtCuenta = (TextView)findViewById(R.id.txt_cuenta);
        txtTipo = (TextView)findViewById(R.id.txt_tipo);

        //Asignacion variables clases
        bd = new BD(getApplicationContext());
        usuario = bd.slectUsuario();
        urls = new Urls();

        // Datos para el datepikerdialog
        Calendar c = Calendar.getInstance();
        ano = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.DAY_OF_MONTH);

        // Llamada a funciones para llenar los spinners y crear los listenrs
        llenarSpinnerFiltro();
        llenarSpinerTipo();
        cracionListeners();
        cargarInicio();

        //Menu, Inicia las variables del menu y llama la funcion encargada de su manipulacion
        drawerLayout = (DrawerLayout) findViewById(R.id.dLayout);
        nav = (NavigationView)findViewById(R.id.navigation);
        menu = nav.getMenu();
        menuNav();
    }


    /**
     * funcion encragada de cargar los primeros registros al iniciar el layout
     */
    private void cargarInicio() {
        progreso.setVisibility(View.VISIBLE);
        Log.i("JSON", "Si entro");
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(DespliegueMovimientos.this).
                addToRequestQueue(
                        request = new JsonObjectRequest(
                                Request.Method.GET,
                                urls.getGetMovimientos() + "idU=" + usuario.getIdUsuario(),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res = response.getString("estado");
                                            switch(res){
                                                case "1":
                                                    Log.i("peticion", "caso 1");
                                                    ArrayList<ObjMovimiento> movimientos = new ArrayList<ObjMovimiento>();
                                                    JSONArray jArrayMarcadores = response.getJSONArray("registros");
                                                    ObjMovimiento[] arrayMovimientos = gson.fromJson(jArrayMarcadores.toString(), ObjMovimiento[].class);
                                                    Log.i("peticion", "tamaño: " + arrayMovimientos.length);
                                                    for(int i = 0; i < arrayMovimientos.length; i++){
                                                        movimientos.add(arrayMovimientos[i]);
                                                    }
                                                    listMovimientos.setAdapter(new AdapterListViewMovimientos(getApplicationContext(), movimientos));
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                case "0":
                                                    Log.i("peticion", "caso 0");
                                                    listMovimientos.setAdapter(null);
                                                    arrayMovimientos = null;
                                                    //Regresar mensaje de que no hay registros
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                default:
                                                    Log.i("peticion", "caso default");
                                                    arrayMovimientos = null;
                                                    listMovimientos.setAdapter(null);
                                                    progreso.setVisibility(View.GONE);
                                                    msg("Ocurrio un problema al conectarse con el sertvidor");
                                                    break;
                                            }
                                        }catch(JSONException json){
                                            Log.e("JSON", json.toString());
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                        )
                );
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }

    /**
     * Funcion encargada de crear los listener del layout
     */
    private void cracionListeners() {
        btnFechaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dpd = new DatePickerDialog(DespliegueMovimientos.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        btnFechaInicial.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        filtro(1);
                    }
                }, ano, mes, dia);
                dpd.show();
                Button ok = dpd.getButton(DialogInterface.BUTTON_POSITIVE);
                ok.setTextColor(Color.parseColor("#949494"));
                Button cancel = dpd.getButton(DialogInterface.BUTTON_NEGATIVE);
                cancel.setTextColor(Color.parseColor("#949494"));
            }
        });
        btnFechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dpd = new DatePickerDialog(DespliegueMovimientos.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        btnFechaFinal.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        filtro(1);
                    }
                }, ano, mes, dia);
                dpd.show();
                Button ok = dpd.getButton(DialogInterface.BUTTON_POSITIVE);
                ok.setTextColor(Color.parseColor("#949494"));
                Button cancel = dpd.getButton(DialogInterface.BUTTON_NEGATIVE);
                cancel.setTextColor(Color.parseColor("#949494"));
            }
        });
    }

    /**
     * Funcion encargada de verificar si se filtrara por medio de rango de fechas y que filtros
     * @param conFecha 1 en caso de que sea por medio de un intervalo de fecha, en otro
     *                 caso el valor que sea
     */
    private void filtro(int conFecha) {
        if(conFecha == 1){
            Log.e("fecha", "a");
            Log.e("btnInicial", btnFechaInicial.getText().toString());
            Log.e("btnFinal", btnFechaFinal.getText().toString());
            if(!btnFechaInicial.getText().toString().equals("Fecha Inicio") && !btnFechaFinal.getText().toString().equals("Fecha Fin")){
                switch (flagFiltro){
                    case 0:
                        Log.e("fecha", "b");
                        cargarListFechaSinFiltro();
                        break;
                    case 1:
                        Log.e("fecha", "c");
                        Log.e("fecha", flagCuenta + "");
                        cargarListFechaCuenta();
                        break;
                    case 2:
                        Log.e("fecha", "d");
                        Log.e("fecha", flagCategoria + "");
                        cargarListFechaCategoria();
                        break;
                    case 3:
                        Log.e("fecha", "f");
                        Log.e("fecha", flagTipo + "");
                        cargarListFechaTipo();
                        break;
                }
            }
        }else{
            switch (flagFiltro){
                case 1:
                    Log.e("fecha", flagCuenta + "");
                    btnFechaInicial.setText("Fecha Inicio");
                    btnFechaFinal.setText("Fecha Fin");
                    cargarListSinFechaCuenta();
                    break;
                case 2:
                    Log.e("fecha", "d");
                    btnFechaInicial.setText("Fecha Inicio");
                    btnFechaFinal.setText("Fecha Fin");
                    Log.e("fecha", flagCategoria + "");
                    cargarListSinFechaCategoria();
                    break;
                case 3:
                    Log.e("fecha", "f");
                    btnFechaInicial.setText("Fecha Inicio");
                    btnFechaFinal.setText("Fecha Fin");
                    Log.e("fecha", flagTipo + "");
                    cargarListSinFechaTipo();
                    break;
            }
        }
    }

    /**
     * Funcion que carga el listView con los filtros de fecha y tipo
     */
    private void cargarListFechaTipo() {
        progreso.setVisibility(View.VISIBLE);
        Log.i("JSON", "Si entro");
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(DespliegueMovimientos.this).
                addToRequestQueue(
                        request = new JsonObjectRequest(
                                Request.Method.GET,
                                urls.getGetMovimientosIntervaloTipo() + "idU=" + usuario.getIdUsuario() +
                                        "&fin=" + btnFechaInicial.getText() +
                                        "&fte=" + btnFechaFinal.getText() +
                                        "&tip=" + flagTipo,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res = response.getString("estado");
                                            switch(res){
                                                case "1":
                                                    Log.i("peticion", "caso 1");
                                                    ArrayList<ObjMovimiento> movimientos = new ArrayList<ObjMovimiento>();
                                                    JSONArray jArrayMarcadores = response.getJSONArray("registros");
                                                    ObjMovimiento[] arrayMovimientos = gson.fromJson(jArrayMarcadores.toString(), ObjMovimiento[].class);
                                                    Log.i("peticion", "tamaño: " + arrayMovimientos.length);
                                                    for(int i = 0; i < arrayMovimientos.length; i++){
                                                        movimientos.add(arrayMovimientos[i]);
                                                    }
                                                    listMovimientos.setAdapter(new AdapterListViewMovimientos(getApplicationContext(), movimientos));
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                case "0":
                                                    Log.i("peticion", "caso 0");
                                                    listMovimientos.setAdapter(null);
                                                    //Regresar mensaje de que no hay registros
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                default:
                                                    Log.i("peticion", "caso default");
                                                    listMovimientos.setAdapter(null);
                                                    progreso.setVisibility(View.GONE);
                                                    //msg("Ocurrio un problema al conectarse con el sertvidor");
                                                    break;
                                            }
                                        }catch(JSONException json){
                                            Log.e("JSON", json.toString());
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                        )
                );
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }

    /**
     * Funcion que carga el listView con los filtros de fecha y categoria
     */
    private void cargarListFechaCategoria() {
        progreso.setVisibility(View.VISIBLE);
        Log.i("JSON", "Si entro");
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(DespliegueMovimientos.this).
                addToRequestQueue(
                        request = new JsonObjectRequest(
                                Request.Method.GET,
                                urls.getGetMovimientosIntervaloCategoria() + "idU=" + usuario.getIdUsuario() +
                                        "&fin=" + btnFechaInicial.getText() +
                                        "&fte=" + btnFechaFinal.getText() +
                                        "&idC=" + flagCategoria,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res = response.getString("estado");
                                            switch(res){
                                                case "1":
                                                    Log.i("peticion", "caso 1");
                                                    ArrayList<ObjMovimiento> movimientos = new ArrayList<ObjMovimiento>();
                                                    JSONArray jArrayMarcadores = response.getJSONArray("registros");
                                                    ObjMovimiento[] arrayMovimientos = gson.fromJson(jArrayMarcadores.toString(), ObjMovimiento[].class);
                                                    Log.i("peticion", "tamaño: " + arrayMovimientos.length);
                                                    for(int i = 0; i < arrayMovimientos.length; i++){
                                                        movimientos.add(arrayMovimientos[i]);
                                                    }
                                                    listMovimientos.setAdapter(new AdapterListViewMovimientos(getApplicationContext(), movimientos));
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                case "0":
                                                    Log.i("peticion", "caso 0");
                                                    listMovimientos.setAdapter(null);
                                                    //Regresar mensaje de que no hay registros
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                default:
                                                    Log.i("peticion", "caso default");
                                                    listMovimientos.setAdapter(null);
                                                    progreso.setVisibility(View.GONE);
                                                    //msg("Ocurrio un problema al conectarse con el sertvidor");
                                                    break;
                                            }
                                        }catch(JSONException json){
                                            Log.e("JSON", json.toString());
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                        )
                );
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }

    /**
     * Funcion que carga el listView con los filtros de fecha y cuenta
     */
    private void cargarListFechaCuenta() {
        progreso.setVisibility(View.VISIBLE);
        Log.i("JSON", "Si entro");
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(DespliegueMovimientos.this).
                addToRequestQueue(
                        request = new JsonObjectRequest(
                                Request.Method.GET,
                                urls.getGetMovimientosIntervaloCuenta() + "idU=" + usuario.getIdUsuario() +
                                        "&fin=" + btnFechaInicial.getText() +
                                        "&fte=" + btnFechaFinal.getText() +
                                        "&idC=" + flagCuenta,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res = response.getString("estado");
                                            switch(res){
                                                case "1":
                                                    Log.i("peticion", "caso 1");
                                                    ArrayList<ObjMovimiento> movimientos = new ArrayList<ObjMovimiento>();
                                                    JSONArray jArrayMarcadores = response.getJSONArray("registros");
                                                    ObjMovimiento[] arrayMovimientos = gson.fromJson(jArrayMarcadores.toString(), ObjMovimiento[].class);
                                                    Log.i("peticion", "tamaño: " + arrayMovimientos.length);
                                                    for(int i = 0; i < arrayMovimientos.length; i++){
                                                        movimientos.add(arrayMovimientos[i]);
                                                    }
                                                    listMovimientos.setAdapter(new AdapterListViewMovimientos(getApplicationContext(), movimientos));
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                case "0":
                                                    Log.i("peticion", "caso 0");
                                                    listMovimientos.setAdapter(null);
                                                    //Regresar mensaje de que no hay registros
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                default:
                                                    Log.i("peticion", "caso default");
                                                    listMovimientos.setAdapter(null);
                                                    progreso.setVisibility(View.GONE);
                                                    //msg("Ocurrio un problema al conectarse con el sertvidor");
                                                    break;
                                            }
                                        }catch(JSONException json){
                                            Log.e("JSON", json.toString());
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                        )
                );
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }

    /**
     * Funcion que carga el listView con el filtro de tipo
     */
    private void cargarListSinFechaTipo() {
        progreso.setVisibility(View.VISIBLE);
        Log.i("JSON", "Si entro");
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(DespliegueMovimientos.this).
                addToRequestQueue(
                        request = new JsonObjectRequest(
                                Request.Method.GET,
                                urls.getGetMovimientosTipo() + "idU=" + usuario.getIdUsuario() +
                                        "&tip=" + flagTipo,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res = response.getString("estado");
                                            switch(res){
                                                case "1":
                                                    Log.i("peticion", "caso 1");
                                                    ArrayList<ObjMovimiento> movimientos = new ArrayList<ObjMovimiento>();
                                                    JSONArray jArrayMarcadores = response.getJSONArray("registros");
                                                    ObjMovimiento[] arrayMovimientos = gson.fromJson(jArrayMarcadores.toString(), ObjMovimiento[].class);
                                                    Log.i("peticion", "tamaño: " + arrayMovimientos.length);
                                                    for(int i = 0; i < arrayMovimientos.length; i++){
                                                        movimientos.add(arrayMovimientos[i]);
                                                    }
                                                    listMovimientos.setAdapter(new AdapterListViewMovimientos(getApplicationContext(), movimientos));
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                case "0":
                                                    Log.i("peticion", "caso 0");
                                                    listMovimientos.setAdapter(null);
                                                    //Regresar mensaje de que no hay registros
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                default:
                                                    Log.i("peticion", "caso default");
                                                    listMovimientos.setAdapter(null);
                                                    progreso.setVisibility(View.GONE);
                                                    msg("Ocurrio un problema al conectarse con el sertvidor");
                                                    break;
                                            }
                                        }catch(JSONException json){
                                            Log.e("JSON", json.toString());
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                        )
                );
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }

    /**
     * Funcion que carga el listView con el filtro de categoria
     */
    private void cargarListSinFechaCategoria() {
        progreso.setVisibility(View.VISIBLE);
        Log.i("JSON", "Si entro");
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(DespliegueMovimientos.this).
                addToRequestQueue(
                        request = new JsonObjectRequest(
                                Request.Method.GET,
                                urls.getGetMovimientosCategoria() + "idU=" + usuario.getIdUsuario() +
                                        "&idC=" + flagCategoria,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res = response.getString("estado");
                                            switch(res){
                                                case "1":
                                                    Log.i("peticion", "caso 1");
                                                    ArrayList<ObjMovimiento> movimientos = new ArrayList<ObjMovimiento>();
                                                    JSONArray jArrayMarcadores = response.getJSONArray("registros");
                                                    ObjMovimiento[] arrayMovimientos = gson.fromJson(jArrayMarcadores.toString(), ObjMovimiento[].class);
                                                    Log.i("peticion", "tamaño: " + arrayMovimientos.length);
                                                    for(int i = 0; i < arrayMovimientos.length; i++){
                                                        movimientos.add(arrayMovimientos[i]);
                                                    }
                                                    listMovimientos.setAdapter(new AdapterListViewMovimientos(getApplicationContext(), movimientos));
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                case "0":
                                                    Log.i("peticion", "caso 0");
                                                    listMovimientos.setAdapter(null);
                                                    //Regresar mensaje de que no hay registros
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                default:
                                                    Log.i("peticion", "caso default");
                                                    listMovimientos.setAdapter(null);
                                                    progreso.setVisibility(View.GONE);
                                                    msg("Ocurrio un problema al conectarse con el sertvidor");
                                                    break;
                                            }
                                        }catch(JSONException json){
                                            Log.e("JSON", json.toString());
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                        )
                );
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }

    /**
     * Funcion que carga el listView con el filtro de cuenta
     */
    private void cargarListSinFechaCuenta() {
        progreso.setVisibility(View.VISIBLE);
        Log.i("JSON", "Si entro");
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(DespliegueMovimientos.this).
                addToRequestQueue(
                        request = new JsonObjectRequest(
                                Request.Method.GET,
                                urls.getGetMovimientosCuenta() + "idU=" + usuario.getIdUsuario() +
                                        "&idC=" + flagCuenta,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res = response.getString("estado");
                                            switch(res){
                                                case "1":
                                                    Log.i("peticion", "caso 1");
                                                    ArrayList<ObjMovimiento> movimientos = new ArrayList<ObjMovimiento>();
                                                    JSONArray jArrayMarcadores = response.getJSONArray("registros");
                                                    ObjMovimiento[] arrayMovimientos = gson.fromJson(jArrayMarcadores.toString(), ObjMovimiento[].class);
                                                    Log.i("peticion", "tamaño: " + arrayMovimientos.length);
                                                    for(int i = 0; i < arrayMovimientos.length; i++){
                                                        movimientos.add(arrayMovimientos[i]);
                                                    }
                                                    listMovimientos.setAdapter(new AdapterListViewMovimientos(getApplicationContext(), movimientos));
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                case "0":
                                                    Log.i("peticion", "caso 0");
                                                    listMovimientos.setAdapter(null);
                                                    //Regresar mensaje de que no hay registros
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                default:
                                                    Log.i("peticion", "caso default");
                                                    listMovimientos.setAdapter(null);
                                                    progreso.setVisibility(View.GONE);
                                                    msg("Ocurrio un problema al conectarse con el sertvidor");
                                                    break;
                                            }
                                        }catch(JSONException json){
                                            Log.e("JSON", json.toString());
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                        )
                );
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }

    /**
     * Funcion que carga el listView con el filtro de fechas
     */
    private void cargarListFechaSinFiltro() {
        progreso.setVisibility(View.VISIBLE);
        Log.i("JSON", "Si entro");
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(DespliegueMovimientos.this).
                addToRequestQueue(
                        request = new JsonObjectRequest(
                                Request.Method.GET,
                                urls.getGetMovimientosIntervalo() + "idU=" + usuario.getIdUsuario() +
                                    "&fin=" + btnFechaInicial.getText() +
                                    "&fte=" + btnFechaFinal.getText(),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res = response.getString("estado");
                                            switch(res){
                                                case "1":
                                                    Log.i("peticion", "caso 1");
                                                    ArrayList<ObjMovimiento> movimientos = new ArrayList<ObjMovimiento>();
                                                    JSONArray jArrayMarcadores = response.getJSONArray("registros");
                                                    ObjMovimiento[] arrayMovimientos = gson.fromJson(jArrayMarcadores.toString(), ObjMovimiento[].class);
                                                    Log.i("peticion", "tamaño: " + arrayMovimientos.length);
                                                    for(int i = 0; i < arrayMovimientos.length; i++){
                                                        movimientos.add(arrayMovimientos[i]);
                                                    }
                                                    listMovimientos.setAdapter(new AdapterListViewMovimientos(getApplicationContext(), movimientos));
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                case "0":
                                                    Log.i("peticion", "caso 0");
                                                    listMovimientos.setAdapter(null);
                                                    //Regresar mensaje de que no hay registros
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                default:
                                                    Log.i("peticion", "caso default");
                                                    listMovimientos.setAdapter(null);
                                                    progreso.setVisibility(View.GONE);
                                                    //msg("Ocurrio un problema al conectarse con el sertvidor");
                                                    break;
                                            }
                                        }catch(JSONException json){
                                            Log.e("JSON", json.toString());
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                        )
                );
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }

    /**
     * Funcion encargada de llenar el espiner de filtro
     */
    private void llenarSpinnerFiltro() {
        final ArrayList<String> filtros = new ArrayList<String>();
        filtros.add("Selecione un filtro");
        filtros.add("Cuenta");
        filtros.add("Categoría");
        filtros.add("Tipo de movimiento");
        spinnerFiltro.setAdapter(new AdaptadorSpinnerMovimientos(getApplicationContext(), filtros));
        spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtFiltro.setText(filtros.get(position));
                switch(position){
                    case 0:
                        contMovimiento.setVisibility(View.GONE);
                        contCategoria.setVisibility(View.GONE);
                        contCuenta.setVisibility(View.GONE);
                        flagFiltro = position;
                        cargarInicio();
                        break;
                    case 1:
                        contMovimiento.setVisibility(View.GONE);
                        contCategoria.setVisibility(View.GONE);
                        contCuenta.setVisibility(View.VISIBLE);
                        flagFiltro = position;
                        llenarSpinerCuenta();
                        break;
                    case 2:
                        contMovimiento.setVisibility(View.GONE);
                        contCategoria.setVisibility(View.VISIBLE);
                        contCuenta.setVisibility(View.GONE);
                        flagFiltro = position;
                        llenarSpinerCategoria();
                        break;
                    case 3:
                        contMovimiento.setVisibility(View.VISIBLE);
                        contCategoria.setVisibility(View.GONE);
                        contCuenta.setVisibility(View.GONE);
                        flagFiltro = position;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Funcion encargada de llenar el espiner de listaMovimientos
     */
    private void llenarSpinerCategoria() {
        progreso.setVisibility(View.VISIBLE);
        Log.i("NICKNAME", usuario.getUsuario());
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(DespliegueMovimientos.this).
                addToRequestQueue(
                        request = new JsonObjectRequest(
                                Request.Method.GET,
                                urls.getGetCategorias() + "usuario=" + usuario.getUsuario(),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res = response.getString("estado");
                                            switch(res){
                                                case "1":
                                                    Log.i("peticion", "caso 1");
                                                    JSONArray jArrayMarcadores = response.getJSONArray("registros");
                                                    final ArrayList<String> textoSpinerCategoria = new ArrayList<String>();
                                                    arrayCategoria = gson.fromJson(jArrayMarcadores.toString(), ObjCategoria[].class);
                                                    Log.i("peticion", "tamaño: " + arrayCategoria.length);
                                                    for(int i = 0; i < arrayCategoria.length; i++){
                                                        if(arrayCategoria.length > 0){
                                                            textoSpinerCategoria.add(arrayCategoria[i].getNombre());
                                                        }
                                                    }
                                                    spinerCategoria.setAdapter(new AdaptadorSpinnerMovimientos(getApplicationContext(), textoSpinerCategoria));
                                                    spinerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                        @Override
                                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                            txtCategoria.setText(textoSpinerCategoria.get(position));
                                                            flagCategoria = Integer.parseInt(arrayCategoria[position].getID());
                                                            filtro(3);
                                                        }

                                                        @Override
                                                        public void onNothingSelected(AdapterView<?> parent) {

                                                        }
                                                    });
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                case "0":
                                                    Log.i("peticion", "caso 0");
                                                    arrayCategoria = null;
                                                    //Regresar mensaje de que no hay registros
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                default:
                                                    Log.i("peticion", "caso default");
                                                    arrayCategoria = null;
                                                    progreso.setVisibility(View.GONE);
                                                    //msg("Ocurrio un problema al conectarse con el sertvidor");
                                                    break;
                                            }
                                        }catch(JSONException json){
                                            Log.e("JSON", json.toString());
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                        )
                );
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }

    /**
     * Funcion encargada de llenar el espiner de listaMovimientos
     */
    private void llenarSpinerTipo() {
        final ArrayList<String> listaTipos = new ArrayList<String>();
        listaTipos.add("Ingresos");
        listaTipos.add("Egresos");
        spinerTipo.setAdapter(new AdaptadorSpinnerMovimientos(getApplicationContext(), listaTipos));
        spinerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtTipo.setText(listaTipos.get(position));
                flagTipo = position + 1;
                filtro(3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Funcion encargada de llenar el espiner de cuenta
     */
    private void llenarSpinerCuenta() {
        progreso.setVisibility(View.VISIBLE);
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(DespliegueMovimientos.this).
                addToRequestQueue(
                        request = new JsonObjectRequest(
                                Request.Method.GET,
                                urls.getGetCuentas() + "usuario=" + usuario.getUsuario(),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res = response.getString("estado");
                                            switch(res){
                                                case "1":
                                                    Log.i("peticion", "caso 1");
                                                    JSONArray jArrayMarcadores = response.getJSONArray("registros");
                                                    final ArrayList<String> textoSpinerCuenta = new ArrayList<String>();
                                                    arrayCuenta = gson.fromJson(jArrayMarcadores.toString(), ObjCuenta[].class);
                                                    Log.i("peticion", "tamaño: " + arrayCuenta.length);
                                                    for(int i = 0; i < arrayCuenta.length; i++){
                                                        if(arrayCuenta.length > 0){
                                                            textoSpinerCuenta.add(arrayCuenta[i].getNombre());
                                                        }
                                                    }
                                                    spinerCuenta.setAdapter(new AdaptadorSpinnerMovimientos(getApplicationContext(), textoSpinerCuenta));
                                                    spinerCuenta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                        @Override
                                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                            txtCuenta.setText(textoSpinerCuenta.get(position));
                                                            flagCuenta = Integer.parseInt(arrayCuenta[position].getID());
                                                            filtro(3);
                                                        }

                                                        @Override
                                                        public void onNothingSelected(AdapterView<?> parent) {

                                                        }
                                                    });
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                case "0":
                                                    Log.i("peticion", "caso 0");
                                                    arrayCuenta = null;
                                                    //Regresar mensaje de que no hay registros
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                default:
                                                    Log.i("peticion", "caso default");
                                                    arrayCuenta = null;
                                                    progreso.setVisibility(View.GONE);
                                                    //msg("Ocurrio un problema al conectarse con el sertvidor");
                                                    break;
                                            }
                                        }catch(JSONException json){
                                            Log.e("JSON", json.toString());
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                        )
                );
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }

    /**
     * Funcion que da funcionalidad al menu
     */
    private void menuNav(){
        for(int i = 0; i < menu.size(); i++){
            items.add(menu.getItem(i));
        }
        items.get(3).setChecked(true);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                int pos = items.indexOf(item);
                if(pos == 0){
                    Intent i = new Intent(DespliegueMovimientos.this, Movimientos.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }else if(pos == 1){
                    Intent i = new Intent(DespliegueMovimientos.this, AgregarCategoria.class);
                    startActivity(i);
                }else if(pos == 2){
                    Intent i = new Intent(DespliegueMovimientos.this, AgregarCuenta.class);
                    startActivity(i);
                }else if(pos == 3) {

                }else if(pos == 4){
                    Intent i = new Intent(DespliegueMovimientos.this, Estadisticas.class);
                    startActivity(i);
                }else if(pos == 5){
                    Intent i = new Intent(DespliegueMovimientos.this, Configuracion.class);
                    startActivity(i);
                }else if(pos == 6){
                    Intent i = new Intent(DespliegueMovimientos.this, Acerca.class);
                    startActivity(i);
                }else if(pos == 7){
                    if(bd.LogoutUsuario(usuario.getIdUsuario()).equals("1")){
                        Intent i = new Intent(DespliegueMovimientos.this, Login.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                }
                drawerLayout.closeDrawer(nav);
                item.setChecked(false);
                return false;
            }
        });

        //Asignacion del header menu en una bariable
        View headerview = nav.getHeaderView(0);

        //Funcionalidad del boton de menu
        btnMenu = (ImageView)findViewById(R.id.Btnmenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(nav);
            }
        });
    }

    /**
     * funcion encargada de crear un snackbar con un mensaje y un boton de aceptar para ocultarla
     * @param msg String que contiene el mensaje
     */
    private void msg(String msg){
        Snackbar.make(ventana, msg, Snackbar.LENGTH_LONG).setAction("Aceptar", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }
}
