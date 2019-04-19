package com.example.mao.messageencrypt;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mao.util.SP;
import com.example.mao.util.ToastSelf;

public class SetPasswordActivity extends AppCompatActivity implements View.OnClickListener{
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
    private TextView tv_next;
    private String password1;
    private String password2;
    private int flag = 1;
    private int what;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        what = getIntent().getIntExtra("what",0);
        initView();
        setListener();
    }

    public void  initView(){
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_password = (EditText) findViewById(R.id.et_password);
        iv_clear = (ImageView) findViewById(R.id.iv_clear);
        tv_0 = (TextView) findViewById(R.id.tv_0);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_5 = (TextView) findViewById(R.id.tv_5);
        tv_6 = (TextView) findViewById(R.id.tv_6);
        tv_7 = (TextView) findViewById(R.id.tv_7);
        tv_8 = (TextView) findViewById(R.id.tv_8);
        tv_9 = (TextView) findViewById(R.id.tv_9);
        tv_next = (TextView) findViewById(R.id.tv_delet);
        if(what == 0){
            if(TextUtils.isEmpty(SP.get(this,"encrypt_num",""))){
                what = 0;
            }else{
                what = 1;
            }
        }
        switch (what){
            case 0:
            //初次设置密码

                break;
            case 1:
            //打开此app
                tv_title.setText("请输入密码");
                tv_next.setText("完成");
                break;
            case 2:
            //关闭密码
                tv_title.setText("请输入密码");
                tv_next.setText("确认关闭");
                break;
            case 3:
            //修改密码
                tv_title.setText("请输入旧密码");
                break;
            case 4:
            //删除图片密码
                tv_title.setText("请输入密码");
                tv_next.setText("完成");
                break;
        }

    }
    public void setListener(){
        iv_clear.setOnClickListener(this);
        tv_0.setOnClickListener(this);
        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        tv_3.setOnClickListener(this);
        tv_4.setOnClickListener(this);
        tv_5.setOnClickListener(this);
        tv_6.setOnClickListener(this);
        tv_7.setOnClickListener(this);
        tv_8.setOnClickListener(this);
        tv_9.setOnClickListener(this);
        tv_next.setOnClickListener(this);
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
                if(charSequence.length() >= 6){
                    tv_next.setEnabled(true);
                    tv_next.setClickable(true);
                    tv_next.setFocusable(true);
                    tv_next.setTextColor(Color.BLUE);
                }else{
                    tv_next.setEnabled(false);
                    tv_next.setClickable(false);
                    tv_next.setFocusable(false);
                    tv_next.setTextColor(Color.GRAY);
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
            case R.id.tv_delet:
                nextListener();
                break;
        }
    }

    public void nextListener(){
        switch (what){


            case 0:
            //初次设置密码
                if(flag == 1){
                    //第一次
                    password1 = et_password.getText().toString();
                    tv_title.setText("请再次输入密码");
                    tv_next.setText("完成");
                    et_password.setText("");
                    flag++;
                }else if(flag == 2){
                    //第二次
                    password2 = et_password.getText().toString();
                    if(password2.equals(password1)){
                        savePassword();
                        ToastSelf.ToastSelf("密码设置成功",this);
                        Intent intent = new Intent(SetPasswordActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        tv_title.setText("密码不匹配，请重试");
                        errorAnim();
                    }
                }
                break;

            case 1:
            //解锁
                if(et_password.getText().toString().equals(getPassword())){
                    //密码正确
                    Intent intent = new Intent(SetPasswordActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    //密码错误
                    tv_title.setText("密码错误，请重试");
                    errorAnim();
                    SP.addNum(this);
                }
                break;

            case 2:
            //关闭密码
                if(et_password.getText().toString().equals(getPassword())){
                    //密码正确
                    ToastSelf.ToastSelf("应用加密已关闭",this);
                    setResult(RESULT_OK);
                    finish();
                }else{
                    //密码错误
                    SP.addNum(this);
                    tv_title.setText("密码错误，请重试");
                    errorAnim();
                }
                break;


            case 3:
            //修改密码
                if(flag == 1){
                    //验证旧密码
                    if(et_password.getText().toString().equals(getPassword())){
                        //密码正确
                        flag++;
                        tv_title.setText("请输入4位新密码");
                        et_password.setText("");
                    }else{
                        //密码错误
                        SP.addNum(this);
                        tv_title.setText("密码错误，请重试");
                        errorAnim();
                    }
                }else if(flag == 2){
                    //第一遍输入新密码
                    password1 = et_password.getText().toString();
                    tv_title.setText("请再次输入新密码");
                    tv_next.setText("完成");
                    et_password.setText("");
                    flag++;
                }else if(flag == 3){
                    //第二遍输入新密码
                    password2 = et_password.getText().toString();
                    if(password2.equals(password1)){
                        savePassword();
                        ToastSelf.ToastSelf("密码修改成功",this);
                        setResult(RESULT_OK);
                        finish();
                    }else{
                        tv_title.setText("密码不匹配，请重试");
                        errorAnim();
                    }
                }

                break;
            case 4:
            //删除图片密码
                if(et_password.getText().toString().equals(getPassword())){
                    //密码正确
                    setResult(RESULT_OK);
                    finish();
                }else{
                    //密码错误
                    tv_title.setText("密码错误，请重试");
                    errorAnim();
                }
                break;
        }
    }
    //判断错误次数，是否锁定
    public void lock(){
        switch (SP.getNum(this)){
            case 5:
                tv_title.setText("");
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
        }

    }
    //保存密码
    public void savePassword(){
        SP.save(this,"encrypt_num",password2);
    }
    //获取密码
    public String getPassword(){
        return  SP.get(this,"encrypt_num","");
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
