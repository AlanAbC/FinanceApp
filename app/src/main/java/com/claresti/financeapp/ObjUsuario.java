package com.claresti.financeapp;

public class ObjUsuario {

    private String idUsuario;
    private String Usuario;
    private String Nombre;
    private String Correo;
    private String Sexo;
    private String Fecha_Nac;

    public ObjUsuario(){

    }

    public ObjUsuario(String idUsuario, String usuario, String nombre, String correo, String sexo, String fecha_Nac) {
        this.idUsuario = idUsuario;
        Usuario = usuario;
        Nombre = nombre;
        Correo = correo;
        Sexo = sexo;
        Fecha_Nac = fecha_Nac;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getSexo() {
        return Sexo;
    }

    public void setSexo(String sexo) {
        Sexo = sexo;
    }

    public String getFecha_Nac() {
        return Fecha_Nac;
    }

    public void setFecha_Nac(String fecha_Nac) {
        Fecha_Nac = fecha_Nac;
    }
}
