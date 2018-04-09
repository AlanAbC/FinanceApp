package com.claresti.financeapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
 * Created by CLARESTI on 01/08/2017.
 * Clase que maneja todas las conexiones con el servidor
 */

public class Comunications {
    private Context context;
    private View view;
    private AdapterAccounts adapterAccounts;
    public static ObjCategoria[] arrayCategoria;
    public static ObjCuenta[] arrayCuenta;
    private static final String TAG = "CONEXIONES";

    private ComunicationsInterface mListener;

    public Comunications(Context context, View view)
    {
        this.context = context;
        this.view = view;
    }

    public Comunications(Context context, ComunicationsInterface listener)
    {
        this.context = context;
        mListener = listener;
    }

    /**
     * Funcion para obtener todos los movimientos dependiendo de los parametros que reciba
     * @param url - url de la API
     * @param paramsGetData - parametros que se enviaran a la funcion
     */
    public void getMovements(String url, Map<String, String> paramsGetData)
    {
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
                        mListener.showData(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.i(TAG, "Error de conexion", error.getCause());
                        mListener.errorResponse();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(2500, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag(TAG);
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * Funcion para obtener todas las categorias dependiendo de los parametros que se envien
     * @param url - url de la API
     * @param paramsGetData - parametros que se enviaran a la API
     */
    public void getCategories(String url, Map<String, String> paramsGetData)
    {
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
                        mListener.showData(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("DCOM", error.getMessage());
                        mListener.errorResponse();
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
    public void getAccounts(String url, Map<String, String> paramsGetData)
    {
        final Map<String, String> paramsMap = paramsGetData;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        mListener.showData(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("DCOM", error.getMessage());
                        mListener.errorResponse();
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
     * @param id - id de la categoria que se seleccionara al llenar el spinner, valor de -1 para no seleccionar ninguna categoria
     */
    public void fillSpinnerCategory(String url, Map<String, String> paramsGetData, Spinner spinner, final ProgressBar progressBar, final String id)
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
                                    int position = -1;
                                    final ArrayList<String> textoSpinerCategoria = new ArrayList<String>();
                                    for(int i = 0; i < itemsArray.length; i++)
                                    {
                                        textoSpinerCategoria.add(itemsArray[i].getNombre());
                                        if(itemsArray[i].getID().equals(id)) position = i;
                                    }
                                    arrayCategoria = itemsArray;
                                    spinnerToFill.setAdapter(new AdaptadorSpinnerMovimientos(context, textoSpinerCategoria));
                                    if (position != -1)
                                    {
                                        spinnerToFill.setSelection(position);
                                    }
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
     * @param id - id de la cuenta que se seleccionara al llenar el spinner, valor de -1 para no seleccionar ninguna cuenta
     */
    public void fillSpinnerAccount(String url, Map<String, String> paramsGetData, Spinner spinner, final ProgressBar progressBar, final String id)
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
                                    int position = -1;
                                    final ArrayList<String> textoSpinerCategoria = new ArrayList<String>();
                                    for(int i = 0; i < itemsArray.length; i++)
                                    {
                                        textoSpinerCategoria.add(itemsArray[i].getNombre());
                                        if(itemsArray[i].getID().equals(id)) position = i;
                                    }
                                    arrayCuenta = itemsArray;
                                    spinnerToFill.setAdapter(new AdaptadorSpinnerMovimientos(context, textoSpinerCategoria));
                                    if (position != -1)
                                    {
                                        spinnerToFill.setSelection(position);
                                    }
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
     * @param progressDialog - barra de progreso que indica que se esta realizando el proceso
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


    /**
     * Funcion para eliminar un item
     * @param url - url de la API
     * @param paramsGetData - parametros que se enviaran a la API
     * @param adapter - adaptador del que se eliminara el movimiento
     * @param position - posicion del item que se eliminara
     */
    public void deleteItem(String url, Map<String, String> paramsGetData, int adapter, int position)
    {
        final int itemPosition = position;
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
                                    mListener.deleteItemOnAdapter(itemPosition);
                                    break;
                                case "0":
                                    mListener.showSnackBarFromComunications(jsonObject.getString("mensaje"));
                                    break;
                                default:
                                    mListener.showSnackBarFromComunications("No se ha podido realizar la accion");
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
                        mListener.errorResponse();
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
     * Funcion que solicita a la api los datos del login y los valida si existe
     * el usuario en caso de existir guarda la informacion en la base de
     * datos local y en caso contrario marca mensaje de que no existe el
     * usuario o contraseña incorrecta
     * @param url url a donde se accedera para el login
     * @param paramsGetData parametros para enviar
     */
    public void login(String url, final Map<String, String> paramsGetData) {
        final Map<String, String> params = paramsGetData;
        final Gson gson = new Gson();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String res = jsonObject.getString("estado");
                            switch(res){
                                case "1":
                                    JSONArray jArrayMarcadores = jsonObject.getJSONArray("usuario");
                                    ObjUsuario[] arrayUsuario = gson.fromJson(jArrayMarcadores.toString(), ObjUsuario[].class);
                                    if(arrayUsuario.length == 1) {
                                        for (ObjUsuario usuario : arrayUsuario) {
                                            UserSessionManager session;
                                            session = new UserSessionManager(context);

                                            session.createUserLoginSession(usuario.getIdUsuario(), usuario.getUsuario(), usuario.getNombre(), usuario.getCorreo(), usuario.getSexo(), usuario.getFecha_Nac());

                                            Intent i = new Intent(context, MainDenarius.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(i);
                                        }
                                    }
                                    break;
                                case "0":
                                    //Regresar mensaje de que no hay registros
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
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = paramsGetData;
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

    public interface ComunicationsInterface{
        void showData(String response);
        void errorResponse();
        void deleteItemOnAdapter(int position);
        void showSnackBarFromComunications(String mensaje);
    }

}
