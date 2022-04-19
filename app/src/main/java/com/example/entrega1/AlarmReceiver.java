package com.example.entrega1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private NotificationCompat.Builder elBuilder;
    private NotificationManager elManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("luego","luego"+ SystemClock.elapsedRealtime());
        //Lanzar notificación de que se inició sesión hace 30 segundos
        elManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        elBuilder = new NotificationCompat.Builder(context, "canalAlarma");
        elBuilder.setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentTitle(context.getString(R.string.login_30_titulo))
                .setContentText(context.getString(R.string.login_30))
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .setAutoCancel(true);

        createNotificationChannel();
        elManager.notify(2, elBuilder.build());
    }

    private void createNotificationChannel() {
        // Crear NotificationChannel, solo para la API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel elCanal = new NotificationChannel("canalAlarma", "canalAlarma",
                    NotificationManager.IMPORTANCE_DEFAULT);

            elCanal.enableLights(true);
            elCanal.setLightColor(Color.RED);
            elCanal.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            elCanal.enableVibration(true);
            elManager.createNotificationChannel(elCanal);
        }
    }
}