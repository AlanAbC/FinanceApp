package com.claresti.financeapp.Fragments;

import android.content.Context;
import android.net.Uri;
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
import com.claresti.financeapp.Modelos.Cuenta;
import com.claresti.financeapp.R;
import com.claresti.financeapp.Tools.Comunicaciones;
import com.claresti.financeapp.Tools.Urls;
import com.claresti.financeapp.Tools.ViewLoadingDotsGrow;
import com.claresti.financeapp.Adapters.AdapterAccounts;
import com.claresti.financeapp.UserSessionManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FragmentAccounts extends Fragment {

    private OnFragmentInteractionListener mListener;

    //RecyclerView
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final String TAG = "ACCOUNTS";
    private ViewLoadingDotsGrow progress;
    private AdapterAccounts adapterAccounts;
    private View layoutContent;
    private UserSessionManager sessionManager;

    /**
     * listener para actualizar categorias, solo los primeros resultados
     */
    private Comunicaciones.ResultadosInterface listenerComunicaciones = new Comunicaciones.ResultadosInterface() {
        @Override
        public void mostrarDatos(JSONObject json) {
            if(json.has("accounts")){
                try {
                    Gson gson = new Gson();
                    final ArrayList<Cuenta> accountysArrayList = new ArrayList<Cuenta>(Arrays.asList(gson.fromJson(json.getString("accounts"), Cuenta[].class)));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterAccounts.setCategorias(accountysArrayList);
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

    public FragmentAccounts() {
        // Required empty public constructor
    }

    public static FragmentAccounts newInstance() {
        FragmentAccounts fragment = new FragmentAccounts();
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

        View view = inflater.inflate(R.layout.fragment_accounts, container, false);

        recyclerView = view.findViewById(R.id.recycler_accounts);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutAccounts);
        progress = view.findViewById(R.id.progress_center_accounts);
        layoutContent = view.findViewById(R.id.layout_content);
        return view;

    }

    /**
     * Con este metodo realizamos acciones una vez que la vista se haya creado y renderizadp
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapterAccounts = new AdapterAccounts(getActivity());
        recyclerView.setAdapter(adapterAccounts);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateCategories();
                swipeRefreshLayout.setRefreshing(false);
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
                Urls.VIEWUSERACCOUNTS + sessionManager.getUserDetails().get(UserSessionManager.KEY_ID),
                Request.Method.GET,
                new JSONObject(),
                headers
        );
    }

    public void showMessage(String mensaje) {
        Snackbar.make(layoutContent, mensaje, Snackbar.LENGTH_SHORT).show();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Funcion en la que se asignaran los listener a os objetos que se requieran
     */
    private void asignarListeners()
    {
        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayout.getItemCount();
                lastVisibleItem = linearLayout.findLastCompletelyVisibleItemPosition();
                if(!AdapterMovements.isLoading && totalItemCount == (lastVisibleItem + 1))
                {
                    adapterMovements.loadMoreItems(bottomProgress);
                }
            }
        });*/
    }
}
