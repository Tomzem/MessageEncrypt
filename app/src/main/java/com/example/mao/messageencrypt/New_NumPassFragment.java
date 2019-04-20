package com.example.mao.messageencrypt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mao.util.ToastSelf;

/**
 * @author Tomze
 * @time 2019年04月20日 18:18
 * @desc
 */
public class New_NumPassFragment extends Fragment implements View.OnClickListener{

    private ImageView iv_close;
    private TextView mTvCallme;
    private TextView mTvBack;
    private Context mContext;

    public New_NumPassFragment() {
        // Required empty public constructor
    }

    public static New_NumPassFragment newInstance() {
        New_NumPassFragment fragment = new New_NumPassFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = this.getContext();
        View view = inflater.inflate(R.layout.new_content_over_time, container, false);
        intiView(view);
        return view;
    }

    private void intiView(View view) {
        iv_close = (ImageView) view.findViewById(R.id.iv_close);
        mTvCallme = (TextView) view.findViewById(R.id.tv_call_me);
        mTvBack = (TextView) view.findViewById(R.id.tc_back_app);
        iv_close.setOnClickListener(this);
        mTvCallme.setOnClickListener(this);
        mTvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == iv_close || view == mTvBack) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        } else if (view == mTvCallme) {
            ToastSelf.ToastSelf("我没做呢，嘿嘿嘿", mContext);
        }
    }
}
