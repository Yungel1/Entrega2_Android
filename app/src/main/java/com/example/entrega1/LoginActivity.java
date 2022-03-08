package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {


    private MiBD gestorDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Gestor base de datos
        gestorDB = new MiBD(this, "Users", null, 1);

    }

    public void onClickEntrar(View v){
        startActivity(new Intent(LoginActivity.this, UsuariosActivity.class));
    }

    public void onClickIdioma(View v){

    }

    public void onClickRegistrate(View v){
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

}