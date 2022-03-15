package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;

public class PreferenceActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Conseguir el tema desde las preferencias y aplicarlo
        int tema = this.getTema();
        setTheme(tema);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        //Iniciar fragmento Preferencias
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment,new Preferencias())
                .commit();

    }

    private int getTema(){

        //Obtener el tema desde las SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String color = prefs.getString("listPref",null);
        int tema;

        if (color==null){
            color = "orange";
        }

        switch (color) {
            case "blue":
                tema = R.style.Theme_TemaAzul;
                break;
            default:
                tema = R.style.Theme_TemaNaranja;
        }
        return tema;
    }
}