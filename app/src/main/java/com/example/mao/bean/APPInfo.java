package com.example.mao.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Mao on 2017/5/5.
 */

public class APPInfo {
    private Drawable image;
    private String appName;
    private boolean radio;
    private String packageName;
    public APPInfo(Drawable image, String appName,boolean radio,String packageName) {
        this.image = image;
        this.appName = appName;
        this.radio = radio;
        this.packageName = packageName;
    }
    public APPInfo() {

    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isRadio() {
        return radio;
    }

    public void setRadio(boolean radio) {
        this.radio = radio;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}