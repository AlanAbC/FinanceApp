package com.claresti.financeapp.Tools;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by usuario on 4/2/2018.
 */

public class Files {

    private static Files singleton;
    private final static String TAG = "FILES";

    private Files(){
    }

    /**
     * Retornamos la instancia unica del singleton
     *@return Instancia
     */
    public static synchronized Files getInstance()
    {
        if(singleton == null)
        {
            singleton = new Files();
        }
        return singleton;
    }

    /**
     *Funcion para obtener un json de un InputStream
     * @param is InputStream on los datos de json
     * @return json obtenido del InputStream
     */
    public JSONObject getJsonFromInputStream(InputStream is){
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            is.close();
            return new JSONObject(writer.toString());
        } catch(JSONException jsone) {
            Log.e(TAG, jsone.getMessage());
            return null;
        } catch(IOException ioe) {
            Log.e(TAG, ioe.getMessage());
            return null;
        }
    }
}
