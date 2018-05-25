package com.claresti.financeapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.claresti.financeapp.Modelos.Categoria;
import com.claresti.financeapp.R;

import java.util.ArrayList;

public class AdapterCategoriesDialog extends RecyclerView.Adapter<AdapterCategoriesDialog.ViewHolderCategorias> {
    private Context context;
    private ArrayList<Categoria> categorias;
    public OnSelectedItem listener;

    public AdapterCategoriesDialog(Context context) {
        this.context = context;
        categorias = new ArrayList<>();
    }

    public void setCategorias(ArrayList<Categoria> categorias){
        this.categorias = categorias;
        notifyDataSetChanged();
    }

    public void setListener(OnSelectedItem listener) {
        this.listener = listener;
    }

    /**
     * Funcion que crea la vista
     * @return view - vista que se mostrara en el recycler view
     */
    @Override
    public AdapterCategoriesDialog.ViewHolderCategorias onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_categorias, null, false);
        return new AdapterCategoriesDialog.ViewHolderCategorias(view);
    }

    /**
     * Funcion de personalizacion de la vista de Cuentas
     * @param holder vista donde se mostrara todo
     * @param position posicion de el objeto que se mostrara
     */
    @Override
    public void onBindViewHolder(AdapterCategoriesDialog.ViewHolderCategorias holder, int position) {
        holder.nombre.setText(categorias.get(position).getName());
        holder.description.setText(categorias.get(position).getDescription());
        Drawable drawable = context.getResources().getDrawable(R.drawable.circular_image_view);
        drawable.clearColorFilter();
        ColorFilter filter = new LightingColorFilter( Color.parseColor(categorias.get(position).getCategory_color()), Color.parseColor(categorias.get(position).getCategory_color()));
        drawable.setColorFilter(filter);
        holder.icono.setBackground(drawable);
    }

    /**
     * Funcion que devuelve el tamaño de el arraylist de categorias
     * @return
     */
    @Override
    public int getItemCount() {
        return categorias.size();
    }


    /**
     * Clase que contiene la vista y conexion a el xml
     */
    public class ViewHolderCategorias extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nombre;
        TextView description;
        ImageView icono;

        public ViewHolderCategorias(View itemView) {
            super(itemView);
            icono = itemView.findViewById(R.id.categorias_icon);
            nombre = itemView.findViewById(R.id.categorias_nombre);
            description = itemView.findViewById(R.id.categorias_descripcion);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.itemSelected(categorias.get(getAdapterPosition()));
        }
    }

    public interface OnSelectedItem{
        void itemSelected(Categoria item);
    }
}
