package com.claresti.financeapp;

public class ObjMovimiento {

    private String ID;
    private String Monto;
    private String Tipo;
    private String fecha;
    private String idCategoria;
    private String idCuenta;
    private String idUsuario;

    public ObjMovimiento(){

    }

    public ObjMovimiento(String ID, String monto, String tipo, String fecha, String idCategoria, String idCuenta, String idUsuario) {
        this.ID = ID;
        Monto = monto;
        Tipo = tipo;
        this.fecha = fecha;
        this.idCategoria = idCategoria;
        this.idCuenta = idCuenta;
        this.idUsuario = idUsuario;
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
}
