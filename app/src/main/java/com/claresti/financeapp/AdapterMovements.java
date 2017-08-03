package com.claresti.financeapp;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by smp_3 on 01/08/2017.
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

    @Override
    public ViewHolderMovements onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movimiento, null, false);
        return new ViewHolderMovements(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderMovements holder, int position) {
        holder.date.setText(movements.get(position).getFecha());
        holder.amount.setText("$ " + movements.get(position).getMonto());
        holder.concept.setText(movements.get(position).getConcepto());
        holder.setMovement(movements.get(position));
        if(movements.get(position).getTipo().equals("1")){
            holder.imageType.setImageResource(R.drawable.arrow_up);
            holder.type.setText("Ingreso");
        }else{
            holder.imageType.setImageResource(R.drawable.arrow_down);
            holder.type.setText("Egreso");
        }
    }

    @Override
    public int getItemCount() {
        return movements.size();
    }

    public void setMovements(ArrayList<ObjMovimiento> movements)
    {
        this.movements = movements;
    }

    public void addItem(ObjMovimiento obj)
    {
        movements.add(obj);
        notifyItemInserted(movements.indexOf(obj));
    }

    public void updateContent()
    {
        UserSessionManager session = new UserSessionManager(context);
        HashMap<String,String> user = session.getUserDetails();
        Map<String, String> paramsMovements = new HashMap<String, String>();
        paramsMovements.put("idUser",user.get(UserSessionManager.KEY_ID));
        Comunications comunications = new Comunications(context, view);
        comunications.getMovements(Urls.GETMOVEMENTS, paramsMovements);
    }

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

        public void setMovement(ObjMovimiento movement)
        {
            this.objMovements = movement;
        }
    }
}
