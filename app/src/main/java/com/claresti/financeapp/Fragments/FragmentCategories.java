package com.claresti.financeapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.claresti.financeapp.Adapters.AdapterCategories;
import com.claresti.financeapp.Modelos.Categoria;
import com.claresti.financeapp.R;
import com.claresti.financeapp.Tools.Comunicaciones;
import com.claresti.financeapp.Tools.Urls;
import com.claresti.financeapp.Tools.ViewLoadingDotsGrow;
import com.claresti.financeapp.Tools.UserSessionManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase encargada de mostrar las categorias que el usuario asigna a sus movimientos
 */
public class FragmentCategories extends Fragment{

    private OnFragmentInteractionListener mListener;

    private UserSessionManager sessionManager;
    private static final String TAG = "CATEGORIES";
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private AdapterCategories adapterCategories;
    private ViewLoadingDotsGrow progress;
    private View layoutContent;

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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterCategories.setCategorias(categoriesArrayList);
                        }
                    });
                } catch(JSONException jsone) {
                    Log.e(TAG, jsone.getMessage());
                    showMessage(getString(R.string.comunicaciones_error));
                }
            }
            progress.setVisibility(View.GONE);
        }

        @Override
        public void setError(String mensaje) {
            Log.e(TAG, mensaje);
            progress.setVisibility(View.GONE);
            showMessage(mensaje);
        }
    };

    public FragmentCategories() {
        // Required empty public constructor
    }
    public static FragmentCategories newInstance() {
        FragmentCategories fragment = new FragmentCategories();
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

        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        recyclerView = view.findViewById(R.id.recycler_categories);
        refreshLayout = view.findViewById(R.id.refreshCategories);
        progress = view.findViewById(R.id.progress_center_categories);
        layoutContent = view.findViewById(R.id.layout_content);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapterCategories = new AdapterCategories(getActivity());
        recyclerView.setAdapter(adapterCategories);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateCategories();
                refreshLayout.setRefreshing(false);
            }
        });

        updateCategories();
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

    public void updateCategories(){
        progress.setVisibility(View.VISIBLE);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        Comunicaciones com = new Comunicaciones(getActivity(), listenerComunicaciones);

        com.peticionJSON(
                Urls.VIEWUSERCATEGORIES + sessionManager.getUserDetails().get(UserSessionManager.KEY_ID),
                Request.Method.GET,
                new JSONObject(),
                headers
        );
    }

    public void showMessage(String mensaje){
        Snackbar.make(layoutContent, mensaje, Snackbar.LENGTH_SHORT).show();
    }

    public interface OnFragmentInteractionListener {
    }

}
