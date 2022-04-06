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

public class AñadirUsuarioWorker extends Worker {
    public AñadirUsuarioWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String direccion = "http://ec2-18-132-60-229.eu-west-2.compute.amazonaws.com/asanchez294/WEB/entrega2/añadirUsuario.php";
        HttpURLConnection urlConnection;
        String usuarioAñade = getInputData().getString("usuarioAñade");
        String usuarioAñadido = getInputData().getString("usuarioAñadido");
        JSONObject parametrosJSON = new JSONObject();
        try {
            parametrosJSON.put("usuarioAñade",usuarioAñade);
            parametrosJSON.put("usuarioAñadido",usuarioAñadido);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //String parametros = "usuarioAñade="+usuarioAñade+"&usuarioAñadido="+usuarioAñadido;
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
