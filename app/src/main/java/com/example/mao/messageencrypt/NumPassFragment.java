package com.example.mao.messageencrypt;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mao.util.SP;

import java.util.List;


public class NumPassFragment extends Fragment implements View.OnClickListener{
    private ImageView iv_close;
    private TextView tv_title;
    private EditText et_password;
    private ImageView iv_clear;
    private TextView tv_0;
    private TextView tv_1;
    private TextView tv_2;
    private TextView tv_3;
    private TextView tv_4;
    private TextView tv_5;
    private TextView tv_6;
    private TextView tv_7;
    private TextView tv_8;
    private TextView tv_9;


    private String PackageName;
    private String AppName = "";
    public NumPassFragment() {
        // Required empty public constructor
    }

    public static NumPassFragment newInstance() {
        NumPassFragment fragment = new NumPassFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //通过包名得到app名，后面要存储用
        PackageName = this.getActivity().getIntent().getStringExtra("packageName");
        List<AppInfo> AppInfos = new ApkTool(this.getActivity()).scanLocalInstallAppList(this.getActivity().getPackageManager());
        for(int i = 0;i<AppInfos.size();i++){
            if(AppInfos.get(i).getPackageName().equals(PackageName))
                AppName = AppInfos.get(i).getAppName();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_num_pass, container, false);
        intiView(view);
        return view;
    }

    public void intiView(View v){
        iv_close = (ImageView) v.findViewById(R.id.iv_close);
        tv_title = (TextView) v.findViewById(R.id.tv_title);
        et_password = (EditText) v.findViewById(R.id.et_password);
        iv_clear = (ImageView) v.findViewById(R.id.iv_clear);
        tv_0 = (TextView) v.findViewById(R.id.tv_0);
        tv_1 = (TextView) v.findViewById(R.id.tv_1);
        tv_2 = (TextView) v.findViewById(R.id.tv_2);
        tv_3 = (TextView) v.findViewById(R.id.tv_3);
        tv_4 = (TextView) v.findViewById(R.id.tv_4);
        tv_5 = (TextView) v.findViewById(R.id.tv_5);
        tv_6 = (TextView) v.findViewById(R.id.tv_6);
        tv_7 = (TextView) v.findViewById(R.id.tv_7);
        tv_8 = (TextView) v.findViewById(R.id.tv_8);
        tv_9 = (TextView) v.findViewById(R.id.tv_9);

        //返回桌面
        iv_close.setOnClickListener(NumPassFragment.this);
        iv_clear.setOnClickListener(NumPassFragment.this);
        tv_0.setOnClickListener(NumPassFragment.this);
        tv_1.setOnClickListener(NumPassFragment.this);
        tv_2.setOnClickListener(NumPassFragment.this);
        tv_3.setOnClickListener(NumPassFragment.this);
        tv_4.setOnClickListener(NumPassFragment.this);
        tv_5.setOnClickListener(NumPassFragment.this);
        tv_6.setOnClickListener(NumPassFragment.this);
        tv_7.setOnClickListener(NumPassFragment.this);
        tv_8.setOnClickListener(NumPassFragment.this);
        tv_9.setOnClickListener(NumPassFragment.this);

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0){
                    iv_clear.setVisibility(View.GONE);
                }else{
                    iv_clear.setVisibility(View.VISIBLE);
                }
                if(charSequence.length() == getPassword().length()){
                    nextListener();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_0:
                et_password.setText(et_password.getText()+"0");
                break;
            case R.id.tv_1:
                et_password.setText(et_password.getText()+"1");
                break;
            case R.id.tv_2:
                et_password.setText(et_password.getText()+"2");
                break;
            case R.id.tv_3:
                et_password.setText(et_password.getText()+"3");
                break;
            case R.id.tv_4:
                et_password.setText(et_password.getText()+"4");
                break;
            case R.id.tv_5:
                et_password.setText(et_password.getText()+"5");
                break;
            case R.id.tv_6:
                et_password.setText(et_password.getText()+"6");
                break;
            case R.id.tv_7:
                et_password.setText(et_password.getText()+"7");
                break;
            case R.id.tv_8:
                et_password.setText(et_password.getText()+"8");
                break;
            case R.id.tv_9:
                et_password.setText(et_password.getText()+"9");
                break;
            case R.id.iv_clear:
                et_password.setText("");
                break;
            case R.id.iv_close:
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                break;
        }
    }

    public void nextListener(){
        if(et_password.getText().toString().equals(getPassword())){
            //密码正确
            SP.save(getActivity(),AppName+"lock", false);
            Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(PackageName);
            getActivity().startActivity(intent);
            getActivity().finish();
        }else{
            //密码错误
            SP.addNum(this.getActivity());
            tv_title.setText("密码错误，请重试");
            et_password.setText("");
            errorAnim();
        }
    }
    //获取密码
    public String getPassword(){
        return  SP.get(getActivity(),"encrypt_num","");
    }
    //密码错误动画
    public void errorAnim(){

        TranslateAnimation translate1 = new TranslateAnimation(-20,0,0,0);
        translate1.setDuration(100);

        tv_title.setAnimation(translate1);
        translate1.start();
        translate1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                TranslateAnimation translate2 = new TranslateAnimation(20,0,0,0);
                translate2.setDuration(100);
                tv_title.setAnimation(translate2);
                translate2.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
