package com.claresti.financeapp;

public class ObjCuenta {

    private String ID;
    private String Nombre;
    private String Descripcion;

    public ObjCuenta(){

    }

    public ObjCuenta(String ID, String nombre, String descripcion) {
        this.ID = ID;
        Nombre = nombre;
        Descripcion = descripcion;
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
}
