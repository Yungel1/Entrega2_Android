package com.example.entrega1;

import android.widget.Toast;

import androidx.annotation.NonNull;

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


        if (remoteMessage.getData().size() > 0) {
            Toast.makeText(getApplicationContext(), "mensaje", Toast.LENGTH_SHORT).show();
        }
        if (remoteMessage.getNotification() != null) {
            Toast.makeText(getApplicationContext(), "noti", Toast.LENGTH_SHORT).show();
        }

    }
}
