package com.claresti.financeapp.Activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.claresti.financeapp.Dialogs.DialogProgress;
import com.claresti.financeapp.R;
import com.claresti.financeapp.Tools.Comunicaciones;
import com.claresti.financeapp.Urls;

import org.json.JSONException;
import org.json.JSONObject;

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

    // Declaracion de variables bandera
    private String flagSexo;

    Comunicaciones.ResultadosInterface resultadosListener = new Comunicaciones.ResultadosInterface() {
        @Override
        public void mostrarDatos(JSONObject json) {
            Log.i(TAG, json.toString());
            msg(getString(R.string.registro_exitoso));
            finish();
        }

        @Override
        public void setError(String mensaje) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_registro);

        //Cambiar el color en la barra de notificaciones (Solo funciona de lollipop hacia arriba)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }

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
