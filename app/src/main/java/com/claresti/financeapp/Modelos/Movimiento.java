package com.claresti.financeapp.Modelos;

import com.orm.SugarRecord;

public class Movimiento extends SugarRecord{
    private int amount;
    private String concept;
    private int category;
    private int account_transfer;
    private String date;
    private int type;
    private int account;
    private boolean inSwiped = false;

    public Movimiento(){}

    public Movimiento(int amount, String concept, int category, int account_transfer, String date, int type, int account) {
        this.amount = amount;
        this.concept = concept;
        this.category = category;
        this.account_transfer = account_transfer;
        this.date = date;
        this.type = type;
        this.account = account;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getAccount_transfer() {
        return account_transfer;
    }

    public void setAccount_transfer(int account_transfer) {
        this.account_transfer = account_transfer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public boolean isInSwiped() {
        return inSwiped;
    }

    public void setInSwiped(boolean inSwiped) {
        this.inSwiped = inSwiped;
    }
}
