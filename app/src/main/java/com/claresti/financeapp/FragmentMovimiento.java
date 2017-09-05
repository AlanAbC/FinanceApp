package com.claresti.financeapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.InputType;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMovimiento.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMovimiento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMovimiento extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Declaracion de variables en el layout
    private Spinner spinerCategoria;
    private Spinner spinerCuenta;
    private Spinner spinerAccountTransfer;
    private TextView txtCategoria;
    private TextView txtCuenta;
    private TextView txtMovimiento;
    private TextView txtAccountTransfer;
    private TextView descriptionAccountTranfer;
    private ImageView ingreso;
    private ImageView egreso;
    private ImageView transfer;
    private EditText inputMonto;
    private EditText dateFechaMovimiento;
    private EditText conceptoMovimiento;
    private ImageButton calendarPicker;
    private Button btnRegistrarMovimiento;
    private ProgressBar progreso;

    //Declaracion de banderas de variables
    private int flagMovimiento;
    private int flagCategoria;
    private int flagCuenta;
    private int flagCuentaTransfer;

    // Declaracion variables para el datapikerdialog
    private int ano;
    private int mes;
    private int dia;

    //Declaracion de variables para la comunicacion con la API
    private Comunications com;
    private UserSessionManager session;
    private HashMap<String,String> user;

    //ProgressDialog
    private ProgressDialogDenarius progressDialog;

    //Contexto y vista
    private View vista;
    private Context context;


    public FragmentMovimiento() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMovimiento.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMovimiento newInstance(String param1, String param2) {
        FragmentMovimiento fragment = new FragmentMovimiento();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movimiento, container, false);

        // Asignacion variables layout
        spinerCategoria = (Spinner)view.findViewById(R.id.spin_categoria);
        spinerCuenta = (Spinner)view.findViewById(R.id.spin_cuenta);
        spinerAccountTransfer = (Spinner)view.findViewById(R.id.spin_accountTransfer);
        inputMonto = (EditText)view.findViewById(R.id.input_monto);
        txtCategoria = (TextView)view.findViewById(R.id.txt_categoria);
        txtCuenta = (TextView)view.findViewById(R.id.txt_cuenta);
        txtAccountTransfer = (TextView)view.findViewById(R.id.txt_accountTransfer);
        descriptionAccountTranfer = (TextView)view.findViewById(R.id.tv_accountTransfer);
        txtMovimiento = (TextView)view.findViewById(R.id.tv_tipomov);
        ingreso = (ImageView)view.findViewById(R.id.img_mas);
        egreso = (ImageView)view.findViewById(R.id.img_menos);
        transfer = (ImageView) view.findViewById(R.id.img_transfer);
        dateFechaMovimiento = (EditText) view.findViewById(R.id.input_fecha);
        conceptoMovimiento = (EditText) view.findViewById(R.id.input_concepto);
        btnRegistrarMovimiento = (Button)view.findViewById(R.id.btn_registrar);
        progreso = (ProgressBar) view.findViewById(R.id.progress);
        calendarPicker= (ImageButton) view.findViewById(R.id.calendar);

        vista = getActivity().findViewById(android.R.id.content);

        progressDialog = new ProgressDialogDenarius(context);

        // Asignacion variables restantes
        flagMovimiento = 5;
        flagCuentaTransfer = -1;
        flagCuenta = -1;
        flagCategoria = -1;

        // Datos para el datepikerdialog
        Calendar c = Calendar.getInstance();
        ano = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.DAY_OF_MONTH);

        // Llamada a funciones para llenar los spinners y crear los listenrs
        session = new UserSessionManager(getContext());
        user = session.getUserDetails();
        com = new Comunications(context, vista);

        Map<String, String> paramsMovements = new HashMap<String, String>();
        paramsMovements.put("username",user.get(UserSessionManager.KEY_USER));
        com.fillSpinnerCategory(Urls.GETCATEGORIES, paramsMovements, spinerCategoria, progreso, null);
        com.fillSpinnerAccount(Urls.GETACCOUNTS, paramsMovements, spinerCuenta, progreso, null);
        com.fillSpinnerAccount(Urls.GETACCOUNTS, paramsMovements, spinerAccountTransfer, progreso, null);

        //Ocultar teclado al iniciar la activity
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        crearListeners();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Funcion encargada de crear los listeners de los objetos del layout
     */
    private void crearListeners() {
        ingreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagMovimiento = 1;
                txtMovimiento.setText("Ingreso");
                Log.i("Movimiento", flagMovimiento + "");
                //ingreso.setBackgroundResource(R.drawable.img_verde_res);
                //egreso.setBackgroundResource(R.drawable.img_roja);
                ingreso.setPadding(20,20,20,20);
                egreso.setPadding(25,25,25,25);
                transfer.setPadding(25,25,25,25);
                ingreso.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                egreso.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                transfer.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                descriptionAccountTranfer.setVisibility(View.GONE);
                spinerAccountTransfer.setVisibility(View.GONE);
                txtAccountTransfer.setVisibility(View.GONE);
            }
        });
        egreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagMovimiento = 2;
                txtMovimiento.setText("Egreso");
                Log.i("Movimiento", flagMovimiento + "");
                //egreso.setBackgroundResource(R.drawable.img_roja_res);
                //ingreso.setBackgroundResource(R.drawable.img_verde);
                egreso.setPadding(20,20,20,20);
                ingreso.setPadding(25,25,25,25);
                transfer.setPadding(25,25,25,25);
                ingreso.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                egreso.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                transfer.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                descriptionAccountTranfer.setVisibility(View.GONE);
                spinerAccountTransfer.setVisibility(View.GONE);
                txtAccountTransfer.setVisibility(View.GONE);
            }
        });

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagMovimiento = 3;
                flagCuentaTransfer = -1;//reseteamos la bandera de cuenta a transferir
                txtMovimiento.setText("Transferencia");
                transfer.setPadding(10,10,10,10);
                ingreso.setPadding(25,25,25,25);
                egreso.setPadding(25,25,25,25);
                ingreso.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                egreso.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                transfer.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                descriptionAccountTranfer.setVisibility(View.VISIBLE);
                spinerAccountTransfer.setVisibility(View.VISIBLE);
                txtAccountTransfer.setVisibility(View.VISIBLE);
            }
        });
        btnRegistrarMovimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarFormulario();
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

        calendarPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFechaMovimiento();
            }
        });

        spinerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtCategoria.setText(Comunications.arrayCategoria[position].getNombre());
                flagCategoria = Integer.parseInt(Comunications.arrayCategoria[position].getID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinerAccountTransfer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtAccountTransfer.setText(Comunications.arrayCuenta[position].getNombre());
                flagCuentaTransfer = Integer.parseInt(Comunications.arrayCuenta[position].getID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinerCuenta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtCuenta.setText(Comunications.arrayCuenta[position].getNombre());
                flagCuenta = Integer.parseInt(Comunications.arrayCuenta[position].getID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(progressDialog.getState())
                {
                    //funcion para limpiar y resetear todos las cosas del fragment
                }
            }
        });
    }

    /**
     * funcion encargada de establecer la fecha en el campo de fecha de movimiento y ocultar el teclado al mostrar el dialog
     */
    private void setFechaMovimiento()
    {
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
    private void validarFormulario() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaActual = new Date();
            Date fecha = sdf.parse(dateFechaMovimiento.getText().toString());
            if (flagMovimiento == 5) {
                msg("Selecciona que tipo de movimiento desea realizar");
            } else if (inputMonto.getText().toString().equals("")) {
                msg("Ingresa la cantidad del movimiento");
            } else if (fechaActual.before(fecha)){
                msg("Ingrese una fecha que ya haya pasado");
            } else if (conceptoMovimiento.getText().toString().equals("")){
                msg("Ingrese el concepto del movimiento a realizar");
            } else if (flagMovimiento == 3 && flagCuentaTransfer == -1) {
                msg("Selecciona una cuenta para la transferencia");
            } else if (flagMovimiento == 3 && flagCuenta == flagCuentaTransfer) {
                msg("No puedes hacer una transferencia a la misma cuenta");
            } else {
                progressDialog.show();
                Map<String, String> paramsMovements = new HashMap<String, String>();
                paramsMovements.put("idUsuario",user.get(UserSessionManager.KEY_ID));
                paramsMovements.put("idCategoria", flagCategoria + "");
                paramsMovements.put("monto", inputMonto.getText().toString());
                paramsMovements.put("idCuenta", flagCuenta + "");
                paramsMovements.put("tipo", flagMovimiento + "");
                paramsMovements.put("fecha", dateFechaMovimiento.getText().toString());
                paramsMovements.put("concepto", conceptoMovimiento.getText().toString());
                if(flagMovimiento == 3)
                {
                    paramsMovements.put("idAccountTransfer", flagCuentaTransfer + "");
                }
                com.newRegister(Urls.NEWMOVEMENT, paramsMovements, progressDialog);

            }
        }catch (Exception e){
            Log.e("SFD PARSE", e.toString());
            msg("No se puede registrar este movimiento");
        }
    }

    private void msg(String msg){
        Snackbar.make(vista, msg, Snackbar.LENGTH_LONG).setAction("Aceptar", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }
}
