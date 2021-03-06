package com.example.mao.messageencrypt;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.mao.util.SP;

import java.util.Timer;

/**
 * Created by Mao on 2017/5/6.
 */

public class LockService  extends Service {
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
        try {
            if (Build.VERSION.SDK_INT < 18) {
                startForeground(1120, new Notification());
            }
        } catch (Exception e) {
        }
        startForeground(FOREGROUND_ID, new Notification());
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimer();
        return START_STICKY;
    }

    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }

    public void onDestroy() {
        if(SP.get(this,"open_encrypt",false)){
            stopForeground(true);
            //startForeground(FOREGROUND_ID, new Notification());
            Intent intent = new Intent("com.example.mao.open");
            sendBroadcast(intent);
        }else {
            super.onDestroy();
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}