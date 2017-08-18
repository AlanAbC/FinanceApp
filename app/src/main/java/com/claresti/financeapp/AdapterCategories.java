package com.claresti.financeapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CLARESTI on 04/08/2017.
 */

public class AdapterCategories extends RecyclerView.Adapter<AdapterCategories.ViewHolderCategories>
{
    private static AdapterCategories adapter;
    ArrayList<ObjCategoria> categories;
    private Context context;
    private View view;

    private AdapterCategories(Context context, View view)
    {
        this.categories = new ArrayList<ObjCategoria>();
        this.context = context;
        this.view = view;
    }

    public static synchronized AdapterCategories getInstance(Context context, View view)
    {
        if(adapter == null)
        {
            adapter = new AdapterCategories(context, view);
        }
        return adapter;
    }

    /**
     * Funcion que crea la vista
     * @param parent
     * @param viewType
     * @return view - vista que se mostrara en el recycler view
     */
    @Override
    public AdapterCategories.ViewHolderCategories onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category, null, false);
        return new AdapterCategories.ViewHolderCategories(view);
    }

    /**
     * Funcion de personalizacion de la vista de Categorias
     * @param holder vista donde se mostrara todo
     * @param position posicion de el objeto que se mostrara
     */
    @Override
    public void onBindViewHolder(AdapterCategories.ViewHolderCategories holder, int position) {
        holder.name.setText(categories.get(position).getNombre());
        holder.description.setText(categories.get(position).getDescripcion());
        holder.imageType.setImageResource(R.drawable.acategoria);
    }

    /**
     * Funcion que devuelve el tamaño de el arraylist de categorias
     * @return
     */
    @Override
    public int getItemCount() {
        return categories.size();
    }

    /**
     * Funcion para sustituir el arrayList de la clase
     * @param categories - ArrayList de las Categorias
     */
    public void setCategories(ArrayList<ObjCategoria> categories)
    {
        this.categories = categories;
    }

    /**
     * Funcion para añadir Items a el arraylist de categorias
     * @param obj - categoria a añadir
     */
    public void addItem(ObjCategoria obj)
    {
        categories.add(obj);
        notifyItemInserted(categories.indexOf(obj));
    }

    /**
     * Funcion para actualizar el contenido de el arraylist
     */
    public void updateContent(final ProgressBar progressBar)
    {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                categories.clear();
                UserSessionManager session = new UserSessionManager(context);
                HashMap<String,String> user = session.getUserDetails();
                Map<String, String> paramsMovements = new HashMap<String, String>();
                paramsMovements.put("username",user.get(UserSessionManager.KEY_USER));
                Comunications comunications = new Comunications(context, view);
                comunications.getCategories(Urls.GETCATEGORIES, paramsMovements, progressBar);
            }
        }).start();
    }

    /**
     * Clase que contiene la vista y conexion a el xml
     */
    public class ViewHolderCategories extends RecyclerView.ViewHolder{
        ObjCategoria category;
        TextView name, description;
        ImageView imageType;

        public ViewHolderCategories(View itemView) {
            super(itemView);
            imageType = (ImageView) itemView.findViewById(R.id.imgCategory);
            name = (TextView) itemView.findViewById(R.id.txt_nameCategory);
            description = (TextView) itemView.findViewById(R.id.txt_descriptionCategory);

        }

        /**
         * Funcion para establecer el objeto de categoria de la vista
         * @param category
         */
        public void setCategory(ObjCategoria category)
        {
            this.category = category;
        }
    }
}
