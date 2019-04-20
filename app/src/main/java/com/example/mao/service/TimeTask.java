package com.example.mao.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.example.mao.app.Config;
import com.example.mao.messageencrypt.ApkTool;
import com.example.mao.util.New_SharePre;
import com.example.mao.util.TimeUtil;

import java.util.List;
import java.util.Random;
import java.util.TimerTask;

/**
 * @author Tomze
 * @time 2019年04月20日 20:02
 * @desc
 */
public class TimeTask extends TimerTask {

    private Context mContext;
    private ActivityManager mActivityManager;
    private int time = 0;

    public TimeTask(Context context) {
        mContext = context;
        mActivityManager = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        time = New_SharePre.getData("time", Config.time);
    }

    @Override
    public void run() {
        String packageName="";
        List localList = mActivityManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo localRunningTaskInfo = (ActivityManager.RunningTaskInfo)localList.get(0);
        packageName = localRunningTaskInfo.topActivity.getPackageName();
        if (findPackageLock(packageName)) {
            time = time - 1;
            if (time%3 == 0) {
                New_SharePre.saveData("time", time);
            }
            if (TimeUtil.isNewDay()) {
                New_SharePre.saveData("time", Config.time);
                time = Config.time;
            }
            if (time <= 0) {
                this.cancel();
                Intent intent = new Intent(mContext,TimeService.class);
                mContext.stopService(intent);
                intent = new Intent(mContext, LockService.class);
                mContext.startService(intent);
            }
        }
    }


    private boolean findPackageLock(String packName){
        if(New_SharePre.getData("isOpenLock", false))
            if (ApkTool.findApp(packName)) {
                return true;
            }
        return false;
    }
}
