package com.claresti.financeapp.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.claresti.financeapp.Dialogs.DialogProgress;
import com.claresti.financeapp.R;
import com.claresti.financeapp.Tools.Comunicaciones;
import com.claresti.financeapp.Tools.Urls;
import com.claresti.financeapp.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CategoryFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private int current_red = 127, current_green = 127, current_blue = 210;
    private EditText inputNombre, inputDescripcion;
    private AppCompatSeekBar seekRed, seekGreen, seekBlue;
    private TextView textRed, textGreen, textBlue;
    private Button buttonGuardar;
    private View viewColor;
    private JSONObject json;
    private static final String TAG = "CATEGORIA";
    private UserSessionManager sessionManager;
    private DialogProgress progress;

    private Comunicaciones.ResultadosInterface comunicacionesListener = new Comunicaciones.ResultadosInterface() {
        @Override
        public void mostrarDatos(JSONObject json) {
            Log.i(TAG, json.toString());
            progress.setOk(getString(R.string.categoria_guardar_ok));
            getActivity().onBackPressed();
        }

        @Override
        public void setError(String mensaje) {
            Log.i(TAG, mensaje);
            progress.setError(mensaje);
        }
    };

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CategoryFragment.
     */
    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        inputNombre = view.findViewById(R.id.input_nombre_categoria);
        inputDescripcion = view.findViewById(R.id.input_descripcion_categoria);
        seekRed = view.findViewById(R.id.seekbar_red);
        seekGreen = view.findViewById(R.id.seekbar_green);
        seekBlue = view.findViewById(R.id.seekbar_blue);
        textRed = view.findViewById(R.id.tv_red);
        textGreen = view.findViewById(R.id.tv_green);
        textBlue = view.findViewById(R.id.tv_blue);
        viewColor = view.findViewById(R.id.view_result);
        buttonGuardar = view.findViewById(R.id.button_guardar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textRed.setText(current_red + "");
        textGreen.setText(current_green + "");
        textBlue.setText(current_blue + "");

        seekRed.setProgress(current_red);
        seekGreen.setProgress(current_green);
        seekBlue.setProgress(current_blue);

        viewColor.setBackgroundColor(Color.rgb(current_red, current_green, current_blue));

        seekRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textRed.setText(i + "");
                current_red = i;
                viewColor.setBackgroundColor(Color.rgb(current_red, current_green, current_blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textGreen.setText(i + "");
                current_green = i;
                viewColor.setBackgroundColor(Color.rgb(current_red, current_green, current_blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textBlue.setText(i + "");
                current_blue = i;
                viewColor.setBackgroundColor(Color.rgb(current_red, current_green, current_blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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
                        json.put("color", "#" + Integer.toHexString(current_red) + Integer.toHexString(current_green) + Integer.toHexString(current_blue));
                    } catch(JSONException jsone) {
                        Log.e(TAG, jsone.getMessage());
                        return;
                    }
                    Log.i(TAG, json.toString());

                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");

                    Comunicaciones com = new Comunicaciones(getActivity(), comunicacionesListener);

                    com.peticionJSON(
                            Urls.REGISTERCATEGORY,
                            Request.Method.POST,
                            json,
                            headers
                    );

                }
            }
        });

    }

    public boolean validarFormulario(){
        String nombre, descripcion;
        nombre = inputNombre.getText().toString();
        descripcion = inputDescripcion.getText().toString();

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
        return true;
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

    public interface OnFragmentInteractionListener {
    }
}
