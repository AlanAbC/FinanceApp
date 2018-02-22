package com.claresti.financeapp;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class AdapterAccounts extends RecyclerView.Adapter<AdapterAccounts.ViewHolderAccounts> implements Comunications.ComunicationsInterface
{
    private static AdapterAccounts adapter;
    ArrayList<ObjCuenta> accounts;
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

    private AdapterAccounts(Context context, InterfaceDataTransfer listener)
    {
        this.accounts = new ArrayList<ObjCuenta>();
        this.context = context;
        this.minID = 0;
        mListener = listener;
    }

    public static synchronized AdapterAccounts getInstance(Context context, InterfaceDataTransfer listener)
    {
        if(adapter == null)
        {
            adapter = new AdapterAccounts(context, listener);
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
    public AdapterAccounts.ViewHolderAccounts onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account, null, false);
        return new AdapterAccounts.ViewHolderAccounts(view);
    }

    /**
     * Funcion de personalizacion de la vista de Cuentas
     * @param holder vista donde se mostrara todo
     * @param position posicion de el objeto que se mostrara
     */
    @Override
    public void onBindViewHolder(final AdapterAccounts.ViewHolderAccounts holder, final int position) {
        if(position == swipedPosition)
        {
            holder.name.setVisibility(View.GONE);
            holder.description.setVisibility(View.GONE);
            holder.imageType.setVisibility(View.GONE);
            holder.money.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(Color.parseColor("#186e1f"));
            holder.edit.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.name.setVisibility(View.VISIBLE);
            holder.description.setVisibility(View.VISIBLE);
            holder.imageType.setVisibility(View.VISIBLE);
            holder.money.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.edit.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
        }
        holder.name.setText(accounts.get(position).getNombre());
        holder.description.setText(accounts.get(position).getDescripcion());
        holder.imageType.setImageResource(R.drawable.acuenta);
        holder.money.setText("$ " + accounts.get(position).getDinero());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAnimation(holder.edit);
                Intent newAccount = new Intent(context, AgregarCuenta.class);
                newAccount.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                newAccount.putExtra("Account", accounts.get(position));
                context.startActivity(newAccount);
                resetSwipe();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAnimation(holder.edit);
                mListener.showDeleteAlert(position, "Atención", "¿Deseas eliminar esta cuenta?");
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
        return accounts.size();
    }

    /**
     * Funcion para sustituir el arrayList de la clase
     * @param accounts - ArrayList de las Categorias
     */
    public void setCategories(ArrayList<ObjCuenta> accounts)
    {
        this.accounts = accounts;
    }

    /**
     * Funcion para añadir Items a el arraylist de categorias
     * @param obj - categoria a añadir
     */
    public void addItem(ObjCuenta obj)
    {
        accounts.add(obj);
        notifyItemInserted(accounts.indexOf(obj));
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
        minID = 0;
        state = 1;
        isLoading = true;
        mListener.showProgressBar(state);
        new Thread(new Runnable() {
            @Override
            public void run() {
                accounts.clear();
                notifyDataSetChanged();
                UserSessionManager session = new UserSessionManager(context);
                HashMap<String,String> user = session.getUserDetails();
                Map<String, String> paramsMovements = new HashMap<String, String>();
                paramsMovements.put("username",user.get(UserSessionManager.KEY_USER));
                Comunications comunications = new Comunications(context, AdapterAccounts.this);
                comunications.getAccounts(Urls.GETACCOUNTS, paramsMovements);
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
        accounts.remove(position);
        notifyItemRemoved(position);
    }

    public void deleteItem(int position, String id)
    {
        Map<String, String> paramsAccounts = new HashMap<String, String>();
        paramsAccounts.put("id", id);
        Comunications comunications = new Comunications(context, AdapterAccounts.this);
        comunications.deleteItem(Urls.DELETEACCOUNT, paramsAccounts, 3, position);
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
                    ObjCuenta[] itemsArray = gson.fromJson(items.toString(), ObjCuenta[].class);
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
            mListener.showSnackbar("No se han podido cargar las cuentas");
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
    public class ViewHolderAccounts extends RecyclerView.ViewHolder{
        TextView name, money, description;
        ImageView imageType, edit, delete;

        public ViewHolderAccounts(View itemView) {
            super(itemView);
            imageType = itemView.findViewById(R.id.imgAccount);
            name = itemView.findViewById(R.id.txt_nameAccount);
            money = itemView.findViewById(R.id.txt_moneyAccount);
            description = itemView.findViewById(R.id.txt_descriptionAccount);
            edit = itemView.findViewById(R.id.img_account_edit);
            delete = itemView.findViewById(R.id.img_account_delete);
        }
    }
}
