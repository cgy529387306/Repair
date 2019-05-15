package com.yxw.cn.carpenterrepair.pop;

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
public class ConfirmOrderPop extends PopupWindow implements View.OnClickListener {
    private View mContentView;
    private Context mContext;
    private TextView mTvCancel;
    private TextView mTvConfirm;
    private OrderItem item;
    private SelectListener mSelectListener;

    public interface SelectListener {
        void onOrderComfirm(OrderItem orderItem);
    }


    public ConfirmOrderPop(Context context, OrderItem item, SelectListener mSelectListener) {
        this.mContext = context;
        this.item = item;
        this.mSelectListener = mSelectListener;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.pop_confirm_order, null);
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
        mTvCancel = mContentView.findViewById(R.id.tv_cancel);
        mTvConfirm = mContentView.findViewById(R.id.tv_confirm);
    }

    private void initListener() {
        mTvCancel.setOnClickListener(this);
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
            this.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else {
            this.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        dismiss();
        if (id == R.id.tv_cancel){
        }else if (id == R.id.tv_confirm){
            mSelectListener.onOrderComfirm(item);
        }
    }
}
