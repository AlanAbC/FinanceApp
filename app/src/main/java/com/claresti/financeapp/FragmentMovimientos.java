package com.claresti.financeapp;

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
import com.claresti.financeapp.Modelos.Movimiento;
import com.claresti.financeapp.Tools.Comunicaciones;
import com.claresti.financeapp.Tools.Urls;
import com.claresti.financeapp.Tools.UserSessionManager;
import com.claresti.financeapp.Tools.ViewLoadingDotsGrow;
import com.google.gson.Gson;
import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FragmentMovimientos extends Fragment{

    private OnFragmentInteractionListener mListener;

    private UserSessionManager sessionManager;
    private static final String TAG = "MOVEMENTS";
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private AdapterListSwipe adapterMovimientos;
    private ViewLoadingDotsGrow progress;
    private View layoutContent;
    private LinearLayoutManager linearLayout;

    /**
     * listener para actualizar categorias, solo los primeros resultados
     */
    private Comunicaciones.ResultadosInterface listenerComunicaciones = new Comunicaciones.ResultadosInterface() {
        @Override
        public void mostrarDatos(JSONObject json) {
            if(json.has("movements")){
                try {
                    Gson gson = new Gson();
                    final ArrayList<Movimiento> movimientosArrayList = new ArrayList<Movimiento>(Arrays.asList(gson.fromJson(json.getString("movements"), Movimiento[].class)));
                    for(Movimiento movement: movimientosArrayList) {
                        movement.save();
                    }
                    getLocalMovements();
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

    public FragmentMovimientos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragmentMovimientos.
     */
    public static FragmentMovimientos newInstance() {
        FragmentMovimientos fragment = new FragmentMovimientos();
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

        View view = inflater.inflate(R.layout.fragment_movimientos, container, false);

        recyclerView = view.findViewById(R.id.recycler_movements);
        layoutContent = view.findViewById(R.id.layout_content);
        progress = view.findViewById(R.id.progress_center_movements);
        refreshLayout = view.findViewById(R.id.swipeRefreshLayoutMovements);

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
        linearLayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayout);
        adapterMovimientos = new AdapterListSwipe(getActivity());
        recyclerView.setAdapter(adapterMovimientos);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateMovements();
                refreshLayout.setRefreshing(false);
            }
        });
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int numeroDeItems = linearLayout.getItemCount();
                Log.i(TAG, numeroDeItems + "");
            }
        });

        getLocalMovements();
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

    public void updateMovements(){
        progress.setVisibility(View.VISIBLE);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        Comunicaciones com = new Comunicaciones(getActivity(), listenerComunicaciones);

        com.peticionJSON(
                Urls.VIEWUSERMOVEMENTS + sessionManager.getUserDetails().get(UserSessionManager.KEY_ID),
                Request.Method.GET,
                new JSONObject(),
                headers
        );
    }

    public void getLocalMovements() {
        progress.setVisibility(View.VISIBLE);
        final ArrayList<Movimiento> movements = new ArrayList<>(SugarRecord.find(Movimiento.class, null, null, null, null, "20"));
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapterMovimientos.setMovimientos(movements);
                progress.setVisibility(View.GONE);
            }
        });
    }

    public void showMessage(String mensaje){
        Snackbar.make(layoutContent, mensaje, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Interface de Comunicaciones, todas las vistas las maneja el Fragment, asi evitamos pasar vistas entre clases
     */
    public interface OnFragmentInteractionListener {
    }
}
