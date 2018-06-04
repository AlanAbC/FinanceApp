package com.claresti.financeapp;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.claresti.financeapp.Fragments.FragmentAccount;
import com.claresti.financeapp.Fragments.FragmentAccounts;
import com.claresti.financeapp.Fragments.FragmentAcerca;
import com.claresti.financeapp.Fragments.FragmentCategories;
import com.claresti.financeapp.Fragments.FragmentEstadisticas;
import com.claresti.financeapp.Fragments.FragmentMovimiento;
import com.claresti.financeapp.Fragments.CategoryFragment;
import com.claresti.financeapp.Tools.UserSessionManager;

/**
 * Created by smp_3 on 04/09/2017.
 */

public class MainDenarius  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        FragmentMovimiento.OnFragmentInteractionListener,
        FragmentMovimientos.OnFragmentInteractionListener,
        FragmentCategories.OnFragmentInteractionListener,
        FragmentAccounts.OnFragmentInteractionListener,
        FragmentAcerca.OnFragmentInteractionListener,
        CategoryFragment.OnFragmentInteractionListener,
        FragmentAccount.OnFragmentInteractionListener,
        FragmentEstadisticas.OnFragmentInteractionListener
{

    private FragmentMovimiento fragmentMovimiento;
    private FragmentMovimientos fragmentMovimientos;
    private FragmentAccounts fragmentAccounts;
    private FragmentCategories fragmentCategories;
    private FragmentAcerca fragmentAcerca;
    private FragmentEstadisticas fragmentEstadisticas;
    private UserSessionManager session;
    private static final String TAGCAT = "CATEGORIA";

    //Identificador para los fragments
    private int id;

    private FloatingActionButton buttonAdds;

    private FragmentManager.OnBackStackChangedListener mOnBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            syncActionBarArrowState();
        }
    };
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private boolean mToolBarNavigationListenerIsRegistered = false;

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

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        buttonAdds = findViewById(R.id.new_action);

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
        fragmentEstadisticas = new FragmentEstadisticas();
        getSupportFragmentManager().addOnBackStackChangedListener(mOnBackStackChangedListener);
        getSupportFragmentManager().beginTransaction().add(R.id.FragmentContent, fragmentMovimiento, "FragmentMovimiento").commit();
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
            transaction.replace(R.id.FragmentContent, FragmentCategories.newInstance());
            buttonAdds.setVisibility(View.VISIBLE);
        }
        else if (id == R.id.opcion4)
        {
            transaction.replace(R.id.FragmentContent, fragmentAccounts);
            buttonAdds.setVisibility(View.VISIBLE);
        }
        else if (id == R.id.opcion5)
        {
            transaction.replace(R.id.FragmentContent, fragmentEstadisticas);
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
                    replaceFragmentWithAnimationToBackStack(CategoryFragment.newInstance(), "CategoryFragment");
                }
                else if (id == R.id.opcion4)
                {
                    replaceFragmentWithAnimationToBackStack(FragmentAccount.newInstance(), "FragmentAccount");
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
            transaction.replace(R.id.FragmentContent, fragmentEstadisticas);
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

    /**
     * funcion para mostrar o no la flecha de atras o icono de hamburguesa
     */
    private void syncActionBarArrowState() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            buttonAdds.setVisibility(View.GONE);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if(!mToolBarNavigationListenerIsRegistered) {
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Doesn't have to be onBackPressed
                        onBackPressed();
                    }
                });

                mToolBarNavigationListenerIsRegistered = true;
            }
        }
        else {
            buttonAdds.setVisibility(View.VISIBLE);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;
        }
    }//syncActionBarArrowState

    public void replaceFragmentWithAnimationToBackStack(android.support.v4.app.Fragment fragment, String tag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.FragmentContent, fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }
}
