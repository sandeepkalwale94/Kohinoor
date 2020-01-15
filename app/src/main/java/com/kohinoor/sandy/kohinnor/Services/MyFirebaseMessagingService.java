package com.kohinoor.sandy.kohinnor.Services;

import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by Belal on 12/8/2017.
 */

//class extending FirebaseMessagingService
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        //if the message contains data payload
        //It is a map of custom keyvalues
        //we can read it easily
        if (remoteMessage.getData().size() > 0) {
            //handle the data message here
            //  Map<String,String> payload = remoteMessage.getData();
            // showNotification(payload);
        }

        //getting the title and the body
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

       /* private void showNotification(Map<String,String> payload)
    {
        NotificationCompat.
    }
        //then here we can use the title and body to build a notification
    }*/
    }
}