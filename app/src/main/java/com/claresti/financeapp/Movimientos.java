package com.claresti.financeapp;

import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.internal.Excluder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private DatePicker dateFechaMovimiento;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_movimientos);

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
        dateFechaMovimiento = (DatePicker)findViewById(R.id.date_fechaMovimiento);
        btnRegistrarMovimiento = (Button)findViewById(R.id.btn_registrar);
        progreso = (ProgressBar)findViewById(R.id.progress);

        //Asignacion variables clases
        bd = new BD(getApplicationContext());
        usuario = bd.slectUsuario();
        urls = new Urls();

        // Asignacion variables restantes
        flagMovimiento = 5;

        // Llamada a funciones para llenar los spinners y crear los listenrs
        llenarSpinerCategoria();
        llenarSpinerCuenta();
        crearListeners();
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
            }
        });
        egreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagMovimiento = 0;
                txtMovimiento.setText("Egreso");
                Log.i("Movimiento", flagMovimiento + "");
            }
        });
        btnRegistrarMovimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarFormulario();
            }
        });
    }

    /**
     * Funcion encargada de llenar el espiner de categorias
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
                                urls.getUrlGetCategorias() + "usuario=" + usuario.getUsuario(),
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
                                                            flagCategoria = position;
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
                                urls.getUrlGetCuentas() + "usuario=" + usuario.getUsuario(),
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
                                                            flagCuenta = position;
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
            Date fecha = sdf.parse(dateFechaMovimiento.getYear() + "-" + (dateFechaMovimiento.getMonth() + 1) + "-" + dateFechaMovimiento.getDayOfMonth());
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
                                urls.getUrlSetMovimiento() + "idU=" + usuario.getIdUsuario() +
                                        "&idC=" + flagCategoria +
                                        "&mon=" + inputMonto.getText() +
                                        "&idCu=" + flagCuenta +
                                        "&tip=" + flagMovimiento +
                                        "&date==" + dateFechaMovimiento.getYear() + "-" + (dateFechaMovimiento.getMonth() + 1) + "-" + dateFechaMovimiento.getDayOfMonth(),
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
                                                    msg("El correo ya esta registrado, prueba con otro correo");
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
