package com.example.mao.service;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.mao.bean.APPInfo;
import com.example.mao.messageencrypt.ApkTool;
import com.example.mao.util.New_SharePre;
import com.example.mao.util.SP;

import java.util.List;
import java.util.SortedMap;
import java.util.TimerTask;
import java.util.TreeMap;

/**
 * Created by Mao on 2017/5/6.
 */

public class LockTask extends TimerTask {
    private Context mContext;
    private ActivityManager mActivityManager;

    public LockTask(Context context) {
        mContext = context;
        mActivityManager = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
    }

    @Override
    public void run() {
        String packageName="";
        List localList = mActivityManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo localRunningTaskInfo = (ActivityManager.RunningTaskInfo)localList.get(0);
        packageName = localRunningTaskInfo.topActivity.getPackageName();
        if (findPackageLock(packageName)) {
            Intent intent = new Intent();
            intent.setClassName("com.example.mao.messageencrypt", "com.example.mao.activity.UnlockActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", packageName);
            mContext.startActivity(intent);
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
