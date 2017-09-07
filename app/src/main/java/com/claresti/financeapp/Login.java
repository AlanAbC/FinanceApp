package com.claresti.financeapp;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class Login extends AppCompatActivity {

    // Declaracion de variables del layout
    private TextView btnRegistro;
    private EditText inputUsuario;
    private EditText inputPassword;
    private Button btnIngresar;
    private ProgressBar progreso;
    private RelativeLayout ventana;

    // Declaracion de las variables de clases
    private Urls urls;

    //Declaracion de variables para el control de bottom sheet
    private Button btnConBottomSheet;
    private LinearLayout bottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        //Cambiar el color en la barra de notificaciones (Solo funciona de lollipop hacia arriba)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.top));
            window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.top));
        }

        // Declaracion de variables de clases
        urls = new Urls();

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
        btnRegistro = (TextView)findViewById(R.id.btn_registro);
        inputUsuario = (EditText)findViewById(R.id.input_user);
        inputPassword = (EditText)findViewById(R.id.input_password);
        btnIngresar = (Button)findViewById(R.id.btn_ingresar);
        progreso = (ProgressBar)findViewById(R.id.progress);
        ventana = (RelativeLayout)findViewById(R.id.l_ventana);

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
            login(correo, pass);
        }
    }

    /**
     * Funcion que solicita a la api los datos del login y los valida si existe
     * el usuario en caso de existir guarda la informacion en la base de
     * datos local y en caso contrario marca mensaje de que no existe el
     * usuario o contraseña incorrecta
     * @param correo correo del usuario
     * @param pass contraseña del usuario
     */
    private void login(String correo, String pass) {
        progreso.setVisibility(View.VISIBLE);
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(Login.this).
                addToRequestQueue(
                        request = new JsonObjectRequest(
                                Request.Method.GET,
                                urls.getLogin() + "identificador=" + correo + "&password=" + pass,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res = response.getString("estado");
                                            switch(res){
                                                case "1":
                                                    JSONArray jArrayMarcadores = response.getJSONArray("usuario");
                                                    ObjUsuario[] arrayUsuario = gson.fromJson(jArrayMarcadores.toString(), ObjUsuario[].class);
                                                    if(arrayUsuario.length == 1) {
                                                        for (ObjUsuario usuaro : arrayUsuario) {
                                                            //crear base de datos local i guardar la informacion del objeto
                                                            UserSessionManager session;
                                                            session = new UserSessionManager(getApplicationContext());

                                                            session.createUserLoginSession(usuaro.getIdUsuario(), usuaro.getUsuario(), usuaro.getNombre(), usuaro.getCorreo(), usuaro.getSexo(), usuaro.getFecha_Nac());

                                                            Intent i = new Intent(getApplicationContext(), MainDenarius.class);
                                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(i);
                                                            finish();
                                                        }
                                                    }
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                case "0":
                                                    //Regresar mensaje de que no hay registros
                                                    progreso.setVisibility(View.GONE);
                                                    mostrarBottom();
                                                    break;
                                                default:
                                                    progreso.setVisibility(View.GONE);
                                                    msg("Ocurrio un problema al conectarse con el sertvidor");
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
     * Funcion encargada de mostrar el BottomSheet con el mensaje de que
     * el usuario o contraseña son incorrectos
     */
    private void mostrarBottom(){
        final BottomSheetBehavior bsb = BottomSheetBehavior.from(bottomSheet);
        //funcion para expandir bottomsheet en cuanto inicia la app
        bsb.setState(BottomSheetBehavior.STATE_EXPANDED);
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
