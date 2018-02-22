package com.claresti.financeapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by smp_3 on 21/09/2017.
 */

public class DialogAlert extends Dialog
{
    private TextView titulo, mensaje;
    private Button aceptar, cancelar;
    private int position;

    private DialogAlertInterface mListener;

    public DialogAlert(Context context, DialogAlertInterface listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert_dialog);
        titulo = findViewById(R.id.alert_dialog_title);
        mensaje = findViewById(R.id.alert_dialog_mensaje);
        aceptar = findViewById(R.id.alert_dialog_btn_aceptar);
        cancelar = findViewById(R.id.alert_dialog_btn_cencelar);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.acceptDialog(position);
                dismiss();
            }
        });
    }



    public void setTitulo(String titulo){
        this.titulo.setText(titulo);
    }

    public void setMessage(String message){
        mensaje.setText(message);
    }

    public void setId(int position){
        this.position = position;
    }

    public interface DialogAlertInterface{
        void acceptDialog(int position);
        void cancelDialog();
    }
}
