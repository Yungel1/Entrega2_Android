package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class UsuariosActivity extends AppCompatActivity {


    ListView lista;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        //Información para la lista

        String[] progBotonLista={"boton1","boton2","boton3","boton3","boton3","boton3","boton3"};
        String[] progNombreLista={"BANANA","COCO++","COCO++","COCO++","COCO++","COCO++","COCO++"};
        int image = R.drawable.mail;
        //Asignación al listview
        lista = (ListView) findViewById(R.id.usersLIST);
        AdaptadorListView eladap= new AdaptadorListView(getApplicationContext(),progBotonLista,progNombreLista,image);
        lista.setAdapter(eladap);

    }

    public void onClickIdioma(View v){

    }

    public void onClickCambiarEmail(View v){

    }

    public void onClickAñadir(View v){
        startActivity(new Intent(UsuariosActivity.this, MailActivity.class));
    }
}