package com.example.mao.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.mao.bean.APPInfo;
import com.example.mao.messageencrypt.ApkTool;
import com.example.mao.service.LockService;
import com.example.mao.util.SP;

import java.util.List;

/**
 * Created by Mao on 2017/5/8.
 */

public class BootReceiver extends BroadcastReceiver  {
    private static final String ACTION_USER_PRESENT = "android.intent.action.USER_PRESENT";
    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    private static final String ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN";


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("com.example.mao.open")) {
            openService(context);
        }

        boolean isServiceRunning = false;
        if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
            //检查Service状态
            ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service :manager.getRunningServices(Integer.MAX_VALUE)) {
                if("com.example.mao.service.LockService".equals(service.service.getClassName()))
                {
                    isServiceRunning = true;
                }
            }
            if (!isServiceRunning) {
                Intent i = new Intent(context, LockService.class);
                openService(context);
            }
        }
        Intent start = new Intent(context,LockService.class);
        start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.getAction().equals("com.example.mao.messageencrypt")) {
            openService(context);
        }else if (ACTION_BOOT.equals(intent.getAction())) {
            openService(context);
        }else if (ACTION_USER_PRESENT.equals(intent.getAction())) {
            List<APPInfo> AppInfos = new ApkTool(context).scanLocalInstallAppList(context.getPackageManager());
            for(int i = 0;i<AppInfos.size();i++){
                SP.save(context,AppInfos.get(i).getAppName()+"lock", true);
            }
            openService(context);
        }else if (ACTION_SHUTDOWN.equals(intent.getAction())) {
            openService(context);
        }
    }

    private void openService(Context context){
        if(SP.get(context,"open_encrypt",false))
            context.startService(new Intent(context, LockService.class));

    }
}