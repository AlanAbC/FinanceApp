package com.claresti.financeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AgregarCuenta extends AppCompatActivity {

    // Declaracion de variables en el layout
    private RelativeLayout ventana;
    private EditText inputCuenta;
    private EditText inputDescripcion;
    private Button btnRegistrarCategoria;
    private ProgressBar progreso;

    // Declaracion de variables de clases
    private BD bd;
    private ObjUsuario usuario;
    private Urls urls;

    //Menu, Declaracion de variables
    private DrawerLayout drawerLayout;
    final List<MenuItem> items = new ArrayList<>();
    private Menu menu;
    private ImageView btnMenu;
    private NavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_agregar_cuenta);

        // Asignacion variables layout
        ventana = (RelativeLayout)findViewById(R.id.l_ventana);
        inputCuenta = (EditText)findViewById(R.id.input_nombreCat);
        inputDescripcion = (EditText)findViewById(R.id.input_descripcionCat);
        btnRegistrarCategoria = (Button)findViewById(R.id.btn_registrar);
        progreso = (ProgressBar)findViewById(R.id.progress);

        // Asignacion variables clases
        bd = new BD(getApplicationContext());
        usuario = bd.slectUsuario();
        urls = new Urls();

        // Llamada a funciones para llenar los spinners y crear los listenrs
        crearListeners();

        //Menu, Inicia las variables del menu y llama la funcion encargada de su manipulacion
        drawerLayout = (DrawerLayout) findViewById(R.id.dLayout);
        nav = (NavigationView)findViewById(R.id.navigation);
        menu = nav.getMenu();
        menuNav();

        //Ocultar teclado al iniciar la activity
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * Funcion encargada de crear los listeners de los objetos del layout
     */
    private void crearListeners() {
        btnRegistrarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarFormulario();
            }
        });
    }

    /**
     * Funcion encargada de validar el formulario, en caso de que algun dato obligatorio sea vacio
     * regresa mensaje de dato faltante, en caso contrario ejectuta reliarRegistro
     */
    private void validarFormulario() {
        if(inputCuenta.getText().toString().equals("")){
            msg("Ingrese el nombre de la nueva categoria");
        }else if(inputDescripcion.getText().toString().equals("")){
            msg("Ingrese una descripcion para la nueva categoria");
        }else{
            realizarRegistro();
        }
    }

    /**
     * Funcion encargada de mandar la informacion del formulario y crear una categoria
     * en caso correcto mostrara mensaje de operecion correcta y en caso contrario
     * mostrara el mensaje del servidor con el error
     */
    private void realizarRegistro() {
        progreso.setVisibility(View.VISIBLE);
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(AgregarCuenta.this).
                addToRequestQueue(
                        request = new JsonObjectRequest(
                                Request.Method.GET,
                                urls.getSetCuenta() + "idU=" + usuario.getIdUsuario() +
                                        "&name=" + inputCuenta.getText() +
                                        "&dsc=" + inputDescripcion.getText(),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res = response.getString("estado");
                                            switch(res){
                                                case "1":
                                                    msg("Se registro correctamente la cuenta");
                                                    inputCuenta.setText("");
                                                    inputDescripcion.setText("");
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
     * Funcion que da funcionalidad al menu
     */
    private void menuNav(){
        for(int i = 0; i < menu.size(); i++){
            items.add(menu.getItem(i));
        }
        items.get(2).setChecked(true);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                int pos = items.indexOf(item);
                if(pos == 0){
                    Intent i = new Intent(AgregarCuenta.this, Movimientos.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }else if(pos == 1){
                    Intent i = new Intent(AgregarCuenta.this, AgregarCategoria.class);
                    startActivity(i);
                }else if(pos == 2){

                }else if(pos == 3) {
                    Intent i = new Intent(AgregarCuenta.this, DespliegueMovimientos.class);
                    startActivity(i);
                }else if(pos == 4){
                    Intent i = new Intent(AgregarCuenta.this, Estadisticas.class);
                    startActivity(i);
                }else if(pos == 5){
                    Intent i = new Intent(AgregarCuenta.this, Configuracion.class);
                    startActivity(i);
                }else if(pos == 6){
                    Intent i = new Intent(AgregarCuenta.this, Acerca.class);
                    startActivity(i);
                }else if(pos == 7){
                    if(bd.LogoutUsuario(usuario.getIdUsuario()).equals("1")){
                        Intent i = new Intent(AgregarCuenta.this, Login.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                }
                drawerLayout.closeDrawer(nav);
                item.setChecked(false);
                return false;
            }
        });

        //Asignacion del header menu en una bariable
        View headerview = nav.getHeaderView(0);

        //Toma la imagen de ususario, la redondea y la coloca nuevamente
        ImageView imgUsuario = (ImageView)headerview.findViewById(R.id.img_Usuario);
        Drawable imgOriginal = imgUsuario.getDrawable(); //getResources().getDrawable(R.drawable.fondo3);
        Bitmap bitOriginal = ((BitmapDrawable) imgOriginal).getBitmap();
        RoundedBitmapDrawable rounderDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitOriginal);
        rounderDrawable.setCornerRadius(bitOriginal.getHeight());
        imgUsuario.setImageDrawable(rounderDrawable);

        //Funcionalidad del boton de menu
        btnMenu = (ImageView)findViewById(R.id.Btnmenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(nav);
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
