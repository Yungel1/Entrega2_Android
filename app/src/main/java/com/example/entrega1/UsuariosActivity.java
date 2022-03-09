package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class UsuariosActivity extends AppCompatActivity {


    private ListView lista;
    private Bundle extras;
    private String usuario;
    private MiBD gestorDB;
    private AdaptadorListView eladap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        //Obtenemos el usuario desde la actividad login o signup
        extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuario");
        }

        //Información para la lista desde la base de datos
        gestorDB = new MiBD(this, "Users", null, 1);
        ArrayList<String> listaUsuarios = gestorDB.conseguirUsuariosAñadidos(usuario);
        int image = R.drawable.mail;

        //Asignación al listview
        lista = (ListView) findViewById(R.id.usersLIST);
        eladap= new AdaptadorListView(getApplicationContext(),listaUsuarios,image);
        lista.setAdapter(eladap);

    }

    public void onClickIdioma(View v){

    }

    public void onClickCambiarEmail(View v){

    }

    public void onClickAñadir(View v){
        eladap.notifyDataSetChanged();
        startActivity(new Intent(UsuariosActivity.this, MailActivity.class));
    }
}