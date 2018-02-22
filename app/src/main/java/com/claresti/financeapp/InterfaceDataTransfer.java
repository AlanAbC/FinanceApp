package com.claresti.financeapp;

/**
 * Created by smp_3 on 13/02/2018.
 */

public interface InterfaceDataTransfer {
    void showDeleteAlert(int position, String titulo, String mensaje);
    void showProgressBar(int state);
    void hideProgressBar(int state);
    void showSnackbar(String mensaje);
}
