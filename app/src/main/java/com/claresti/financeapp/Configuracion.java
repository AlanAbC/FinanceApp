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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class Configuracion extends AppCompatActivity {

    // Declaracion de variables en el layout
    private RelativeLayout ventana;
    private ProgressBar progreso;
    private EditText inputNombre;
    private EditText inputPassword;
    private EditText inputValidPassword;
    private RadioGroup radioSexo;
    private RadioButton hombre;
    private RadioButton mujer;
    private Button btnGuardar;
    private DatePicker dateNac;


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
        setContentView(R.layout.activity_configuracion);

        // Asignacion variables layout
        ventana = (RelativeLayout)findViewById(R.id.l_ventana);
        progreso = (ProgressBar)findViewById(R.id.progress);
        inputNombre = (EditText)findViewById(R.id.input_name);
        inputPassword = (EditText)findViewById(R.id.input_password);
        inputValidPassword = (EditText)findViewById(R.id.input_valid_password);
        radioSexo = (RadioGroup)findViewById(R.id.rg_sex);
        btnGuardar = (Button)findViewById((R.id.btn_ingresar));
        dateNac = (DatePicker)findViewById(R.id.date_fechaNac);
        hombre = (RadioButton)findViewById(R.id.radio_masculino);
        mujer = (RadioButton)findViewById(R.id.radio_femenino);

        //Asignacion variables clases
        bd = new BD(getApplicationContext());
        usuario = bd.slectUsuario();
        urls = new Urls();

        // Insercion de los valores registrados del usuario en el layout
        inputNombre.setHint(usuario.getNombre());
        if(usuario.getSexo().equals("1")){
            hombre.setChecked(true);
        }else{
            mujer.setChecked(true);
        }
        String[] fechaSep = usuario.getFecha_Nac().split("-");
        Log.i("hola", fechaSep[0] + fechaSep[1] + fechaSep[2]);
        dateNac.init(Integer.parseInt(fechaSep[0]), Integer.parseInt(fechaSep[1]), Integer.parseInt(fechaSep[2]), null);

        //Menu, Inicia las variables del menu y llama la funcion encargada de su manipulacion
        drawerLayout = (DrawerLayout) findViewById(R.id.dLayout);
        nav = (NavigationView)findViewById(R.id.navigation);
        menu = nav.getMenu();
        menuNav();

        //Ocultar teclado al iniciar la activity
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * Funcion que da funcionalidad al menu
     */
    private void menuNav(){
        for(int i = 0; i < menu.size(); i++){
            items.add(menu.getItem(i));
        }
        items.get(5).setChecked(true);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                int pos = items.indexOf(item);
                if(pos == 0){
                    Intent i = new Intent(Configuracion.this, Movimientos.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }else if(pos == 1){
                    Intent i = new Intent(Configuracion.this, AgregarCategoria.class);
                    startActivity(i);
                }else if(pos == 2){
                    Intent i = new Intent(Configuracion.this, AgregarCuenta.class);
                    startActivity(i);
                }else if(pos == 3) {
                    Intent i = new Intent(Configuracion.this, DespliegueMovimientos.class);
                    startActivity(i);
                }else if(pos == 4){
                    Intent i = new Intent(Configuracion.this, Estadisticas.class);
                    startActivity(i);
                }else if(pos == 5){

                }else if(pos == 6){
                    Intent i = new Intent(Configuracion.this, Acerca.class);
                    startActivity(i);
                }else if(pos == 7){
                    if(bd.LogoutUsuario(usuario.getIdUsuario()).equals("1")){
                        Intent i = new Intent(Configuracion.this, Login.class);
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
