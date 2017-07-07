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
import android.view.Menu;
import android.view.MenuItem;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.ProgressBar;
        import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class Estadisticas extends AppCompatActivity {

    // Declaracion de variables en el layout
    private RelativeLayout ventana;
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
        setContentView(R.layout.activity_estadisticas);

        // Asignacion variables layout
        ventana = (RelativeLayout)findViewById(R.id.l_ventana);
        progreso = (ProgressBar)findViewById(R.id.progress);

        //Asignacion variables clases
        bd = new BD(getApplicationContext());
        usuario = bd.slectUsuario();
        urls = new Urls();

        //Menu, Inicia las variables del menu y llama la funcion encargada de su manipulacion
        drawerLayout = (DrawerLayout) findViewById(R.id.dLayout);
        nav = (NavigationView)findViewById(R.id.navigation);
        menu = nav.getMenu();
        menuNav();
    }

    /**
     * Funcion que da funcionalidad al menu
     */
    private void menuNav(){
        for(int i = 0; i < menu.size(); i++){
            items.add(menu.getItem(i));
        }
        items.get(4).setChecked(true);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                int pos = items.indexOf(item);
                if(pos == 0){
                    Intent i = new Intent(Estadisticas.this, Movimientos.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }else if(pos == 1){
                    Intent i = new Intent(Estadisticas.this, AgregarCategoria.class);
                    startActivity(i);
                }else if(pos == 2){
                    Intent i = new Intent(Estadisticas.this, AgregarCuenta.class);
                    startActivity(i);
                }else if(pos == 3) {
                    Intent i = new Intent(Estadisticas.this, DespliegueMovimientos.class);
                    startActivity(i);
                }else if(pos == 4){

                }else if(pos == 5){
                    Intent i = new Intent(Estadisticas.this, Configuracion.class);
                    startActivity(i);
                }else if(pos == 6){
                    Intent i = new Intent(Estadisticas.this, Acerca.class);
                    startActivity(i);
                }else if(pos == 7){
                    if(bd.LogoutUsuario(usuario.getIdUsuario()).equals("1")){
                        Intent i = new Intent(Estadisticas.this, Login.class);
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
