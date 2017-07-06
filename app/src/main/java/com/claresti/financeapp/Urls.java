package com.claresti.financeapp;

public class Urls {

    private String urlLogin;
    private String urlRegistro;

    public Urls(){
        this.urlLogin = "http://cpmx.claresti.com/login.php?";
        this.urlRegistro = "http://cpmx.claresti.com/registro_usuario.php?";
    }

    public String getUrlLogin() {
        return urlLogin;
    }

    public String getUrlRegistro() {
        return urlRegistro;
    }
}
