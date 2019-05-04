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
import com.yxw.cn.carpenterrepair.entity.OrderItem;


/**
 * Created by chenqm
 */
public class ContactPop extends PopupWindow implements View.OnClickListener {
    private View mContentView;
    private Activity mContext;
    private TextView mTvCall;
    private TextView mTvUpdateTime;
    private TextView mTvConfirm;
    private SelectListener mSelectListener;
    private OrderItem mOrderItem;

    public void setSelectListener(SelectListener selectListener) {
        this.mSelectListener = selectListener;
    }

    public interface SelectListener {
        void onCall(OrderItem orderItem);
        void onTime(OrderItem orderItem);
        void onConfirm(OrderItem orderItem);
    }

    public ContactPop(Activity context, SelectListener selectListener, OrderItem orderItem) {
        this.mContext = context;
        this.mSelectListener = selectListener;
        this.mOrderItem = orderItem;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.pop_schedule_remind, null);
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
        mTvCall = mContentView.findViewById(R.id.tv_call);
        mTvUpdateTime = mContentView.findViewById(R.id.tv_update_time);
        mTvConfirm = mContentView.findViewById(R.id.tv_confirm);
    }

    private void initListener() {
        mContentView.findViewById(R.id.lly_all).setOnClickListener(this);
        mTvCall.setOnClickListener(this);
        mTvUpdateTime.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAtLocation(parent, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
        } else {
            this.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        dismiss();
        if (id == R.id.tv_call){
            mSelectListener.onCall(mOrderItem);
        }else if (id == R.id.tv_update_time){
            mSelectListener.onTime(mOrderItem);
        }else if (id == R.id.tv_confirm){
            mSelectListener.onConfirm(mOrderItem);
        }
    }
}
