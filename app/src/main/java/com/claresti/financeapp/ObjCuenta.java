package com.claresti.financeapp;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ObjCuenta implements Serializable{

    private String ID;
    private String Nombre;
    private String Descripcion;

    private float dinero;

    public ObjCuenta(){

    }

    public ObjCuenta(String ID, String nombre, String descripcion, float dinero) {
        this.ID = ID;
        Nombre = nombre;
        Descripcion = descripcion;
        this.dinero = dinero;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public float getDinero() {
        return dinero;
    }

    public void setDinero(float dinero) {
        this.dinero = dinero;
    }
}
