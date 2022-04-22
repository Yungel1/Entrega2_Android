package com.example.entrega1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ServicioFirebase extends FirebaseMessagingService {

    public ServicioFirebase() {
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);


    }

    public void onMessageReceived(RemoteMessage remoteMessage) {

        NotificationCompat.Builder elBuilder;
        NotificationManager elManager;

        elManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        if (remoteMessage.getData().size() > 0) {

        }
        if (remoteMessage.getNotification() != null) {
            //Se crea un canal en caso de ser necesario y se lanza la notificación
            String title = remoteMessage.getNotification().getTitle();
            String text = remoteMessage.getNotification().getBody();

            elBuilder = new NotificationCompat.Builder(this, "IdCanal");
            elBuilder.setSmallIcon(android.R.drawable.stat_sys_warning)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(text))
                    .setVibrate(new long[]{0, 1000, 500, 1000})
                    .setAutoCancel(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationChannel elCanal = new NotificationChannel("IdCanal", "NombreCanal",
                        NotificationManager.IMPORTANCE_DEFAULT);

                elCanal.enableLights(true);
                elCanal.setLightColor(Color.RED);
                elCanal.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                elCanal.enableVibration(true);

                elManager.createNotificationChannel(elCanal);
            }

            elManager.notify(1, elBuilder.build());


        }

    }

}
