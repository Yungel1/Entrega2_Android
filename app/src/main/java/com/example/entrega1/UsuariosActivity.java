package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UsuariosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
    }

    public void onClickIdioma(View v){

    }

    public void onClickCambiarEmail(View v){

    }

    public void onClickAñadir(View v){
        startActivity(new Intent(UsuariosActivity.this, MailActivity.class));
    }
}