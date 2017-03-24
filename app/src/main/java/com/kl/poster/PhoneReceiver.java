package com.kl.poster;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by 10170153 on 12/2/2016.
 */
public class PhoneReceiver extends BroadcastReceiver {
    private static final String TAG = "message";
    private static boolean mIncomingFlag = false;
    private static String mIncomingNumber = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果是拨打电话
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            mIncomingFlag = false;
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.i(TAG, "call OUT:" + phoneNumber);
        } else {
            // 如果是来电
            TelephonyManager tManager = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
            switch (tManager.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    mIncomingNumber = intent.getStringExtra("incoming_number");
                    Log.i(TAG, "RINGING :" + mIncomingNumber);
                    String pName = getName(context,mIncomingNumber);
                    if(pName==null){
                        Sender.broadcast(mIncomingNumber+ " 来电...");
                    }else{
                        Sender.broadcast(pName + " 来电...");
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (mIncomingFlag) {
                        Log.i(TAG, "incoming ACCEPT :" + mIncomingNumber);
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if (mIncomingFlag) {
                        Log.i(TAG, "incoming IDLE");
                    }
                    break;
            }
        }
    }
    public String getName(Context context, String mNumber) {
        String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        Log.d(TAG, "getPeople ---------");

        // 将自己添加到 msPeers 中
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,    // Which columns to return.
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = '" + mNumber + "'", // WHERE clause.
                null,          // WHERE clause value substitution
                null);   // Sort order.

        if( cursor == null ) {
            Log.d(TAG, "getPeople null");
            return null;
        }
        Log.d(TAG, "getPeople cursor.getCount() = " + cursor.getCount());
        for( int i = 0; i < cursor.getCount(); i++ )
        {
            cursor.moveToPosition(i);

            // 取得联系人名字
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String name = cursor.getString(nameFieldColumnIndex);
            Log.i("Contacts", "" + name + " .... " + nameFieldColumnIndex); // 这里提示 force close
            return name;
        }
        return null;
    }
}
