package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private String error;
    private int duration;
    private Toast toast;

    private MiBD gestorDB;

    private EditText usuarioET;
    private EditText contraseñaET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Dejamos el toast preparado para el caso de usuario contraseña incorrectos
        error = getString(R.string.login_error);
        duration = Toast.LENGTH_SHORT;
        toast = Toast.makeText(getApplicationContext(), error, duration);

        //Gestor base de datos
        gestorDB = new MiBD(this, "Users", null, 1);

        //Obtenemos los elementos necesarios
        usuarioET = findViewById(R.id.usuarioET);
        contraseñaET = findViewById(R.id.contraseñaET);

    }

    public void onClickEntrar(View v){

        //Comprobar que son correctos usuario y contraseña
        String usuario = usuarioET.getText().toString();
        String contraseña = contraseñaET.getText().toString();
        Intent i;

        int resultado = gestorDB.existenUsuarioContraseña(usuario,contraseña);

        if(resultado==0){
            //Si es correcto pasamos a la siguiente actividad
            i = new Intent (LoginActivity.this, UsuariosActivity.class);
            i.putExtra("usuario",usuario);
            startActivity(i);
        }else{
            //Si es incorrecto mostramos el toast
            toast.show();
        }
    }

    public void onClickIdioma(View v){

    }

    public void onClickRegistrate(View v){
        Intent i = new Intent (LoginActivity.this, SignupActivity.class);
        startActivity(i);
    }

}