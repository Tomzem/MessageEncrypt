package com.example.mao.util;

import android.os.SystemClock;

/**
 * @author Tomze
 * @time 2019年04月20日 10:02
 * @desc
 */
public class New_Util {

    static long[] mClicks = new long[2];

    /**
     * 判断是否双击，快速的重复请求
     * @return
     */
    public static boolean isDoubleClick() {
        System.arraycopy(mClicks, 1, mClicks, 0, mClicks.length-1);
        mClicks[mClicks.length-1] = SystemClock.uptimeMillis();
        return mClicks[0] > (SystemClock.uptimeMillis() - 500);
    }
}
