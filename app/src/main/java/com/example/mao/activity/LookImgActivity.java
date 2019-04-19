package com.example.mao.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mao.messageencrypt.PointView;
import com.example.mao.messageencrypt.R;
import com.example.mao.util.SP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.FileNotFoundException;


public class LookImgActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tv_cancel,tv_title,tv_delet;
    private FrameLayout frameLayout;
    private ImageView iv_img;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_img);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        tv_title.setText(name);


        try {
            JSONObject object = new JSONObject(SP.get(this,name,""));
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(object.optString("uri"))));
            iv_img.setImageBitmap(bitmap);

            JSONArray array = object.optJSONArray("points");
            for(int i=0; i<array.length() ; i++){
                JSONObject points = array.optJSONObject(i);
                addPointView((float) points.optDouble("x"),(float) points.optDouble("y"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    //
    public void addPointView(float xl,float yl){
        float x = xl;
        float y = yl;
        float round = 30;
        PointView pointView=new PointView(LookImgActivity.this,x,y,round);
        frameLayout.addView(pointView);
    }
    public void initView(){
//        parentLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_delet = (TextView) findViewById(R.id.tv_delet);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        iv_img = (ImageView) findViewById(R.id.iv_img);
        tv_cancel.setOnClickListener(this);
        tv_delet.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancel:
                onBackPressed();
                break;
            case R.id.tv_delet:
                //删除
                Intent intent = new Intent(LookImgActivity.this,SetPasswordActivity.class);
                intent.putExtra("what",4);
                startActivityForResult(intent,4);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            SP.remove(this,name);
            setResult(RESULT_OK);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
