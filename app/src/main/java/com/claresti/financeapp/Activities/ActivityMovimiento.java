package com.claresti.financeapp.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.claresti.financeapp.Adapters.AdaptadorSpinnerAccounts;
import com.claresti.financeapp.Adapters.AdapterCategoriesDialog;
import com.claresti.financeapp.Dialogs.DialogProgress;
import com.claresti.financeapp.Modelos.Categoria;
import com.claresti.financeapp.Modelos.Cuenta;
import com.claresti.financeapp.R;
import com.claresti.financeapp.Tools.Comunicaciones;
import com.claresti.financeapp.Tools.Urls;
import com.claresti.financeapp.Tools.UserSessionManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ActivityMovimiento extends AppCompatActivity implements AdapterCategoriesDialog.OnSelectedItem {

    private Categoria categoria;
    // Declaracion de variables en el layout
    private Spinner spinerCuenta;
    private Spinner spinerAccountTransfer;
    private EditText inputMonto;
    private EditText dateFechaMovimiento;
    private EditText conceptoMovimiento;
    private EditText categoriaMovimiento;
    private ImageView iconoCategoria;
    private ImageButton calendarPicker;
    private Button btnRegistrarMovimiento;
    private TabLayout tabLayout;
    private TextView textTransfer;
    private static final String TAG = "MOVIMIENTO";

    AdapterCategoriesDialog adapterCategories;
    Dialog dialog;

    // Declaracion variables para el datapikerdialog
    private int ano;
    private int mes;
    private int dia;

    //Declaracion de variables para la comunicacion con la API
    private UserSessionManager session;
    private HashMap<String,String> user;

    //Contexto y vista
    private View vista;

    private int flagMovimiento = 1;

    private AdaptadorSpinnerAccounts adapterAccounts, adapterAccountsTransfer;

    private DialogProgress progress;

    private JSONObject json;

    private Comunicaciones.ResultadosInterface resultadosInterfaceListener = new Comunicaciones.ResultadosInterface() {
        @Override
        public void mostrarDatos(JSONObject json) {
            progress.dismiss();
            limpiarFormulario();
        }

        @Override
        public void setError(String mensaje) {
            progress.dismiss();
            msg(mensaje);
        }
    };

    /**
     * listener para actualizar categorias, solo los primeros resultados
     */
    private Comunicaciones.ResultadosInterface listenerComunicaciones = new Comunicaciones.ResultadosInterface() {
        @Override
        public void mostrarDatos(JSONObject json) {
            if(json.has("categories")){
                try {
                    Gson gson = new Gson();
                    final ArrayList<Categoria> categoriesArrayList = new ArrayList<Categoria>(Arrays.asList(gson.fromJson(json.getString("categories"), Categoria[].class)));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterCategories.setCategorias(categoriesArrayList);
                        }
                    });
                } catch(JSONException jsone) {
                    Log.e(TAG, jsone.getMessage());
                    msg(getString(R.string.comunicaciones_error));
                }
            }
        }

        @Override
        public void setError(String mensaje) {
            Log.e(TAG, mensaje);
            msg(mensaje);
        }
    };

    private Comunicaciones.ResultadosInterface listenerMovements = new Comunicaciones.ResultadosInterface() {
        @Override
        public void mostrarDatos(final JSONObject json) {
            if(json.has("accounts")){
                try {
                    Gson gson = new Gson();
                    final ArrayList<Cuenta> arrayCuenta = new ArrayList<Cuenta>(Arrays.asList(gson.fromJson(json.getString("accounts"), Cuenta[].class)));
                    final ArrayList<Cuenta> arrayCuentaTransfer = new ArrayList<Cuenta>(Arrays.asList(gson.fromJson(json.getString("accounts"), Cuenta[].class)));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterAccounts.setElementos(arrayCuenta);
                            adapterAccountsTransfer.setElementos(arrayCuentaTransfer);
                        }
                    });
                } catch(JSONException jsone) {
                    Log.e(TAG, jsone.getMessage());
                    msg(getString(R.string.comunicaciones_error));
                }
            }
        }

        @Override
        public void setError(String mensaje) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimiento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new UserSessionManager(this);

        // Asignacion variables layout
        spinerCuenta = findViewById(R.id.spin_cuenta);
        spinerAccountTransfer = findViewById(R.id.spin_accountTransfer);
        inputMonto = findViewById(R.id.input_monto);
        dateFechaMovimiento = findViewById(R.id.input_fecha);
        conceptoMovimiento = findViewById(R.id.input_concepto);
        iconoCategoria = findViewById(R.id.movimiento_categoria_icono);
        btnRegistrarMovimiento = findViewById(R.id.btn_registrar);
        calendarPicker = findViewById(R.id.calendar);
        textTransfer = findViewById(R.id.text_cuenta_destino);
        tabLayout = findViewById(R.id.tab_layout);
        categoriaMovimiento = findViewById(R.id.input_categoria);
        vista = findViewById(R.id.movimiento_content);


        crearListeners();
    }

    /**
     * Funcion encargada de crear los listeners de los objetos del layout
     */
    private void crearListeners() {

        tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                Log.i(TAG, tabLayout.getSelectedTabPosition() + "");
                flagMovimiento = tabLayout.getSelectedTabPosition() + 1;
                switch (flagMovimiento){
                    case 1:
                        spinerAccountTransfer.setVisibility(View.GONE);
                        textTransfer.setVisibility(View.GONE);
                        break;
                    case 2:
                        spinerAccountTransfer.setVisibility(View.GONE);
                        textTransfer.setVisibility(View.GONE);
                        break;
                    case 3:
                        spinerAccountTransfer.setVisibility(View.VISIBLE);
                        textTransfer.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.grey_60), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Datos para el datepikerdialog
        Calendar c = Calendar.getInstance();
        ano = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.DAY_OF_MONTH);
        String fecha = ano + "-" + (mes + 1) + "-" + dia;
        dateFechaMovimiento.setText(fecha);

        // Llamada a funciones para llenar los spinners y crear los listenrs
        session = new UserSessionManager(this);
        user = session.getUserDetails();

        //Agregamos listener para saber si el EditText de fecha tiene el focus
        dateFechaMovimiento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    setFechaMovimiento();
                }
            }
        });

        dateFechaMovimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFechaMovimiento();
            }
        });

        categoriaMovimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategoriesDialog();
            }
        });

        categoriaMovimiento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    showCategoriesDialog();
                }
            }
        });

        calendarPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFechaMovimiento();
            }
        });

        //Ocultar teclado al iniciar el fragment
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        btnRegistrarMovimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarFormulario()){
                    progress = new DialogProgress();
                    progress.show(ActivityMovimiento.this.getFragmentManager(), "DialogProgress");
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");

                    Comunicaciones com = new Comunicaciones(ActivityMovimiento.this, resultadosInterfaceListener);

                    com.peticionJSON(
                            Urls.REGISTERMOVEMENT,
                            Request.Method.POST,
                            json,
                            headers
                    );
                }
            }
        });

        adapterAccounts = new AdaptadorSpinnerAccounts(this);
        adapterAccountsTransfer = new AdaptadorSpinnerAccounts(this);
        spinerCuenta.setAdapter(adapterAccounts);
        spinerAccountTransfer.setAdapter(adapterAccountsTransfer);

        updateMovements();

    }

    @Override
    public void itemSelected(Categoria item) {
        categoria = item;
        categoriaMovimiento.setText(categoria.getName());
        Drawable drawable = this.getResources().getDrawable(R.drawable.circular_image_view);
        drawable.clearColorFilter();
        ColorFilter filter = new LightingColorFilter( Color.parseColor(categoria.getCategory_color()), Color.parseColor(categoria.getCategory_color()));
        drawable.setColorFilter(filter);
        iconoCategoria.setBackground(drawable);
        dialog.dismiss();
    }

    /**
     * funcion encargada de establecer la fecha en el campo de fecha de movimiento y ocultar el teclado al mostrar el dialog
     */
    private void setFechaMovimiento() {
        //Ocultamos el Teclado
        dateFechaMovimiento.setInputType(InputType.TYPE_NULL);
        InputMethodManager inputMethodManager =  (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(dateFechaMovimiento.getWindowToken(), 0);

        DatePickerDialog dpd = new DatePickerDialog(this, R.style.Calendar, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(year <= ano && month <= mes && dayOfMonth <= dia) {
                    dateFechaMovimiento.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                }
            }
        }, ano, mes, dia);
        dpd.show();
        Button ok = dpd.getButton(DialogInterface.BUTTON_POSITIVE);
        ok.setTextColor(Color.parseColor("#949494"));
        Button cancel = dpd.getButton(DialogInterface.BUTTON_NEGATIVE);
        cancel.setTextColor(Color.parseColor("#949494"));
    }

    /**
     * Funcion encargada de validar el formulario, en caso de que algun dato oblicatorio sea vacio
     * regresa mensaje de dato faltante, en caso contrario ejectuta realizarRegistro
     */
    private boolean validarFormulario() {
        json = new JSONObject();
        String concepto = conceptoMovimiento.getText().toString();
        String fecha = dateFechaMovimiento.getText().toString();

        if(TextUtils.isEmpty(inputMonto.getText().toString())){
            inputMonto.setError(getString(R.string.error_input_vacio));
            return false;
        }
        int monto = Integer.parseInt(inputMonto.getText().toString());

        if(categoria == null){
            categoriaMovimiento.setError(getString(R.string.error_input_vacio));
            return false;
        }

        if(flagMovimiento == 3) {
            if(spinerCuenta.getSelectedItemId() == spinerAccountTransfer.getSelectedItemId()) {
                msg(getString(R.string.error_misma_cuenta));
                return false;
            }
        }

        try{
            json.put("user_id", user.get(UserSessionManager.KEY_ID));
            json.put("amount", monto);
            json.put("concept", concepto);
            json.put("category_id", categoria.getId());
            json.put("date", fecha);
            json.put("type", flagMovimiento);
            json.put("account_id", spinerCuenta.getSelectedItemId());
            if(flagMovimiento == 3) {
                json.put("account_transfer_id", spinerAccountTransfer.getSelectedItemId());
            } else {
                json.put("account_transfer_id", "");
            }
        } catch(JSONException jsone) {
            Log.e(TAG, jsone.getMessage());
            return false;
        }
        Log.i(TAG, json.toString());
        return true;
    }

    private void msg(String msg){
        Snackbar.make(vista, msg, Snackbar.LENGTH_LONG).setAction("Aceptar", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    public void updateCategories(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        Comunicaciones com = new Comunicaciones(this, listenerComunicaciones);

        com.peticionJSON(
                Urls.VIEWUSERCATEGORIES + session.getUserDetails().get(UserSessionManager.KEY_ID),
                Request.Method.GET,
                new JSONObject(),
                headers
        );
    }

    public void updateMovements(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        Comunicaciones com = new Comunicaciones(this, listenerMovements);

        com.peticionJSON(
                Urls.VIEWUSERACCOUNTS + session.getUserDetails().get(UserSessionManager.KEY_ID),
                Request.Method.GET,
                new JSONObject(),
                headers
        );
    }

    private void showCategoriesDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_categorias);
        if(dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);



        RecyclerView contenido = dialog.findViewById(R.id.dialog_recycler_categorias);
        Button buttonCancel = dialog.findViewById(R.id.dialog_categorias_button);
        adapterCategories = new AdapterCategoriesDialog(dialog.getContext());
        adapterCategories.setListener(this);
        contenido.setAdapter(adapterCategories);
        contenido.setLayoutManager(new LinearLayoutManager(this));
        updateCategories();
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void limpiarFormulario(){
        inputMonto.setText("");
        conceptoMovimiento.setText("");
        iconoCategoria.setBackground(getResources().getDrawable(R.drawable.icono_categoria));
        categoriaMovimiento.setText("");
        categoria = null;
        spinerCuenta.setSelection(0);
        spinerAccountTransfer.setSelection(0);

    }

}
