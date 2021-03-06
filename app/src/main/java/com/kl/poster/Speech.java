package com.kl.poster;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by 10170153 on 3/20/2017.
 */
class SpeechControl {
    static TextToSpeech textToSpeech;
    public static boolean isSpeakCmd = false;

    public static void initTTS(Context context) {
        if (textToSpeech == null){
            textToSpeech = new TextToSpeech(context, listener); // 参数Context,TextToSpeech.OnInitListener
        }
    }

    public static void speak(String msg) {

        if(!isSpeakCmd){
            return;
        }
//        textToSpeech.setPitch(3.0f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
        textToSpeech.setSpeechRate(1.5f);
        textToSpeech.speak(msg, TextToSpeech.QUEUE_FLUSH, null);

    }

    static TextToSpeech.OnInitListener listener = new TextToSpeech.OnInitListener() {
        /**
         * 用来初始化TextToSpeech引擎
         * status:SUCCESS或ERROR这2个值
         * setLanguage设置语言，帮助文档里面写了有22种
         * TextToSpeech.LANG_MISSING_DATA：表示语言的数据丢失。
         * TextToSpeech.LANG_NOT_SUPPORTED:不支持
         */
        @Override
        public void onInit(int status) {
            if(status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.CHINA);
            }
        }
    };

    public static void shutdown() {
        if(textToSpeech!=null) {
            try {
                textToSpeech.stop(); // 不管是否正在朗读TTS都被打断
                textToSpeech.shutdown(); // 关闭，释放资源
                textToSpeech = null;
            }catch (Exception ex){
                textToSpeech = null;
            }
        }
    }

    public static void stop(){
        textToSpeech.stop();
    }

}
