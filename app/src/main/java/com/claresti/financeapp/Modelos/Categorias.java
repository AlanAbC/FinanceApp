package com.claresti.financeapp.Modelos;

/**
 * Created by smp_3 on 04/04/2018.
 */

public class Categorias {
    private int id;
    private String nombre;
    private int id_categoria;

    public Categorias(int id, String nombre, int id_categoria) {
        this.id = id;
        this.nombre = nombre;
        this.id_categoria = id_categoria;
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

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }
}
