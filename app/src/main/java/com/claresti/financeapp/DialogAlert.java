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

    private DialogAlertInterface listener;

    public DialogAlert(Context context) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        create();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert_dialog);
        titulo = (TextView) findViewById(R.id.alert_dialog_title);
        mensaje = (TextView) findViewById(R.id.alert_dialog_mensaje);
        aceptar = (Button) findViewById(R.id.alert_dialog_btn_aceptar);
        cancelar = (Button) findViewById(R.id.alert_dialog_btn_cencelar);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.acceptDialog(position);
                dismiss();
            }
        });
    }

    public void setTitle(String titulo){
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
