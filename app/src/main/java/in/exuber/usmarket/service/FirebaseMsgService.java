package in.exuber.usmarket.service;



import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.splash.SplashActivity;
import in.exuber.usmarket.utils.Constants;

public class FirebaseMsgService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e("Notification","Recieved");
        Log.e("Notification Body", remoteMessage.getNotification().getBody());


        createNotification(remoteMessage.getNotification().getBody());

    }

    private void createNotification(String messageBody) {

        //Initialising shared preferences
        SharedPreferences marketPreference =  getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        boolean isLoggedIn = marketPreference.getBoolean(Constants.IS_LOGGED_IN, false);

        if (isLoggedIn)
        {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setContentTitle("US MRKT Notification");
            mBuilder.setContentText(messageBody);

            Intent intent= new Intent(this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Constants.IS_NOTIFICATION_CLICK,true);
            PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pi);
            mBuilder.setAutoCancel(true);


            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // notificationID allows you to update the notification later on.
            mNotificationManager.notify(1, mBuilder.build());
        }


    }


}
