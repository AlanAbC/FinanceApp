package com.claresti.financeapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CLARESTI on 04/08/2017.
 */

public class AdapterAccounts extends RecyclerView.Adapter<AdapterAccounts.ViewHolderAccounts>
{
    private static AdapterAccounts adapter;
    ArrayList<ObjCuenta> accounts;
    private Context context;
    private View view;

    private AdapterAccounts(Context context, View view)
    {
        this.accounts = new ArrayList<ObjCuenta>();
        this.context = context;
        this.view = view;
    }

    public static synchronized AdapterAccounts getInstance(Context context, View view)
    {
        if(adapter == null)
        {
            adapter = new AdapterAccounts(context, view);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movimiento, null, false);
        return new AdapterAccounts.ViewHolderAccounts(view);
    }

    /**
     * Funcion de personalizacion de la vista de Cuentas
     * @param holder vista donde se mostrara todo
     * @param position posicion de el objeto que se mostrara
     */
    @Override
    public void onBindViewHolder(AdapterAccounts.ViewHolderAccounts holder, int position) {
        holder.type.setText(accounts.get(position).getNombre());
        holder.concept.setText(accounts.get(position).getDescripcion());
        holder.imageType.setImageResource(R.drawable.acuenta);
        holder.date.setText("Ingreso");
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
    }

    /**
     * Funcion para actualizar el contenido de el arraylist
     */
    public void updateContent()
    {
        accounts.clear();
        UserSessionManager session = new UserSessionManager(context);
        HashMap<String,String> user = session.getUserDetails();
        Map<String, String> paramsMovements = new HashMap<String, String>();
        paramsMovements.put("username",user.get(UserSessionManager.KEY_USER));
        Comunications comunications = new Comunications(context, view);
        comunications.getAccounts(Urls.GETACCOUNTS, paramsMovements);
    }

    /**
     * Clase que contiene la vista y conexion a el xml
     */
    public class ViewHolderAccounts extends RecyclerView.ViewHolder{
        ObjCuenta account;
        TextView type, date, amount, concept;
        ImageView imageType;

        public ViewHolderAccounts(View itemView) {
            super(itemView);
            imageType = (ImageView) itemView.findViewById(R.id.tipoMovimiento);
            type = (TextView) itemView.findViewById(R.id.txt_tipoMovimiento);
            date = (TextView) itemView.findViewById(R.id.txt_fechaMovimiento);
            amount = (TextView) itemView.findViewById(R.id.txt_MontoMovimiento);
            concept = (TextView) itemView.findViewById(R.id.txt_ConceptoMovimiento);

        }

        /**
         * Funcion para establecer el objeto de cuenta de la vista
         * @param account
         */
        public void setAccount(ObjCuenta account)
        {
            this.account = account;
        }
    }
}
