package com.claresti.financeapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.claresti.financeapp.Adapters.AdaptadorSpinnerCategorias;
import com.claresti.financeapp.Dialogs.DialogCategorias;
import com.claresti.financeapp.Modelos.Categorias;
import com.claresti.financeapp.Tools.Comunicaciones;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class FragmentMovimiento extends Fragment {

    private OnFragmentInteractionListener mListener;

    private DialogCategorias dialogCategorias;
    private Categorias categorias;

    // Declaracion de variables en el layout
    private Spinner spinerCuenta;
    private Spinner spinerAccountTransfer;
    private EditText inputMonto;
    private EditText dateFechaMovimiento;
    private EditText conceptoMovimiento;
    private EditText categoriaMovimiento;
    private ImageButton calendarPicker;
    private Button btnRegistrarMovimiento;
    private TabLayout tabLayout;
    private TextView textTransfer;
    private static final String TAG = "MOVIMIENTO";

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

    private AdaptadorSpinnerCategorias.OnItemSelectedListener itemSelectedListener = new AdaptadorSpinnerCategorias.OnItemSelectedListener() {
        @Override
        public void itemSelected(Categorias categoria) {
            categoriaMovimiento.setText(categoria.getNombre());
            categorias = categoria;
            dialogCategorias.dismiss();
        }
    };

    private Comunicaciones.ResultadosInterface resultadosInterfaceListener = new Comunicaciones.ResultadosInterface() {
        @Override
        public void mostrarDatos(JSONObject json) {

        }

        @Override
        public void mostrarDatos(String response) {

        }

        @Override
        public void setError(String mensaje) {

        }
    };


    public FragmentMovimiento() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragmentMovimiento.
     */
    public static FragmentMovimiento newInstance() {
        FragmentMovimiento fragment = new FragmentMovimiento();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movimiento, container, false);

        // Asignacion variables layout
        spinerCuenta = view.findViewById(R.id.spin_cuenta);
        spinerAccountTransfer = view.findViewById(R.id.spin_accountTransfer);
        inputMonto = view.findViewById(R.id.input_monto);
        dateFechaMovimiento = view.findViewById(R.id.input_fecha);
        conceptoMovimiento = view.findViewById(R.id.input_concepto);
        btnRegistrarMovimiento = view.findViewById(R.id.btn_registrar);
        calendarPicker = view.findViewById(R.id.calendar);
        textTransfer = view.findViewById(R.id.text_cuenta_destino);
        tabLayout = view.findViewById(R.id.tab_layout);
        categoriaMovimiento = view.findViewById(R.id.input_categoria);
        vista = view.findViewById(R.id.movimiento_content);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        crearListeners();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        session = new UserSessionManager(getContext());
        user = session.getUserDetails();
        Comunicaciones com = new Comunicaciones(getActivity(), resultadosInterfaceListener);

        JSONObject parametro = new JSONObject();
        try{
            parametro.put("username",user.get(UserSessionManager.KEY_USER));
        } catch (JSONException jsone) {
            Log.e(TAG, jsone.getMessage());
        }
        Map<String, String> headersFake = new HashMap<String, String>();
        com.peticionJSON(Urls.GETACCOUNTS, Request.Method.POST, parametro, headersFake);
        //com.fillSpinnerAccount(Urls.GETACCOUNTS, paramsMovements, spinerCuenta, progreso, null);
        //com.fillSpinnerAccount(Urls.GETACCOUNTS, paramsMovements, spinerAccountTransfer, progreso, null);

        categoriaMovimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCategorias = new DialogCategorias(getActivity());
                dialogCategorias.show();
                dialogCategorias.setOnItemSelectedListener(itemSelectedListener);
            }
        });

        //Agregamos listener para saber si el EditText de fecha tiene el focus
        dateFechaMovimiento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(dateFechaMovimiento.isFocused())
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

        //Agregamos listener para saber si el EditText de fecha tiene el focus
        categoriaMovimiento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                dialogCategorias = new DialogCategorias(getActivity());
                dialogCategorias.show();
                dialogCategorias.setOnItemSelectedListener(itemSelectedListener);
            }
        });

        calendarPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFechaMovimiento();
            }
        });

        //Ocultar teclado al iniciar el fragment
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        btnRegistrarMovimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarFormulario();
            }
        });

    }

    /**
     * funcion encargada de establecer la fecha en el campo de fecha de movimiento y ocultar el teclado al mostrar el dialog
     */
    private void setFechaMovimiento() {
        //Ocultamos el Teclado
        dateFechaMovimiento.setInputType(InputType.TYPE_NULL);
        InputMethodManager inputMethodManager =  (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(dateFechaMovimiento.getWindowToken(), 0);

        DatePickerDialog dpd = new DatePickerDialog(getContext(), R.style.Calendar, new DatePickerDialog.OnDateSetListener() {
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
     * regresa mensaje de dato faltante, en caso contrario ejectuta reliarRegistro
     */
    private boolean validarFormulario() {
        JSONObject json = new JSONObject();
        String concepto = conceptoMovimiento.getText().toString();
        String fecha = dateFechaMovimiento.getText().toString();

        if(TextUtils.isEmpty(inputMonto.getText().toString())){
            inputMonto.setError(getString(R.string.error_input_vacio));
            return false;
        }
        int monto = Integer.parseInt(inputMonto.getText().toString());

        if(categorias == null){
            categoriaMovimiento.setError(getString(R.string.error_input_vacio));
            return false;
        }

        try{
            json.put("monto", monto);
            json.put("concepto", concepto);
            json.put("categoria", categorias.getId());
            json.put("fecha", fecha);
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

    public interface OnFragmentInteractionListener {

    }
}
