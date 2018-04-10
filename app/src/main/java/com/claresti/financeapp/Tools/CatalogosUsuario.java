package com.claresti.financeapp.Tools;

import android.content.Context;
import android.util.Log;

import com.claresti.financeapp.Modelos.Categorias;
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
    public static ArrayList<CatCategorias> catCategorias;
    public static ArrayList<Categorias> categorias;

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

    public static ArrayList<CatCategorias> getCatCategorias() {
        return catCategorias;
    }

    public static void setCatCategorias(ArrayList<CatCategorias> catCategorias) {
        CatalogosUsuario.catCategorias = catCategorias;
    }

    public static ArrayList<Categorias> getCategorias() {
        return categorias;
    }

    public static void setCategorias(ArrayList<Categorias> categorias) {
        CatalogosUsuario.categorias = categorias;
    }

    private void cargarCategorias(){
        categorias = new ArrayList<>();
        JSONObject jsonObject = Files.getInstance().getJsonFromInputStream(context.getResources().openRawResource(R.raw.categorias));
        try{
            Gson gson = new Gson();
            Categorias[] categoriasArreglo = gson.fromJson(jsonObject.get("categorias").toString(), Categorias[].class);
            for(int i = 0; i < categoriasArreglo.length; i++) {
                categorias.add(categoriasArreglo[i]);
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
            CatCategorias[] categoriasArreglo = gson.fromJson(jsonObject.get("categorias").toString(), CatCategorias[].class);
            for(int i = 0; i < categoriasArreglo.length; i++) {
                catCategorias.add(categoriasArreglo[i]);
            }
        } catch(JSONException jsone) {
            Log.e(TAG, jsone.getMessage());
        }
    }

    public static ArrayList<Categorias> buscarCategoria(CatCategorias catCategorias){
        ArrayList<Categorias> arrayList = new ArrayList<>();
        for(int i = 0; i < categorias.size(); i++) {
            if(categorias.get(i).getId_categoria() == catCategorias.getId())arrayList.add(categorias.get(i));
        }
        return arrayList;
    }
}
