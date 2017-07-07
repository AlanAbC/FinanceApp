package com.claresti.financeapp;

public class Urls {

    private String login;
    private String registro;
    private String getCategorias;
    private String getCuentas;
    private String setMovimiento;
    private String setCategoria;
    private String setCuenta;


    public Urls(){
        this.login = "http://cpmx.claresti.com/login.php?";
        this.registro = "http://cpmx.claresti.com/registro_usuario.php?";
        this.getCategorias = "http://cpmx.claresti.com/get_categorias.php?";
        this.getCuentas ="http://cpmx.claresti.com/get_cuentas.php?";
        this.setMovimiento = "http://cpmx.claresti.com/nuevo_movimiento.php?";
        this.setCategoria = "http://cpmx.claresti.com/nueva_categoria.php?";
        this.setCuenta = "http://cpmx.claresti.com/nueva_cuenta.php?";
    }

    public String getLogin() {
        return login;
    }

    public String getRegistro() {
        return registro;
    }

    public String getGetCategorias() {
        return getCategorias;
    }

    public String getGetCuentas() {
        return getCuentas;
    }

    public String getSetMovimiento() {
        return setMovimiento;
    }

    public String getSetCategoria() {
        return setCategoria;
    }

    public String getSetCuenta() {
        return setCuenta;
    }
}
