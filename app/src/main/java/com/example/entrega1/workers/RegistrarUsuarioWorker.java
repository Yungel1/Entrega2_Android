package com.example.entrega1.workers;

import android.content.Context;

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

public class RegistrarUsuarioWorker extends Worker {
    public RegistrarUsuarioWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/asanchez294/WEB/entrega2/registrarUsuario.php";
        HttpURLConnection urlConnection;
        //Obtener los parámetros del Data
        String usuario = getInputData().getString("usuario");
        String contraseña = getInputData().getString("contraseña");
        String email = getInputData().getString("email");
        JSONObject parametrosJSON = new JSONObject();
        try {
            //Pasar como parámetros JSON
            parametrosJSON.put("usuario",usuario);
            parametrosJSON.put("contraseña",contraseña);
            parametrosJSON.put("email",email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            //Abrir conexión
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
