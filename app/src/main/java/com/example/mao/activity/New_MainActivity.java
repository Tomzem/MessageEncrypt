package com.example.mao.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.mao.app.New_Observer;
import com.example.mao.adapter.New_AppAdapter;
import com.example.mao.bean.APPInfo;
import com.example.mao.messageencrypt.ApkTool;
import com.example.mao.messageencrypt.R;
import com.example.mao.util.ToastSelf;
import com.example.mao.weiget.New_RelativeLayout;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class New_MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView lv_app;
    private New_AppAdapter mAppAdapter;
    private Context mContext;
    private New_RelativeLayout mLockSwitch;
    private New_RelativeLayout mSurplusTime;

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
        mLockSwitch = (New_RelativeLayout) lv_header.findViewById(R.id.item_switch);
        mSurplusTime = (New_RelativeLayout) lv_header.findViewById(R.id.item_surplus_time);
        mLockSwitch.setOnClickListener(this);
        mSurplusTime.setOnClickListener(this);

        lv_app.addHeaderView(lv_header);
        mAppAdapter = new New_AppAdapter();
        lv_app.setAdapter(mAppAdapter);
    }

    private void initAppList(){
        final ProgressDialog dialog  = new ProgressDialog(mContext);
        dialog.setMessage("加载应用列表...");
        dialog.setCancelable(false);
        dialog.show();
        Observable.create(new ObservableOnSubscribe<List<APPInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<APPInfo>> emitter) throws Exception {
                ApkTool apk = new ApkTool(mContext);
                List<APPInfo> appInfo = apk.scanLocalInstallAppList(mContext.getPackageManager());
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
        if (view == mLockSwitch) {
            ToastSelf.ToastSelf("mLockSwitch", mContext);
        } else if (view == mSurplusTime) {
            ToastSelf.ToastSelf("mSurplusTime", mContext);
        }
    }

    @Override
    public void onBackPressed() {
    }
}
