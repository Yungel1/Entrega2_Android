package com.example.entrega1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MiBD extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    public MiBD(@Nullable Context context, @Nullable String name,
                @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE Usuarios " +
                "(usuario VARCHAR(255) PRIMARY KEY NOT NULL, " +
                "contraseña VARCHAR(255),email VARCHAR(255))");

        sqLiteDatabase.execSQL("CREATE TABLE Añadir " +
                "(usuarioAñade VARCHAR(255) NOT NULL," +
                "usuarioAñadido VARCHAR(255) NOT NULL, PRIMARY KEY(usuarioAñade,usuarioAñadido)," +
                "FOREIGN KEY(usuarioAñade) REFERENCES Usuarios(usuario)," +
                "FOREIGN KEY(usuarioAñadido) REFERENCES Usuarios(usuario))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void registrarUsuario(String usuario, String contraseña, String email){
        db.execSQL("INSERT INTO Usuarios(usuario,contraseña,email) VALUES ('"+usuario+"','"+contraseña+"','"+
                email+"');");
    }

    public void añadirUsuario(String usuarioAñade,String usuarioAñadido){

        db.execSQL("INSERT INTO Añadir(usuarioAñade,usuarioAñadido) " +
                "VALUES ('"+usuarioAñade+"','"+usuarioAñadido+"');");

    }

    public void cambiarEmail(String usuario,String email){
        db.execSQL("UPDATE Usuarios SET email='"+email+"' WHERE usuario='"+usuario+"';");
    }

    public int existeUsuarioAñadido(String usuarioAñade,String usuarioAñadido){
        int resultado;

        Cursor c = db.rawQuery("SELECT CASE WHEN EXISTS(" +
                        "SELECT 1 FROM Añadir WHERE usuarioAñade='"+usuarioAñade+
                        "' AND usuarioAñadido='"+usuarioAñadido+"') " +
                        "THEN 0 ELSE 1 END"
                ,null);

        c.moveToNext();
        resultado = c.getInt(0);

        return resultado;
    }

    public int existeUsuario(String usuario){
        //si usuario existe, devuelve 0, sino 1

        int resultado;

        Cursor c = db.rawQuery("SELECT CASE WHEN EXISTS(" +
                "SELECT usuario FROM Usuarios WHERE usuario='"+usuario+
                "') " +
                "THEN 0 ELSE 1 END"
        ,null);

        c.moveToNext();
        resultado = c.getInt(0);

        return resultado;
    }

    public int existenUsuarioContraseña(String usuario,String contraseña){
        //si usuario y contraseña existen, devuelve 0, sino 1

        int resultado;

        Cursor c = db.rawQuery("SELECT CASE WHEN EXISTS(" +
                        "SELECT usuario,contraseña FROM Usuarios WHERE usuario='"+usuario+
                        "' AND contraseña='"+contraseña+"') " +
                        "THEN 0 ELSE 1 END"
                ,null);

        c.moveToNext();
        resultado = c.getInt(0);
        c.close();
        return resultado;
    }

    public ArrayList<String> conseguirUsuariosAñadidos(String usuarioAñade){

        ArrayList<String> listaUsuariosAñadidos = new ArrayList<String>();

        Cursor c = db.rawQuery("SELECT usuarioAñadido FROM Añadir " +
                        "WHERE usuarioAñade='"+usuarioAñade+"';"
                ,null);

        while(c.moveToNext()){
            listaUsuariosAñadidos.add(c.getString(0));
        }
        c.close();
        return listaUsuariosAñadidos;
    }

    public String conseguirEmail(String usuario){
        String email;

        Cursor c = db.rawQuery("SELECT email FROM Usuarios " +
                        "WHERE usuario='"+usuario+"';"
                ,null);

        c.moveToNext();
        email = c.getString(0);

        c.close();
        return email;
    }
    /*
    public void borrarUsuario(String usuario){
        db.execSQL("DELETE FROM Usuarios WHERE usuario='"+usuario+"';");
    }
    */
}
