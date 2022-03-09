package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MailActivity extends AppCompatActivity {

    private TextView usuarioTV;
    private TextView emailTV;
    private EditText asuntoET;
    private EditText cuerpoET;
    private Bundle extras;
    private MiBD gestorDB;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        }
        gestorDB = new MiBD(this, "Users", null, 1);
        email = gestorDB.conseguirEmail(usuario);
        emailTV.setText(email);

    }

    public void onClickEnviar(View v){
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