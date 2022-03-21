package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class UsuariosActivity extends AppCompatActivity {


    private ListView lista;
    private Bundle extras;
    private String usuario;
    private MiBD gestorDB;
    private AdaptadorListView eladap;
    ArrayList<String> listaUsuarios;

    public static String idioma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Conseguir el tema desde las preferencias y aplicarlo
        int tema = this.getTema();
        setTheme(tema);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        //Obtenemos el usuario desde la actividad login o signup
        extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuario");
            //Coger el idioma
            idioma = extras.getString("idioma");
            if(idioma!=null){
                Locale nuevaloc = new Locale(idioma);
                if(!nuevaloc.getLanguage().equals(getBaseContext().getResources().getConfiguration().locale.getLanguage())){
                    cambiarIdioma();
                }
            }
        }

        //Información para la lista desde la base de datos
        gestorDB = new MiBD(this, "Users", null, 1);
        listaUsuarios = gestorDB.conseguirUsuariosAñadidos(usuario);
        int image = R.drawable.mail;

        //Asignación al listview
        lista = (ListView) findViewById(R.id.usersLIST);
        eladap= new AdaptadorListView(getApplicationContext(),listaUsuarios,image);
        lista.setAdapter(eladap);

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

    public void onClickCambiarEmail(View v){
        //Alerta para cambiar el email
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText emailCambioET = new EditText(this);
        emailCambioET.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        //Título y texto (email actual)
        alert.setTitle(R.string.change_email);
        alert.setMessage(getString(R.string.actual_email)+" "+gestorDB.conseguirEmail(usuario));

        alert.setView(emailCambioET);

        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                //Cambiar email de la base de datos
                gestorDB.cambiarEmail(usuario,emailCambioET.getText().toString());

                //Toast para saber que se ha cambiado
                String cambiado = getString(R.string.email_changed);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), cambiado, duration);
                toast.show();

            }
        });
        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Si hace click en cancel no se cambia el email
            }
        });
        alert.show();
    }

    public void onClickAñadir(View v){
        //Dialogo para añadir un usuario a la lista mediante un EditText
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText usuarioAñadidoET = new EditText(this);
        //alert.setMessage("Enter Your Message");
        alert.setTitle(R.string.add_user);

        alert.setView(usuarioAñadidoET);

        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Mirar si ya existe el registro en Añadir
                int resultadoAñadido = gestorDB.existeUsuarioAñadido(usuario,usuarioAñadidoET.getText().toString());

                //Mirar si el usuario a añadir existe
                int resultadoExiste = gestorDB.existeUsuario(usuarioAñadidoET.getText().toString());

                if(resultadoAñadido==1 && resultadoExiste==0){
                    gestorDB.añadirUsuario(usuario,usuarioAñadidoET.getText().toString());
                    listaUsuarios.clear();
                    listaUsuarios.addAll(gestorDB.conseguirUsuariosAñadidos(usuario));
                    eladap.notifyDataSetChanged();
                }
                else if(resultadoExiste==1){
                    //Si el usuario a añadir no existe
                    String error = getString(R.string.user_not_exists);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), error, duration);
                    toast.show();
                }else{
                    //Si ya se encuentra en la lista
                    String error = getString(R.string.already_on_list);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), error, duration);
                    toast.show();
                }

            }
        });
        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Si hace click en cancel no se añade el usuario
            }
        });
        alert.show();
    }
}