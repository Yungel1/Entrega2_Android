package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;

public class PreferenceActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int tema = this.getTema();
        setTheme(tema);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment,new Preferencias())
                .commit();

    }

    private int getTema(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String color = prefs.getString("listPref",null);
        int tema;
        //Toast toast = Toast.makeText(getActivity(), "Ajustes guardados", Toast.LENGTH_LONG);
        //toast.show();
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