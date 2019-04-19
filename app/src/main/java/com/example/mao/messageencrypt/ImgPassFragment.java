package com.example.mao.messageencrypt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mao.util.SP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ImgPassFragment extends Fragment {

    private Handler handler = new Handler();
    private static final String WHAT = "positon";
    private int mWhat;
    private ImageView imageView;
    private FrameLayout frameLayout;
    private TextView tv_cancel;
    private TextView tv_num;
    private TextView tv_title;
    private String name;

    private List<View> viewsList = new ArrayList<>();
    private List<Map<String,Float>> pointsList = new LinkedList<>();
    private List<Map<String,Float>> centersList = new LinkedList<>();
    private JSONObject jsonObject = null;
    private int time = 0;

    private String PackageName;
    private String AppName = "";

    public ImgPassFragment() {
        // Required empty public constructor
    }

    public static ImgPassFragment newInstance(int param1) {
        ImgPassFragment fragment = new ImgPassFragment();
        Bundle args = new Bundle();
        args.putInt(WHAT, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWhat = getArguments().getInt(WHAT);
        }


        findName();
        JSONArray array = jsonObject.optJSONArray("points");
        for(int i=0; i<array.length() ; i++){
            JSONObject points = array.optJSONObject(i);
            Map<String,Float> map = new HashMap<>();
            map.put("x",(float) points.optDouble("x"));
            map.put("y",(float) points.optDouble("y"));
            centersList.add(map);
        }

        //通过包名得到app名，后面要存储用
        PackageName = this.getActivity().getIntent().getStringExtra("packageName");
        List<AppInfo> AppInfos = new ApkTool(this.getActivity()).scanLocalInstallAppList(this.getActivity().getPackageManager());
        for(int i = 0;i<AppInfos.size();i++){
            if(AppInfos.get(i).getPackageName().equals(PackageName))
                AppName = AppInfos.get(i).getAppName();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_img_pass, container, false);
        imageView = (ImageView) view.findViewById(R.id.iv_img);
        frameLayout = (FrameLayout) view.findViewById(R.id.frameLayout);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_num = (TextView) view.findViewById(R.id.tv_num);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(pointsList.size() < centersList.size()){
                    Map<String,Float> map = new HashMap<>();
                    map.put("x",motionEvent.getX());
                    map.put("y",motionEvent.getY());
                    pointsList.add(map);

                    PointView pointView=new PointView(getActivity(),motionEvent.getX(),motionEvent.getY(),40);
                    viewsList.add(pointView);
                }
                if(pointsList.size() == centersList.size()){
                    //开始验证
                    if(isRight(pointsList,centersList)){
                        //正确 ， 解锁
                        //将该app状态改成已解锁并跳转进去
                        SP.save(ImgPassFragment.this.getActivity(),AppName+"lock", false);
                        Intent intent = ImgPassFragment.this.getActivity().getPackageManager().getLaunchIntentForPackage(PackageName);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }else{
                        //错误 ， 显示密码点
                        errorShow();
                    }
                }

                return false;
            }
        });
        //返回桌面
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
            }
        });

        tv_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnlockActivity activity = (UnlockActivity) getActivity();
                activity.vp_unlock.setCurrentItem(1);
            }
        });



        Glide.with(this)
                .load(Uri.parse(jsonObject.optString("uri")))
                .into(imageView);

        return view;
    }

    /**
     * 错误时显示密码点
     */
    public void errorShow(){
        SP.addNum(this.getActivity());
        frameLayout.setEnabled(false);
        tv_title.setText("密码点不匹配，请重试");
        errorAnim();
        for (View v: viewsList) {
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
                        for (View v: viewsList) {
                            frameLayout.removeView(v);
                        }
                        pointsList.clear();
                        viewsList.clear();
                        frameLayout.setEnabled(true);
                    }
                });
            }
        }.start();
    }

    /**
     * 判断Fragment应该显示哪张图片
     */
    public void findName(){
        switch (mWhat){
            case 1:
                name = "图片密码1";
                break;
            case 2:
                name = "图片密码2";
                break;
            case 3:
                name = "图片密码3";
                break;
        }

        try {
            jsonObject = new JSONObject(SP.get(this.getActivity(),name,""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     *验证
     */
    public boolean isRight(List<Map<String,Float>> points, List<Map<String,Float>> centers){

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
