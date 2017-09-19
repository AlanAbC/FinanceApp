package com.claresti.financeapp;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ObjCategoria implements Serializable{

    private String ID;
    private String Nombre;
    private String Descripcion;

    public ObjCategoria(){

    }

    public ObjCategoria(String ID, String nombre, String descripcion) {
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
