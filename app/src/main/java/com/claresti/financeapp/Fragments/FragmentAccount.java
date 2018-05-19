package com.claresti.financeapp.Fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.claresti.financeapp.Dialogs.DialogProgress;
import com.claresti.financeapp.R;
import com.claresti.financeapp.Tools.Comunicaciones;
import com.claresti.financeapp.Tools.Urls;
import com.claresti.financeapp.Tools.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragmentAccount extends Fragment {

    private FragmentAccount.OnFragmentInteractionListener mListener;
    private EditText inputNombre, inputDescripcion, inputMoney;
    private Button buttonGuardar;
    private JSONObject json;
    private static final String TAG = "ACCOUNT";
    private UserSessionManager sessionManager;
    private DialogProgress progress;

    private Comunicaciones.ResultadosInterface comunicacionesListener = new Comunicaciones.ResultadosInterface() {
        @Override
        public void mostrarDatos(JSONObject json) {
            Log.i(TAG, json.toString());
            progress.setOk(getString(R.string.cuenta_guardar_ok));
            getActivity().onBackPressed();
        }

        @Override
        public void setError(String mensaje) {
            Log.i(TAG, mensaje);
            progress.setError(mensaje);
        }
    };

    public FragmentAccount() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CategoryFragment.
     */
    public static FragmentAccount newInstance() {
        FragmentAccount fragment = new FragmentAccount();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new UserSessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        inputNombre = view.findViewById(R.id.input_nombre_cuenta);
        inputDescripcion = view.findViewById(R.id.input_descripcion_cuenta);
        inputMoney = view.findViewById(R.id.input_descripcion_money);
        buttonGuardar = view.findViewById(R.id.button_guardar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarFormulario()){
                    progress = new DialogProgress();
                    progress.show(getActivity().getFragmentManager(), "DialogProgress");
                    json = new JSONObject();
                    try {
                        json.put("user_id", sessionManager.getUserDetails().get(UserSessionManager.KEY_ID));
                        json.put("name", inputNombre.getText().toString());
                        json.put("description", inputDescripcion.getText().toString());
                        json.put("money", inputMoney.getText().toString());
                    } catch(JSONException jsone) {
                        Log.e(TAG, jsone.getMessage());
                        return;
                    }
                    Log.i(TAG, json.toString());

                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");

                    Comunicaciones com = new Comunicaciones(getActivity(), comunicacionesListener);

                    com.peticionJSON(
                            Urls.REGISTERACCOUNT,
                            Request.Method.POST,
                            json,
                            headers
                    );

                }
            }
        });

    }

    public boolean validarFormulario(){
        String nombre, descripcion, money;
        nombre = inputNombre.getText().toString();
        descripcion = inputDescripcion.getText().toString();
        money = inputMoney.getText().toString();

        if(TextUtils.isEmpty(nombre)){
            inputNombre.setError(getString(R.string.error_input_vacio));
            inputNombre.requestFocus();
            return false;
        }

        if(TextUtils.isEmpty(descripcion)){
            inputDescripcion.setError(getString(R.string.error_input_vacio));
            inputDescripcion.requestFocus();
            return false;
        }

        if(TextUtils.isEmpty(money)){
            inputMoney.setError(getString(R.string.error_input_vacio));
            inputMoney.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentAccount.OnFragmentInteractionListener) {
            mListener = (FragmentAccount.OnFragmentInteractionListener) context;
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

    public interface OnFragmentInteractionListener {
    }
}
