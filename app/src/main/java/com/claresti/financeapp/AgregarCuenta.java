package com.claresti.financeapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.Map;

public class AgregarCuenta extends AppCompatActivity {

    // Declaracion de variables en el layout
    private RelativeLayout ventana;
    private EditText inputCuenta;
    private EditText inputDescripcion;
    private Button btnRegistrarCategoria;
    private ProgressBar progreso;

    //Declaracion de variables para la comunicacion con la API
    private Comunications com;
    private UserSessionManager session;
    private HashMap<String,String> user;

    //ProgressDialog
    private ProgressDialogDenarius progressDialog;

    private int flagTipoMovimiento = 1;
    private ObjCuenta account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_agregar_cuenta);

        //Cambiar el color en la barra de notificaciones (Solo funciona de lollipop hacia arriba)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }

        // Asignacion variables layout
        ventana = (RelativeLayout)findViewById(R.id.l_ventana);
        inputCuenta = (EditText)findViewById(R.id.input_nombreCat);
        inputDescripcion = (EditText)findViewById(R.id.input_descripcionCat);
        btnRegistrarCategoria = (Button)findViewById(R.id.btn_registrar);
        progreso = (ProgressBar)findViewById(R.id.progress);

        progressDialog = new ProgressDialogDenarius(this);

        // Llamada a funciones para llenar los spinners y crear los listenrs
        crearListeners();

        //Ocultar teclado al iniciar la activity
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //inicializamos datos de sesion y comunicaciones
        session = new UserSessionManager(getApplicationContext());
        user = session.getUserDetails();
        com = new Comunications(getApplicationContext(), ventana);

        if(getIntent().hasExtra("Account"))
        {
            flagTipoMovimiento = 2;
            modificarAccount();
        }
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

        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(progressDialog.getState())
                {
                    finish();
                }
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
            progressDialog.show();
            Map<String, String> paramsMovements = new HashMap<String, String>();
            paramsMovements.put("name", inputCuenta.getText().toString());
            paramsMovements.put("description", inputDescripcion.getText().toString());

            if(flagTipoMovimiento == 2)
            {
                paramsMovements.put("id", account.getID());
                com.newRegister(Urls.UPDATEACCOUNT, paramsMovements, progressDialog);
            }
            else
            {
                paramsMovements.put("idUser",user.get(UserSessionManager.KEY_ID));
                com.newRegister(Urls.NEWACCOUNT, paramsMovements, progressDialog);
            }
        }
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

    private void modificarAccount()
    {
        account = (ObjCuenta) getIntent().getExtras().getSerializable("Account");

        inputCuenta.setText(account.getNombre());
        inputDescripcion.setText(account.getDescripcion());
        btnRegistrarCategoria.setText("Modificar");
    }
}
