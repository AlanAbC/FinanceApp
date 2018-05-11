package com.claresti.financeapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.claresti.financeapp.Modelos.Movimiento;
import com.claresti.financeapp.Tools.Urls;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterListSwipe extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeItemTouchHelper.SwipeHelperAdapter {

    private Context context;
    private ArrayList<Movimiento> movimientos;
    private ArrayList<Movimiento> movimientosSwiped;

    public AdapterListSwipe(Context context) {
        context = context;
        movimientos = new ArrayList<>();
    }

    public void setMovimientos(ArrayList<Movimiento> movimientos){
        this.movimientos = movimientos;
        notifyDataSetChanged();
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
            itemView.setBackgroundColor(context.getResources().getColor(R.color.grey_5));
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
            final Movimiento p = movimientos.get(position);
            if (p.getType() == 1) {
                view.imageType.setImageResource(R.drawable.arrow_up);
                view.type.setText("Ingreso");
            } else if(p.getType() == 2) {
                view.imageType.setImageResource(R.drawable.arrow_down);
                view.type.setText("Egreso");
            } else {
                view.imageType.setImageResource(R.drawable.icon_transfer);
                view.type.setText("Transferencia");
            }
            view.date.setText(p.getDate());
            view.amount.setText(p.getAmount());
            view.concept.setText(p.getConcept());

            view.undo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movimientos.get(position).setInSwiped(false);
                    movimientosSwiped.remove(movimientosSwiped.get(position));
                    notifyItemChanged(position);
                }
            });

            if (p.isInSwiped()) {
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
                for (Movimiento s : movimientosSwiped) {
                    int index_removed = movimientos.indexOf(s);
                    if (index_removed != -1) {
                        movimientos.remove(index_removed);
                        notifyItemRemoved(index_removed);
                    }
                }
                movimientosSwiped.clear();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return movimientos.size();
    }

    @Override
    public void onItemDismiss(int position) {

        // handle when double swipe
        if (movimientos.get(position).isInSwiped()) {
            movimientosSwiped.remove(movimientos.get(position));
            movimientos.remove(position);
            notifyItemRemoved(position);
            return;
        }

        movimientos.get(position).setInSwiped(true);
        movimientosSwiped.add(movimientos.get(position));
        notifyItemChanged(position);
    }

    public void deleteItemFromAdapter(int position)
    {
        movimientos.remove(position);
        notifyItemRemoved(position);
    }

}