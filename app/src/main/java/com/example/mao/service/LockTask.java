package com.example.mao.service;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.mao.bean.APPInfo;
import com.example.mao.messageencrypt.ApkTool;
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
        if (Build.VERSION.SDK_INT >= 21) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) mContext.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,1000, time);
            if (stats != null) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    packageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            List localList = mActivityManager.getRunningTasks(1);
            ActivityManager.RunningTaskInfo localRunningTaskInfo = (ActivityManager.RunningTaskInfo)localList.get(0);
            packageName = localRunningTaskInfo.topActivity.getPackageName();
        }
        if (findPackageLock(packageName)) {
            Intent intent = new Intent();
            intent.setClassName("com.example.mao.messageencrypt", "com.example.mao.activity.UnlockActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", packageName);
            mContext.startActivity(intent);

        }


    }

    private boolean findPackageLock(String packageName){
        List<APPInfo> AppInfos = new ApkTool(mContext).scanLocalInstallAppList(mContext.getPackageManager());
        for(int i = 0;i<AppInfos.size();i++){
            if (AppInfos.get(i).getPackageName().equals(packageName)){
                if(SP.get(mContext,AppInfos.get(i).getAppName(),false)){
                    if(SP.get(mContext,AppInfos.get(i).getAppName()+"lock",false)){
                        if(SP.get(mContext,"open_encrypt",false))
                            return true;
                        else
                            return false;
                    } else{
                        return false;
                    }
                } else{
                    return false;
                }
            }
        }
        return false;
    }
}
