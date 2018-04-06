package com.claresti.financeapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.claresti.financeapp.Modelos.CatCategorias;
import com.claresti.financeapp.Modelos.Categorias;
import com.claresti.financeapp.R;
import com.claresti.financeapp.Tools.CatalogosUsuario;
import java.util.ArrayList;

/**
 * Created by smp_3 on 04/04/2018.
 */

public class AdaptadorSpinnerCategorias extends RecyclerView.Adapter<AdaptadorSpinnerCategorias.ViewHolderCategorias> {
    private Context context;
    private ArrayList<Categorias> categorias;
    private ArrayList<CatCategorias> catCategorias;

    private OnItemSelectedListener itemSelectedListener;

    /**
     * bandera para cateCategorias o categorias
     * false - catCategorias
     * true = categorias
     */
    private boolean mode = false;

    public AdaptadorSpinnerCategorias(Context context) {
        this.context = context;
        CatalogosUsuario.getInstance(context);
        this.catCategorias = CatalogosUsuario.catCategorias;
        this.categorias = CatalogosUsuario.categorias;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener itemSelectedListener){
        mode = false;
        this.itemSelectedListener = itemSelectedListener;
    }

    /**
     * Funcion que crea la vista
     * @return view - vista que se mostrara en el recycler view
     */
    @Override
    public ViewHolderCategorias onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_categorias, null, false);
        return new ViewHolderCategorias(view);
    }

    /**
     * Funcion de personalizacion de la vista de Cuentas
     * @param holder vista donde se mostrara todo
     * @param position posicion de el objeto que se mostrara
     */
    @Override
    public void onBindViewHolder(ViewHolderCategorias holder, int position) {
        if(mode){
            holder.nombre.setText(categorias.get(position).getNombre());
            holder.iconoSiguiente.setVisibility(View.GONE);
        } else {
            holder.nombre.setText(catCategorias.get(position).getNombre());
            holder.iconoSiguiente.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Funcion que devuelve el tamaño de el arraylist de categorias
     * @return
     */
    @Override
    public int getItemCount() {
        if(mode){
            return categorias.size();
        }
        return catCategorias.size();
    }


    /**
     * Clase que contiene la vista y conexion a el xml
     */
    public class ViewHolderCategorias extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nombre;
        ImageView icono, iconoSiguiente;

        public ViewHolderCategorias(View itemView) {
            super(itemView);
            icono = itemView.findViewById(R.id.categorias_icon);
            nombre = itemView.findViewById(R.id.categorias_nombre);
            iconoSiguiente = itemView.findViewById(R.id.categorias_button_mostrar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mode) {
                if(itemSelectedListener != null)itemSelectedListener.itemSelected(categorias.get(getAdapterPosition()));
                return;
            }
            mode = true;
            categorias = CatalogosUsuario.buscarCategoria(catCategorias.get(getAdapterPosition()));
            notifyDataSetChanged();
        }
    }

    public interface OnItemSelectedListener{
        void itemSelected(Categorias categoria);
    }
}
