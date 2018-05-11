package com.claresti.financeapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.claresti.financeapp.Modelos.Cuenta;
import com.claresti.financeapp.R;

import java.util.ArrayList;

public class AdapterAccounts extends RecyclerView.Adapter<AdapterAccounts.ViewHolderCuentas>{
    private Context context;
    private ArrayList<Cuenta> cuentas;

    public AdapterAccounts(Context context) {
        this.context = context;
        cuentas = new ArrayList<>();
    }

    public void setCategorias(ArrayList<Cuenta> cuentas){
        this.cuentas = cuentas;
        notifyDataSetChanged();
    }

    /**
     * Funcion que crea la vista
     * @return view - vista que se mostrara en el recycler view
     */
    @Override
    public AdapterAccounts.ViewHolderCuentas onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_cuentas, null, false);
        return new AdapterAccounts.ViewHolderCuentas(view);
    }

    /**
     * Funcion de personalizacion de la vista de Cuentas
     * @param holder vista donde se mostrara todo
     * @param position posicion de el objeto que se mostrara
     */
    @Override
    public void onBindViewHolder(AdapterAccounts.ViewHolderCuentas holder, int position) {
        holder.nombre.setText(cuentas.get(position).getName());
        holder.description.setText(cuentas.get(position).getDescription());
        holder.money.setText(cuentas.get(position).getMoney() + "");
    }

    /**
     * Funcion que devuelve el tamaño de el arraylist de categorias
     * @return
     */
    @Override
    public int getItemCount() {
        return cuentas.size();
    }


    /**
     * Clase que contiene la vista y conexion a el xml
     */
    public class ViewHolderCuentas extends RecyclerView.ViewHolder{
        TextView nombre;
        TextView description;
        TextView money;

        public ViewHolderCuentas(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txt_nameAccount);
            description = itemView.findViewById(R.id.txt_descriptionAccount);
            money = itemView.findViewById(R.id.txt_moneyAccount);
        }
    }
}