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

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void registrarUsuario(String usuario, String contraseña, String email,Context context){
        //Registrar usuario
        Data datos = new Data.Builder().putString("usuario",usuario)
                .putString("contraseña",contraseña).putString("email",email).build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(RegistrarUsuarioWorker.class).setInputData(datos).build();
        WorkManager.getInstance(context).enqueue(otwr);

    }

    public OneTimeWorkRequest añadirUsuario(String usuarioAñade,String usuarioAñadido,Context context){
        //Añadir usuario a un usuario
        Data datos = new Data.Builder().putString("usuarioAñade",usuarioAñade)
                .putString("usuarioAñadido",usuarioAñadido).build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(AñadirUsuarioWorker.class).setInputData(datos).build();
        WorkManager.getInstance(context).enqueue(otwr);

        return otwr;

    }

    public OneTimeWorkRequest existeUsuarioAñadido(String usuarioAñade,String usuarioAñadido,Context context){
        //Si existe el usuario añadido devuelve 0, sino 1
        Data datos = new Data.Builder().putString("usuarioAñade",usuarioAñade)
                .putString("usuarioAñadido",usuarioAñadido).build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ExisteUsuarioAñadidoWorker.class).setInputData(datos).addTag("existeUsuarioAñadido").build();
        WorkManager.getInstance(context).enqueue(otwr);

        return otwr;

    }

    public OneTimeWorkRequest existeUsuario(String usuario,Context context){
        //si usuario existe, devuelve 0, sino 1
        Data datos = new Data.Builder().putString("usuario",usuario).build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ExisteUsuarioWorker.class).setInputData(datos).addTag("existeUsuario").build();
        WorkManager.getInstance(context).enqueue(otwr);

        return otwr;

    }

    public OneTimeWorkRequest existenUsuarioContraseña(String usuario,String contraseña,Context context){
        //si usuario y contraseña existen, devuelve 0, sino 1
        Data datos = new Data.Builder().putString("usuario",usuario).putString("contraseña",contraseña).build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ExistenUsuarioContraseñaWorker.class).setInputData(datos).build();
        WorkManager.getInstance(context).enqueue(otwr);

        return otwr;

    }

    public OneTimeWorkRequest conseguirUsuariosAñadidos(String usuarioAñade,Context context){
        //Coneguir todos los usuarios añadidos por un usuario
        Data datos = new Data.Builder().putString("usuarioAñade",usuarioAñade).build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ConseguirUsuariosAñadidosWorker.class).setInputData(datos).build();
        WorkManager.getInstance(context).enqueue(otwr);

        return otwr;

    }

    public OneTimeWorkRequest conseguirEmail(String usuario,Context context){
        //Conseguir email del usuario
        Data datos = new Data.Builder().putString("usuario",usuario).build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ConseguirEmailWorker.class).setInputData(datos).build();
        WorkManager.getInstance(context).enqueue(otwr);

        return otwr;

    }

    public void insertarFoto(String uriString,int ancho, int alto, Context context){
        //Insertar imagen Base64
        Data datos = new Data.Builder().putString("uriString",uriString)
                .putInt("ancho",ancho).putInt("alto",alto).build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(InsertarFotoWorker.class).setInputData(datos).build();
        WorkManager.getInstance(context).enqueue(otwr);

    }

}
