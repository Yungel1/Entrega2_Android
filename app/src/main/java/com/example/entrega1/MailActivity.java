package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MailActivity extends AppCompatActivity {

    private TextView usuarioTV;
    private TextView emailTV;
    private EditText asuntoET;
    private EditText cuerpoET;
    private Bundle extras;
    private MiBD gestorDB;
    String email;

    private String idioma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Conseguir el tema desde las preferencias y aplicarlo
        int tema = this.getTema();
        setTheme(tema);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        //Asignar elementos
        usuarioTV = findViewById(R.id.paraUsuarioTV);
        emailTV = findViewById(R.id.paraEmailTV);
        asuntoET = findViewById(R.id.asuntoET);
        cuerpoET = findViewById(R.id.cuerpoET);

        //Insertar usuario y email en cada TextView (el email lo obtendremos de la base de datos)
        String usuario= "";
        extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuarioAñadido");
            usuarioTV.setText(usuario);
            //Coger el idioma
            idioma = extras.getString("idioma");
            if(idioma!=null){
                Locale nuevaloc = new Locale(idioma);
                if(!nuevaloc.getLanguage().equals(getBaseContext().getResources().getConfiguration().locale.getLanguage())){
                    cambiarIdioma();
                }
            }
        }
        gestorDB = new MiBD(this, "Users", null, 1);
        email = gestorDB.conseguirEmail(usuario);
        emailTV.setText(email);

    }

    private void cambiarIdioma() {
        //Cambiar idioma
        Locale nuevaloc = new Locale(idioma);

        Locale.setDefault(nuevaloc);
        Configuration configuration =
                getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);

        Context context = getBaseContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

        finish();
        startActivity(getIntent().putExtra("idioma", idioma));
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

    public void onClickEnviar(View v){

        //Intent implícito para mandar el email
        String[] emails = new String[] {email};
        String subject = asuntoET.getText().toString();
        String body = cuerpoET.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, emails);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}