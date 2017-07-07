package com.claresti.financeapp;

public class ObjetoDatosGraficaCuentas {

    private String Nombre;
    private String monto;

    public ObjetoDatosGraficaCuentas(){

    }

    public ObjetoDatosGraficaCuentas(String nombre, String monto) {
        Nombre = nombre;
        this.monto = monto;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }
}
