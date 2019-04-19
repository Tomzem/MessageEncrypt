package com.example.mao.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.mao.adapter.New_AppAdapter;
import com.example.mao.bean.APPInfo;
import com.example.mao.messageencrypt.ApkTool;
import com.example.mao.messageencrypt.R;

import java.util.List;

public class New_MainActivity extends AppCompatActivity {
    private ListView lv_app;
    private New_AppAdapter mAppAdapter;
    private Context mContext;
    public Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__main);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_label);
        setSupportActionBar(toolbar);

        initView();
        initAppList();
    }

    private void initView() {
        lv_app = (ListView) findViewById(R.id.lv_app);
        LinearLayout lv_header = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.new_head_list_app,null);
        lv_app.addHeaderView(lv_header);
        mAppAdapter = new New_AppAdapter();
        lv_app.setAdapter(mAppAdapter);
    }

    private void initAppList(){
        final ProgressDialog dialog  = new ProgressDialog(mContext);
        dialog.setMessage("加载应用列表...");
        dialog.setCancelable(false);
        dialog.show();
        new Thread(){
            @Override
            public void run() {
                super.run();
                //扫描得到APP列表
                ApkTool apk = new ApkTool(mContext);
                final List<APPInfo> appInfos = apk.scanLocalInstallAppList(mContext.getPackageManager());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAppAdapter.setData(mContext,appInfos);
                        dialog.dismiss();
                    }
                });
            }
        }.start();
    }
}
