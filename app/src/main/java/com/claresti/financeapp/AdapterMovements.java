package com.claresti.financeapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private int minID;
    private int swipedPosition = -1, lastSwipedPosition = -1;
    public static boolean isLoading = false;

    private AdapterMovements(Context context, View view)
    {
        this.movements = new ArrayList<ObjMovimiento>();
        this.context = context;
        this.view = view;
        this.minID = 0;
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
        if(position == swipedPosition)
        {
            holder.date.setVisibility(View.GONE);
            holder.amount.setVisibility(View.GONE);
            holder.concept.setVisibility(View.GONE);
            holder.type.setVisibility(View.GONE);
            holder.imageType.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(Color.parseColor("#185e1f"));
            holder.edit.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.edit.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.date.setVisibility(View.VISIBLE);
            holder.date.setText(movements.get(position).getFecha() + movements.get(position).getID());
            holder.amount.setVisibility(View.VISIBLE);
            holder.amount.setText("$ " + movements.get(position).getMonto());
            holder.concept.setVisibility(View.VISIBLE);
            holder.concept.setText(movements.get(position).getConcepto());
            holder.type.setVisibility(View.VISIBLE);
            holder.imageType.setVisibility(View.VISIBLE);
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

        /*holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int positionMovement = position;
                Intent newMovement = new Intent(context, Movimientos.class);
                newMovement.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                newMovement.putExtra("Movimiento", movements.get(positionMovement));
                context.startActivity(newMovement);
            }
        });*/

    }

    public void setSwipedPosition(int position)
    {
        lastSwipedPosition = swipedPosition;
        swipedPosition = position;
        notifyItemChanged(lastSwipedPosition);
        notifyItemChanged(swipedPosition);
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
     * @param progressBar - spinner que indica que se esta realizando el proceso
     */
    public void updateContent(final ProgressBar progressBar)
    {
        minID = 0;
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                movements.clear();
                notifyDataSetChanged();
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
     * Funcion que carga mas movimientos al llegar hasta abajo de la pantalla
     * @param progressBar - spinner que indica que se esta realizando el proceso
     */
    public void loadMoreItems(final ProgressBar progressBar)
    {
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserSessionManager session = new UserSessionManager(context);
                HashMap<String,String> user = session.getUserDetails();
                Map<String, String> paramsMovements = new HashMap<String, String>();
                paramsMovements.put("idUser",user.get(UserSessionManager.KEY_ID));
                paramsMovements.put("minIdMovement", minID + "");
                Comunications comunications = new Comunications(context, view);
                comunications.getMovements(Urls.GETMOVEMENTS, paramsMovements, progressBar);
            }
        }).start();
    }

    /**
     * Funcion que retorna el ID minimo del adaptador(cargar mas items)
     * @return minID - ID minimo del adaptador
     */
    public int getId()
    {
        return minID;
    }

    public void cleanData()
    {
        movements.clear();
        notifyDataSetChanged();
    }

    /**
     * Clase que contiene la vista y conexion a el xml
     */
    public class ViewHolderMovements extends RecyclerView.ViewHolder{
        ObjMovimiento objMovements;
        TextView type, date, amount, concept;
        ImageView imageType, edit, delete;
        public ViewHolderMovements(View itemView) {
            super(itemView);
            imageType = (ImageView) itemView.findViewById(R.id.tipoMovimiento);
            type = (TextView) itemView.findViewById(R.id.txt_tipoMovimiento);
            date = (TextView) itemView.findViewById(R.id.txt_fechaMovimiento);
            amount = (TextView) itemView.findViewById(R.id.txt_MontoMovimiento);
            concept = (TextView) itemView.findViewById(R.id.txt_ConceptoMovimiento);
            edit = (ImageView) itemView.findViewById(R.id.img_movement_edit);
            delete = (ImageView) itemView.findViewById(R.id.img_movement_delete);

        }



    }
}
