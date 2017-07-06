package com.claresti.financeapp;

import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class Movimientos extends AppCompatActivity {

    // Declaracion de variables en el layout
    private Spinner spinerMovimiento;
    private Spinner spinerCategoria;
    private Spinner spinerCuenta;
    private EditText inputMonto;
    private RelativeLayout ventana;

    // Declaracion de variables de clases
    private BD bd;
    private ObjUsuario usuario;
    private Urls urls;

    //Declaracion de banderas de variables
    private String flagMovimiento;
    private String flagCategoria;
    private String flagCuenta;

    //Declaracion de variables para el control de bottom sheet
    private Button btnConBottomSheet;
    private LinearLayout bottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_movimientos);

        // Asignacion variables layout
        spinerMovimiento = (Spinner)findViewById(R.id.spin_tipomov);
        spinerCategoria = (Spinner)findViewById(R.id.spin_categoria);
        spinerCuenta = (Spinner)findViewById(R.id.spin_cuenta);
        inputMonto = (EditText)findViewById(R.id.input_monto);
        ventana = (RelativeLayout)findViewById(R.id.l_ventana);

        //Asignacion variables clases
        bd = new BD(getApplicationContext());
        usuario = new ObjUsuario();
        urls = new Urls();

        // Llamada a funciones para llenar los spinners
        llenarSpinerMovimiento();
        llenarSpinerCategoria();
        llenarSpinerCuenta();

        //Llamada de las variables para el control de bottomsheet
        bottomSheet = (LinearLayout)findViewById(R.id.bottomSheet);
        final BottomSheetBehavior bsb = BottomSheetBehavior.from(bottomSheet);

        ///////////////////////Aqui se debe colocar la comprobacion de si es o no la primera ves que se abre la aplicacion y cambiar el estado de la bottomsheet
        bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);

        //Control para esconder bottomsheet
        btnConBottomSheet=(Button)findViewById(R.id.btnConBottomSheet);
        btnConBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
                Intent i = new Intent(Movimientos.this, Configuracion.class);
                startActivity(i);
            }
        });
    }

    /**
     * Funcion encargada de llenar el espiner de movimientos
     */
    private void llenarSpinerMovimiento() {
        String[] movimientos = {"Ingreso", "Egreso"};
        spinerMovimiento.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, movimientos));
    }

    /**
     * Funcion encargada de llenar el espiner de categorias
     */
    private void llenarSpinerCategoria() {
    }

    /**
     * Funcion encargada de llenar el espiner de cuenta
     */
    private void llenarSpinerCuenta() {
    }
}
