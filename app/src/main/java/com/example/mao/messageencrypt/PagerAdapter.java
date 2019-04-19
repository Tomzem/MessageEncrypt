package com.example.mao.messageencrypt;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.view.View;

/**
 * Created by LongRiver on 2017/5/8.
 */

public class PagerAdapter extends FragmentPagerAdapter {
    private int count = 1;
    private Context context;
    private int what;

    public PagerAdapter(FragmentManager fm,Context context,int what) {
        super(fm);
        this.context = context;
        if(what != -1){
            this.count++;
        }
        this.what = what;
    }

    @Override
    public Fragment getItem(int position) {
        if(count > 1){
            if(count - position == 1){
                return NumPassFragment.newInstance();
            }else{
                return ImgPassFragment.newInstance(what);
            }
        }else{
            return NumPassFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}
