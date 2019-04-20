package com.example.mao.weiget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * @author Tomze
 * @time 2019年04月20日 9:22
 * @desc 屏蔽子View焦点
 */
public class New_RelativeLayout extends RelativeLayout {
    public New_RelativeLayout(Context context) {
        super(context);
    }

    public New_RelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public New_RelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return true;
    }
}
