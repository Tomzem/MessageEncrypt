package com.example.mao.service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.mao.messageencrypt.R;
import com.example.mao.util.New_SharePre;
import com.example.mao.util.SP;

import java.util.Timer;

/**
 * Created by Mao on 2017/5/6.
 */

public class LockService  extends Service {
    //要引用的布局文件.
    RelativeLayout toucherLayout;
    //布局参数.
    WindowManager.LayoutParams params;
    //实例化的WindowManager.
    WindowManager windowManager;
    //状态栏高度.（接下来会用到）
    int statusBarHeight = -1;
    ImageButton img_xfc;

    private Timer mTimer;
    public static final int FOREGROUND_ID = 0;
    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
            LockTask lockTask = new LockTask(this);
            mTimer.schedule(lockTask, 0L, 1000L);
        }
    }

    public void onCreate() {
        super.onCreate();
        //OnCreate中来生成悬浮窗.
        createToucher();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimer();
        startForeground(FOREGROUND_ID, new Notification());
        return START_STICKY;
    }

    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }

    public void onDestroy() {
        if (New_SharePre.getData("isOpenLock", false)) {
            stopForeground(true);
            //startForeground(FOREGROUND_ID, new Notification());
            Intent intent = new Intent("com.example.mao.open");
            sendBroadcast(intent);
        } else {
            if (toucherLayout != null) {
                windowManager.removeView(toucherLayout);
            }
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
            super.onDestroy();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createToucher() {
        //赋值WindowManager&LayoutParam.
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        //设置type.系统提示型窗口，一般都在应用程序窗口之上.
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //设置效果为背景透明.
        params.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        //设置窗口初始停靠位置.
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;

        //设置悬浮窗口长宽数据.
        //注意，这里的width和height均使用px而非dp.这里我偷了个懒
        //如果你想完全对应布局设置，需要先获取到机器的dpi
        //px与dp的换算为px = dp * (dpi / 160).
        params.width = 140;
        params.height = 140;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局.
        toucherLayout = (RelativeLayout) inflater.inflate(R.layout.new_layout_xfc,null);
        //添加toucherlayout
        windowManager.addView(toucherLayout,params);

        //主动计算出当前View的宽高信息.
        toucherLayout.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

        //用于检测状态栏高度.
        int resourceId = getResources().getIdentifier("status_bar_height","dimen","android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        //浮动窗口按钮.
        img_xfc = (ImageButton) toucherLayout.findViewById(R.id.xfc_img);
        img_xfc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //ImageButton我放在了布局中心，布局一共300dp
                params.x = (int) event.getRawX() - 70;
                //这就是状态栏偏移量用的地方
                params.y = (int) event.getRawY() - 70 - statusBarHeight;
                windowManager.updateViewLayout(toucherLayout,params);
                return false;
            }
        });

        //其他代码...
    }
}