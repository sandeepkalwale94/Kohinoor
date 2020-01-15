package com.kohinoor.sandy.kohinnor.CustomClasses;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.kohinoor.sandy.kohinnor.Activities.ArticleListActivity;
import com.kohinoor.sandy.kohinnor.Activities.HomeActivity;
import com.kohinoor.sandy.kohinnor.Activities.StudyDataActivity;
import com.kohinoor.sandy.kohinnor.R;

import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Belal on 12/8/2017.
 */

public class MyNotificationManager {

    private Context mCtx;
    private static MyNotificationManager mInstance;

    private MyNotificationManager(Context context) {
        mCtx = context;
    }

    public static synchronized MyNotificationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyNotificationManager(context);
        }
        return mInstance;
    }

    public void displayNotification(String title, String body, String notesType, String materialType, String subItem, String key) {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //builder.setSound(sound);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mCtx, Constants.CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_app_logo)
                        .setContentTitle(title)
                        .setSound(sound)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setVibrate(new long[]{500, 500})
                        .setContentText(body);
        //Need to modified the logic according to activity
        Intent resultIntent = new Intent();
        if (title.equals("नवीन PDF नोट्स")) {
            resultIntent = new Intent(mCtx, StudyDataActivity.class);
        } else if (title.equals("महत्वाचे लेख")) {
            resultIntent = new Intent(mCtx, ArticleListActivity.class);
        } else {
            resultIntent = new Intent(mCtx, HomeActivity.class);
        }

        resultIntent.putExtra("notesType", notesType);
        resultIntent.putExtra("StudyMaterialType", materialType);
        resultIntent.putExtra("subItem", subItem);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        // mBuilder.setFullScreenIntent(pendingIntent,true);

        NotificationManager mNotifyMgr =
                (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);
        if (mNotifyMgr != null) {
            mNotifyMgr.notify(1, mBuilder.build());
        }
    }

}