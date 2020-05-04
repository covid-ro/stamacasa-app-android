package ro.gov.stamacasa.broadcast;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import ro.gov.stamacasa.R;
import ro.gov.stamacasa.ui.intro.SplashScreenActivity;

/**
 * Broadcast that is triggered when alarm goes on
 */

public class BroadcastAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context nContext, Intent nIntent) {

        if (nIntent!=null && nIntent.getExtras()!=null) {
            Bundle mBundle = nIntent.getExtras();
            sendNotification(nContext,mBundle.getString("request_code"));
        }
    }

    /**
     * Notification to trigger when alarm came
     * @param nContext - #wow
     * @param nRequestCode - for intents ( same combination : hour * 100 +minutes )
     */
    private void sendNotification(Context nContext,String nRequestCode) {

        String channelId = "reminder-01";
        String channelName = "Reminder";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) nContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }


        //intent with activity to open
        Intent mIntent = new Intent(nContext, SplashScreenActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mIntent.putExtra("reminder_form","yes" );

        PendingIntent mPendingIntent = PendingIntent.getActivity(nContext, Integer.parseInt(nRequestCode), mIntent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(nContext, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setLargeIcon(BitmapFactory.decodeResource(nContext.getResources(),R.mipmap.ic_launcher_round))
                        .setContentTitle(nContext.getString(R.string.notification_title))
                        .setContentText(nContext.getString(R.string.notification_text))
                        .setAutoCancel(false)
                        .setSound(defaultSoundUri)
                        .setColor(ContextCompat.getColor(nContext, R.color.colorPrimaryDark))
                        .setContentIntent(mPendingIntent);

        NotificationManager notificationManager = (NotificationManager) nContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Integer.parseInt(nRequestCode), notificationBuilder.build());
    }
}
