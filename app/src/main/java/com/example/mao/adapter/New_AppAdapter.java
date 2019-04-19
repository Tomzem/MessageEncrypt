package com.example.mao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.mao.app.New_Application;
import com.example.mao.bean.APPInfo;
import com.example.mao.messageencrypt.R;
import com.example.mao.util.SP;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tomze
 * @time 2019年04月20日 1:06
 * @desc
 */
public class New_AppAdapter extends BaseAdapter {

    List<APPInfo> AppInfos = new ArrayList<APPInfo>();
    Context context;
    public void setData(Context context,List<APPInfo> AppInfos) {
        this.AppInfos = AppInfos;
        this.context = context;
        notifyDataSetChanged();
    }

    public List<APPInfo> getData() {
        return AppInfos;
    }

    @Override
    public int getCount() {
        if (AppInfos != null && AppInfos.size() > 0) {
            return AppInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (AppInfos != null && AppInfos.size() > 0) {
            return AppInfos.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        New_AppAdapter.ViewHolder mViewHolder;
        APPInfo myAppInfo = AppInfos.get(position);
        if (convertView == null) {
            mViewHolder = new New_AppAdapter.ViewHolder();
            convertView = LayoutInflater.from(New_Application.getContext()).inflate(R.layout.new_item_list_app, null);
            mViewHolder.iv_app_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
            mViewHolder.tx_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (New_AppAdapter.ViewHolder) convertView.getTag();
        }
        mViewHolder.iv_app_icon.setImageDrawable(myAppInfo.getImage());
        mViewHolder.tx_app_name.setText(myAppInfo.getAppName());
        return convertView;
    }

    class ViewHolder {
        ImageView iv_app_icon;
        TextView tx_app_name;
    }
}
