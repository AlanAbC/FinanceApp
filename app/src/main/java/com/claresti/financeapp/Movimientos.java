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
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Movimientos extends AppCompatActivity {

    // Declaracion de variables en el layout
    private Spinner spinerCategoria;
    private Spinner spinerCuenta;
    private TextView txtCategoria;
    private TextView txtCuenta;
    private TextView txtMovimiento;
    private ImageView ingreso;
    private ImageView egreso;
    private EditText inputMonto;
    private EditText dateFechaMovimiento;
    private Button btnRegistrarMovimiento;
    private RelativeLayout ventana;
    private ProgressBar progreso;

    // Declaracion de variables de clases
    private BD bd;
    private ObjUsuario usuario;
    private Urls urls;
    private ObjCategoria[] arrayCategoria;
    private ObjCuenta[] arrayCuenta;

    //Declaracion de banderas de variables
    private int flagMovimiento;
    private int flagCategoria;
    private int flagCuenta;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_movimientos);

        //Cambiar el color en la barra de notificaciones (Solo funciona de lollipop hacia arriba)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.top));
        }

        // Asignacion variables layout
        spinerCategoria = (Spinner)findViewById(R.id.spin_categoria);
        spinerCuenta = (Spinner)findViewById(R.id.spin_cuenta);
        inputMonto = (EditText)findViewById(R.id.input_monto);
        ventana = (RelativeLayout)findViewById(R.id.l_ventana);
        txtCategoria = (TextView)findViewById(R.id.txt_categoria);
        txtCuenta = (TextView)findViewById(R.id.txt_cuenta);
        txtMovimiento = (TextView)findViewById(R.id.tv_tipomov);
        ingreso = (ImageView)findViewById(R.id.img_mas);
        egreso = (ImageView)findViewById(R.id.img_menos);
        dateFechaMovimiento = (EditText) findViewById(R.id.input_fecha);
        btnRegistrarMovimiento = (Button)findViewById(R.id.btn_registrar);
        progreso = (ProgressBar)findViewById(R.id.progress);

        //Asignacion variables clases
        bd = new BD(getApplicationContext());
        usuario = bd.slectUsuario();
        urls = new Urls();

        // Asignacion variables restantes
        flagMovimiento = 5;

        // Datos para el datepikerdialog
        Calendar c = Calendar.getInstance();
        ano = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.DAY_OF_MONTH);

        // Llamada a funciones para llenar los spinners y crear los listenrs
        llenarSpinerCategoria();
        llenarSpinerCuenta();
        crearListeners();

        //Menu, Inicia las variables del menu y llama la funcion encargada de su manipulacion
        drawerLayout = (DrawerLayout) findViewById(R.id.dLayout);
        nav = (NavigationView)findViewById(R.id.navigation);
        menu = nav.getMenu();
        menuNav();

        //Ocultar teclado al iniciar la activity
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * Funcion encargada de crear los listeners de los objetos del layout
     */
    private void crearListeners() {
        ingreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagMovimiento = 1;
                txtMovimiento.setText("Ingreso");
                Log.i("Movimiento", flagMovimiento + "");
                ingreso.setBackgroundResource(R.drawable.img_verde_res);
                egreso.setBackgroundResource(R.drawable.img_roja);
                ingreso.setPadding(45,45,45,45);
                egreso.setPadding(45,45,45,45);
                ingreso.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                egreso.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            }
        });
        egreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagMovimiento = 2;
                txtMovimiento.setText("Egreso");
                Log.i("Movimiento", flagMovimiento + "");
                egreso.setBackgroundResource(R.drawable.img_roja_res);
                ingreso.setBackgroundResource(R.drawable.img_verde);
                ingreso.setPadding(45,45,45,45);
                egreso.setPadding(45,45,45,45);
                ingreso.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                egreso.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            }
        });
        btnRegistrarMovimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarFormulario();
            }
        });

        //Agregamos listener para saber si el EditText de fecha tiene el focus
        dateFechaMovimiento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(dateFechaMovimiento.isFocused())
                {
                    setFechaMovimiento();
                }
            }
        });

        dateFechaMovimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFechaMovimiento();
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
        VolleySingleton.getInstance(Movimientos.this).
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
     * Funcion encargada de llenar el espiner de cuenta
     */
    private void llenarSpinerCuenta() {
        progreso.setVisibility(View.VISIBLE);
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(Movimientos.this).
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
     * Funcion encargada de validar el formulario, en caso de que algun dato oblicatorio sea vacio
     * regresa mensaje de dato faltante, en caso contrario ejectuta reliarRegistro
     */
    private void validarFormulario() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaActual = new Date();
            Date fecha = sdf.parse(dateFechaMovimiento.getText().toString());
            if (flagMovimiento == 5) {
                msg("Selecciona que tipo de movimiento desea realizar");
            } else if (inputMonto.getText().toString().equals("")) {
                msg("Ingresa la cantidad del movimiento");
            } else if (fechaActual.before(fecha)){
                msg("Ingrese una fecha que ya haya pasado");
            }else{
                crearMovimiento();
            }
        }catch (Exception e){
            Log.e("SFD PARSE", e.toString());
        }
    }

    /**
     * Funcion encargada de mandar la informacion del formulario y crear un movimiento
     * en caso correcto mostrara mensaje de operecion correcta y en caso contrario
     * mostrara el mensaje del servidor con el error
     */
    private void crearMovimiento() {
        progreso.setVisibility(View.VISIBLE);
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(Movimientos.this).
                addToRequestQueue(
                        request = new JsonObjectRequest(
                                Request.Method.GET,
                                urls.getSetMovimiento() + "idU=" + usuario.getIdUsuario() +
                                        "&idC=" + flagCategoria +
                                        "&mon=" + inputMonto.getText() +
                                        "&idCu=" + flagCuenta +
                                        "&tip=" + flagMovimiento +
                                        "&date=" + dateFechaMovimiento.getText().toString(),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res = response.getString("estado");
                                            switch(res){
                                                case "1":
                                                    msg("Se registro correctamente el movimiento");
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                case "0":
                                                    msg(response.getString("mensaje"));
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                default:

                                                    progreso.setVisibility(View.GONE);
                                                    msg("Ocurrio un error inesperado");
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
        items.get(0).setChecked(true);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                int pos = items.indexOf(item);
                if(pos == 0){

                }else if(pos == 1){
                    Intent i = new Intent(Movimientos.this, AgregarCategoria.class);
                    startActivity(i);
                }else if(pos == 2){
                    Intent i = new Intent(Movimientos.this, AgregarCuenta.class);
                    startActivity(i);
                }else if(pos == 3) {
                    Intent i = new Intent(Movimientos.this, DespliegueMovimientos.class);
                    startActivity(i);
                }else if(pos == 4){
                    Intent i = new Intent(Movimientos.this, Estadisticas.class);
                    startActivity(i);
                }else if(pos == 5){
                    Intent i = new Intent(Movimientos.this, Acerca.class);
                    startActivity(i);
                }else if(pos == 6){
                    if(bd.LogoutUsuario(usuario.getIdUsuario()).equals("1")){
                        Intent i = new Intent(Movimientos.this, Login.class);
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

    private void setFechaMovimiento()
    {
        //Ocultamos el Teclado
        dateFechaMovimiento.setInputType(InputType.TYPE_NULL);
        InputMethodManager inputMethodManager =  (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(dateFechaMovimiento.getWindowToken(), 0);

        DatePickerDialog dpd = new DatePickerDialog(Movimientos.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(year <= ano && month <= mes && dayOfMonth <= dia) {
                    dateFechaMovimiento.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                }
            }
        }, ano, mes, dia);
        dpd.show();
        Button ok = dpd.getButton(DialogInterface.BUTTON_POSITIVE);
        ok.setTextColor(Color.parseColor("#949494"));
        Button cancel = dpd.getButton(DialogInterface.BUTTON_NEGATIVE);
        cancel.setTextColor(Color.parseColor("#949494"));
    }
}
