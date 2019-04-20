package com.example.mao.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mao.messageencrypt.R;


/**
 * Created by Mao on 2017/3/22.
 */

public class ToastSelf {

    private static Toast mToast;
    private static TextView mTvToast;
    public static void ToastSelf(String content, Context ctx) {
        if (mToast == null) {
            mToast = new Toast(ctx);
            mToast.setGravity(Gravity.CENTER, 0, 0);//设置toast显示的位置，这是居中
            mToast.setDuration(Toast.LENGTH_SHORT);//设置toast显示的时长
            View _root = LayoutInflater.from(ctx).inflate(R.layout.self_toast, null);//自定义样式，自定义布局文件
            mTvToast = (TextView) _root.findViewById(R.id.TextViewInfo);
            mToast.setView(_root);//设置自定义的view
        }
        mTvToast.setText(content);//设置文本
        mToast.show();//展示toast
    }
    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
            mTvToast = null;
        }
    }
}
