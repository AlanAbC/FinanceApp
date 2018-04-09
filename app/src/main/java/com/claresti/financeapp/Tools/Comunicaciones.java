package com.claresti.financeapp.Tools;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.claresti.financeapp.R;
import com.claresti.financeapp.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Clase encargada de las comunicaciones :)
 */
public class Comunicaciones {
    private Context context;
    private ResultadosInterface mListener;
    private static final String TAG = "COMM";

    /**
     * Constructor
     * @param context
     * @param listener interface para la lectura de los datos recibidos
     */
    public Comunicaciones(Context context, ResultadosInterface listener)
    {
        this.context = context;
        mListener = listener;
    }//fin constructor

    /**
     * Método para obtener un json como respuesta del servidor
     * @param url url a donde se accedera
     * @param method método que se utilizará
     * @param params parametros a enviar al servidor
     * @param headers headers para la peticion
     */
    public void peticionJSON(String url, int method, JSONObject params, final Map<String, String> headers)
    {
        final JsonObjectRequest stringRequest = new JsonObjectRequest(
                method,
                url,
                params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.i(TAG, response.toString());
                        mListener.mostrarDatos(response);
                    }

                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e(TAG, "VolleyError " + error.getCause());
                        //Primero verificamos errores de Conexion
                        if(error instanceof TimeoutError || error instanceof NoConnectionError){
                            mListener.setError(context.getString(R.string.comunicaciones_error_no_connection));
                            return;
                        }

                        //Verificamos la respuesta que nos de el servidor
                        if(error.networkResponse != null){
                            if(error.networkResponse.statusCode == 404)mListener.setError(context.getString(R.string.comunicaciones_error_404));//No se ha encontrado
                            if(error.networkResponse.statusCode == 403)mListener.setError(context.getString(R.string.comunicaciones_error_403));//No hay permisos para acceder a este elemento
                            if(error.networkResponse.statusCode == 401)mListener.setError(context.getString(R.string.comunicaciones_error_401));//No se han autentificado aún o la sesion ha caducado
                            if(error.networkResponse.statusCode == 400)mListener.setError(context.getString(R.string.comunicaciones_error_400));//La peticion es incorrecta
                            if(error.networkResponse.statusCode == 500)mListener.setError(context.getString(R.string.comunicaciones_error_500));//Error en el servidor
                            return;
                        }
                        mListener.setError(context.getString(R.string.comunicaciones_error ));
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mParams = headers;
                return mParams;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    Log.i(TAG, responseString + "\n" + response.data.toString());
                    if(responseString.equals("200")){
                        try{
                            return Response.success(new JSONObject(response.data.toString()), HttpHeaderParser.parseCacheHeaders(response));
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                            return Response.success(new JSONObject(), HttpHeaderParser.parseCacheHeaders(response));
                        }
                    }
                }
                return super.parseNetworkResponse(response);
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(2500, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag(TAG);
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }//fin getSomethingJSON

    public interface ResultadosInterface{
        void mostrarDatos(JSONObject json);
        void setError(String mensaje);
    }
}
