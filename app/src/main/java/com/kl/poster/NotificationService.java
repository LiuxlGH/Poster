package com.kl.poster;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class NotificationService extends NotificationListenerService {


    final String TAG = "KL";
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
// TODO Auto-generated method stub
        Log.d(TAG, "notification posted.");
        Notification mNotification = sbn.getNotification();
        if (mNotification != null) {
            Bundle extras = mNotification.extras;

            String title = extras.getString("android.title");
            String text = extras.getString("android.text");
            int userId = extras.getInt("android.originatingUserId");

            Log.d(TAG,"title: "+title);
            Log.d(TAG,"text: "+text);
            Log.d(TAG,"userId: "+userId);

            String[] strs = getResources().getStringArray(R.array.arr);
            for(String str : strs){
                if(!title.contains(str)){
                    continue;
                }

//                FloatWindowControl.showInTopWindow(this,text);
                SpeechControl.speak(text);
                Sender.broadcast(title+":: "+text);
                break;
            }

            Notification.Action[] mActions = mNotification.actions;

            if (mActions != null) {
                for (Notification.Action mAction : mActions) {
                    int icon = mAction.icon;
                    CharSequence actionTitle = mAction.title;
                    PendingIntent pendingIntent = mAction.actionIntent;

                }
            }
        }
    }


    @Override
    public void onNotificationRemoved(StatusBarNotification arg0) {
// TODO Auto-generated method stub

        Log.d(TAG, "notification removed.");

    }
}