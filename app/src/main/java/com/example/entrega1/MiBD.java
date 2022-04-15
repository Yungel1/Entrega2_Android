package com.example.entrega1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.entrega1.workers.AñadirUsuarioWorker;
import com.example.entrega1.workers.ConseguirEmailWorker;
import com.example.entrega1.workers.ConseguirUsuariosAñadidosWorker;
import com.example.entrega1.workers.ExisteUsuarioAñadidoWorker;
import com.example.entrega1.workers.ExisteUsuarioWorker;
import com.example.entrega1.workers.ExistenUsuarioContraseñaWorker;
import com.example.entrega1.workers.InsertarFotoWorker;
import com.example.entrega1.workers.RegistrarUsuarioWorker;

import java.util.ArrayList;

public class MiBD extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    public String result;

    public MiBD(@Nullable Context context, @Nullable String name,
                @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Crear las tablas necesarias
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

    public void registrarUsuario(String usuario, String contraseña, String email,Context context){

        Data datos = new Data.Builder().putString("usuario",usuario)
                .putString("contraseña",contraseña).putString("email",email).build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(RegistrarUsuarioWorker.class).setInputData(datos).build();
        WorkManager.getInstance(context).enqueue(otwr);
        /*db.execSQL("INSERT INTO Usuarios(usuario,contraseña,email) VALUES ('"+usuario+"','"+contraseña+"','"+
                email+"');");*/
    }

    public OneTimeWorkRequest añadirUsuario(String usuarioAñade,String usuarioAñadido,Context context){

        Data datos = new Data.Builder().putString("usuarioAñade",usuarioAñade)
                .putString("usuarioAñadido",usuarioAñadido).build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(AñadirUsuarioWorker.class).setInputData(datos).build();
        WorkManager.getInstance(context).enqueue(otwr);

        return otwr;

        /* db.execSQL("INSERT INTO Añadir(usuarioAñade,usuarioAñadido) " +
                 "VALUES ('"+usuarioAñade+"','"+usuarioAñadido+"');");*/
    }

    /*public void cambiarEmail(String usuario,String email){
        db.execSQL("UPDATE Usuarios SET email='"+email+"' WHERE usuario='"+usuario+"';");
    }*/

    public OneTimeWorkRequest existeUsuarioAñadido(String usuarioAñade,String usuarioAñadido,Context context){

        Data datos = new Data.Builder().putString("usuarioAñade",usuarioAñade)
                .putString("usuarioAñadido",usuarioAñadido).build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ExisteUsuarioAñadidoWorker.class).setInputData(datos).addTag("existeUsuarioAñadido").build();
        WorkManager.getInstance(context).enqueue(otwr);

        return otwr;

        /*
        int resultado;

        Cursor c = db.rawQuery("SELECT CASE WHEN EXISTS(" +
                        "SELECT 1 FROM Añadir WHERE usuarioAñade='"+usuarioAñade+
                        "' AND usuarioAñadido='"+usuarioAñadido+"') " +
                        "THEN 0 ELSE 1 END"
                ,null);

        c.moveToNext();
        resultado = c.getInt(0);

        return resultado;*/
    }

    public OneTimeWorkRequest existeUsuario(String usuario,Context context){

        Data datos = new Data.Builder().putString("usuario",usuario).build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ExisteUsuarioWorker.class).setInputData(datos).addTag("existeUsuario").build();
        WorkManager.getInstance(context).enqueue(otwr);

        return otwr;

        //si usuario existe, devuelve 0, sino 1
        /*
        int resultado;

        Cursor c = db.rawQuery("SELECT CASE WHEN EXISTS(" +
                "SELECT usuario FROM Usuarios WHERE usuario='"+usuario+
                "') " +
                "THEN 0 ELSE 1 END"
        ,null);

        c.moveToNext();
        resultado = c.getInt(0);

        return resultado;
        */
    }

    public OneTimeWorkRequest existenUsuarioContraseña(String usuario,String contraseña,Context context){

        Data datos = new Data.Builder().putString("usuario",usuario).putString("contraseña",contraseña).build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ExistenUsuarioContraseñaWorker.class).setInputData(datos).build();
        WorkManager.getInstance(context).enqueue(otwr);

        return otwr;

        /*
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
        return resultado;*/
    }

    public OneTimeWorkRequest conseguirUsuariosAñadidos(String usuarioAñade,Context context){

        Data datos = new Data.Builder().putString("usuarioAñade",usuarioAñade).build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ConseguirUsuariosAñadidosWorker.class).setInputData(datos).build();
        WorkManager.getInstance(context).enqueue(otwr);

        return otwr;

        /*ArrayList<String> listaUsuariosAñadidos = new ArrayList<String>();

        Cursor c = db.rawQuery("SELECT usuarioAñadido FROM Añadir " +
                        "WHERE usuarioAñade='"+usuarioAñade+"';"
                ,null);

        while(c.moveToNext()){
            listaUsuariosAñadidos.add(c.getString(0));
        }
        c.close();
        return listaUsuariosAñadidos;*/
    }

    public OneTimeWorkRequest conseguirEmail(String usuario,Context context){

        Data datos = new Data.Builder().putString("usuario",usuario).build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ConseguirEmailWorker.class).setInputData(datos).build();
        WorkManager.getInstance(context).enqueue(otwr);

        return otwr;


        /*
        String email;

        Cursor c = db.rawQuery("SELECT email FROM Usuarios " +
                        "WHERE usuario='"+usuario+"';"
                ,null);

        c.moveToNext();
        email = c.getString(0);

        c.close();
        return email;*/
    }

    public void insertarFoto(String uriString,int ancho, int alto, Context context){

        Data datos = new Data.Builder().putString("uriString",uriString)
                .putInt("ancho",ancho).putInt("alto",alto).build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(InsertarFotoWorker.class).setInputData(datos).build();
        WorkManager.getInstance(context).enqueue(otwr);

    }

    /*
    public void borrarUsuario(String usuario){
        db.execSQL("DELETE FROM Usuarios WHERE usuario='"+usuario+"';");
    }
    */
}
