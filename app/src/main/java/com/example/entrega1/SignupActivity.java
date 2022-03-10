package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class SignupActivity extends AppCompatActivity {

    private String errorUsuario;
    private String errorEmail;
    private int duration;
    private Toast toast;

    private MiBD gestorDB;

    private EditText usuarioET;
    private EditText contraseñaET;
    private EditText emailET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

            // y pasamos a la siguiente actividad
            i = new Intent (SignupActivity.this, UsuariosActivity.class);
            i.putExtra("usuario",usuario);
            startActivity(i);
        } else{
            //Si es no es válido mostramos el toast de usuario no válido
            toast = Toast.makeText(getApplicationContext(), errorUsuario, duration);
            toast.show();
        }

    }

    public void onClickIdioma(View v){

    }

    public void onClickIniciaSesion(View v){
        Intent i = new Intent (SignupActivity.this, LoginActivity.class);
        startActivity(i);
    }
}