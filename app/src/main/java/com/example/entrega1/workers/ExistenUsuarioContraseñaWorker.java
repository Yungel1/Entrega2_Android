package com.example.entrega1.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ExistenUsuarioContraseñaWorker extends Worker {
    public ExistenUsuarioContraseñaWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/asanchez294/WEB/entrega2/existenUsuarioContraseña.php";
        HttpURLConnection urlConnection;
        try {
            URL destino = new URL(direccion);
            //Obtener los parámetros del Data
            String usuario = getInputData().getString("usuario");
            String contraseña = getInputData().getString("contraseña");
            JSONObject parametrosJSON = new JSONObject();
            try {
                //Pasar como parámetros JSON
                parametrosJSON.put("usuario",usuario);
                parametrosJSON.put("contraseña",contraseña);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Abrir conexión
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
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                JSONObject jsonObject = new JSONObject(result);
                String resultado = jsonObject.getString("resultado");
                inputStream.close();

                Data resultados = new Data.Builder()
                        .putString("resultado",resultado)
                        .build();
                return Result.success(resultados);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Result.failure();
    }
}
