package com.claresti.financeapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterListSwipe extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeItemTouchHelper.SwipeHelperAdapter, Comunications.ComunicationsInterface {

    private ArrayList<ObjMovimiento> items = new ArrayList<>();
    private ArrayList<ObjMovimiento> items_swiped = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

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

    public interface OnItemClickListener {
        void onItemClick(View view, ObjMovimiento obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setInterfaceDataTransfer(InterfaceDataTransfer listener){
        this.mListener = listener;
    }

    public AdapterListSwipe(Context context) {
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder implements SwipeItemTouchHelper.TouchViewHolder {
        public TextView type, date, amount, concept, undo;
        public ImageView imageType, edit, delete;
        public View lyt_parent;

        public OriginalViewHolder(View itemView) {
            super(itemView);
            imageType = itemView.findViewById(R.id.tipoMovimiento);
            type = itemView.findViewById(R.id.txt_tipoMovimiento);
            date = itemView.findViewById(R.id.txt_fechaMovimiento);
            amount = itemView.findViewById(R.id.txt_MontoMovimiento);
            concept = itemView.findViewById(R.id.txt_ConceptoMovimiento);
            edit = itemView.findViewById(R.id.img_movement_edit);
            delete = itemView.findViewById(R.id.img_movement_delete);
            undo = itemView.findViewById(R.id.movimiento_undo);
            lyt_parent = itemView.findViewById(R.id.lyt_parent);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(ctx.getResources().getColor(R.color.grey_5));
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movimiento, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;
            final ObjMovimiento p = items.get(position);
            if (p.getTipo().equals("1")) {
                view.imageType.setImageResource(R.drawable.arrow_up);
                view.type.setText("Ingreso");
            } else if(p.getTipo().equals("2")) {
                view.imageType.setImageResource(R.drawable.arrow_down);
                view.type.setText("Egreso");
            } else {
                view.imageType.setImageResource(R.drawable.icon_transfer);
                view.type.setText("Transferencia");
            }
            view.date.setText(p.getFecha());
            view.amount.setText(p.getMonto());
            view.concept.setText(p.getConcepto());
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

            view.undo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    items.get(position).setSwiped(false);
                    items_swiped.remove(items.get(position));
                    notifyItemChanged(position);
                }
            });

            if (p.isSwiped()) {
                view.lyt_parent.setVisibility(View.GONE);
            } else {
                view.lyt_parent.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                for (ObjMovimiento s : items_swiped) {
                    int index_removed = items.indexOf(s);
                    if (index_removed != -1) {
                        items.remove(index_removed);
                        notifyItemRemoved(index_removed);
                    }
                }
                items_swiped.clear();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onItemDismiss(int position) {

        // handle when double swipe
        if (items.get(position).isSwiped()) {
            items_swiped.remove(items.get(position));
            items.remove(position);
            notifyItemRemoved(position);
            return;
        }

        items.get(position).setSwiped(true);
        items_swiped.add(items.get(position));
        notifyItemChanged(position);
    }

    public void loadMoreItems()
    {
        isLoading = true;
        state = 1;
        mListener.showProgressBar(state);
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserSessionManager session = new UserSessionManager(ctx);
                HashMap<String,String> user = session.getUserDetails();
                Map<String, String> paramsMovements = new HashMap<String, String>();
                paramsMovements.put("idUser",user.get(UserSessionManager.KEY_ID));
                paramsMovements.put("minIdMovement", minID + "");
                Comunications comunications = new Comunications(ctx, AdapterListSwipe.this);
                comunications.getMovements(Urls.GETMOVEMENTS, paramsMovements);
            }
        }).start();
    }

    public void updateContent()
    {
        minID = 0;
        state = 1;
        isLoading = true;
        mListener.showProgressBar(state);
        new Thread(new Runnable() {
            @Override
            public void run() {
                items.clear();
                notifyDataSetChanged();
                UserSessionManager session = new UserSessionManager(ctx);
                HashMap<String,String> user = session.getUserDetails();
                Map<String, String> paramsMovements = new HashMap<>();
                paramsMovements.put("idUser",user.get(UserSessionManager.KEY_ID));
                Comunications comunications = new Comunications(ctx, AdapterListSwipe.this);
                comunications.getMovements(Urls.GETMOVEMENTS, paramsMovements);
            }
        }).start();
    }

    /**
     * Funcion para añadir Items a el arraylist de movimientos
     * @param obj - movimiento a añadir
     */
    public void addItem(ObjMovimiento obj)
    {
        items.add(obj);
        notifyItemInserted(items.indexOf(obj));
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

    public void deleteItem(int position)
    {
        Map<String, String> paramsMovements = new HashMap<>();
        paramsMovements.put("id", items.get(position).getID());
        Comunications comunications = new Comunications(ctx, this);
        comunications.deleteItem(Urls.DELETEMOVEMENT, paramsMovements, 1, position);
    }

    public void deleteItemFromAdapter(int position)
    {
        items.remove(position);
        notifyItemRemoved(position);
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
                    ObjMovimiento[] itemsArray = gson.fromJson(items.toString(), ObjMovimiento[].class);
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
            mListener.showSnackbar("No se han podido cargar los movimientos");
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

}