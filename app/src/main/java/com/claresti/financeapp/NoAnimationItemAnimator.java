package com.claresti.financeapp;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

/**
 * Created by CLARESTI on 24/08/2017.
 * Clase para quitar animaciones de eliminar elemento
 */

public class NoAnimationItemAnimator extends SimpleItemAnimator {
    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        dispatchRemoveFinished(holder);

        return false;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        dispatchAddFinished(holder);

        return false;
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        dispatchMoveFinished(holder);

        return false;
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromX, int fromY, int toX, int toY) {
        dispatchChangeFinished(oldHolder, true);
        dispatchChangeFinished(newHolder, false);

        return false;
    }

    @Override
    public void runPendingAnimations() {
        // stub
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {
        // stub
    }

    @Override
    public void endAnimations() {
        // stub
    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
