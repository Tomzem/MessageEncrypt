package com.example.mao.activity;

import android.app.ProgressDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mao.app.Config;
import com.example.mao.app.New_Observer;
import com.example.mao.adapter.New_AppAdapter;
import com.example.mao.bean.APPInfo;
import com.example.mao.messageencrypt.ApkTool;
import com.example.mao.messageencrypt.R;
import com.example.mao.service.LockService;
import com.example.mao.service.TimeService;
import com.example.mao.util.New_SharePre;
import com.example.mao.util.New_Util;
import com.example.mao.util.SP;
import com.example.mao.util.ToastSelf;
import com.example.mao.weiget.New_RelativeLayout;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 *  存剩余秒数
 *  当剩余秒数为0，且时间到的情况下 打开serviice
 *
 *  开关打开 时间没技术的化 打开计时器
 *
 *
 */
public class New_MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView lv_app;
    private New_AppAdapter mAppAdapter;
    private Context mContext;
    private New_RelativeLayout mLockSwitch;
    private New_RelativeLayout mSurplusTime;
    private Switch mSwitch;
    private Intent intent;
    private TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__main);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_label);
        setSupportActionBar(toolbar);
        getPremission();
        initView();
        initData();
        initAppList();
    }

    private void initView() {
        lv_app = (ListView) findViewById(R.id.lv_app);
        LinearLayout lv_header = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.new_head_list_app,null);
        mLockSwitch = (New_RelativeLayout) lv_header.findViewById(R.id.item_switch);
        mSurplusTime = (New_RelativeLayout) lv_header.findViewById(R.id.item_surplus_time);
        mSwitch = (Switch) lv_header.findViewById(R.id.swt_menu_open);
        tvTime = (TextView) lv_header.findViewById(R.id.tv_surplus_time);
        mLockSwitch.setOnClickListener(this);
        mSurplusTime.setOnClickListener(this);
        lv_app.addFooterView(lv_header);
    }

    private void initData() {
        mAppAdapter = new New_AppAdapter();
        lv_app.setAdapter(mAppAdapter);
        mSwitch.setChecked(New_SharePre.getData("isOpenLock", false));
        int time = New_SharePre.getData("time", Config.time);
        tvTime.setText(time+"秒");
        if (mSwitch.isChecked()) {
            if (time == 0) {
                intent = new Intent(this,LockService.class);
            } else {
                intent = new Intent(this,TimeService.class);
            }
            startService(intent);
        }
    }

    private void initAppList(){
        final ProgressDialog dialog  = new ProgressDialog(mContext);
        dialog.setMessage("加载应用列表...");
        dialog.setCancelable(false);
        dialog.show();
        Observable.create(new ObservableOnSubscribe<List<APPInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<APPInfo>> emitter) throws Exception {
                List<APPInfo> appInfo = ApkTool.scanLocalInstallAppList(mContext.getPackageManager());
                emitter.onNext(appInfo);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new New_Observer<List<APPInfo>>() {
                    @Override
                    public void onNext(List<APPInfo> appInfos) {
                        mAppAdapter.setData(mContext,appInfos);
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastSelf.ToastSelf("应用列表加载失败", mContext);
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (New_Util.isDoubleClick()){
            return;
        }
        if (view == mLockSwitch) {
            if (mSwitch.isChecked()) {
                mSwitch.setChecked(false);
                New_SharePre.saveData("isOpenLock", false);
                stopService(intent);
            } else {
                mSwitch.setChecked(true);
                New_SharePre.saveData("isOpenLock", true);
                int time = New_SharePre.getData("time", Config.time);
                if (time == 0) {
                    intent = new Intent(this,LockService.class);
                } else {
                    intent = new Intent(this,TimeService.class);
                }
                openSet();
                startService(intent);
            }
        } else if (view == mSurplusTime) {
            ToastSelf.ToastSelf("mSurplusTime", mContext);
        }
    }

    @Override
    public void onBackPressed() {
    }

    public void getPremission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(mContext)) {
                //若没有权限，提示获取.
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                ToastSelf.ToastSelf("请打开悬浮窗权限", mContext);
                startActivity(intent);
            }
        }
    }

    private void openSet(){
        if(isNoOption()){
            if(isNoSwitch()){
                SP.save(this,"isOpenLock", true);
                startService(new Intent(this, TimeService.class));
                sendBroadcast(new Intent("com.example.mao.messageencrypt"));
            }else{
                SP.save(this,"isOpenLock", true);
                startService(new Intent(this, TimeService.class));
                sendBroadcast(new Intent("com.example.mao.messageencrypt"));
                ToastSelf.ToastSelf("请您为应用开放高级权限",this);
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
            }
        }else{
            SP.save(this,"isOpenLock", true);
            startService(new Intent(this, TimeService.class));
            sendBroadcast(new Intent("com.example.mao.messageencrypt"));
            ToastSelf.ToastSelf("请您为应用开放高级权限",this);
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }

    }

    //判断有没有这个设备
    private boolean isNoOption(){
        PackageManager packageManager = getApplicationContext().getPackageManager();
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        List<ResolveInfo> info = packageManager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
        return info.size()>0;
    }
    //判断有没有打开
    private boolean isNoSwitch(){
        long ts  = System.currentTimeMillis();
        UsageStatsManager usageStatsManager =(UsageStatsManager) getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);
            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,0,ts);
            if(queryUsageStats == null||queryUsageStats.isEmpty())
                return false;
            return true;
    }
}
