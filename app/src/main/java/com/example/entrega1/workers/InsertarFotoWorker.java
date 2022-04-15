package com.example.entrega1.workers;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class InsertarFotoWorker extends Worker {
    public InsertarFotoWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String direccion = "http://ec2-18-132-60-229.eu-west-2.compute.amazonaws.com/asanchez294/WEB/entrega2/insertarFoto.php";
        HttpURLConnection urlConnection;
        String uriString = getInputData().getString("uriString");
        int anchoDestino = getInputData().getInt("ancho",100);
        int altoDestino = getInputData().getInt("alto",100);
        Uri uriimagen = Uri.parse(uriString);
        String image = null;
        byte[] fototransformada = null;
        try {
            Bitmap bitmapFoto = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uriimagen);
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
            Bitmap bitmapredimensionado = Bitmap.createScaledBitmap(bitmapFoto,anchoFinal,altoFinal,true);

            //Guardar en la base de datos
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapredimensionado.compress(Bitmap.CompressFormat.PNG, 100, stream);
            fototransformada = stream.toByteArray();
            image = Base64.encodeToString(fototransformada, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject parametrosJSON = new JSONObject();
        try {
            parametrosJSON.put("image",image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametrosJSON);
            out.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                return Result.success();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.failure();
    }
}
