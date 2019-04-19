package com.example.mao.util;

import android.os.CountDownTimer;

/**
 * Created by LongRiver on 2017/5/23.
 */

public class CountDownUtil extends CountDownTimer{
    public CountDownUtil(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    //过程
    @Override
    public void onTick(long l) {

    }
    //结束
    @Override
    public void onFinish() {

    }
}
