package com.claresti.financeapp;

import android.content.Context;
import android.media.Image;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CLARESTI on 01/08/2017.
 */

public class AdapterMovements extends RecyclerView.Adapter<AdapterMovements.ViewHolderMovements>
{
    private static AdapterMovements adapter;
    ArrayList<ObjMovimiento> movements;
    private Context context;
    private View view;

    private AdapterMovements(Context context, View view)
    {
        this.movements = new ArrayList<ObjMovimiento>();
        this.context = context;
        this.view = view;
    }

    public static synchronized AdapterMovements getInstance(Context context, View view)
    {
        if(adapter == null)
        {
            adapter = new AdapterMovements(context, view);
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
    public ViewHolderMovements onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movimiento, null, false);
        return new ViewHolderMovements(view);
    }

    /**
     * Funcion de personalizacion de la vista de cada Movimiento
     * @param holder vista donde se mostrara todo
     * @param position posicion de el objeto que se mostrara
     */
    @Override
    public void onBindViewHolder(ViewHolderMovements holder, int position) {
        holder.date.setText(movements.get(position).getFecha());
        holder.amount.setText("$ " + movements.get(position).getMonto());
        holder.concept.setText(movements.get(position).getConcepto());
        holder.setMovement(movements.get(position));
        if(movements.get(position).getTipo().equals("1")){
            holder.imageType.setImageResource(R.drawable.arrow_up);
            holder.type.setText("Ingreso");
        }else if(movements.get(position).getTipo().equals("2")){
            holder.imageType.setImageResource(R.drawable.arrow_down);
            holder.type.setText("Egreso");
        }else
        {
            holder.imageType.setImageResource(R.drawable.icon_transfer);
            holder.type.setText("Transferencia");
        }
    }

    /**
     * Funcion que devuelve el tamaño de el arraylist de movimientos
     * @return
     */
    @Override
    public int getItemCount() {
        return movements.size();
    }

    /**
     * Funcion para sustituir el arrayList de la clase
     * @param movements - ArrayList de los Movimientos
     */
    public void setMovements(ArrayList<ObjMovimiento> movements)
    {
        this.movements = movements;
    }

    /**
     * Funcion para añadir Items a el arraylist de movimientos
     * @param obj - movimiento a añadir
     */
    public void addItem(ObjMovimiento obj)
    {
        movements.add(obj);
        notifyItemInserted(movements.indexOf(obj));
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

                movements.clear();
                UserSessionManager session = new UserSessionManager(context);
                HashMap<String,String> user = session.getUserDetails();
                Map<String, String> paramsMovements = new HashMap<String, String>();
                paramsMovements.put("idUser",user.get(UserSessionManager.KEY_ID));
                Comunications comunications = new Comunications(context, view);
                comunications.getMovements(Urls.GETMOVEMENTS, paramsMovements, progressBar);
            }
        }).start();
    }

    /**
     * Clase que contiene la vista y conexion a el xml
     */
    public class ViewHolderMovements extends RecyclerView.ViewHolder{
        ObjMovimiento objMovements;
        TextView type, date, amount, concept;
        ImageView imageType;
        public ViewHolderMovements(View itemView) {
            super(itemView);
            imageType = (ImageView) itemView.findViewById(R.id.tipoMovimiento);
            type = (TextView) itemView.findViewById(R.id.txt_tipoMovimiento);
            date = (TextView) itemView.findViewById(R.id.txt_fechaMovimiento);
            amount = (TextView) itemView.findViewById(R.id.txt_MontoMovimiento);
            concept = (TextView) itemView.findViewById(R.id.txt_ConceptoMovimiento);

        }

        /**
         * Funcion para establecer el objeto de movimiento de la vista
         * @param movement
         */
        public void setMovement(ObjMovimiento movement)
        {
            this.objMovements = movement;
        }
    }
}
