package com.claresti.financeapp;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.claresti.financeapp.Activities.Registro;
import com.claresti.financeapp.Activities.Splash;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    // Declaracion de variables del layout
    private TextView btnRegistro;
    private EditText inputUsuario;
    private EditText inputPassword;
    private Button btnIngresar;
    private RelativeLayout ventana, progress;
    private CardView card;
    private JSONObject json;
    private static final String TAG = "LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        //Cambiar el color en la barra de notificaciones (Solo funciona de lollipop hacia arriba)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
            window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.grey_10));
        }

        /*inicio de sesión handler de sesión global*/
        UserSessionManager session;
        session = new UserSessionManager(getApplicationContext());

        //comprobamos que el usuario tenga sesion iniciada
        if(session.isUserLoggedIn()){
            Intent i = new Intent(getApplicationContext(),Splash.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }

        // Asignacion de variables del layout
        inputUsuario = findViewById(R.id.login_input_user);
        inputPassword = findViewById(R.id.login_input_password);
        btnIngresar = findViewById(R.id.login_button_ingresar);
        ventana = findViewById(R.id.login_ventana);
        card = findViewById(R.id.login_card);
        progress = findViewById(R.id.login_progress);
        btnRegistro = findViewById(R.id.login_label_registro);

        agregarListeners();

        //Ocultar teclado al iniciar la activity
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


    }

    /**
     * Funcion que se encarga de obtener los valores de los EditText y comprueba
     * que no esten vacios, en caso de que esten vacios regresa mensaje con
     * cual campo esta vacio y en caso contrario ejecuta la funcion de login
     */
    private boolean validarLogin() {
        String correo = inputUsuario.getText().toString();
        String pass = inputPassword.getText().toString();

        if(TextUtils.isEmpty(correo)) {
            inputUsuario.setError(getString(R.string.error_input_vacio));
            return false;
        }

        if(TextUtils.isEmpty(pass)) {
            inputPassword.setError(getString(R.string.error_input_vacio));
            return false;
        }

        json = new JSONObject();
        try {
            json.put("user", correo);
            json.put("password", pass);
        } catch(JSONException jsone) {
            Log.e(TAG, jsone.toString());
            return false;
        }
        Log.i(TAG, json.toString());
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

    /**
     * Funcion encargada de crear los listener de los distintos elementos de la interfaz de usuario
     */
    private void agregarListeners()
    {
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Registro.class);
                startActivity(i);
            }
        });

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarLogin()) {
                    progress.setVisibility(View.VISIBLE);
                    Animation centerHide = AnimationUtils.loadAnimation(Login.this, R.anim.animation_center_hide);
                    card.startAnimation(centerHide);
                    card.setVisibility(View.GONE);
                }
            }
        });

    }
}
