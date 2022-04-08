package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

        //lanzar el worker con la petición a la base de datos (obtener la lista de usuarios añadidos)
        OneTimeWorkRequest otwr = gestorDB.conseguirUsuariosAñadidos(usuario,this);
        //observar los cambios en el work
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if(workInfo != null && workInfo.getState().isFinished()){

                            String listaString = workInfo.getOutputData().getString("datos");
                            if (listaString!=null) {
                                //Convertir el string en array de nuevo
                                String[] strings = listaString.substring(1, listaString.length() - 1).split(",");
                                for (int i = 1; i < strings.length; i++) {
                                    strings[i] = strings[i].substring(1);
                                }
                                //Crear lista de usuarios
                                List<String> list = Arrays.asList(strings);
                                listaUsuarios = new ArrayList<String>(list);
                            }
                            else{
                                listaUsuarios = new ArrayList<String>();
                            }

                            int image = R.drawable.mail;

                            //Asignación al listview
                            lista = (ListView) findViewById(R.id.usersLIST);
                            eladap= new AdaptadorListView(getApplicationContext(),listaUsuarios,image);
                            lista.setAdapter(eladap);
                        }
                    }
                });

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

    /*public void onClickCambiarEmail(View v){
        //Alerta para cambiar el email
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText emailCambioET = new EditText(this);
        emailCambioET.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        //Título y texto (email actual)
        alert.setTitle(R.string.change_email);
        alert.setMessage(getString(R.string.actual_email)+" "+gestorDB.conseguirEmail(usuario,this));

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
    }*/

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
                //lanzar el worker con la petición a la base de datos (comprobar si existe el usuario añadido)
                OneTimeWorkRequest otwrAñadido = gestorDB.existeUsuarioAñadido(usuario,usuarioAñadidoET.getText().toString(),UsuariosActivity.this);

                //lanzar el worker con la petición a la base de datos (comprobar si existe el usuario)
                OneTimeWorkRequest otwrUsuario = gestorDB.existeUsuario(usuarioAñadidoET.getText().toString(),UsuariosActivity.this);

                //encadenar work
                WorkContinuation syncChain = WorkManager.getInstance(UsuariosActivity.this)
                        .beginWith(otwrAñadido)
                        .then(otwrUsuario);
                syncChain.enqueue();

                //observar los cambios en la syncChain
                syncChain.getWorkInfosLiveData()
                        .observe(UsuariosActivity.this, new Observer<List<WorkInfo>>() {
                            @Override
                            public void onChanged(List<WorkInfo> workInfos) {

                                if(workInfos.get(0) != null && workInfos.get(0).getState().isFinished() && workInfos.get(1) != null && workInfos.get(1).getState().isFinished()){

                                    int resultadoAñadido;
                                    int resultadoExiste;
                                    //Saber que work es cada uno para recoger el resultado correcto
                                    if(workInfos.get(0).getTags().contains("existeUsuario")){
                                        resultadoExiste = Integer.parseInt(workInfos.get(0).getOutputData().getString("resultado"));
                                        resultadoAñadido = Integer.parseInt(workInfos.get(1).getOutputData().getString("resultado"));

                                    }else{
                                        resultadoAñadido = Integer.parseInt(workInfos.get(0).getOutputData().getString("resultado"));
                                        resultadoExiste = Integer.parseInt(workInfos.get(1).getOutputData().getString("resultado"));
                                    }
                                    if(resultadoAñadido==1 && resultadoExiste==0){
                                        //Lanzar el worker para añadir usuario a la lista del usuario logeado
                                        OneTimeWorkRequest otwrAñadir = gestorDB.añadirUsuario(usuario,usuarioAñadidoET.getText().toString(),UsuariosActivity.this);

                                        //observar los cambios en el work
                                        WorkManager.getInstance(UsuariosActivity.this).getWorkInfoByIdLiveData(otwrAñadir.getId())
                                                .observe(UsuariosActivity.this, new Observer<WorkInfo>() {
                                                    @Override
                                                    public void onChanged(WorkInfo workInfo) {
                                                        if(workInfo != null && workInfo.getState().isFinished()){

                                                            OneTimeWorkRequest otwrUsuarios = gestorDB.conseguirUsuariosAñadidos(usuario,UsuariosActivity.this);

                                                            //observar los cambios en el work
                                                            WorkManager.getInstance(UsuariosActivity.this).getWorkInfoByIdLiveData(otwrUsuarios.getId())
                                                                    .observe(UsuariosActivity.this, new Observer<WorkInfo>() {
                                                                        @Override
                                                                        public void onChanged(WorkInfo workInfo) {
                                                                            if (workInfo != null && workInfo.getState().isFinished()) {
                                                                                String listaString = workInfo.getOutputData().getString("datos");
                                                                                listaUsuarios.clear();
                                                                                if (listaString != null) {
                                                                                    //Convertir el string en array de nuevo
                                                                                    String[] strings = listaString.substring(1, listaString.length() - 1).split(",");
                                                                                    for (int i = 1; i < strings.length; i++) {
                                                                                        strings[i] = strings[i].substring(1);
                                                                                    }
                                                                                    ArrayList<String> list = new ArrayList<String>(Arrays.asList(strings));
                                                                                    //Añadir la nueva lista
                                                                                    listaUsuarios.addAll(list);
                                                                                }
                                                                                eladap.notifyDataSetChanged();
                                                                            }
                                                                        }
                                                                        }
                                                                    );
                                                        }
                                                    }
                                                });

                                    }
                                    else if(resultadoExiste==1){
                                        //Si el usuario a añadir no existe
                                        Log.d("usuarioExiste",Integer.toString(resultadoExiste));
                                        Log.d("usuarioAñadido",Integer.toString(resultadoAñadido));
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
                            }
                        });
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