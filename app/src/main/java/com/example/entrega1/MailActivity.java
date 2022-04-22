package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MailActivity extends AppCompatActivity {

    private TextView usuarioTV;
    private TextView emailTV;
    private EditText asuntoET;
    private EditText cuerpoET;
    private Bundle extras;
    private MiBD gestorDB;
    String email;
    ImageButton botonImagen;

    private String idioma;

    static final int CODIGO_FOTO_ARCHIVO = 1;
    Uri uriimagen = null;
    Bitmap bitmapredimensionado = null;

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
        botonImagen = findViewById(R.id.imagenBTN);


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

        //lanzar el worker con la petición a la base de datos  (conseguir email)
        OneTimeWorkRequest otwr = gestorDB.conseguirEmail(usuario,this);
        //observar los cambios en el work
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if(workInfo != null && workInfo.getState().isFinished()){
                            //Obtener el resultado de la petición (si existe o no el usuario en la base de datos)
                            email = workInfo.getOutputData().getString("email");
                            emailTV.setText(email);

                        }
                    }
                });

        //Conseguir imagen para el ImageButton en caso de girar la pantalla por ejemplo
        if (savedInstanceState != null){
            bitmapredimensionado = savedInstanceState.getParcelable("bitmap");
            botonImagen.setImageBitmap(bitmapredimensionado);
        }

    }

    //Para obtener el bitmap que nos viene del intent para sacar la foto
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODIGO_FOTO_ARCHIVO && resultCode == RESULT_OK) {
            Bitmap bitmapFoto = null;
            try {
                //Obtener el bitmap de la uri
                bitmapFoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriimagen);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Recortar
            int anchoDestino = botonImagen.getWidth();
            int altoDestino = botonImagen.getHeight();
            int anchoImagen = bitmapFoto.getWidth();
            int altoImagen = bitmapFoto.getHeight();
            float ratioImagen = (float) anchoImagen / (float) altoImagen;
            float ratioDestino = (float) anchoDestino / (float) altoDestino;
            int anchoFinal = anchoDestino;
            int altoFinal = altoDestino;
            if (ratioDestino > ratioImagen) {
                anchoFinal = (int) ((float)altoDestino * ratioImagen);
            } else {
                altoFinal = (int) ((float)anchoDestino / ratioImagen);
            }
            bitmapredimensionado = Bitmap.createScaledBitmap(bitmapFoto,anchoFinal,altoFinal,true);
            //Poner la imagen en el ButtonImage
            botonImagen.setImageBitmap(bitmapredimensionado);
            //Llamada a MiBD para que inserte la foto en la base de datos
            gestorDB.insertarFoto(uriimagen.toString(),anchoDestino,altoDestino,this);

        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (bitmapredimensionado != null) {
            //Para que no se pierda la imagen al girar la pantalla, por ejemplo
            outState.putParcelable("bitmap", bitmapredimensionado);
        }
    }

    //Click en el botón para poder sacar la foto
    public void onClickImagen(View v){

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String nombrefich = "IMG_" + timeStamp + "_";
        File directorio=this.getFilesDir();
        File fichImg = null;
        uriimagen = null;

        try {
            fichImg = File.createTempFile(nombrefich, ".jpg",directorio);
        } catch (IOException e) {
            e.printStackTrace();
        }
        uriimagen = FileProvider.getUriForFile(this, "com.example.tema17ejercicio1.provider", fichImg);

        Intent elIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        elIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriimagen);
        startActivityForResult(elIntent, CODIGO_FOTO_ARCHIVO);

        /*//Sacar la foto
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }*/

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
        intent.putExtra(Intent.EXTRA_STREAM, uriimagen);
        intent.setType("image/png");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }



}