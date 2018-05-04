package com.claresti.financeapp.Tools;

import android.content.Context;
import android.util.Log;

import com.claresti.financeapp.Modelos.Categoria;
import com.claresti.financeapp.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by smp_3 on 04/04/2018.
 */

public class CatalogosUsuario {

    private Context context;
    private static CatalogosUsuario singleton;
    private final static String TAG = "Catalogos";
    public static ArrayList<Categoria> catCategorias;
    public static ArrayList<Categoria> categorias;

    private CatalogosUsuario(Context context)
    {
        this.context = context;
        cargarCategorias();
        cargarCatCategorias();
    }

    /**
     * Retornamos la instancia unica del singleton
     *@param context contexto donde se ejecutaran las peticiones
     *@return Instancia
     */
    public static synchronized CatalogosUsuario getInstance(Context context)
    {
        if(singleton == null)
        {
            singleton = new CatalogosUsuario(context);
        }
        return singleton;
    }

    public static ArrayList<Categoria> getCatCategorias() {
        return catCategorias;
    }

    public static void setCatCategorias(ArrayList<Categoria> catCategorias) {
        CatalogosUsuario.catCategorias = catCategorias;
    }

    public static ArrayList<Categoria> getCategorias() {
        return categorias;
    }

    public static void setCategorias(ArrayList<Categoria> categorias) {
        CatalogosUsuario.categorias = categorias;
    }

    private void cargarCategorias(){
        categorias = new ArrayList<>();
        JSONObject jsonObject = Files.getInstance().getJsonFromInputStream(context.getResources().openRawResource(R.raw.categorias));
        try{
            Gson gson = new Gson();
            Categoria[] categoriaArreglo = gson.fromJson(jsonObject.get("categorias").toString(), Categoria[].class);
            for(int i = 0; i < categoriaArreglo.length; i++) {
                categorias.add(categoriaArreglo[i]);
            }
        } catch(JSONException jsone) {
            Log.e(TAG, jsone.getMessage());
        }
    }

    private void cargarCatCategorias(){
        catCategorias = new ArrayList<>();
        JSONObject jsonObject = Files.getInstance().getJsonFromInputStream(context.getResources().openRawResource(R.raw.cat_categorias));
        try{
            Gson gson = new Gson();
            Categoria[] categoriaArreglo = gson.fromJson(jsonObject.get("categorias").toString(), Categoria[].class);
            for(int i = 0; i < categoriaArreglo.length; i++) {
                catCategorias.add(categoriaArreglo[i]);
            }
        } catch(JSONException jsone) {
            Log.e(TAG, jsone.getMessage());
        }
    }
}
