package com.example.mao.util;

/**
 * Created by Mao on 2017/5/22.
 */

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

public class MyDeviceAdmin extends DeviceAdminReceiver {
    @Override
    public void onEnabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
        ToastSelf.ToastSelf("主动防御已经打开",context);
        SP.save(context,"open_defence", true);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        ToastSelf.ToastSelf("主动防御已经关闭",context);
        ToastSelf.ToastSelf("本软件存在被第三方恶意卸载风险",context);
        SP.save(context,"open_defence", false);
    }
    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "如果取消激活，则存在第三方恶意卸载风险";
    }
}