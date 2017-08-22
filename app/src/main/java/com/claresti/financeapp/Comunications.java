package com.claresti.financeapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by smp_3 on 01/08/2017.
 * Clase que maneja todas las conexiones con el servidor
 */

public class Comunications {
    private Context context;
    private View view;
    private AdapterMovements adapterMovements;
    private AdapterCategories adapterCategories;
    private AdapterAccounts adapterAccounts;
    public static ObjCategoria[] arrayCategoria;
    public static ObjCuenta[] arrayCuenta;

    public Comunications(Context context, View view)
    {
        this.context = context;
        this.view = view;
        this.adapterMovements = AdapterMovements.getInstance(context, view);
        this.adapterCategories = AdapterCategories.getInstance(context, view);
        this.adapterAccounts = AdapterAccounts.getInstance(context, view);
    }

    /**
     * Funcion para obtener todos los movimientos dependiendo de los parametros que reciba
     * @param url - url de la API
     * @param paramsGetData - parametros que se enviaran a la funcion
     */
    public void getMovements(String url, Map<String, String> paramsGetData, final ProgressBar progressBar)
    {
        final Gson gson = new Gson();
        final Map<String, String> paramsMap = paramsGetData;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.i("JSON", response);
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response);
                            String state = jsonObject.getString("state");
                            switch(state)
                            {
                                case "1":
                                    JSONArray items = jsonObject.getJSONArray("items");
                                    ObjMovimiento[] itemsArray = gson.fromJson(items.toString(), ObjMovimiento[].class);
                                    for(int i = 0; i < itemsArray.length; i++)
                                    {
                                        adapterMovements.addItem(itemsArray[i]);

                                    }
                                    break;
                                case "0":
                                    String mensaje = jsonObject.getString("message");
                                    Snackbar.make(view, mensaje, Snackbar.LENGTH_SHORT).show();
                                    break;
                            }
                            progressBar.setVisibility(View.GONE);
                            adapterMovements.isLoading = false;

                        }
                        catch(JSONException jsone)
                        {
                            Snackbar.make(view, "No se han podido cargar los movimientos", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("DCOM", error.getMessage());
                        Snackbar.make(view, "Error de conexion", Snackbar.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = paramsMap;
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(stringRequest);
    }

    /**
     * Funcion para obtener todas las categorias dependiendo de los parametros que se envien
     * @param url - url de la API
     * @param paramsGetData - parametros que se enviaran a la API
     */
    public void getCategories(String url, Map<String, String> paramsGetData, final ProgressBar progressBar)
    {
        final Gson gson = new Gson();
        final Map<String, String> paramsMap = paramsGetData;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response);
                            String state = jsonObject.getString("state");
                            switch(state)
                            {
                                case "1":
                                    JSONArray items = jsonObject.getJSONArray("items");
                                    ObjCategoria[] itemsArray = gson.fromJson(items.toString(), ObjCategoria[].class);
                                    for(int i = 0; i < itemsArray.length; i++)
                                    {
                                        adapterCategories.addItem(itemsArray[i]);

                                    }
                                    progressBar.setVisibility(View.GONE);
                                    break;
                                case "0":
                                    String mensaje = jsonObject.getString("message");
                                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                                    break;
                            }

                        }
                        catch(JSONException jsone)
                        {
                            Snackbar.make(view, "No se han podido cargar las categorias", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("DCOM", error.getMessage());
                        Snackbar.make(view, "Error de conexion", Snackbar.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = paramsMap;
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(stringRequest);
    }

    /**
     * Funcion para obtener todas las cuentas dependiendo de los parametros que se envien
     * @param url - url de la API
     * @param paramsGetData - parametros que se enviaran a la API
     */
    public void getAccounts(String url, Map<String, String> paramsGetData, final ProgressBar progressBar)
    {
        final Gson gson = new Gson();
        final Map<String, String> paramsMap = paramsGetData;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response);
                            String state = jsonObject.getString("state");
                            switch(state)
                            {
                                case "1":
                                    JSONArray items = jsonObject.getJSONArray("items");
                                    ObjCuenta[] itemsArray = gson.fromJson(items.toString(), ObjCuenta[].class);
                                    for(int i = 0; i < itemsArray.length; i++)
                                    {
                                        adapterAccounts.addItem(itemsArray[i]);

                                    }
                                    progressBar.setVisibility(View.GONE);
                                    break;
                                case "0":
                                    String mensaje = jsonObject.getString("message");
                                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                                    break;
                            }

                        }
                        catch(JSONException jsone)
                        {
                            Snackbar.make(view, "No se han podido cargar las cuentas", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("DCOM", error.getMessage());
                        Snackbar.make(view, "Error de conexion", Snackbar.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = paramsMap;
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(stringRequest);
    }

    /**
     * Funcion para llenar un spinner que contenga categorias
     * @param url - url de la API
     * @param paramsGetData - parametros que se enviaran a la API
     * @param spinner - spinner a llenar
     * @param progressBar - barra de progreso para mostrar que se estan descargando datos
     */
    public void fillSpinnerCategory(String url, Map<String, String> paramsGetData, Spinner spinner, final ProgressBar progressBar)
    {
        progressBar.setVisibility(View.VISIBLE);
        final Gson gson = new Gson();
        final Map<String, String> paramsMap = paramsGetData;
        final Spinner spinnerToFill = spinner;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response);
                            String state = jsonObject.getString("state");
                            switch(state)
                            {
                                case "1":
                                    JSONArray items = jsonObject.getJSONArray("items");
                                    ObjCategoria[] itemsArray = gson.fromJson(items.toString(), ObjCategoria[].class);

                                    final ArrayList<String> textoSpinerCategoria = new ArrayList<String>();
                                    for(int i = 0; i < itemsArray.length; i++)
                                    {
                                        textoSpinerCategoria.add(itemsArray[i].getNombre());
                                    }
                                    arrayCategoria = itemsArray;
                                    spinnerToFill.setAdapter(new AdaptadorSpinnerMovimientos(context, textoSpinerCategoria));
                                    break;
                                case "0":
                                    String mensaje = jsonObject.getString("message");
                                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            progressBar.setVisibility(View.GONE);

                        }
                        catch(JSONException jsone)
                        {
                            Snackbar.make(view, "No se han podido cargar las cuentas", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("DCOM", error.getMessage());
                        Snackbar.make(view, "Error de conexion", Snackbar.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = paramsMap;
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(stringRequest);
    }

    /**
     * Funcion para llenar un spinner que contenga cuentas
     * @param url - url de la API
     * @param paramsGetData - parametros que se enviaran a la API
     * @param spinner - spinner a llenar
     * @param progressBar - barra de progreso para mostrar que se estan descargando datos
     */
    public void fillSpinnerAccount(String url, Map<String, String> paramsGetData, Spinner spinner, final ProgressBar progressBar)
    {
        progressBar.setVisibility(View.VISIBLE);
        final Gson gson = new Gson();
        final Map<String, String> paramsMap = paramsGetData;
        final Spinner spinnerToFill = spinner;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response);
                            String state = jsonObject.getString("state");
                            switch(state)
                            {
                                case "1":
                                    JSONArray items = jsonObject.getJSONArray("items");
                                    ObjCuenta[] itemsArray = gson.fromJson(items.toString(), ObjCuenta[].class);

                                    final ArrayList<String> textoSpinerCategoria = new ArrayList<String>();
                                    for(int i = 0; i < itemsArray.length; i++)
                                    {
                                        textoSpinerCategoria.add(itemsArray[i].getNombre());
                                    }
                                    arrayCuenta = itemsArray;
                                    spinnerToFill.setAdapter(new AdaptadorSpinnerMovimientos(context, textoSpinerCategoria));
                                    break;
                                case "0":
                                    String mensaje = jsonObject.getString("message");
                                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            progressBar.setVisibility(View.GONE);

                        }
                        catch(JSONException jsone)
                        {
                            Snackbar.make(view, "No se han podido cargar las cuentas", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("DCOM", error.getMessage());
                        Snackbar.make(view, "Error de conexion", Snackbar.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = paramsMap;
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(stringRequest);
    }

    /**
     * Funcion para hacer un nuevo registro (Movimiento, Categoria, Cuenta)
     * @param url - url de la API
     * @param paramsGetData - parametros que se enviaran a la API
     * @param progressDialog - barra de progreso
     */
    public void newRegister(String url, Map<String, String> paramsGetData, ProgressDialogDenarius progressDialog)
    {
        final ProgressDialogDenarius progress = progressDialog;
        final Map<String, String> paramsMap = paramsGetData;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.i("RES", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String res = jsonObject.getString("estado");
                            switch(res){
                                case "1":
                                    progress.setActionCorrect(jsonObject.getString("mensaje"));
                                    break;
                                case "0":
                                    progress.setActionIncorrect(jsonObject.getString("mensaje"));
                                    break;
                                default:
                                    progress.setActionIncorrect(jsonObject.getString("mensaje"));
                                    break;
                            }
                        }catch(JSONException json){
                            Log.e("JSON", json.toString());
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("DCOM", error.getMessage());
                        progress.setActionIncorrect("No se ha podido conectar a Internet");
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = paramsMap;
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(stringRequest);
    }

}
