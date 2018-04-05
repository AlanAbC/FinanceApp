package com.claresti.financeapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.Toast;

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

    //Declaracion de banderas de variables
    private int flagMovimiento;
    private int flagCategoria;
    private int flagCuenta;
    private int flagCuentaTransfer;

    // Declaracion variables para el datapikerdialog
    private int ano;
    private int mes;
    private int dia;

    //Declaracion de variables para la comunicacion con la API
    private Comunications com;
    private UserSessionManager session;
    private HashMap<String,String> user;

    //ProgressDialog
    private ProgressDialogDenarius progressDialog;

    //Bandera de Tipo de movimiento
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimientos);

        //Cambiar el color en la barra de notificaciones (Solo funciona de lollipop hacia arriba)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }

        // Asignacion variables layout
        spinerCategoria = (Spinner)findViewById(R.id.spin_categoria);
        spinerCuenta = (Spinner)findViewById(R.id.spin_cuenta);
        spinerAccountTransfer = (Spinner)findViewById(R.id.spin_accountTransfer);
        inputMonto = (EditText)findViewById(R.id.input_monto);
        ventana = (RelativeLayout)findViewById(R.id.l_ventana);
        dateFechaMovimiento = (EditText) findViewById(R.id.input_fecha);
        conceptoMovimiento = (EditText) findViewById(R.id.input_concepto);
        btnRegistrarMovimiento = (Button)findViewById(R.id.btn_registrar);
        progreso = (ProgressBar)findViewById(R.id.progress);
        calendarPicker= (ImageButton) findViewById(R.id.calendar);

        progressDialog = new ProgressDialogDenarius(this);


        // Asignacion variables restantes
        flagMovimiento = 5;
        flagCuentaTransfer = -1;
        flagCuenta = -1;
        flagCategoria = -1;

        // Datos para el datepikerdialog
        Calendar c = Calendar.getInstance();
        ano = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.DAY_OF_MONTH);

        // Llamada a funciones para llenar los spinners y crear los listenrs
        session = new UserSessionManager(getApplicationContext());
        user = session.getUserDetails();
        com = new Comunications(getApplicationContext(), ventana);

        //Ocultar teclado al iniciar la activity
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        modifyMovement();

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
                flagCuentaTransfer = -1;//reseteamos la bandera de cuenta a transferir
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
                txtCategoria.setText(Comunications.arrayCategoria[position].getNombre());
                flagCategoria = Integer.parseInt(Comunications.arrayCategoria[position].getID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinerAccountTransfer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtAccountTransfer.setText(Comunications.arrayCuenta[position].getNombre());
                flagCuentaTransfer = Integer.parseInt(Comunications.arrayCuenta[position].getID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinerCuenta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtCuenta.setText(Comunications.arrayCuenta[position].getNombre());
                flagCuenta = Integer.parseInt(Comunications.arrayCuenta[position].getID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(progressDialog.getState())
                {
                    finish();
                }
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
            } else if (flagMovimiento == 3 && flagCuentaTransfer == -1) {
                msg("Selecciona una cuenta para la transferencia");
            } else if (flagMovimiento == 3 && flagCuenta == flagCuentaTransfer) {
                msg("No puedes hacer una transferencia a la misma cuenta");
            } else {
                progressDialog.show();
                Map<String, String> paramsMovements = new HashMap<String, String>();
                paramsMovements.put("idUsuario",user.get(UserSessionManager.KEY_ID));
                paramsMovements.put("idCategoria", flagCategoria + "");
                paramsMovements.put("monto", inputMonto.getText().toString());
                paramsMovements.put("idCuenta", flagCuenta + "");
                paramsMovements.put("tipo", flagMovimiento + "");
                paramsMovements.put("fecha", dateFechaMovimiento.getText().toString());
                paramsMovements.put("concepto", conceptoMovimiento.getText().toString());
                if(flagMovimiento == 3)
                {
                    paramsMovements.put("idAccountTransfer", flagCuentaTransfer + "");
                }
                paramsMovements.put("idMovement", ID);
                com.newRegister(Urls.UPDATEMOVEMENT, paramsMovements, progressDialog);

            }
        }catch (Exception e){
            Log.e("SFD PARSE", e.toString());
        }
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

        DatePickerDialog dpd = new DatePickerDialog(Movimientos.this, R.style.Calendar, new DatePickerDialog.OnDateSetListener() {
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

    /**
     * Funcion que rellena los campos con los datos de un movimiento a modificar
     */
    private void modifyMovement()
    {
        ObjMovimiento movement = (ObjMovimiento) getIntent().getExtras().getSerializable("Movimiento");
        Map<String, String> paramsMovements = new HashMap<String, String>();
        paramsMovements.put("username",user.get(UserSessionManager.KEY_USER));
        com.fillSpinnerCategory(Urls.GETCATEGORIES, paramsMovements, spinerCategoria, progreso, movement.getIdCategoria());
        com.fillSpinnerAccount(Urls.GETACCOUNTS, paramsMovements, spinerCuenta, progreso, movement.getIdCuenta());
        try {
            switch (Integer.parseInt(movement.getTipo())) {
                case 1:
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
                    com.fillSpinnerAccount(Urls.GETACCOUNTS, paramsMovements, spinerAccountTransfer, progreso, null);
                    break;
                case 2:
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
                    com.fillSpinnerAccount(Urls.GETACCOUNTS, paramsMovements, spinerAccountTransfer, progreso, null);
                    break;
                case 3:
                    flagMovimiento = 3;
                    flagCuentaTransfer = -1;//reseteamos la bandera de cuenta a transferir
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
                    com.fillSpinnerAccount(Urls.GETACCOUNTS, paramsMovements, spinerAccountTransfer, progreso, movement.getIdCuentaTransfer());
                    break;
            }
        }
        catch (NumberFormatException nfe)
        {
            finish();
        }
        ID = movement.getID();
        inputMonto.setText(movement.getMonto());
        conceptoMovimiento.setText(movement.getConcepto());
        dateFechaMovimiento.setText(movement.getFecha());
        dateFechaMovimiento.setClickable(false);

    }

}
