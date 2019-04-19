package com.example.mao.util;

/**
 * Created by Mao on 2017/5/22.
 */

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.example.mao.messageencrypt.R;

public class TestPopwindow2 extends PopupWindow {
    // 根视图
    private View mRootView;
    // LayoutInflater
    LayoutInflater mInflater;

    public TestPopwindow2(Activity context) {
        super(context);
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = mInflater.inflate(R.layout.test_popwindow_2, null);
        setContentView(mRootView);

        this.setWidth(LayoutParams.WRAP_CONTENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);

        // 设置PopUpWindow弹出的相关属性
        setTouchable(true);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable(context.getResources()));
        update();

        getContentView().setFocusableInTouchMode(true);
        getContentView().setFocusable(true);
    }
}