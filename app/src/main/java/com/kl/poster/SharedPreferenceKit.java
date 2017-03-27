package com.kl.poster;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.ParcelUuid;

/**
 * Created by 10170153 on 3/27/2017.
 */

public class SharedPreferenceKit {

    private SharedPreferences sp;
    private String CMD_VOICE= "voice";
    private String CMD_POPUP= "popup";
    private String CMD_BROADCAST= "broadcast";
    public SharedPreferenceKit(){}
    public static SharedPreferenceKit getInstance(){
        return ClassHolder.INSTANCE;
    }
    static class ClassHolder{
        static final SharedPreferenceKit INSTANCE = new SharedPreferenceKit();
    }

    public void initSharedPreferences(Context context){
        sp = context.getSharedPreferences("POSTER",
                Activity.MODE_PRIVATE);
    }

    public void saveVoiceCMD(boolean cmd){
        sp.edit().putBoolean(CMD_VOICE,cmd).commit();
    }
    public boolean getVoiceCMD(){
        return sp.getBoolean(CMD_VOICE,false);
    }

    public void saveFloatWindowCMD(boolean cmd){
        sp.edit().putBoolean(CMD_POPUP,cmd).commit();
    }
    public boolean getFloatWindowCMD(){
        return sp.getBoolean(CMD_POPUP,false);
    }

    public void saveBroadcastCMD(boolean cmd){
        sp.edit().putBoolean(CMD_BROADCAST,cmd).commit();
    }
    public boolean getBroadcastCMD(){
        return sp.getBoolean(CMD_BROADCAST,false);
    }

}
