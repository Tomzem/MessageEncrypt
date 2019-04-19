package com.example.mao.messageencrypt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mao.util.SP;
import com.example.mao.util.ToastSelf;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SetPointActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_cancel,tv_title,tv_next,tv_rest;
    private FrameLayout frameLayout;
    private ImageView iv_img;
    private List<View> viewList1 = new ArrayList<>();
    private List<Map<String,Float>> pointList1 = new LinkedList<>();
    private List<View> viewList2 = new ArrayList<>();
    private List<Map<String,Float>> pointList2 = new LinkedList<>();
    private int num = 0 ;
    private int flag = 0;
    private Handler handler = new Handler();
    private Uri uri;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_point);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        Intent intent = getIntent();
        uri = Uri.parse(intent.getStringExtra("URI"));
        name = intent.getStringExtra("name");
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            iv_img.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(flag == 0){
                    if(pointList1.size() <6){
                        float x = motionEvent.getX();
                        float y = motionEvent.getY();
                        float round = 30;
                        PointView pointView=new PointView(SetPointActivity.this,x,y,round);
                        frameLayout.addView(pointView);
                        viewList1.add(pointView);


                        Map<String,Float> map = new HashMap<>();
                        map.put("x",x);
                        map.put("y",y);
                        pointList1.add(num,map);
                        num++;
                    }else{
                        ToastSelf.ToastSelf("您最多可以设置6个密码点",SetPointActivity.this);
                    }
                }else if(flag == 1){
                    if(pointList2.size() != pointList1.size()){
                        float x = motionEvent.getX();
                        float y = motionEvent.getY();
                        float round = 30;
                        PointView pointView=new PointView(SetPointActivity.this,x,y,round);
                        viewList2.add(pointView);

                        Map<String,Float> map = new HashMap<>();
                        map.put("x",x);
                        map.put("y",y);
                        pointList2.add(num,map);
                        if(pointList1.size() == pointList2.size()){
                            //开始验证
                            //如果正确，设置成功
                            //如果错误，显示密码点，两秒后去掉，重新设置
                            if(isRight(pointList2,pointList1)){
                                //正确
                                JSONObject jsonObject = new JSONObject();
                                JSONArray jsonArray = new JSONArray(pointList1);
                                try {
                                    jsonObject.put("uri",uri);
                                    jsonObject.put("points",jsonArray);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                SP.save(SetPointActivity.this,name,jsonObject.toString());

                                setResult(RESULT_OK);
                                finish();
                            }else{
                                //错误
                                tv_title.setText("密码点不匹配，请重试");
                                errorAnim();
                                for (View v: viewList2) {
                                    frameLayout.addView(v);
                                }
                                new Thread(){
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                for (View v: viewList2) {
                                                    frameLayout.removeView(v);
                                                }
                                                pointList2.clear();
                                                viewList2.clear();
                                                num = 0;
                                            }
                                        });
                                    }
                                }.start();

                            }
                        }
                        num++;
                    }

                }
                return false;
            }
        });
    }

    public void initView(){
//        parentLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_rest = (TextView) findViewById(R.id.tv_rest);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_next = (TextView) findViewById(R.id.tv_delet);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        iv_img = (ImageView) findViewById(R.id.iv_img);

        tv_cancel.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        tv_rest.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancel:
                onBackPressed();
                break;
            case R.id.tv_rest:
                if(flag == 0){
                    pointList1.clear();
                    for (View v: viewList1) {
                        frameLayout.removeView(v);
                    }
                    viewList1.clear();
                    num = 0;
                }
                break;
            case R.id.tv_delet:
                if(flag == 0){
                    if(pointList1.size() >= 4){
                        flag++;
                        tv_title.setText("请验证密码点");
                        for (View v: viewList1) {
                            frameLayout.removeView(v);
                        }
                        tv_next.setVisibility(View.GONE);
                        tv_rest.setVisibility(View.GONE);
                        num = 0;
                    }else{
                        tv_title.setText("请至少设置4个密码点");
                        errorAnim();
                    }
                }else if(flag == 1){
                    for (Map<String,Float> map: pointList2) {
                        Log.e("s",map.get("x")+","+map.get("y"));
                    }

                }

                break;
        }
    }

    /**
     *验证
     */
    public boolean isRight(List<Map<String,Float>> points,List<Map<String,Float>> centers){

        for(int i = 0; i<centers.size(); i++){
            if(!isInPoint(points.get(i), centers.get(i))){
                return false;
            }
        }
        return true;
    }

    //计算某个点是否在密码点内
    public boolean isInPoint(Map<String,Float> point,Map<String,Float> center){
        Float pointX = point.get("x");
        Float pointY = point.get("y");
        Float centerX = center.get("x");
        Float centerY = center.get("y");

        Float distanceX = pointX - centerX;
        Float distanceY = pointY - centerY;
        double distance = Math.sqrt( (double) ((distanceX*distanceX)+(distanceY*distanceY)));
        Log.e("x","坐标点："+pointX+","+pointY+"\n中心点："+centerX+","+centerY+"\n距离："+distance);
        if(distance <= 40){
            return true;
        }else{
            return false;
        }
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
