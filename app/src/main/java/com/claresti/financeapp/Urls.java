package com.claresti.financeapp;

public class Urls {

    public static final String clares = "http://cpmx.claresti.com/";
    public static final String GETMOVEMENTS = clares + "get_movements.php";
    public static final String GETCATEGORIES = clares + "get_categories.php";
    public static final String GETACCOUNTS = clares + "get_accounts.php";
    public static final String NEWMOVEMENT = clares + "insert_movement.php";
    public static final String NEWCATEGORY = clares + "insert_category.php";
    public static final String NEWACCOUNT = clares + "insert_account.php";

    private String login;
    private String registro;
    private String getCategorias;
    private String getCuentas;
    private String setMovimiento;
    private String setCategoria;
    private String setCuenta;
    private String getMovimientos;
    private String getMovimientosIntervalo;
    private String getMovimientosIntervaloCuenta;
    private String getMovimientosIntervaloCategoria;
    private String getMovimientosIntervaloTipo;
    private String getMovimientosCuenta;
    private String getMovimientosCategoria;
    private String getMovimientosTipo;
    private String getGraficaCuentas;
    private String getGraficaCategorias;
    private String getEstadisticas;
    private String getGraficaCuentasIntervalo;
    private String getGraficaCategoriasIntervalo;
    private String getEstadisticasIntervalo;


    public Urls(){
        this.login = "http://cpmx.claresti.com/login.php?";
        this.registro = "http://cpmx.claresti.com/registro_usuario.php?";
        this.getCategorias = "http://cpmx.claresti.com/get_categorias.php?";
        this.getCuentas ="http://cpmx.claresti.com/get_cuentas.php?";
        this.setMovimiento = "http://cpmx.claresti.com/nuevo_movimiento.php?";
        this.setCategoria = "http://cpmx.claresti.com/nueva_categoria.php?";
        this.setCuenta = "http://cpmx.claresti.com/nueva_cuenta.php?";
        this.getMovimientos = "http://cpmx.claresti.com/get_movimientossemana.php?";
        this.getMovimientosIntervalo = "http://cpmx.claresti.com/get_movimientosintervalo.php?";
        this.getMovimientosCuenta = "http://cpmx.claresti.com/get_movcuesem.php?";
        this.getMovimientosCategoria = "http://cpmx.claresti.com/get_movimientoscatsemana.php?";
        this.getMovimientosTipo = "http://cpmx.claresti.com/get_movtiposem.php?";
        this.getMovimientosIntervaloCuenta = "http://cpmx.claresti.com/get_movcueint.php?";
        this.getMovimientosIntervaloCategoria = "http://cpmx.claresti.com/get_movcatsemint.php?";
        this.getMovimientosIntervaloTipo = "http://cpmx.claresti.com/get_movtipoint.php?";
        this.getGraficaCuentas = "http://cpmx.claresti.com/grafica_cuenta.php?";
        this.getGraficaCategorias = "http://cpmx.claresti.com/grafica_categoria.php?";
        this.getEstadisticas = "http://cpmx.claresti.com/statsCategoria.php?";
        this.getGraficaCuentasIntervalo = "http://cpmx.claresti.com/grafica_cuenta.php?";
        this.getGraficaCategoriasIntervalo = "http://cpmx.claresti.com/grafica_categoria.php?";
        this.getEstadisticasIntervalo = "http://cpmx.claresti.com/statsCategoriaRango.php?";
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

    public String getGetMovimientos() {
        return getMovimientos;
    }

    public String getGetMovimientosIntervalo() {
        return getMovimientosIntervalo;
    }

    public String getGetMovimientosIntervaloCuenta() {
        return getMovimientosIntervaloCuenta;
    }

    public String getGetMovimientosIntervaloCategoria() {
        return getMovimientosIntervaloCategoria;
    }

    public String getGetMovimientosCuenta() {
        return getMovimientosCuenta;
    }

    public String getGetMovimientosCategoria() {
        return getMovimientosCategoria;
    }

    public String getGetMovimientosIntervaloTipo() {
        return getMovimientosIntervaloTipo;
    }

    public String getGetMovimientosTipo() {
        return getMovimientosTipo;
    }

    public String getGetGraficaCuentas() {
        return getGraficaCuentas;
    }

    public String getGetGraficaCategorias() {
        return getGraficaCategorias;
    }

    public String getGetEstadisticas() {
        return getEstadisticas;
    }

    public String getGetGraficaCuentasIntervalo() {
        return getGraficaCuentasIntervalo;
    }

    public String getGetGraficaCategoriasIntervalo() {
        return getGraficaCategoriasIntervalo;
    }

    public String getGetEstadisticasIntervalo() {
        return getEstadisticasIntervalo;
    }
}
