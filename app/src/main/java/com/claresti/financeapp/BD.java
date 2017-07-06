package com.claresti.financeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;

public class BD extends SQLiteOpenHelper {

    private static final int DataBaseVersion = 1;
    private static final String DataBaseName = "FinanzasApp.db";

    public BD(Context context) {
        super(context, DataBaseName, null, DataBaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tablaUsuario = "CREATE TABLE Usuario(" +
                "id TEXT NOT NULL PRIMARY KEY," +
                "usuario TEXT DEFAULT NULL," +
                "nombre TEXT DEFAULT NULL," +
                "correo TEXT NOT NULL," +
                "sexo TEXT NOT NULL," +
                "fechaNacimiento TEXT NOT NULL" +
                ")";

        String tablaImg = "CREATE TABLE Img(" +
                "usuImg TEXT)";

        //Crear tablas
        db.execSQL(tablaUsuario);
        db.execSQL(tablaImg);

        //ObjUsuario
        ContentValues v = new ContentValues();
        v.put("id", "0");
        v.put("usuario", "0");
        v.put("nombre", "0");
        v.put("correo", "0");
        v.put("sexo", "0");
        v.put("fechaNacimiento", "0");
        db.insert("Usuario", null, v);

        ContentValues v2 = new ContentValues();
        v2.put("usuImg", "0");
        db.insert("img", null, v2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ObjUsuario slectUsuario(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Usuario", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            ObjUsuario usuario = new ObjUsuario(
                    cursor.getString(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("usuario")),
                    cursor.getString(cursor.getColumnIndex("nombre")),
                    cursor.getString(cursor.getColumnIndex("correo")),
                    cursor.getString(cursor.getColumnIndex("sexo")),
                    cursor.getString(cursor.getColumnIndex("fechaNacimiento")));
            return usuario;
        }
        return null;
    }

    public String LoginUsuario(ObjUsuario usuario){
        try{
            SQLiteDatabase bd = this.getWritableDatabase();
            ContentValues v = new ContentValues();
            v.put("id", usuario.getIdUsuario());
            v.put("usuario", usuario.getUsuario());
            v.put("nombre", usuario.getNombre());
            v.put("correo", usuario.getCorreo());
            v.put("sexo", usuario.getSexo());
            v.put("fechaNacimiento", usuario.getFecha_Nac());
            bd.update("Usuario", v, "id = 0", null);
            return "1";
        }catch(Exception e){
            return e.toString();
        }
    }

    public String LogoutUsuario(String id){
        try{
            SQLiteDatabase bd = this.getWritableDatabase();
            ContentValues v = new ContentValues();
            v.put("id", "0");
            v.put("usuario", "0");
            v.put("nombre", "0");
            v.put("correo", "0");
            v.put("sexo", "0");
            v.put("fechaNacimiento", "0");
            bd.update("Usuario", v, "id = " + id, null);
            return "1";
        }catch(Exception e){
            return e.toString();
        }
    }

    public String updateUsuario(ObjUsuario usuario){
        try{
            SQLiteDatabase bd = this.getWritableDatabase();
            ContentValues v = new ContentValues();
            v.put("usuario", usuario.getIdUsuario());
            v.put("nombre", usuario.getNombre());
            v.put("correo", usuario.getCorreo());
            v.put("sexo", usuario.getSexo());
            v.put("fechaNacimiento", usuario.getFecha_Nac());
            bd.update("Usuario", v, "id = " + usuario.getIdUsuario(), null);
            return "1";
        }catch(Exception e){
            return e.toString();
        }
    }

    public String updateImg(String img){
        try{
            SQLiteDatabase bd = this.getWritableDatabase();
            ContentValues v = new ContentValues();
            v.put("usuImg", img);
            bd.update("img", v, null, null);
            return "1";
        }catch(Exception e){
            return e.toString();
        }
    }

    public String deleteImg(String img){
        try{
            SQLiteDatabase bd = this.getWritableDatabase();
            ContentValues v = new ContentValues();
            v.put("usuImg", "0");
            bd.update("img", v, null, null);
            return "1";
        }catch(Exception e){
            return e.toString();
        }
    }

}
