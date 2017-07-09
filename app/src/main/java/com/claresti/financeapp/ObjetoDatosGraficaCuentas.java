package com.claresti.financeapp;

public class ObjetoDatosGraficaCuentas {

    private String nombre;
    private String ingresos;
    private String egresos;


    public ObjetoDatosGraficaCuentas(){

    }

    public ObjetoDatosGraficaCuentas(String nombre, String ingresos, String egresos) {
        this.nombre = nombre;
        this.ingresos = ingresos;
        this.egresos = egresos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIngresos() {
        return ingresos;
    }

    public void setIngresos(String ingresos) {
        this.ingresos = ingresos;
    }

    public String getEgresos() {
        return egresos;
    }

    public void setEgresos(String egresos) {
        this.egresos = egresos;
    }
}
