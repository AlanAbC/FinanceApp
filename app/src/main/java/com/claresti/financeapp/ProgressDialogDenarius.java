package com.claresti.financeapp;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by smp_3 on 17/08/2017.
 */

public class ProgressDialogDenarius extends Dialog {
    private ProgressBar progressBar;
    private ImageView dialogImage;
    private TextView description;
    private boolean state = false;

    public ProgressDialogDenarius(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.progress_dialog);
        progressBar = (ProgressBar) findViewById(R.id.dialogProgressbar);
        dialogImage = (ImageView) findViewById(R.id.dialogImg);
        description = (TextView) findViewById(R.id.dialogDescription);
        setCancelable(false);
    }

    public void setActionCorrect(final String message)
    {
        state = false;
        progressBar.animate().scaleX(0.1f).scaleY(0.1f).setDuration(500).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                progressBar.setVisibility(View.GONE);
                progressBar.clearAnimation();
                description.setText(message);
                dialogImage.setScaleX(0.1f);
                dialogImage.setScaleY(0.1f);
                dialogImage.setImageResource(R.drawable.iconcorrect);
                dialogImage.setVisibility(View.VISIBLE);
                dialogImage.animate().scaleX(1.0f).scaleY(1.0f).setDuration(1500).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        state = true;
                        dismiss();

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    public void setActionIncorrect(final String message)
    {
        progressBar.animate().scaleX(0.1f).scaleY(0.1f).setDuration(500).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                progressBar.setVisibility(View.GONE);
                progressBar.clearAnimation();
                description.setText(message);
                dialogImage.setScaleX(0.1f);
                dialogImage.setScaleY(0.1f);
                dialogImage.setImageResource(R.drawable.iconerror);
                dialogImage.setVisibility(View.VISIBLE);
                dialogImage.animate().scaleX(1.0f).scaleY(1.0f).setDuration(1500).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        dismiss();

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
