package com.example.mao.activity;

import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mao.bean.APPInfo;
import com.example.mao.messageencrypt.ApkTool;
import com.example.mao.messageencrypt.R;
import com.example.mao.service.LockService;
import com.example.mao.util.MyDeviceAdmin;
import com.example.mao.util.SP;
import com.example.mao.weiget.TestPopwindow2;
import com.example.mao.util.ToastSelf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,View.OnClickListener {
    private ListView lv_app;
    private LinearLayout lv_header;
    private AppAdapter mAppAdapter;
    public Handler mHandler = new Handler();
    private Switch swt_menu_open;
    private RadioButton app_radio;
    private View menu_pic1,menu_pic2,menu_pic3;
    private TextView tv_menu_pic1,tv_menu_pic2,tv_menu_pic3;
    private LinearLayout add_pic;
    private View itemSwitch;
    private View itemChange;
    private View itemDefence;
    private String imgUUID;
    private Uri tempUri;
    private ImageView defence_tip;
    private Switch swt_defence_open;
    private TestPopwindow2 mTestPopwindow2 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,UnlockActivity.class);
                startActivity(intent);
            }
        });
        setSupportActionBar(toolbar);


        initView();

        lv_app.setOverScrollMode(View.OVER_SCROLL_NEVER);
        lv_app.addHeaderView(lv_header);
        mAppAdapter = new AppAdapter();
        lv_app.setAdapter(mAppAdapter);
        initAppList();


        lv_app.setOnItemClickListener(this);
        itemSwitch.setOnClickListener(this);
        itemChange.setOnClickListener(this);
        itemDefence.setOnClickListener(this);
        add_pic.setOnClickListener(this);
        menu_pic1.setOnClickListener(this);
        menu_pic2.setOnClickListener(this);
        menu_pic3.setOnClickListener(this);
        defence_tip.setOnClickListener(this);
        menuHide();
        mTestPopwindow2 = new TestPopwindow2(this);
    }


    private void initView() {

        lv_header = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.listview_header,null);
        lv_app = (ListView) findViewById(R.id.lv_app);
        swt_menu_open = (Switch) lv_header.findViewById(R.id.swt_menu_open);
        swt_menu_open.setClickable(false);
        defence_tip = (ImageView) lv_header.findViewById(R.id.defence_tip);
        itemSwitch = lv_header.findViewById(R.id.item_switch);
        itemChange = lv_header.findViewById(R.id.item_change);
        itemDefence = lv_header.findViewById(R.id.item_defence);
        swt_defence_open = (Switch) lv_header.findViewById(R.id.swt_defence_open);
        swt_defence_open.setClickable(false);
        tv_menu_pic1 = (TextView) lv_header.findViewById(R.id.tv_menu_pic1);
        tv_menu_pic2 = (TextView) lv_header.findViewById(R.id.tv_menu_pic2);
        tv_menu_pic3 = (TextView) lv_header.findViewById(R.id.tv_menu_pic3);
        add_pic = (LinearLayout) lv_header.findViewById(R.id.add_pic);
        menu_pic1 = lv_header.findViewById(R.id.menu_pic1);
        menu_pic2 =  lv_header.findViewById(R.id.menu_pic2);
        menu_pic3 = lv_header.findViewById(R.id.menu_pic3);

        if(SP.get(this,"open_encrypt", false)){
            //密码已经开启
            swt_menu_open.setChecked(true);
            startService(new Intent(this,LockService.class));
        }else{
            setImgDisEnable();
            swt_menu_open.setChecked(false);
        }
        if(SP.get(this,"open_defence", false)){
            //防御已经开启
            swt_defence_open.setChecked(true);
        }else{
            //防御未开启
            swt_defence_open.setChecked(false);
        }
    }


    //将图案密码部分隐藏
    private void menuHide() {
        menu_pic1.setVisibility(View.GONE);
        menu_pic2.setVisibility(View.GONE);
        menu_pic3.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(SP.get(this,"图片密码1",""))){
            menu_pic1.setVisibility(View.VISIBLE);
        }

        if(!TextUtils.isEmpty(SP.get(this,"图片密码2",""))){
            menu_pic2.setVisibility(View.VISIBLE);
        }

        if(!TextUtils.isEmpty(SP.get(this,"图片密码3",""))){
            menu_pic3.setVisibility(View.VISIBLE);
        }
        //添加按钮
        if(menu_pic1.getVisibility() == View.VISIBLE && menu_pic2.getVisibility() == View.VISIBLE && menu_pic3.getVisibility() == View.VISIBLE){
            add_pic.setVisibility(View.GONE);
        }else{
            add_pic.setVisibility(View.VISIBLE);
        }
    }

    //APP列表监听
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView tv = (TextView) view.findViewById(R.id.tv_app_name);
        app_radio = (RadioButton) view.findViewById(R.id.app_radio);
        if(app_radio.isChecked() == false){
            app_radio.setChecked(true);
            SP.save(this,tv.getText().toString(), true);
            SP.save(this,tv.getText().toString()+"lock", true);
        }
        else{
            app_radio.setChecked(false);
            SP.save(this,tv.getText().toString(), false);
            SP.save(this,tv.getText().toString()+"lock", false);
        }
    }


    //图案密码监听
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.item_switch:
                //密码开关
                if (swt_menu_open.isChecked()) {
                    //关闭应用加密
                    Intent intentClose = new Intent(this,SetPasswordActivity.class);
                    intentClose.putExtra("what",2);
                    startActivityForResult(intentClose,2);
                } else {
                    //开启应用加密
                    swt_menu_open.setChecked(true);
                    SP.save(this,"open_encrypt", true);
                    setImgEnable();
                    openSet();
                }
                break;
            case R.id.item_defence:
                openDevice();
                break;
            case R.id.item_change:
                //修改密码
                Intent intentChange = new Intent(this,SetPasswordActivity.class);
                intentChange.putExtra("what",3);
                startActivityForResult(intentChange,3);
                break;
            case R.id.add_pic:
                //添加图片密码
                Intent intentChoose = new Intent(Intent.ACTION_PICK);
                intentChoose.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intentChoose,3);
                break;
            case R.id.menu_pic1:
                //图片密码1
                Intent intent1 = new Intent(MainActivity.this,LookImgActivity.class);
                intent1.putExtra("name","图片密码1");
                startActivityForResult(intent1,6);
                break;
            case R.id.menu_pic2:
                //图片密码2
                Intent intent2 = new Intent(MainActivity.this,LookImgActivity.class);
                intent2.putExtra("name","图片密码2");
                startActivityForResult(intent2,6);
                break;
            case R.id.menu_pic3:

                //图片密码3
                Intent intent3 = new Intent(MainActivity.this,LookImgActivity.class);
                intent3.putExtra("name","图片密码3");
                startActivityForResult(intent3,6);
                break;
            case R.id.defence_tip:
                OnPopwindowTest2();
                break;
        }
    }

    private void openDevice() {
        // 申请权限
        ComponentName componentName = new ComponentName(this, MyDeviceAdmin.class);
        // 设备安全管理服务 2.2之前的版本是没有对外暴露的 只能通过反射技术获取
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        // 判断该组件是否有系统管理员的权限
        boolean isAdminActive = devicePolicyManager.isAdminActive(componentName);
        if (!isAdminActive) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "选择激活可防止恶意卸载");
            startActivityForResult(intent, 20);

        } else {
            devicePolicyManager.removeActiveAdmin(componentName);
            swt_defence_open.setChecked(false);
        }
    }

    private void OnPopwindowTest2() {
        if (mTestPopwindow2 == null)
            return;

        // location获得控件的位置
        int[] location = new int[2];
        View v = findViewById(R.id.defence_tip);
        if (v != null)
            v.getLocationOnScreen(location);   // 控件在屏幕的位置

        // mTestPopwindow2弹出在某控件(button)的下面
        mTestPopwindow2.showAtLocation(v, Gravity.TOP | Gravity.LEFT,
                location[0] - v.getWidth()+100, location[1] + v.getHeight());
    }



    private void initAppList(){
        final ProgressDialog dialog  = new ProgressDialog(MainActivity.this);
        dialog.setMessage("加载应用列表...");
        dialog.setCancelable(false);
        dialog.show();
        new Thread(){
            @Override
            public void run() {
                super.run();
                //扫描得到APP列表
                ApkTool apk = new ApkTool(MainActivity.this);
                final List<APPInfo> appInfos = apk.scanLocalInstallAppList(MainActivity.this.getPackageManager());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAppAdapter.setData(MainActivity.this,appInfos);
                        dialog.dismiss();
                    }
                });
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 2:
            //关闭密码结果
                if(resultCode == RESULT_OK){
                    //关闭密码成功
                    setImgDisEnable();
                    swt_menu_open.setChecked(false);
                    SP.save(this,"open_encrypt", false);
                    stopService(new Intent(this, LockService.class));
                }
                break;
            case 3:
            //选择图片返回
                //打开裁剪
                if(resultCode == RESULT_OK){
                    imgUUID = UUID.randomUUID().toString();
                    openCrop(data, imgUUID);
                }
                break;
            case 4:
            //裁剪图片返回
                if(resultCode == RESULT_OK){
                    //跳转到设置密码点Activity
                    String name = "";
                    if(menu_pic1.getVisibility() == View.GONE){
                        name = "图片密码1";
                    }else if(menu_pic2.getVisibility() == View.GONE){
                        name = "图片密码2";
                    }else if(menu_pic3.getVisibility() == View.GONE){
                        name = "图片密码3";
                    }
                    Intent setPointIntent = new Intent(MainActivity.this,SetPointActivity.class);
                    setPointIntent.putExtra("URI",tempUri.toString());
                    setPointIntent.putExtra("name",name);
                    startActivityForResult(setPointIntent,5);
                }
                break;
            case 5:
            //设置图片密码返回
            case 6:
            //删除密码返回
                if(resultCode == RESULT_OK) menuHide();
                break;
            case 20:
                if(SP.get(this,"open_defence", false)){
                    //防御已经开启
                    swt_defence_open.setChecked(true);
                }else{
                    //防御未开启
                    swt_defence_open.setChecked(false);swt_defence_open.setChecked(false);
                }
                break;

        }

    }

    /**
     * 打开截图
     * @param data
     */
    public void openCrop(Intent data, String name) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width  = metrics.widthPixels;

        //裁剪后图片路径
        File tempFile = new File(getExternalFilesDir(null)+"/"+name+".jpg");
        tempUri = Uri.fromFile(tempFile);

        Uri uri = data.getData();
        Intent intentCrop = new Intent("com.android.camera.action.CROP");
        intentCrop.setDataAndType(uri,"image/*");
        intentCrop.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intentCrop.putExtra("aspectX", width);
        intentCrop.putExtra("aspectY", height);
        // outputX outputY 是裁剪图片宽高
//        intentCrop.putExtra("outputX", width);
//        intentCrop.putExtra("outputY", height);
        //裁剪时是否保留图片的比例，这里的比例是1:1
        intentCrop.putExtra("scale", true);
        //是否返回为缩略图
        intentCrop.putExtra("return-data", false);
        //设置裁剪后图片保存路径的URI
        intentCrop.putExtra(MediaStore.EXTRA_OUTPUT,tempUri);

        startActivityForResult(intentCrop,4);
    }


    //图片密码不可点击
    public void setImgDisEnable(){
        add_pic.setBackgroundColor(getResources().getColor(R.color.colorEnadble));
        TextView textView = (TextView)add_pic.findViewById(R.id.tv_menu_pic_add);
        textView.setTextColor(Color.GRAY);
        add_pic.setEnabled(false);
        add_pic.setClickable(false);
        add_pic.setFocusable(false);

        menu_pic1.setBackgroundColor(getResources().getColor(R.color.colorEnadble));
        tv_menu_pic1.setTextColor(Color.GRAY);
        menu_pic1.setEnabled(false);
        menu_pic1.setClickable(false);
        menu_pic1.setFocusable(false);

        menu_pic2.setBackgroundColor(getResources().getColor(R.color.colorEnadble));
        tv_menu_pic2.setTextColor(Color.GRAY);
        menu_pic2.setEnabled(false);
        menu_pic2.setClickable(false);
        menu_pic2.setFocusable(false);

        menu_pic3.setBackgroundColor(getResources().getColor(R.color.colorEnadble));
        tv_menu_pic3.setTextColor(Color.GRAY);
        menu_pic3.setEnabled(false);
        menu_pic3.setClickable(false);
        menu_pic3.setFocusable(false);
    }
    //图片密码可点击
    public void setImgEnable(){

        add_pic.setBackground(itemSwitch.getBackground());
        TextView textView = (TextView)add_pic.findViewById(R.id.tv_menu_pic_add);
        textView.setTextColor(Color.BLACK);
        add_pic.setEnabled(true);
        add_pic.setClickable(true);
        add_pic.setFocusable(true);

        menu_pic1.setBackground(itemSwitch.getBackground());
        tv_menu_pic1.setTextColor(Color.BLACK);
        menu_pic1.setEnabled(true);
        menu_pic1.setClickable(true);
        menu_pic1.setFocusable(true);

        menu_pic2.setBackground(itemSwitch.getBackground());
        tv_menu_pic2.setTextColor(Color.BLACK);
        menu_pic2.setEnabled(true);
        menu_pic2.setClickable(true);
        menu_pic2.setFocusable(true);

        menu_pic3.setBackground(itemSwitch.getBackground());
        tv_menu_pic3.setTextColor(Color.BLACK);
        menu_pic3.setEnabled(true);
        menu_pic3.setClickable(true);
        menu_pic3.setFocusable(true);
    }

    private void openSet(){
        List<APPInfo> AppInfos = new ApkTool(this).scanLocalInstallAppList(this.getPackageManager());
        for(int i = 0;i<AppInfos.size();i++){
            SP.save(this,AppInfos.get(i).getAppName()+"lock", true);
        }
        if(isNoOption()){
            if(isNoSwitch()){
                SP.save(this,"open_encrypt", true);
                startService(new Intent(this, LockService.class));
                sendBroadcast(new Intent("com.example.mao.messageencrypt"));
            }else{
                SP.save(this,"open_encrypt", true);
                startService(new Intent(this, LockService.class));
                sendBroadcast(new Intent("com.example.mao.messageencrypt"));
                ToastSelf.ToastSelf("请您为应用开放高级权限",this);
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
            }
        }else{
            SP.save(this,"open_encrypt", true);
            startService(new Intent(this, LockService.class));
            sendBroadcast(new Intent("com.example.mao.messageencrypt"));
            ToastSelf.ToastSelf("您的手机系统未开放高级权限，无法有效的为您加密",this);
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,0,ts);
            if(queryUsageStats == null||queryUsageStats.isEmpty())
                return false;
            return true;
        }else
            return false;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        sendBroadcast(new Intent("com.example.mao.messageencrypt"));
    }
    //重写返回
    public void onBackPressed(){
        finish();
    }



    class AppAdapter extends BaseAdapter {

        List<APPInfo> AppInfos = new ArrayList<APPInfo>();
        Context context;
        public void setData(Context context,List<APPInfo> AppInfos) {
            this.AppInfos = AppInfos;
            this.context = context;
            notifyDataSetChanged();
        }

        public List<APPInfo> getData() {
            return AppInfos;
        }

        @Override
        public int getCount() {
            if (AppInfos != null && AppInfos.size() > 0) {
                return AppInfos.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (AppInfos != null && AppInfos.size() > 0) {
                return AppInfos.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder;
            APPInfo myAppInfo = AppInfos.get(position);
            if (convertView == null) {
                mViewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.listview_app, null);
                mViewHolder.iv_app_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
                mViewHolder.tx_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
                mViewHolder.ra_app_radio = (RadioButton) convertView.findViewById(R.id.app_radio);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            mViewHolder.iv_app_icon.setImageDrawable(myAppInfo.getImage());
            mViewHolder.tx_app_name.setText(myAppInfo.getAppName());
            mViewHolder.ra_app_radio.setChecked(SP.get(context,mViewHolder.tx_app_name.getText().toString(),false));
            return convertView;
        }

        class ViewHolder {
            ImageView iv_app_icon;
            TextView tx_app_name;
            RadioButton ra_app_radio;
        }
    }
}
