package com.claresti.financeapp;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class Registro extends AppCompatActivity {

    // Declaracion de variables del layout
    private RelativeLayout ventana;
    private EditText inputNombre;
    private EditText inputCorreo;
    private EditText inputUsuario;
    private EditText inputPassword;
    private EditText inputValidPassword;
    private DatePicker dateFechaNacimiento;
    private RadioGroup radioSexo;
    private ProgressBar progreso;
    private Button registrar;

    // Declaracion de variables de objetos
    private BD bd;
    private ObjUsuario nuevoUsuario;
    private Urls urls;

    // Declaracion de variables bandera
    private String flagSexo;

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

        // Asignacion del los objetos
        bd = new BD(getApplicationContext());
        nuevoUsuario = new ObjUsuario();
        urls = new Urls();

        // Asignacion de variables del layout
        ventana = (RelativeLayout)findViewById(R.id.lay_principal);
        inputNombre = (EditText)findViewById(R.id.input_name);
        inputCorreo = (EditText)findViewById(R.id.input_email);
        inputUsuario = (EditText)findViewById(R.id.input_user);
        inputPassword = (EditText)findViewById(R.id.input_password);
        inputValidPassword = (EditText)findViewById(R.id.input_valid_password);
        dateFechaNacimiento = (DatePicker)findViewById(R.id.date);
        radioSexo = (RadioGroup)findViewById(R.id.rg_sex);
        progreso = (ProgressBar)findViewById(R.id.progress);
        registrar = (Button)findViewById(R.id.btn_registrar);

        // Asignacion de variables bandera
        flagSexo = "1";

        // Asignacion de listeners de los elementos del layout
        radioSexo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.radio_masculino){
                    flagSexo = "1";
                }else if(checkedId == R.id.radio_femenino){
                    flagSexo = "0";
                }
            }
        });
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarFormulario();
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
    private void validarFormulario() {
        if(inputNombre.getText().toString().equals("")){
            msg("Ingrese su nombre");
        }else if(inputCorreo.getText().toString().equals("")){
            msg("Ingrese su correo");
        }else if(inputUsuario.getText().toString().equals("")){
            msg("Ingrese su nombre de usuario");
        }else if(inputPassword.getText().toString().equals("")){
            msg("Ingrese una contraseña");
        }else if(!inputValidPassword.getText().toString().equals(inputPassword.getText().toString())){
            msg("Las contraseñas no coinciden");
        }else if(dateFechaNacimiento.getYear() < (dateFechaNacimiento.getYear() - 9)){
            msg("Eres muy chico para usar esta aplicacion");
        }else{
            registrarUsuario();
        }
    }

    /**
     * Funcion encargada de registrar al usuario en el web service y validar que el usuario o
     * correo electronico no se encuentre registrado en caso de que se cree el usuario
     * correctamente se guarda su informacion en la base de datos y lo redirecciona a la
     * actvity siguiete
     */
    private void registrarUsuario() {
        progreso.setVisibility(View.VISIBLE);
        Log.i("JSON", "Si entro");
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(Registro.this).
                addToRequestQueue(
                        request = new JsonObjectRequest(
                                Request.Method.GET,
                                urls.getRegistro() + "nom=" + inputNombre.getText() +
                                        "&use=" + inputUsuario.getText() +
                                        "&pas=" + inputPassword.getText() +
                                        "&cor=" + inputCorreo.getText() +
                                        "&sex=" + flagSexo +
                                        "&fna=" + dateFechaNacimiento.getYear() + "-" + dateFechaNacimiento.getMonth() + "-" + dateFechaNacimiento.getDayOfMonth(),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res = response.getString("estado");
                                            switch(res){
                                                case "1":
                                                    Log.e("error", response.getString("mensaje"));
                                                    Toast toast = Toast.makeText(getApplicationContext(), "Se hizo el registro correctamente", Toast.LENGTH_SHORT);
                                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                                    toast.show();
                                                    Intent i = new Intent(Registro.this, Login.class);
                                                    startActivity(i);
                                                    finish();
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
