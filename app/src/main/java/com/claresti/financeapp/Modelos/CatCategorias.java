package com.claresti.financeapp.Modelos;

/**
 * Created by smp_3 on 04/04/2018.
 */

public class CatCategorias {
    private int id;
    private String nombre;

    public CatCategorias(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
