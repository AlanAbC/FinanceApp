package com.claresti.financeapp;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ObjMovimiento implements Serializable{

    private String ID;
    private String Monto;
    private String Tipo;
    private String fecha;
    private String idCategoria;
    private String idCuenta;
    private String idUsuario;
    private String concepto;
    private String idCuentaTransfer;
    private boolean swiped = false;

    public ObjMovimiento(){

    }

    public ObjMovimiento(String ID, String monto, String tipo, String fecha, String idCategoria, String idCuenta, String idUsuario, String concepto) {
        this.ID = ID;
        Monto = monto;
        Tipo = tipo;
        this.fecha = fecha;
        this.idCategoria = idCategoria;
        this.idCuenta = idCuenta;
        this.idUsuario = idUsuario;
        this.concepto = concepto;
    }

    public ObjMovimiento(String ID, String monto, String tipo, String fecha, String idCategoria, String idCuenta, String idUsuario, String concepto, String idCuentaTransfer) {
        this.ID = ID;
        Monto = monto;
        Tipo = tipo;
        this.fecha = fecha;
        this.idCategoria = idCategoria;
        this.idCuenta = idCuenta;
        this.idUsuario = idUsuario;
        this.concepto = concepto;
        this.idCuentaTransfer = idCuentaTransfer;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMonto() {
        return Monto;
    }

    public void setMonto(String monto) {
        Monto = monto;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(String idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getConcepto() {
        return this.concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getIdCuentaTransfer() {
        return idCuentaTransfer;
    }

    public void setIdCuentaTransfer(String idCuentaTransfer) {
        this.idCuentaTransfer = idCuentaTransfer;
    }

    public boolean isSwiped() {
        return swiped;
    }

    public void setSwiped(boolean swiped) {
        this.swiped = swiped;
    }
}
