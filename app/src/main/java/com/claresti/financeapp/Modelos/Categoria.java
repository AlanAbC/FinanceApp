package com.claresti.financeapp.Modelos;

/**
 * Created by smp_3 on 04/04/2018.
 */

public class Categoria {
    private int id;
    private String description, name, category_color;

    public Categoria(int id, String description, String name, String category_color) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.category_color = category_color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory_color() {
        return category_color;
    }

    public void setCategory_color(String category_color) {
        this.category_color = category_color;
    }
}
