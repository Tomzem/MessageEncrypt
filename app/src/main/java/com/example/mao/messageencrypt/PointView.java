package com.example.mao.messageencrypt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

/**
 * Created by LongRiver on 2017/5/1.
 */

public class PointView extends View {
    private float x;
    private float y;
    private float round;
    public PointView(Context context, float x, float y, float round) {
        super(context);
        this.x = x;
        this.y = y;
        this.round = round;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(8);
        p.setAntiAlias(true);//消除锯齿
        p.setColor(Color.RED);// 设置红色
        canvas.drawCircle(x, y, 40, p);// 小圆
    }



}
