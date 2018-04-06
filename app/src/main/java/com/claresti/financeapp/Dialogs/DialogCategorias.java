package com.claresti.financeapp.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.claresti.financeapp.Adapters.AdaptadorSpinnerCategorias;
import com.claresti.financeapp.Modelos.Categorias;
import com.claresti.financeapp.R;

/**
 * Created by smp_3 on 04/04/2018.
 */

public class DialogCategorias extends Dialog {

    private TextView titulo;
    private RecyclerView contenido;
    private Button buttonCancel;
    private AdaptadorSpinnerCategorias adaptadorSpinnerCategorias;

    public DialogCategorias(Context context) {
        super(context);
    }

    public void setOnItemSelectedListener(AdaptadorSpinnerCategorias.OnItemSelectedListener onItemSelectedListener) {
        adaptadorSpinnerCategorias.setOnItemSelectedListener(onItemSelectedListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_categorias);

        contenido = findViewById(R.id.dialog_recycler_categorias);
        buttonCancel = findViewById(R.id.dialog_categorias_button);

        adaptadorSpinnerCategorias = new AdaptadorSpinnerCategorias(getContext());
        contenido.setLayoutManager(new LinearLayoutManager(getContext()));
        contenido.setAdapter(adaptadorSpinnerCategorias);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
