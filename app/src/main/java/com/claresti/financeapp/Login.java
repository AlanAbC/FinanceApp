package com.claresti.financeapp;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    // Declaracion de variables del layout
    private static TextView btnRegistro, msgLogin;
    private static TextInputLayout textInputLayoutUsuario;
    private static EditText inputUsuario;
    private static TextInputLayout textInputLayoutPassword;
    private static EditText inputPassword;
    private static Button btnIngresar;
    private static RelativeLayout ventana;
    private static ImageView logo;

    // Declaracion de las variables de clases
    private Urls urls;

    //Declaracion de variables para el control de bottom sheet
    private Button btnConBottomSheet;
    private LinearLayout bottomSheet;

    private Comunications com;

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
            window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }

        /*inicio de sesión handler de sesión global*/
        UserSessionManager session;
        session = new UserSessionManager(getApplicationContext());

        /***
         *
         */
        if(session.isUserLoggedIn()){
            Intent i = new Intent(getApplicationContext(),MainDenarius.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }

        // Asignacion de variables del layout
        logo = (ImageView) findViewById(R.id.logo);
        msgLogin = (TextView) findViewById(R.id.msg_session);
        btnRegistro = (TextView)findViewById(R.id.btn_registro);
        textInputLayoutUsuario = (TextInputLayout) findViewById(R.id.input_layout_user);
        inputUsuario = (EditText)findViewById(R.id.input_user);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputPassword = (EditText)findViewById(R.id.input_password);
        btnIngresar = (Button)findViewById(R.id.btn_ingresar);
        ventana = (RelativeLayout)findViewById(R.id.l_ventana);

        com = new Comunications(this, ventana);

        // Declaracion de los listeners
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Registro.class);
                startActivity(i);
            }
        });
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarLogin();
            }
        });

        //Llamada de las variables para el control de bottomsheet
        bottomSheet = (LinearLayout)findViewById(R.id.bottomSheet);
        final BottomSheetBehavior bsb = BottomSheetBehavior.from(bottomSheet);
        bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);

        //Control para esconder bottomsheet
        btnConBottomSheet=(Button)findViewById(R.id.btnConBottomSheet);
        btnConBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        agregarListeners();

        //Ocultar teclado al iniciar la activity
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


    }

    /**
     * Funcion que se encarga de obtener los valores de los EditText y comprueba
     * que no esten vacios, en caso de que esten vacios regresa mensaje con
     * cual campo esta vacio y en caso contrario ejecuta la funcion de login
     */
    private void validarLogin() {
        String correo = inputUsuario.getText().toString();
        String pass = inputPassword.getText().toString();
        if(correo.equals("")){
            msg("Ingrese su correo electronico o usuario");
        }else if(pass.equals("")){
            msg("Ingrese su contraseña");
        }else{
            Map<String, String> paramsMovements = new HashMap<String, String>();
            paramsMovements.put("identificador", correo);
            paramsMovements.put("password", pass);
            com.login(Urls.LOGIN, paramsMovements);
            loginAnimation();
        }
    }

    /**
     * funcion encargada de crear un snackbar con un mensaje y un boton de aceptar para ocultarla
     * @param msg String que contiene el mensaje
     */
    private static void msg(String msg){
        Snackbar.make(ventana, msg, Snackbar.LENGTH_LONG).setAction("Aceptar", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    private void agregarListeners()
    {

    }

    private void loginAnimation()
    {
        textInputLayoutUsuario.animate().alpha(0f).setDuration(800);
        inputUsuario.animate().alpha(0f).setDuration(800);
        textInputLayoutPassword.animate().alpha(0f).setDuration(800);
        inputPassword.animate().alpha(0f).setDuration(800);
        btnIngresar.animate().alpha(0f).setDuration(800);
        btnRegistro.animate().alpha(0f).setDuration(800);
        msgLogin.animate().alpha(1f).setDuration(800);
        showDenariusLogo();
    }

    public static void errorAnimation(String errorMessage)
    {
        textInputLayoutUsuario.animate().alpha(1f).setDuration(800);
        inputUsuario.animate().alpha(1f).setDuration(800);
        textInputLayoutPassword.animate().alpha(1f).setDuration(800);
        inputPassword.animate().alpha(1f).setDuration(800);
        btnIngresar.animate().alpha(1f).setDuration(800);
        btnRegistro.animate().alpha(1f).setDuration(800);
        msgLogin.animate().alpha(0f).setDuration(800);
        logo.animate().setListener(null);
        logo.clearAnimation();
        logo.animate().alpha(1f);
        msg(errorMessage);
    }

    private void showDenariusLogo()
    {
        logo.animate().alpha(0.1f).setDuration(900).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                dismissDenariusLogo();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void dismissDenariusLogo()
    {
        logo.animate().alpha(1f).setDuration(500).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                showDenariusLogo();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
}
