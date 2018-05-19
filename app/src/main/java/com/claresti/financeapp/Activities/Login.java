package com.claresti.financeapp.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.claresti.financeapp.R;
import com.claresti.financeapp.Tools.Comunicaciones;
import com.claresti.financeapp.Tools.Urls;
import com.claresti.financeapp.Tools.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;

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
    private UserSessionManager sessionManager;

    private Comunicaciones.ResultadosInterface resultadosInterface = new Comunicaciones.ResultadosInterface() {
        @Override
        public void mostrarDatos(JSONObject json) {
            Log.i(TAG, json.toString());
            sessionManager = new UserSessionManager(Login.this);
            try{
                sessionManager.createUserLoginSession(
                        json.getString("id"),
                        json.getString("nickname"),
                        json.getString("full_name"),
                        json.getString("email"),
                        json.getString("gender"),
                        json.getString("birth_date")
                );
                Intent i = new Intent(Login.this, Splash.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Login.this.startActivity(i);
            } catch (JSONException jsone) {
                msg(getString(R.string.comunicaciones_error));
                card.setVisibility(View.VISIBLE);
                Animation centerHide = AnimationUtils.loadAnimation(Login.this, R.anim.animation_center);
                card.startAnimation(centerHide);
                progress.setVisibility(GONE);
            }
        }

        @Override
        public void setError(String mensaje) {
            msg(mensaje);
            card.setVisibility(View.VISIBLE);
            Animation centerHide = AnimationUtils.loadAnimation(Login.this, R.anim.animation_center);
            card.startAnimation(centerHide);
            progress.setVisibility(GONE);
        }
    };

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
            json.put("nickname", correo);
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
                    Animation centerHide = AnimationUtils.loadAnimation(Login.this, R.anim.animation_center_hide);
                    centerHide.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            card.setVisibility(GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    card.startAnimation(centerHide);
                    progress.setVisibility(View.VISIBLE);

                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-type", "application/json");

                    Comunicaciones com = new Comunicaciones(Login.this, resultadosInterface);
                    com.peticionJSON(Urls.NEWLOGIN, Request.Method.POST, json, headers);

                }
            }
        });

    }
}
