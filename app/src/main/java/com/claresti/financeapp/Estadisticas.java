package com.claresti.financeapp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
        import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
        import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
        import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Estadisticas extends AppCompatActivity {

    // Declaracion de variables en el layout
    private RelativeLayout ventana;
    private ProgressBar progreso;
    private Button btnFechaInicial;
    private Button btnFechaFinal;
    private Button btnLinkSocios;
    private TextView txtIngreso;
    private TextView txtEgreso;
    private TextView txtTotales;

    // Declaracion de variables de clases
    private BD bd;
    private ObjUsuario usuario;
    private Urls urls;
    private ArrayList<ObjetoDatosGraficaCuentas> cuentas;
    private ArrayList<ObjetoDatosGraficaCategorias> categorias;

    // Declaracion variables para el datapikerdialog
    private int ano;
    private int mes;
    private int dia;

    //Menu, Declaracion de variables
    private DrawerLayout drawerLayout;
    final List<MenuItem> items = new ArrayList<>();
    private Menu menu;
    private ImageView btnMenu;
    private NavigationView nav;

    //Declaracion de variables para el control de bottom sheet
    private Button btnConBottomSheet;
    private LinearLayout bottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_estadisticas);

        //Cambiar el color en la barra de notificaciones (Solo funciona de lollipop hacia arriba)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.top));
        }

        // Asignacion variables layout
        ventana = (RelativeLayout)findViewById(R.id.l_ventana);
        progreso = (ProgressBar)findViewById(R.id.progress);
        btnFechaInicial = (Button)findViewById(R.id.btn_fechaInicio);
        btnFechaFinal = (Button)findViewById(R.id.btn_fechaFin);
        btnLinkSocios = (Button)findViewById(R.id.btnConBottomSheet);
        txtEgreso = (TextView)findViewById(R.id.txt_egresos);
        txtIngreso = (TextView)findViewById(R.id.txt_ingresos);
        txtTotales = (TextView)findViewById(R.id.txt_total);

        //Asignacion variables clases
        bd = new BD(getApplicationContext());
        usuario = bd.slectUsuario();
        urls = new Urls();
        cuentas = new ArrayList<ObjetoDatosGraficaCuentas>();
        categorias = new ArrayList<ObjetoDatosGraficaCategorias>();

        // Datos para el datepikerdialog
        Calendar c = Calendar.getInstance();
        ano = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.DAY_OF_MONTH);

        //Llamada de las variables para el control de bottomsheet
        bottomSheet = (LinearLayout)findViewById(R.id.bottomSheet);
        final BottomSheetBehavior bsb = BottomSheetBehavior.from(bottomSheet);
        bsb.setState(BottomSheetBehavior.STATE_EXPANDED);

        //Control para esconder bottomsheet
        btnConBottomSheet=(Button)findViewById(R.id.btnConBottomSheet);
        btnConBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        //Menu, Inicia las variables del menu y llama la funcion encargada de su manipulacion
        drawerLayout = (DrawerLayout) findViewById(R.id.dLayout);
        nav = (NavigationView)findViewById(R.id.navigation);
        menu = nav.getMenu();
        menuNav();

        // Llamdo de las funciones y creacion de liseners
        creacionListeners();
        generarGraficaCuentas();
        generarGraficaCategorias();
        llenarTablaTotales();
    }

    private void creacionListeners() {
        btnFechaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dpd = new DatePickerDialog(Estadisticas.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        btnFechaInicial.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        if(!btnFechaInicial.getText().toString().equals("Fecha Inicio") && !btnFechaFinal.getText().toString().equals("Fecha Fin")){
                            rellenarGraficaCategoriasIntervalo();
                            rellenarGraficaCuentasIntervalo();
                            rellenarTablaIntervalos();
                        }
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

                DatePickerDialog dpd = new DatePickerDialog(Estadisticas.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        btnFechaFinal.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        if(!btnFechaInicial.getText().toString().equals("Fecha Inicio") && !btnFechaFinal.getText().toString().equals("Fecha Fin")){
                            rellenarGraficaCategoriasIntervalo();
                            rellenarGraficaCuentasIntervalo();
                            rellenarTablaIntervalos();
                        }
                    }
                }, ano, mes, dia);
                dpd.show();
                Button ok = dpd.getButton(DialogInterface.BUTTON_POSITIVE);
                ok.setTextColor(Color.parseColor("#949494"));
                Button cancel = dpd.getButton(DialogInterface.BUTTON_NEGATIVE);
                cancel.setTextColor(Color.parseColor("#949494"));
            }
        });
        btnLinkSocios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://cpmx.claresti.com/socios.php");
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });
    }

    /**
     * Funcion encargada de rellenar la grafica con los datos necesitados
     */
    private void rellenarTablaIntervalos() {
        progreso.setVisibility(View.VISIBLE);
        Log.i("JSON", "Si entro");
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(Estadisticas.this).
                addToRequestQueue(
                        request = new JsonObjectRequest(
                                Request.Method.GET,
                                urls.getGetEstadisticasIntervalo() + "idU=" + usuario.getIdUsuario() +
                                        "&inicio=" + btnFechaInicial.getText() +
                                        "&fin=" + btnFechaFinal.getText(),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res = response.getString("estado");
                                            switch(res){
                                                case "1":
                                                    txtEgreso.setText(response.getString("egresos"));
                                                    txtIngreso.setText(response.getString("ingresos"));
                                                    txtTotales.setText(response.getString("total"));

                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                case "0":
                                                    Log.i("peticion", "caso 0");
                                                    //Regresar mensaje de que no hay registros
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                default:
                                                    Log.i("peticion", "caso default");
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
     * Funcion encargada de rellenar la grafica con los datos necesitados
     */
    private void rellenarGraficaCategoriasIntervalo() {
        progreso.setVisibility(View.VISIBLE);
        Log.i("JSON", "Si entro");
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(Estadisticas.this).
                addToRequestQueue(
                        request = new JsonObjectRequest(
                                Request.Method.GET,
                                urls.getGetGraficaCategoriasIntervalo() + "idU=" + usuario.getIdUsuario() +
                                        "&inicio=" + btnFechaInicial.getText() +
                                        "&fin=" + btnFechaFinal.getText(),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res = response.getString("estado");
                                            switch(res){
                                                case "1":
                                                    cuentas.clear();
                                                    JSONArray jArrayMarcadores = response.getJSONArray("registros");
                                                    Log.i("peticion", jArrayMarcadores.toString());
                                                    ObjetoDatosGraficaCuentas[] arraycuentasTotales = gson.fromJson(jArrayMarcadores.toString(), ObjetoDatosGraficaCuentas[].class);
                                                    Log.i("peticion", "tamaño: " + arraycuentasTotales.length);
                                                    for(int i = 0; i < arraycuentasTotales.length; i++){
                                                        cuentas.add(arraycuentasTotales[i]);
                                                    }
                                                    rellenarGraficaCuentas();
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                case "0":
                                                    Log.i("peticion", "caso 0");
                                                    //Regresar mensaje de que no hay registros
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                default:
                                                    Log.i("peticion", "caso default");
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
     * Funcion encargada de rellenar la grafica con los datos necesitados
     */
    private void rellenarGraficaCuentasIntervalo() {
        progreso.setVisibility(View.VISIBLE);
        Log.i("JSON", "Si entro");
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(Estadisticas.this).
                addToRequestQueue(
                        request = new JsonObjectRequest(
                                Request.Method.GET,
                                urls.getGetGraficaCuentasIntervalo() + "idU=" + usuario.getIdUsuario() +
                                        "&inicio=" + btnFechaInicial.getText() +
                                        "&fin=" + btnFechaFinal.getText(),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res = response.getString("estado");
                                            switch(res){
                                                case "1":
                                                    Log.i("peticion", "caso 1");
                                                    cuentas.clear();
                                                    JSONArray jArrayMarcadores = response.getJSONArray("registros");
                                                    ObjetoDatosGraficaCuentas[] arraycuentasTotales = gson.fromJson(jArrayMarcadores.toString(), ObjetoDatosGraficaCuentas[].class);
                                                    Log.i("peticion", "tamaño: " + arraycuentasTotales.length);
                                                    for(int i = 0; i < arraycuentasTotales.length; i++){
                                                        cuentas.add(arraycuentasTotales[i]);
                                                    }
                                                    rellenarGraficaCuentas();
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                case "0":
                                                    Log.i("peticion", "caso 0");
                                                    //Regresar mensaje de que no hay registros
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                default:
                                                    Log.i("peticion", "caso default");
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
     * Funcion encargada de rellenar la tabla con lo totales
     */
    private void llenarTablaTotales() {
        progreso.setVisibility(View.VISIBLE);
        Log.i("JSON", "Si entro");
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(Estadisticas.this).
                addToRequestQueue(
                        request = new JsonObjectRequest(
                                Request.Method.GET,
                                urls.getGetEstadisticas() + "idU=" + usuario.getIdUsuario(),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res = response.getString("estado");
                                            switch(res){
                                                case "1":
                                                    txtEgreso.setText(response.getString("egresos"));
                                                    txtIngreso.setText(response.getString("ingresos"));
                                                    txtTotales.setText(response.getString("total"));

                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                case "0":
                                                    Log.i("peticion", "caso 0");
                                                    //Regresar mensaje de que no hay registros
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                default:
                                                    Log.i("peticion", "caso default");
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
     * Funcion encargada de pedir la informacion para despues crear la
     * grafica de categorias
     */
    private void generarGraficaCategorias() {
        progreso.setVisibility(View.VISIBLE);
        Log.i("JSON", "Si entro");
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(Estadisticas.this).
                addToRequestQueue(
                        request = new JsonObjectRequest(
                                Request.Method.GET,
                                urls.getGetGraficaCategorias() + "idU=" + usuario.getIdUsuario(),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res = response.getString("estado");
                                            switch(res){
                                                case "1":
                                                    Log.i("peticion", "caso 1");
                                                    categorias.clear();
                                                    JSONArray jArrayMarcadores = response.getJSONArray("registros");
                                                    ObjetoDatosGraficaCategorias[] arraycuentasTotales = gson.fromJson(jArrayMarcadores.toString(), ObjetoDatosGraficaCategorias[].class);
                                                    Log.i("peticion", "tamaño: " + arraycuentasTotales.length);
                                                    for(int i = 0; i < arraycuentasTotales.length; i++){
                                                        categorias.add(arraycuentasTotales[i]);
                                                    }
                                                    rellenarGraficaCategorias();
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                case "0":
                                                    Log.i("peticion", "caso 0");
                                                    //Regresar mensaje de que no hay registros
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                default:
                                                    Log.i("peticion", "caso default");
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
     * Funcion encargada de rellenar la grafica con los datos necesitados
     */
    private void rellenarGraficaCategorias() {
        //creacion de la grafica
        BarChart barChart = (BarChart) findViewById(R.id.graficaCategorias);

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        int flagIndex = 0;

        for(ObjetoDatosGraficaCategorias c : categorias){
            entries.add(new BarEntry(Float.parseFloat(c.getMonto()), flagIndex));
            labels.add(c.getNombre());
            flagIndex ++;
        }

        BarDataSet dataset = new BarDataSet(entries, "Dinero gastado");

        BarData data = new BarData(labels, dataset);
        // dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        barChart.setData(data);
        barChart.animateY(5000);
    }

    /**
     * Funcion encargada de pedir la informacion para despues crear la
     * grafica de cuentas
     */
    private void generarGraficaCuentas() {
        progreso.setVisibility(View.VISIBLE);
        Log.i("JSON", "Si entro");
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(Estadisticas.this).
                addToRequestQueue(
                        request = new JsonObjectRequest(
                                Request.Method.GET,
                                urls.getGetGraficaCuentas() + "idU=" + usuario.getIdUsuario(),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res = response.getString("estado");
                                            switch(res){
                                                case "1":
                                                    Log.i("peticion", "caso 1");
                                                    cuentas.clear();
                                                    JSONArray jArrayMarcadores = response.getJSONArray("registros");
                                                    ObjetoDatosGraficaCuentas[] arraycuentasTotales = gson.fromJson(jArrayMarcadores.toString(), ObjetoDatosGraficaCuentas[].class);
                                                    Log.i("peticion", "tamaño: " + arraycuentasTotales.length);
                                                    for(int i = 0; i < arraycuentasTotales.length; i++){
                                                        cuentas.add(arraycuentasTotales[i]);
                                                    }
                                                    rellenarGraficaCuentas();
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                case "0":
                                                    Log.i("peticion", "caso 0");
                                                    //Regresar mensaje de que no hay registros
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                default:
                                                    Log.i("peticion", "caso default");
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
     * Funcion encargada de rellenar la grafica con los datos necesitados
     */
    private void rellenarGraficaCuentas() {
        //creacion de la grafica
        BarChart barChart = (BarChart) findViewById(R.id.graficaCuentas);

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        int flagIndex = 0;

        for(ObjetoDatosGraficaCuentas c : cuentas){
            entries.add(new BarEntry(Float.parseFloat(c.getMonto()), flagIndex));
            labels.add(c.getNombre());
            flagIndex ++;
        }

        BarDataSet dataset = new BarDataSet(entries, "Dinero gastado");

        BarData data = new BarData(labels, dataset);
        // dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        barChart.setData(data);
        barChart.animateY(5000);
    }

    /**
     * Funcion que da funcionalidad al menu
     */
    private void menuNav(){
        for(int i = 0; i < menu.size(); i++){
            items.add(menu.getItem(i));
        }
        items.get(4).setChecked(true);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                int pos = items.indexOf(item);
                if(pos == 0){
                    Intent i = new Intent(Estadisticas.this, Movimientos.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }else if(pos == 1){
                    Intent i = new Intent(Estadisticas.this, AgregarCategoria.class);
                    startActivity(i);
                }else if(pos == 2){
                    Intent i = new Intent(Estadisticas.this, AgregarCuenta.class);
                    startActivity(i);
                }else if(pos == 3) {
                    Intent i = new Intent(Estadisticas.this, DespliegueMovimientos.class);
                    startActivity(i);
                }else if(pos == 4){

                }else if(pos == 5){
                    Intent i = new Intent(Estadisticas.this, Configuracion.class);
                    startActivity(i);
                }else if(pos == 6){
                    Intent i = new Intent(Estadisticas.this, Acerca.class);
                    startActivity(i);
                }else if(pos == 7){
                    if(bd.LogoutUsuario(usuario.getIdUsuario()).equals("1")){
                        Intent i = new Intent(Estadisticas.this, Login.class);
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
