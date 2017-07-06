package com.claresti.financeapp;

import android.icu.util.Calendar;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import java.util.Date;

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
    }

    /**
     * Metodo encargado de revisar que todos los campos no se encuentren vacios, en caso
     * de que algun campo no este completo mostrará un snackbar con el mensaje de que campo
     * está vacio y en caso de que ningun campo esté vacio ejecuta la funcion registrar
     */
    private void validarFormulario() {
        if(inputNombre.equals("")){
            msg("Ingrese su nombre");
        }else if(inputCorreo.equals("")){
            msg("Ingrese su correo");
        }else if(inputUsuario.equals("")){
            msg("Ingrese su nombre de usuario");
        }else if(inputPassword.equals("")){
            msg("Ingrese una contraseña");
        }else if(!inputValidPassword.equals(inputPassword)){
            msg("Las contraseñas no coinciden");
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
}
