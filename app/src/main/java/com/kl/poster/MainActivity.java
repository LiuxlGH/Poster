package com.kl.poster;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sp;
    SpeechControl speechControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp= getSharedPreferences("POSTER",
                Activity.MODE_PRIVATE);

        SpeechControl.initTTS(this);
        FloatWindowControl.init(this);

        if (Build.VERSION.SDK_INT >= 23) {
            if(!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(intent);
                return;
            } else {
                //绘ui代码, 这里说明6.0系统已经有权限了
            }
        } else {
            //绘ui代码,这里android6.0以下的系统直接绘出即可
        }

        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this,NotificationService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(this,NotificationService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);

    }

    /**
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
//        address = getLocalIPAddress();
//        new BroadCastUdp(number).start();
        updateButton();
    }

    private void updateButton() {
        boolean isBroadcast = sp.getBoolean("broadcast",true);
        if(isBroadcast){
            findViewById(R.id.btnStartBroadcast).setVisibility(View.GONE);
            findViewById(R.id.btnStopBroadcast).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.btnStartBroadcast).setVisibility(View.VISIBLE);
            findViewById(R.id.btnStopBroadcast).setVisibility(View.GONE);
        }
        Sender.isBroadcast = isBroadcast;

        boolean isVoiceOn = sp.getBoolean("voice",true);
        if(isVoiceOn){
            findViewById(R.id.btnVoiceOff).setVisibility(View.VISIBLE);
            findViewById(R.id.btnVoiceOn).setVisibility(View.GONE);
        }else{
            findViewById(R.id.btnVoiceOn).setVisibility(View.VISIBLE);
            findViewById(R.id.btnVoiceOff).setVisibility(View.GONE);
        }
        SpeechControl.isSpeakCmd = isVoiceOn;

        boolean isPopUp = sp.getBoolean("popup",true);
        if(isPopUp){
            findViewById(R.id.btnFloatWindowOff).setVisibility(View.VISIBLE);
            findViewById(R.id.btnFloatWindowOn).setVisibility(View.GONE);
        }else{
            findViewById(R.id.btnVoiceOn).setVisibility(View.VISIBLE);
            findViewById(R.id.btnFloatWindowOff).setVisibility(View.GONE);
        }
        FloatWindowControl.isPopup=isPopUp;

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnTest:
                String text = "你好，小朋友, 好久不见，你已经长大了";
//                Sender.broadcast(text);
//                FloatWindowControl.showInTopWindow(text);
//                SpeechControl.speak(text);

                NotificationManager  manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification.Builder builder = new Notification.Builder(this);
                builder.setAutoCancel(true);
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setContentTitle("滴滴");
                builder.setContentText(text);
                builder.setDefaults(Notification.DEFAULT_SOUND| Notification.DEFAULT_VIBRATE);
                Intent intent0 = new Intent(this, MainActivity2.class);
                PendingIntent pIntent = PendingIntent.getActivity(this, 1, intent0,PendingIntent.FLAG_ONE_SHOT);
                builder.setContentIntent(pIntent);
                manager.notify(0, builder.build());

                break;
            case R.id.btnStartBroadcast:
                sp.edit().putBoolean("broadcast",true).commit();
                break;
            case R.id.btnStopBroadcast:
                sp.edit().putBoolean("broadcast",false).commit();
                break;
            case R.id.btnVoiceOff:
                sp.edit().putBoolean("voice",false).commit();
                speechControl.isSpeakCmd=false;
                speechControl.shutdown();
                break;
            case R.id.btnVoiceOn:
                sp.edit().putBoolean("voice",true).commit();
                speechControl.isSpeakCmd = true;
                speechControl.initTTS(this);
                break;
            case R.id.btnFloatWindowOff:
                sp.edit().putBoolean("popup",true).commit();
                break;
            case R.id.btnFloatWindowOn:
                sp.edit().putBoolean("popup",true).commit();
                break;
            case R.id.btnOpenPermission:
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
                break;

        }
        updateButton();
    }


    private static String LOG_TAG="WifiBroadcastActivity";
    private boolean start = true;
    private String address;
    public static final int DEFAULT_PORT = 1987;
    private static final int MAX_DATA_PACKET_LENGTH = 40;
    private byte[] buffer = new byte[MAX_DATA_PACKET_LENGTH];


    private String number = getRandomNumber();

    private String getLocalIPAddress(){
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex){
            Log.e(LOG_TAG, ex.toString());
        }
        return null;
    }

    private String getRandomNumber(){
        int num = new Random().nextInt(65536);
        String numString = String.format("xaaaaaaa", num);
        return numString;
    }

    public class BroadCastUdp extends Thread{
        private String dataString;
        private DatagramSocket udpSocket;
        public BroadCastUdp( String dataString ) {
            this.dataString = dataString;
        }

        public void run(){
            DatagramPacket dataPacket = null;

            try {
                udpSocket = new DatagramSocket(DEFAULT_PORT );

                dataPacket = new DatagramPacket(buffer, MAX_DATA_PACKET_LENGTH);
                byte[] data = dataString.getBytes();
                dataPacket.setData( data );
                dataPacket.setLength( data.length );
                dataPacket.setPort( DEFAULT_PORT );

                InetAddress broadcastAddr;

                broadcastAddr = InetAddress.getByName("255.255.255.255");
                dataPacket.setAddress(broadcastAddr);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.toString());
            }
//            while( start ){
                try {
                    udpSocket.send(dataPacket);
                    sleep( 10 );
                } catch(Exception e) {
                    Log.e(LOG_TAG, e.toString());
                }
//            }

            udpSocket.close();
        }
    }
}
