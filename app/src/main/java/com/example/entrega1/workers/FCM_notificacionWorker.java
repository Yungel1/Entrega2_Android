package com.example.entrega1.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FCM_notificacionWorker extends Worker {

    public FCM_notificacionWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String direccion = "http://ec2-18-132-60-229.eu-west-2.compute.amazonaws.com/asanchez294/WEB/entrega2/fcm_notificacion.php";
        HttpURLConnection urlConnection;
        String token = getInputData().getString("token");
        String titulo_noti = getInputData().getString("titulo_noti");
        String texto_noti = getInputData().getString("texto_noti");
        JSONObject parametrosJSON = new JSONObject();
        try {
            parametrosJSON.put("token",token);
            parametrosJSON.put("titulo_noti",titulo_noti);
            parametrosJSON.put("texto_noti",texto_noti);
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