package com.claresti.financeapp.Modelos;

public class Cuenta {
    private int id;
    private String description, name, register_date;
    private double money;

    public Cuenta(int id, String description, String name, String register_date, double money) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.register_date = register_date;
        this.money = money;
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

    public String getRegister_date() {
        return register_date;
    }

    public void setRegister_date(String register_date) {
        this.register_date = register_date;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
