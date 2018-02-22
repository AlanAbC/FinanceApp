package com.claresti.financeapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by smp_3 on 04/09/2017.
 */

public class MainDenarius  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        FragmentMovimiento.OnFragmentInteractionListener,
        FragmentMovimientos.OnFragmentInteractionListener,
        FragmentCategories.OnFragmentInteractionListener,
        FragmentAccounts.OnFragmentInteractionListener,
        FragmentAcerca.OnFragmentInteractionListener
{

    private FragmentMovimiento fragmentMovimiento;
    private FragmentMovimientos fragmentMovimientos;
    private FragmentAccounts fragmentAccounts;
    private FragmentCategories fragmentCategories;
    private FragmentAcerca fragmentAcerca;
    private UserSessionManager session;

    //Identificador para los fragments
    private int id;

    private FloatingActionButton buttonAdds;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_denarius);

        //Implementacion de toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Cambiar el color en la barra de notificaciones (Solo funciona de lollipop hacia arriba)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
            //window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.top));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        buttonAdds = (FloatingActionButton) findViewById(R.id.new_action);

        agregarListeners();

        //Manejo de Session
        session = new UserSessionManager(MainDenarius.this);

        if(!session.isUserLoggedIn()) finish();

        //Fragments

        fragmentMovimiento = new FragmentMovimiento();
        fragmentMovimientos = new FragmentMovimientos();
        fragmentCategories = new FragmentCategories();
        fragmentAccounts = new FragmentAccounts();
        fragmentAcerca = new FragmentAcerca();
        getSupportFragmentManager().beginTransaction().add(R.id.FragmentContent, fragmentMovimiento).commit();
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        id = item.getItemId();

        //Manejo de Fragments
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        if (id == R.id.opcion1)
        {
            transaction.replace(R.id.FragmentContent, fragmentMovimiento);
            buttonAdds.setVisibility(View.GONE);
        }
        else if (id == R.id.opcion2)
        {
            transaction.replace(R.id.FragmentContent, fragmentMovimientos);
            buttonAdds.setVisibility(View.GONE);
        }
        else if (id == R.id.opcion3)
        {
            transaction.replace(R.id.FragmentContent, fragmentCategories);
            buttonAdds.setVisibility(View.VISIBLE);
        }
        else if (id == R.id.opcion4)
        {
            transaction.replace(R.id.FragmentContent, fragmentAccounts);
            buttonAdds.setVisibility(View.VISIBLE);
        }
        else if (id == R.id.opcion5)
        {
        }
        else if (id == R.id.opcion6)
        {
            transaction.replace(R.id.FragmentContent, fragmentAcerca);
            buttonAdds.setVisibility(View.GONE);
        }
        else if (id == R.id.opcion7)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
            builder.setMessage("¿Deseas Cerrar Sesión?");
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    session = new UserSessionManager(MainDenarius.this);
                    session.logoutUser();
                }
            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.cancel();
                        }
                    });
            builder.show();
        }

        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    private void agregarListeners()
    {
        buttonAdds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (id == R.id.opcion3)
                {
                    Intent newCategory = new Intent(MainDenarius.this, AgregarCategoria.class);
                    startActivity(newCategory);
                }
                else if (id == R.id.opcion4)
                {
                    Intent newAccount = new Intent(MainDenarius.this, AgregarCuenta.class);
                    startActivity(newAccount);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("id", id);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        id = savedInstanceState.getInt("id");
        //Manejo de Fragments
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        if (id == R.id.opcion1)
        {
            transaction.replace(R.id.FragmentContent, fragmentMovimiento);
            buttonAdds.setVisibility(View.GONE);
        }
        else if (id == R.id.opcion2)
        {
            transaction.replace(R.id.FragmentContent, fragmentMovimientos);
            buttonAdds.setVisibility(View.GONE);
        }
        else if (id == R.id.opcion3)
        {
            transaction.replace(R.id.FragmentContent, fragmentCategories);
            buttonAdds.setVisibility(View.VISIBLE);
        }
        else if (id == R.id.opcion4)
        {
            transaction.replace(R.id.FragmentContent, fragmentAccounts);
            buttonAdds.setVisibility(View.VISIBLE);
        }
        else if (id == R.id.opcion5)
        {
        }
        else if (id == R.id.opcion6)
        {
            transaction.replace(R.id.FragmentContent, fragmentAcerca);
            buttonAdds.setVisibility(View.GONE);
        }
        else if (id == R.id.opcion7)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
            builder.setMessage("¿Deseas Cerrar Sesión?");
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    session = new UserSessionManager(MainDenarius.this);
                    session.logoutUser();
                }
            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.cancel();
                        }
                    });
            builder.show();
        }

        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}
