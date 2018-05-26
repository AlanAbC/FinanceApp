package com.claresti.financeapp.Activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.claresti.financeapp.Dialogs.DialogProgress;
import com.claresti.financeapp.R;
import com.claresti.financeapp.Tools.Comunicaciones;
import com.claresti.financeapp.Tools.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    // Declaracion de variables del layout
    private View ventana;
    private EditText inputNombre;
    private EditText inputCorreo;
    private EditText inputUsuario;
    private EditText inputPassword;
    private EditText inputValidPassword;
    private EditText inputFecha;
    private RadioGroup radioSexo;
    private Button registrar;
    private JSONObject jsonObject;
    private static final String TAG = "REGISTRO";
    private DialogProgress progress;

    // Declaracion variables para el datapikerdialog
    private int ano;
    private int mes;
    private int dia;

    // Declaracion de variables bandera
    private String flagSexo;

    Comunicaciones.ResultadosInterface resultadosListener = new Comunicaciones.ResultadosInterface() {
        @Override
        public void mostrarDatos(JSONObject json) {
            progress.dismiss();
            Log.i(TAG, json.toString());
            msg(getString(R.string.registro_exitoso));
            finish();
        }

        @Override
        public void setError(String mensaje) {
            Log.i(TAG, "Error registrado" + mensaje);
            progress.dismiss();
            msg(mensaje);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Asignacion de variables del layout
        ventana = (View)findViewById(R.id.lay_principal);
        inputNombre = findViewById(R.id.input_name);
        inputCorreo = findViewById(R.id.input_email);
        inputUsuario = findViewById(R.id.input_user);
        inputPassword = findViewById(R.id.input_password);
        inputValidPassword = findViewById(R.id.input_valid_password);
        inputFecha = findViewById(R.id.input_fecha);
        radioSexo = findViewById(R.id.rg_sex);
        registrar = findViewById(R.id.btn_registrar);

        // Datos para el datepikerdialog
        Calendar c = Calendar.getInstance();
        ano = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.DAY_OF_MONTH);

        // Asignacion de variables bandera
        flagSexo = "1";

        // Asignacion de listeners de los elementos del layout
        radioSexo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.radio_masculino){
                    flagSexo = "1";
                }else if(checkedId == R.id.radio_femenino){
                    flagSexo = "2";
                }
            }
        });

        progress = new DialogProgress();

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarFormulario()) {
                    progress.show(getFragmentManager(), "DialogProgress");
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-type", "application/json");
                    Comunicaciones com = new Comunicaciones(Registro.this, resultadosListener);
                    com.peticionJSON(Urls.REGISTRO, Request.Method.POST, jsonObject, headers);
                }
            }
        });

        //Agregamos listener para saber si el EditText de fecha tiene el focus
        inputFecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    setFechaMovimiento();
                }
            }
        });

        inputFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFechaMovimiento();
            }
        });

        //Ocultar teclado al iniciar la activity
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * Metodo encargado de revisar que todos los campos no se encuentren vacios, en caso
     * de que algun campo no este completo mostrará un snackbar con el mensaje de que campo
     * está vacio y en caso de que ningun campo esté vacio ejecuta la funcion registrar
     */
    private boolean validarFormulario() {
        String nombre, correo, usuario, password, passwordc, fecha;
        nombre = inputNombre.getText().toString();
        correo = inputCorreo.getText().toString();
        usuario = inputCorreo.getText().toString();
        password = inputPassword.getText().toString();
        passwordc = inputValidPassword.getText().toString();
        fecha = inputFecha.getText().toString();

        if(TextUtils.isEmpty(nombre)){
            inputNombre.setError(getString(R.string.error_input_vacio));
            return false;
        }
        if(TextUtils.isEmpty(correo)){
            inputCorreo.setError(getString(R.string.error_input_vacio));
            return false;
        }
        if(!correo.contains("@") || !correo.contains(".")){
            inputCorreo.setError(getString(R.string.error_correo_invalido));
            return false;
        }
        if(TextUtils.isEmpty(usuario)){
            inputUsuario.setError(getString(R.string.error_input_vacio));
            return false;
        }
        if(TextUtils.isEmpty(password)){
            inputPassword.setError(getString(R.string.error_input_vacio));
            return false;
        }
        if(TextUtils.isEmpty(passwordc)){
            inputValidPassword.setError(getString(R.string.error_input_vacio));
            return false;
        }
        if(TextUtils.isEmpty(fecha)){
            inputFecha.setError(getString(R.string.error_input_vacio));
            return false;
        }
        if(!password.equals(passwordc)){
            inputValidPassword.setError(getString(R.string.registro_error_pass));
            return false;
        }

        jsonObject = new JSONObject();
        try{
            jsonObject.put("name", nombre);
            jsonObject.put("email", correo);
            jsonObject.put("nickname", usuario);
            jsonObject.put("password", password);
            jsonObject.put("birth_date", fecha);
            jsonObject.put("gender", flagSexo);
        } catch(JSONException jsone) {
            Log.e(TAG, jsone.getMessage());
            return false;
        }
        return true;
    }

    /**
     * funcion encargada de establecer la fecha en el campo de fecha de movimiento y ocultar el teclado al mostrar el dialog
     */
    private void setFechaMovimiento() {
        //Ocultamos el Teclado
        inputFecha.setInputType(InputType.TYPE_NULL);
        InputMethodManager inputMethodManager =  (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(inputFecha.getWindowToken(), 0);

        DatePickerDialog dpd = new DatePickerDialog(this, R.style.Calendar, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(year <= ano && month <= mes && dayOfMonth <= dia) {
                    inputFecha.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                }
            }
        }, ano, mes, dia);
        dpd.show();
        Button ok = dpd.getButton(DialogInterface.BUTTON_POSITIVE);
        ok.setTextColor(getResources().getColor(R.color.colorAccent));
        Button cancel = dpd.getButton(DialogInterface.BUTTON_NEGATIVE);
        cancel.setTextColor(getResources().getColor(R.color.grey_40));
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
