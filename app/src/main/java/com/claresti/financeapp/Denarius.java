package com.claresti.financeapp;

import android.app.Application;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by CLARESTI on 01/08/2017.
 */

public class Denarius extends AppCompatActivity
{
    private DrawerLayout drawerLayout;
    private NavigationView nav;
    private Menu menu;
    private ImageView btnMenu;
    final List<MenuItem> items = new ArrayList<>();

    private View view;
    private FloatingActionButton new_action;

    private RecyclerView recyclerView;

    //Adaptadores
    private AdapterMovements adapterMovements;
    private AdapterCategories adapterCategories;
    private AdapterAccounts adapterAccounts;
    private int flagAdaptador = 0;

    //TextView del titulo de la actividad
    private TextView title;

    //ProgressBar's
    private ProgressBar progressBar;
    private ProgressBar progressBarLoading;

    //variables para carga de items y actualizaciones
    private int lastVisibleItem, totalItemCount;
    private LinearLayoutManager linearLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //Cambiar el color en la barra de notificaciones (Solo funciona de lollipop hacia arriba)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.top));
            window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.top));
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.dLayout);
        nav = (NavigationView) findViewById(R.id.navigation);
        menu = nav.getMenu();

        //Asignacion del header menu en una bariable
        View headerview = nav.getHeaderView(0);

        //Funcionalidad del boton de menu, funcionalidad y titulo de la aplicacion
        title = (TextView) findViewById(R.id.title_denarius);
        btnMenu = (ImageView) findViewById(R.id.Btnmenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(nav);
            }
        });
        menuNav();

        //Manejo de Session
        UserSessionManager session = new UserSessionManager(getApplicationContext());

        if(!session.isUserLoggedIn()) finish();

        new_action = (FloatingActionButton) findViewById(R.id.new_action);




        recyclerView = (RecyclerView) findViewById(R.id.main_recycler);
        view = (RelativeLayout) findViewById(R.id.l_ventana);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        linearLayout = (LinearLayoutManager) recyclerView.getLayoutManager();

        //Configuraciones del SwipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        progressBar = (ProgressBar) findViewById(R.id.progressDenarius);
        progressBarLoading = (ProgressBar) findViewById(R.id.progressDenariusLoading);
        asignarListeners();

        asignarAdaptadores();

        setUpRecyclerSwipe();
        setUpAnimationDecoratorHelper();


        NoAnimationItemAnimator noAnimationItemAnimator = new NoAnimationItemAnimator();
        recyclerView.setItemAnimator(noAnimationItemAnimator);
    }

    /**
     * Funcion en la que se asignaran los listener a os objetos que se requieran
     */
    private void asignarListeners()
    {
        new_action.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                switch (flagAdaptador)
                {
                    case 0:
                        Intent newMovement = new Intent(Denarius.this, Movimientos.class);
                        startActivity(newMovement);
                        break;
                    case 1:
                        Intent newCategory = new Intent(Denarius.this, AgregarCategoria.class);
                        startActivity(newCategory);
                        break;
                    case 2:
                        Intent newAccount = new Intent(Denarius.this, AgregarCuenta.class);
                        startActivity(newAccount);
                        break;
                }
            }
        });

        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayout.getItemCount();
                lastVisibleItem = linearLayout.findLastCompletelyVisibleItemPosition();
                switch (flagAdaptador)
                {
                    case 0:
                        if(!AdapterMovements.isLoading && totalItemCount == (lastVisibleItem + 1))
                        {
                            adapterMovements.loadMoreItems(progressBarLoading);
                        }
                        break;
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (flagAdaptador)
                {
                    case 0:
                        if(!AdapterMovements.isLoading)
                        {
                            adapterMovements.updateContent(progressBar);
                        }
                        break;
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void setUpRecyclerSwipe()
    {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
        {
            Drawable background = new ColorDrawable(Color.parseColor("#185e1f"));

            boolean isSwiped = false;

            public void drawSwipedColor()
            {
                background = new ColorDrawable(Color.parseColor("#185e1f"));
            }

            public void drawWhiteColor()
            {
                background = new ColorDrawable(Color.WHITE);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                Log.i("MOV", viewHolder.itemView.getRight() + "");
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if(isSwiped)
                {
                    Snackbar.make(view, "blanco", Snackbar.LENGTH_SHORT).show();
                    isSwiped = false;
                    adapterMovements.setSwipedPosition(-1);
                }
                else
                {
                    Snackbar.make(view, "Verde", Snackbar.LENGTH_SHORT).show();
                    isSwiped = true;
                    adapterMovements.setSwipedPosition(position);
                }

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    Toast.makeText(Denarius.this, "lel", Toast.LENGTH_SHORT).show();
                }

                if(isSwiped)
                {
                    drawWhiteColor();
                }
                else
                {
                    drawSwipedColor();
                }


                // draw red background
                if(((int) dX) < 0)
                {
                    background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                    background.draw(c);
                }
                else
                {
                    background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + (int) dX, itemView.getBottom());
                    background.draw(c);
                }


                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
     * after an item is removed.
     */
    private void setUpAnimationDecoratorHelper() {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background = new ColorDrawable(Color.RED);

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }


    /**
     * Funcion para inicializar u obtener la instancia de los adaptadores que contendran los datos de la aplicacion
     */
    private void asignarAdaptadores()
    {
        adapterMovements = AdapterMovements.getInstance(getApplicationContext(), view);
        adapterCategories = AdapterCategories.getInstance(getApplicationContext(), view);
        adapterAccounts = AdapterAccounts.getInstance(getApplicationContext(), view);
        adapterMovements.updateContent(progressBar);
        recyclerView.setAdapter(adapterMovements);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filtros, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    /**
     * Funcion que da funcionalidad al menu
     */
    private void menuNav(){
        for(int i = 0; i < menu.size(); i++){
            items.add(menu.getItem(i));
        }
        items.get(0).setChecked(true);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                int pos = items.indexOf(item);
                if(pos == 0)
                {
                    title.setText("Movimientos");
                    adapterMovements.updateContent(progressBar);
                    recyclerView.setAdapter(adapterMovements);
                    recyclerView.invalidate();
                    flagAdaptador = 0;
                }else if(pos == 1){
                    title.setText("Categorias");
                    adapterCategories.updateContent(progressBar);
                    recyclerView.setAdapter(adapterCategories);
                    recyclerView.invalidate();
                    flagAdaptador = 1;
                }else if(pos == 2){
                    title.setText("Cuentas");
                    adapterAccounts.updateContent(progressBar);
                    recyclerView.setAdapter(adapterAccounts);
                    recyclerView.invalidate();
                    flagAdaptador = 2;
                }else if(pos == 3) {
                    Snackbar.make(view, "Movimientos4", Snackbar.LENGTH_SHORT).show();
                }else if(pos == 4){
                    Snackbar.make(view, "Movimientos5", Snackbar.LENGTH_SHORT).show();
                }else if(pos == 5){
                    Snackbar.make(view, "Movimientos6", Snackbar.LENGTH_SHORT).show();
                }else if(pos == 6){
                    Snackbar.make(view, "Movimientos7", Snackbar.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(nav);
                return false;
            }
        });

        //Asignacion del header menu en una variable
        View headerview = nav.getHeaderView(0);

        //Funcionalidad del boton de menu
        btnMenu = (ImageView)findViewById(R.id.Btnmenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(nav);
            }
        });
    }

    //SELECT * FROM movimiento where movimiento.idCuenta = 20 and ID < 40 ORDER by fecha DESC LIMIT 0, 5 - Consulta para la carga de datos :v

}
