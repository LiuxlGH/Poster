package com.kl.poster;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by 10170153 on 12/2/2016.
 */
public class Sender {
    private static String sendToIP = "255.255.255.255";//"10.0.2.2" for emulator
    private static int inPort = 9988;//7798
    private static int outPort = 1987;
    public static boolean isBroadcast = false;

    public static void broadcast(final String sentData) {

        if(!isBroadcast){
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                DatagramSocket socket = null;
                try {

                    DatagramPacket packet = null;
                    socket = new DatagramSocket();
                    socket.setBroadcast(true);

                    InetAddress address = InetAddress.getByName("224.0.0.1");
                    InetAddress address255 = InetAddress.getByName("255.255.255.255");

//            Calendar calendar = Calendar.getInstance(java.util.Locale.getDefault());
//            long offsetInitial = calendar.get(Calendar.ZONE_OFFSET)
//                    + calendar.get(Calendar.DST_OFFSET);
//            long timestamp = System.currentTimeMillis() + offsetInitial;
//            String sentData = String.valueOf(timestamp) +  ":" + inPort;

                    packet = new DatagramPacket(sentData.getBytes(),
                            sentData.getBytes().length, new InetSocketAddress(
                            address255, outPort));

                    // send the packet to server
                    socket.send(packet);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e("SendThread Error", e.toString());
                } finally {
                }

            }
        }).start();
    }
}
