package com.claresti.financeapp;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CLARESTI on 04/08/2017.
 */

public class AdapterCategories extends RecyclerView.Adapter<AdapterCategories.ViewHolderCategories> implements Comunications.ComunicationsInterface
{
    private static AdapterCategories adapter;
    ArrayList<ObjCategoria> categories;
    private Context context;
    private int minID;
    private int swipedPosition = -1, lastSwipedPosition = -1;
    public static boolean isLoading = false;

    /**
     * Estados para mostrar los diferentes progress y comportamientos mientras se descargan datos
     * 0 - nada
     * 1 - actualizando contenido
     * 2 - cargando mas datos
     */
    private int state = 0;

    private InterfaceDataTransfer mListener;

    private AdapterCategories(Context context, InterfaceDataTransfer listener)
    {
        this.categories = new ArrayList<>();
        this.context = context;
        mListener = listener;
        this.minID = 0;
    }

    public static synchronized AdapterCategories getInstance(Context context, InterfaceDataTransfer listener)
    {
        if(adapter == null)
        {
            adapter = new AdapterCategories(context, listener);
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
    public void onBindViewHolder(final AdapterCategories.ViewHolderCategories holder, final int position) {
        if(position == swipedPosition)
        {
            holder.name.setVisibility(View.GONE);
            holder.description.setVisibility(View.GONE);
            holder.imageType.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(Color.parseColor("#186e1f"));
            holder.edit.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.name.setVisibility(View.VISIBLE);
            holder.description.setVisibility(View.VISIBLE);
            holder.imageType.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.edit.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
        }
        holder.name.setText(categories.get(position).getNombre());
        holder.description.setText(categories.get(position).getDescripcion());
        holder.imageType.setImageResource(R.drawable.acategoria);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAnimation(holder.edit);
                Intent newCategory = new Intent(context, AgregarCategoria.class);
                newCategory.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                newCategory.putExtra("Category", categories.get(position));
                context.startActivity(newCategory);
                resetSwipe();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAnimation(holder.delete);
                mListener.showDeleteAlert(position, "Atención", "Se eliminaran todos los movimientos asociados con esta categoria");
            }
        });
    }

    public void setSwipedPosition(int position)
    {
        lastSwipedPosition = swipedPosition;
        swipedPosition = position;
        notifyItemChanged(lastSwipedPosition);
        notifyItemChanged(swipedPosition);
    }

    public void resetSwipe()
    {
        lastSwipedPosition = -1;
        swipedPosition = -1;
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
        int ID = Integer.parseInt(obj.getID());
        if(minID == 0)
        {
            minID = ID;
        }
        else if(ID < minID)
        {
            minID = ID;
        }
    }

    /**
     * Funcion para actualizar el contenido de el arraylist
     */
    public void updateContent()
    {
        state = 1;
        mListener.showProgressBar(state);
        new Thread(new Runnable() {
            @Override
            public void run() {
                categories.clear();
                notifyDataSetChanged();
                UserSessionManager session = new UserSessionManager(context);
                HashMap<String,String> user = session.getUserDetails();
                Map<String, String> paramsMovements = new HashMap<String, String>();
                paramsMovements.put("username",user.get(UserSessionManager.KEY_USER));
                Comunications comunications = new Comunications(context, AdapterCategories.this);
                comunications.getCategories(Urls.GETCATEGORIES, paramsMovements);
            }
        }).start();
    }

    public int getId()
    {
        return minID;
    }

    public int getSwipedPosition()
    {
        return swipedPosition;
    }

    public void addAnimation(final View view)
    {
        view.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        addAnimation(view);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    public void deleteItemFromAdapter(int position)
    {
        categories.remove(position);
        notifyItemRemoved(position);
    }

    public void deleteItem(int position)
    {
        Map<String, String> paramsCategory = new HashMap<>();
        paramsCategory.put("id", categories.get(position).getID());
        Comunications comunications = new Comunications(context, this);
        comunications.deleteItem(Urls.DELETECATEGORY, paramsCategory, 2, position);
    }

    @Override
    public void showData(String response) {
        try
        {
            JSONObject jsonObject = new JSONObject(response);
            String state = jsonObject.getString("state");
            Gson gson = new Gson();
            switch(state)
            {
                case "1":
                    JSONArray items = jsonObject.getJSONArray("items");
                    ObjCategoria[] itemsArray = gson.fromJson(items.toString(), ObjCategoria[].class);
                    for(int i = 0; i < itemsArray.length; i++)
                    {
                        addItem(itemsArray[i]);

                    }
                    break;
                case "0":
                    String mensaje = jsonObject.getString("message");
                    mListener.showSnackbar(mensaje);
                    break;
            }
            mListener.hideProgressBar(this.state);
            isLoading = false;

        }
        catch(JSONException jsone)
        {
            mListener.showSnackbar("No se han podido cargar las categorias");
        }
    }

    @Override
    public void errorResponse() {
        mListener.hideProgressBar(this.state);
    }

    @Override
    public void deleteItemOnAdapter(int position) {
        deleteItemFromAdapter(position);
    }

    @Override
    public void showSnackBarFromComunications(String mensaje) {
        mListener.showSnackbar(mensaje);
    }

    /**
     * Clase que contiene la vista y conexion a el xml
     */
    public class ViewHolderCategories extends RecyclerView.ViewHolder{
        ObjCategoria category;
        TextView name, description;
        ImageView imageType, edit, delete;

        public ViewHolderCategories(View itemView) {
            super(itemView);
            imageType = (ImageView) itemView.findViewById(R.id.imgCategory);
            name = (TextView) itemView.findViewById(R.id.txt_nameCategory);
            description = (TextView) itemView.findViewById(R.id.txt_descriptionCategory);
            edit = (ImageView) itemView.findViewById(R.id.img_category_edit);
            delete = (ImageView) itemView.findViewById(R.id.img_category_delete);
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
