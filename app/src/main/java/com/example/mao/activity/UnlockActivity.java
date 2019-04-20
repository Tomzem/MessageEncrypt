package com.example.mao.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;

import com.example.mao.messageencrypt.PagerAdapter;
import com.example.mao.messageencrypt.R;
import com.example.mao.util.SP;
import com.example.mao.weiget.ViewPagerNoScroll;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UnlockActivity extends AppCompatActivity {

    public ViewPagerNoScroll vp_unlock;
    private int count = 1;
    private List<Integer> list = new ArrayList<>();
    private int what;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        vp_unlock = (ViewPagerNoScroll) findViewById(R.id.vp_unlock);


        if(!TextUtils.isEmpty(SP.get(this,"图片密码1",""))){
            list.add(1);
        }

        if(!TextUtils.isEmpty(SP.get(this,"图片密码2",""))){
            list.add(2);
        }

        if(!TextUtils.isEmpty(SP.get(this,"图片密码3",""))){
            list.add(3);
        }
        if(list.size() > 0){
//            int max=list.size()-1;
//            int min=0;
//            Random random = new Random();
//            int s = random.nextInt(max)%(max-min+1) + min;
            Random random = new Random();
            int s=random.nextInt(list.size());
            what = list.get(s);
        }else{
            what = -1;
        }




        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),this,what);
        vp_unlock.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {

    }
}
