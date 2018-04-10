package com.claresti.financeapp.Dialogs;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.claresti.financeapp.R;
import com.claresti.financeapp.Tools.ViewLoadingDotsGrow;

/**
 * Clase para mostrar un dialog de retroalimentacion de el usuario
 */
public class DialogProgress extends DialogFragment {
    private TextView mensaje;
    private Button buttonAceptar;
    private ViewLoadingDotsGrow progress;
    private String msgDialog;
    private RelativeLayout layoutMensaje;
    private OnDismissListener onDismissListener;

    public DialogProgress() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_progress, container, false);
        mensaje = view.findViewById(R.id.dialog_mensaje);
        buttonAceptar = view.findViewById(R.id.dialog_button_aceptar);
        progress = view.findViewById(R.id.dialog_progress);
        layoutMensaje = view.findViewById(R.id.dialog_layout_mensaje);
        setCancelable(false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(onDismissListener != null) onDismissListener.onDismiss();
            }
        });
        if(msgDialog != null) mensaje.setText(msgDialog);
    }

    public void setArguments(String msg) {
        this.msgDialog = msg;
    }

    /**
     * funcion para mostrar un mensaje en el dialog
     * @param msg mensaje a mostrar
     */
    public void setMessage(String msg){
        mensaje.setText(msg);
    }//fin setMessage

    /**
     * funcion para mostrar un mensaje de invalido en el dialog
     * @param msg mensaje a mostrar
     */
    public void setOk(String msg){
        layoutMensaje.setVisibility(View.VISIBLE);
        Animation slideUp = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_center);
        layoutMensaje.startAnimation(slideUp);
        mensaje.setText(msg);
        progress.setVisibility(View.GONE);
    }//fin setError

    /**
     * funcion para mostrar un mensaje de invalido en el dialog
     * @param msg mensaje a mostrar
     */
    public void setError(String msg){
        Animation slideUp = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_center);
        layoutMensaje.setVisibility(View.VISIBLE);
        layoutMensaje.startAnimation(slideUp);
        mensaje.setText(msg);
        progress.setVisibility(View.GONE);
    }//fin setError

    public void setOnDismissListener(OnDismissListener onDismissListener){
        this.onDismissListener = onDismissListener;
    }

    public interface OnDismissListener{
        void onDismiss();
    }
}
