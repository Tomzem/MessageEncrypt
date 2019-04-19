package com.example.mao.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.mao.messageencrypt.R;


/**
 * Created by Mao on 2017/3/22.
 */

public class ToastSelf {

    public static void ToastSelf(String Message,Context context){
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View toastRoot = inflater.inflate(R.layout.self_toast,null);
        android.widget.Toast toast=new android.widget.Toast(context);
        toast.setView(toastRoot);
        TextView tv=(TextView)toastRoot.findViewById(R.id.TextViewInfo);
        tv.setText(Message);
        toast.show();
    }
}
