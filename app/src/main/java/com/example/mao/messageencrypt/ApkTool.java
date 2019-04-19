package com.example.mao.messageencrypt;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.mao.bean.APPInfo;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mao on 16/3/3.
 * 扫描本地安装的应用,工具类
 */
public class ApkTool {
    static  String TAG = "ApkTool";
    Context context;
    public static List<APPInfo> mLocalInstallApps = null;
    public ApkTool(Context context){
        this.context = context;
    }
    public  List<APPInfo> scanLocalInstallAppList(PackageManager packageManager) {
        List<APPInfo> AppInfos = new ArrayList<APPInfo>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            SharedPreferences sp= context.getSharedPreferences("APPLock", Activity.MODE_PRIVATE);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //过滤掉系统app
                if(packageInfo.applicationInfo.loadLabel(packageManager).toString()=="短信"){

                }else if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                    continue;
                }

                APPInfo myAppInfo = new APPInfo();
                myAppInfo.setAppName(packageInfo.applicationInfo.loadLabel(packageManager).toString());
                if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
                    continue;
                }
                myAppInfo.setPackageName(packageInfo.applicationInfo.packageName);
                myAppInfo.setImage(packageInfo.applicationInfo.loadIcon(packageManager));
                myAppInfo.setRadio(sp.getBoolean(packageInfo.applicationInfo.loadLabel(packageManager).toString(),false));
                AppInfos.add(myAppInfo);
            }
        }catch (Exception e){
            Log.e(TAG,"===============获取应用包信息失败");
        }
        return findAllClientConnections(AppInfos);
    }

    public List<APPInfo> findAllClientConnections(List<APPInfo> AppInfos)
    {
        //将所有的数组按APP名称的首字母进行排序。
        Collections.sort(AppInfos,new Comparator<APPInfo>(){
            public int compare(APPInfo o1, APPInfo o2) {
                String s1=o1.getAppName();
                String s2=o2.getAppName();
                return Collator.getInstance(Locale.CHINESE).compare(s1, s2);
            }
        });
        return AppInfos;
    }

}