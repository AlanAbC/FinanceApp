package com.claresti.financeapp.Dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.claresti.financeapp.Adapters.AdapterCategories;
import com.claresti.financeapp.R;

/**
 * Created by smp_3 on 04/04/2018.
 */

public class DialogCategorias extends DialogFragment {

    private TextView titulo;
    private RecyclerView contenido;
    private Button buttonCancel;
    private AdapterCategories adaptadorSpinnerCategorias;

    public DialogCategorias() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.Theme_AppCompat_Dialog_Alert);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_categorias, container, false);
        contenido = view.findViewById(R.id.dialog_recycler_categorias);
        buttonCancel = view.findViewById(R.id.dialog_categorias_button);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adaptadorSpinnerCategorias = new AdapterCategories(getActivity());
        contenido.setAdapter(adaptadorSpinnerCategorias);
        contenido.setLayoutManager(new LinearLayoutManager(getActivity()));

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
