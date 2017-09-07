package com.claresti.financeapp;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by smp_3 on 04/09/2017.
 */

public class MainDenarius  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentMovimiento.OnFragmentInteractionListener, FragmentMovimientos.OnFragmentInteractionListener, FragmentCategories.OnFragmentInteractionListener, FragmentAccounts.OnFragmentInteractionListener, FragmentAcerca.OnFragmentInteractionListener
{

    FragmentMovimiento fragmentMovimiento;
    FragmentMovimientos fragmentMovimientos;
    FragmentAccounts fragmentAccounts;
    FragmentCategories fragmentCategories;
    FragmentAcerca fragmentAcerca;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_denarius);

        //Implementacion de toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Cambiar el color en la barra de notificaciones (Solo funciona de lollipop hacia arriba)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.top));
            //window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.top));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Manejo de Fragments
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        if (id == R.id.opcion1)
        {
            transaction.replace(R.id.FragmentContent, fragmentMovimiento);
        }
        else if (id == R.id.opcion2)
        {
            transaction.replace(R.id.FragmentContent, fragmentMovimientos);
        }
        else if (id == R.id.opcion3)
        {
            transaction.replace(R.id.FragmentContent, fragmentCategories);
        }
        else if (id == R.id.opcion4)
        {
            transaction.replace(R.id.FragmentContent, fragmentAccounts);
        }
        else if (id == R.id.opcion5)
        {

        }
        else if (id == R.id.opcion6)
        {
            transaction.replace(R.id.FragmentContent, fragmentAcerca);
        }
        else if (id == R.id.opcion7)
        {

        }

        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
