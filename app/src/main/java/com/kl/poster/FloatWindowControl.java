package com.kl.poster;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

/**
 * Created by 10170153 on 3/20/2017.
 */

public class FloatWindowControl {
    static WindowManager wm;
    static TextView txt;
    static boolean isTxtUpdated = false;
    static Context context;

    public static boolean isPopup = false;

    public static void init(Activity activity){
        context = activity.getApplicationContext();
    }

    public static void showInTopWindow(Context context,String msg) {
        if (!isPopup) {
            return;
        }

        wm = (WindowManager) context.getApplicationContext().getSystemService(
                Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//
//        // 设置window type
//        params.type = WindowManager.LayoutParams.TYPE_PHONE;//TYPE_SYSTEM_ALERT  has high priority
//        /*
//         * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
//         * 即拉下通知栏不可见
//         */
//
//        params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
//
//        // 设置Window flag
//        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        /*
//         * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
//         * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
//         * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
//         */
//
//        // 设置悬浮窗的长得宽
//        params.width = context.getResources().getDisplayMetrics().widthPixels;
//        params.height = 200;

//        // 设置悬浮窗的Touch监听
//        btn_floatView.setOnTouchListener(new OnTouchListener()
//        {
//            int lastX, lastY;
//            int paramX, paramY;
//
//            public boolean onTouch(View v, MotionEvent event)
//            {
//                switch (event.getAction())
//                {
//                    case MotionEvent.ACTION_DOWN:
//                        lastX = (int) event.getRawX();
//                        lastY = (int) event.getRawY();
//                        paramX = params.x;
//                        paramY = params.y;
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        int dx = (int) event.getRawX() - lastX;
//                        int dy = (int) event.getRawY() - lastY;
//                        params.x = paramX + dx;
//                        params.y = paramY + dy;
//                        // 更新悬浮窗位置
//                        wm.updateViewLayout(btn_floatView, params);
//                        break;
//                }
//                return true;
//            }
//        });


        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, 0, 0,
                PixelFormat.TRANSPARENT);
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.gravity = Gravity.TOP;
//        params.x = 100;
//        params.y = 300;

        isTxtUpdated = false;
        if (txt != null) {
            txt.setText(msg);
            isTxtUpdated = true;
        } else {
            txt = new TextView(context);
            txt.setText(msg);
            txt.setPadding(20, 30, 30, 20);
            txt.setTextColor(context.getResources().getColor(android.R.color.white));
            txt.getPaint().setFakeBoldText(true);
            txt.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
            TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1.0f,
                    Animation.RELATIVE_TO_SELF, 0f);
            animation.setDuration(500);
            txt.startAnimation(animation);
            wm.addView(txt, params);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    int count = 0;
                    while (count < 6) {
                        if (isTxtUpdated) {
                            count = 0;
                        }
                        try {
                            Thread.sleep(500);
                            count++;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if (txt == null) {
                        return;
                    }
//                    context.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            wm.removeView(txt);
//                            txt = null;
//                        }
//                    });
                }
            }).start();
        }
    }

}
