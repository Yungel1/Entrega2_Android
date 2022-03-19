package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;


public class SignupActivity extends AppCompatActivity {

    private String errorUsuario;
    private String errorEmail;
    private int duration;
    private Toast toast;

    private MiBD gestorDB;

    private EditText usuarioET;
    private EditText contraseñaET;
    private EditText emailET;

    private NotificationCompat.Builder elBuilder;
    private NotificationManager elManager;

    private Bundle extras;
    private String idioma;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Conseguir el tema desde las preferencias y aplicarlo
        int tema = this.getTema();
        setTheme(tema);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //toast por si el usuario ya existe o si el mail no es válido
        errorUsuario = getString(R.string.signup_error);
        errorEmail = getString(R.string.email_error);
        duration = Toast.LENGTH_SHORT;

        //Gestor base de datos
        gestorDB = new MiBD(this, "Users", null, 1);

        //Obtenemos los elementos necesarios
        usuarioET = findViewById(R.id.usuarioET);
        contraseñaET = findViewById(R.id.contraseñaET);
        emailET = findViewById(R.id.emailET);

        //Configuración de la notificación y canales
        elBuilder = new NotificationCompat.Builder(this, "IdCanal");
        elBuilder.setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentTitle(getString(R.string.new_signup))
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .setAutoCancel(true);
        elManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        this.createNotificationChannel();

        //Coger el idioma
        extras = getIntent().getExtras();
        if (extras != null) {
            idioma = extras.getString("idioma");
            if(idioma!=null){
                Locale nuevaloc = new Locale(idioma);
                if(!nuevaloc.getLanguage().equals(getBaseContext().getResources().getConfiguration().locale.getLanguage())){
                    cambiarIdioma();
                }
            }
        }
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

    private void createNotificationChannel() {
        // Crear NotificationChannel, solo para la API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel elCanal = new NotificationChannel("IdCanal", "NombreCanal",
                    NotificationManager.IMPORTANCE_DEFAULT);

            elCanal.enableLights(true);
            elCanal.setLightColor(Color.RED);
            elCanal.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            elCanal.enableVibration(true);
            elManager.createNotificationChannel(elCanal);
        }
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

    public void onClickEntrar(View v){

        //Comprobar que el usuario es válido (no está en uso)
        String usuario = usuarioET.getText().toString();
        String contraseña = contraseñaET.getText().toString();
        String email = emailET.getText().toString();
        Intent i;

        int resultado = gestorDB.existeUsuario(usuario);

        if(!email.contains("@")){
            //si el mail no tiene un '@'
            toast = Toast.makeText(getApplicationContext(), errorEmail, duration);
            toast.show();
        } else if(resultado==1&&!usuario.equals("")){
            //Si es válido, guardamos los datos en la base de datos
            gestorDB.registrarUsuario(usuario,contraseña,email);

            //Lanzamos la notificación de que se ha registrado
            elBuilder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(getString(R.string.has_been_registered)+": "+usuario));
            elManager.notify(1, elBuilder.build());

            // y pasamos a la siguiente actividad
            i = new Intent (SignupActivity.this, UsuariosActivity.class);
            i.putExtra("usuario",usuario);
            startActivity(i.putExtra("idioma",idioma));
        } else{
            //Si es no es válido mostramos el toast de usuario no válido
            toast = Toast.makeText(getApplicationContext(), errorUsuario, duration);
            toast.show();
        }

    }

    public void onClickIniciaSesion(View v){
        Intent i = new Intent (SignupActivity.this, LoginActivity.class);
        startActivity(i);
    }

    /*public void onClickIdioma(View v){

        Button idiomaBTN = (Button)v;
        //Saber a que idioma cambiar
        String idiomaCambiar = idiomaBTN.getText().toString();

        if (idiomaCambiar.equals("ES")){
            idiomaCambiar = "es";
        }
        else{
            idiomaCambiar = "en";
        }

        Locale nuevaloc = new Locale(idiomaCambiar);
        Locale.setDefault(nuevaloc);
        Configuration configuration =
                getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);

        Context context = getBaseContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

        finish();
        startActivity(getIntent());
    }*/


}