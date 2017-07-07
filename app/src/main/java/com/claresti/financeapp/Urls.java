package com.claresti.financeapp;

public class Urls {

    private String urlLogin;
    private String urlRegistro;
    private String urlGetCategorias;
    private String urlGetCuentas;

    public Urls(){
        this.urlLogin = "http://cpmx.claresti.com/login.php?";
        this.urlRegistro = "http://cpmx.claresti.com/registro_usuario.php?";
        this.urlGetCategorias = "http://cpmx.claresti.com/get_categorias.php?";
        this.urlGetCuentas ="http://cpmx.claresti.com/get_cuentas.php?";
    }

    public String getUrlLogin() {
        return urlLogin;
    }

    public String getUrlRegistro() {
        return urlRegistro;
    }

    public String getUrlGetCategorias() {
        return urlGetCategorias;
    }

    public String getUrlGetCuentas() {
        return urlGetCuentas;
    }
}
