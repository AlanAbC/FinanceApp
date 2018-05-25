package com.claresti.financeapp.Modelos;

import com.orm.SugarRecord;

public class Cuenta extends SugarRecord{
    private String description, name, register_date;
    private double money;

    public Cuenta(){}

    public Cuenta(String description, String name, String register_date, double money) {
        this.description = description;
        this.name = name;
        this.register_date = register_date;
        this.money = money;
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
