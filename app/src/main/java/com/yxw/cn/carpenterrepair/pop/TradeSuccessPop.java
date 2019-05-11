package com.yxw.cn.carpenterrepair.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yxw.cn.carpenterrepair.R;


/**
 * Created by chenqm
 */
public class TradeSuccessPop extends PopupWindow implements View.OnClickListener {
    private View mContentView;
    private Activity mContext;
    private TextView mTvConfirm;
    private View.OnClickListener mOnClickListener;

    public TradeSuccessPop(Activity context, View.OnClickListener listener) {
        this.mContext = context;
        this.mOnClickListener = listener;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.pop_trade_success, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(mContentView);
        setFocusable(false);
        setOutsideTouchable(false);
        setWidth(ViewPager.LayoutParams.MATCH_PARENT);
        setHeight(ViewPager.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
        initListener();
    }

    private void initView() {
        mTvConfirm = mContentView.findViewById(R.id.tv_confirm);
    }

    private void initListener() {
        mContentView.findViewById(R.id.lly_all).setOnClickListener(this);
        mTvConfirm.setOnClickListener(mOnClickListener);
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else {
            this.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
