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
import android.widget.ImageButton;
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
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Movimientos extends AppCompatActivity {

    // Declaracion de variables en el layout
    private Spinner spinerCategoria;
    private Spinner spinerCuenta;
    private Spinner spinerAccountTransfer;
    private TextView txtCategoria;
    private TextView txtCuenta;
    private TextView txtMovimiento;
    private TextView txtAccountTransfer;
    private TextView descriptionAccountTranfer;
    private ImageView ingreso;
    private ImageView egreso;
    private ImageView transfer;
    private EditText inputMonto;
    private EditText dateFechaMovimiento;
    private EditText conceptoMovimiento;
    private ImageButton calendarPicker;
    private Button btnRegistrarMovimiento;
    private RelativeLayout ventana;
    private ProgressBar progreso;

    // Declaracion de variables de clases
    private BD bd;
    private ObjUsuario usuario;
    private Urls urls;
    public static ObjCategoria[] arrayCategoria;
    public static ObjCuenta[] arrayCuenta;

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
        spinerAccountTransfer = (Spinner)findViewById(R.id.spin_accountTransfer);
        inputMonto = (EditText)findViewById(R.id.input_monto);
        ventana = (RelativeLayout)findViewById(R.id.l_ventana);
        txtCategoria = (TextView)findViewById(R.id.txt_categoria);
        txtCuenta = (TextView)findViewById(R.id.txt_cuenta);
        txtAccountTransfer = (TextView)findViewById(R.id.txt_accountTransfer);
        descriptionAccountTranfer = (TextView)findViewById(R.id.tv_accountTransfer);
        txtMovimiento = (TextView)findViewById(R.id.tv_tipomov);
        ingreso = (ImageView)findViewById(R.id.img_mas);
        egreso = (ImageView)findViewById(R.id.img_menos);
        transfer = (ImageView) findViewById(R.id.img_transfer);
        dateFechaMovimiento = (EditText) findViewById(R.id.input_fecha);
        conceptoMovimiento = (EditText) findViewById(R.id.input_concepto);
        btnRegistrarMovimiento = (Button)findViewById(R.id.btn_registrar);
        progreso = (ProgressBar)findViewById(R.id.progress);
        calendarPicker= (ImageButton) findViewById(R.id.calendar);

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
        UserSessionManager session = new UserSessionManager(getApplicationContext());
        HashMap<String,String> user = session.getUserDetails();
        Map<String, String> paramsMovements = new HashMap<String, String>();
        paramsMovements.put("username",user.get(UserSessionManager.KEY_USER));
        Comunications com = new Comunications(getApplicationContext(), ventana);
        com.fillSpinnerCategory(Urls.GETCATEGORIES, paramsMovements, spinerCategoria, progreso);
        com.fillSpinnerAccount(Urls.GETACCOUNTS, paramsMovements, spinerCuenta, progreso);
        com.fillSpinnerAccount(Urls.GETACCOUNTS, paramsMovements, spinerAccountTransfer, progreso);
        crearListeners();

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
                //ingreso.setBackgroundResource(R.drawable.img_verde_res);
                //egreso.setBackgroundResource(R.drawable.img_roja);
                ingreso.setPadding(20,20,20,20);
                egreso.setPadding(25,25,25,25);
                transfer.setPadding(25,25,25,25);
                ingreso.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                egreso.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                transfer.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                descriptionAccountTranfer.setVisibility(View.GONE);
                spinerAccountTransfer.setVisibility(View.GONE);
                txtAccountTransfer.setVisibility(View.GONE);
            }
        });
        egreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagMovimiento = 2;
                txtMovimiento.setText("Egreso");
                Log.i("Movimiento", flagMovimiento + "");
                //egreso.setBackgroundResource(R.drawable.img_roja_res);
                //ingreso.setBackgroundResource(R.drawable.img_verde);
                egreso.setPadding(20,20,20,20);
                ingreso.setPadding(25,25,25,25);
                transfer.setPadding(25,25,25,25);
                ingreso.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                egreso.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                transfer.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                descriptionAccountTranfer.setVisibility(View.GONE);
                spinerAccountTransfer.setVisibility(View.GONE);
                txtAccountTransfer.setVisibility(View.GONE);
            }
        });

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagMovimiento = 3;
                txtMovimiento.setText("Transferencia");
                transfer.setPadding(10,10,10,10);
                ingreso.setPadding(25,25,25,25);
                egreso.setPadding(25,25,25,25);
                ingreso.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                egreso.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                transfer.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                descriptionAccountTranfer.setVisibility(View.VISIBLE);
                spinerAccountTransfer.setVisibility(View.VISIBLE);
                txtAccountTransfer.setVisibility(View.VISIBLE);
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

        calendarPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFechaMovimiento();
            }
        });

        spinerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtCategoria.setText(arrayCategoria[position].getNombre());
                flagCategoria = Integer.parseInt(arrayCategoria[position].getID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinerAccountTransfer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtAccountTransfer.setText(arrayCuenta[position].getNombre());
                flagCategoria = Integer.parseInt(arrayCuenta[position].getID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinerCuenta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtCuenta.setText(arrayCuenta[position].getNombre());
                flagCategoria = Integer.parseInt(arrayCuenta[position].getID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
            } else if (conceptoMovimiento.getText().toString().equals("")){
                msg("Ingrese el concepto del movimiento a realizar");
            } else{
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
                                        "&date=" + dateFechaMovimiento.getText().toString() +
                                        "&concepto=" + conceptoMovimiento.getText().toString(),
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

    /**
     * funcion encargada de establecer la fecha en el campo de fecha de movimiento y ocultar el teclado al mostrar el dialog
     */
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
