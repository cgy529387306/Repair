package com.yxw.cn.carpenterrepair.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.entity.Abnormal;

import java.util.List;

/**
 * Created by CY on 2018/11/25
 */
public class OrderAbnormalAdapter extends BaseQuickAdapter<Abnormal, BaseViewHolder> {

    private int select=0;

    public void setSelect(int select) {
        this.select = select;
    }

    public OrderAbnormalAdapter(List<Abnormal> data) {
        super(R.layout.item_worker_abnormal, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Abnormal item) {
        helper.setBackgroundRes(R.id.name,select==helper.getLayoutPosition()?R.drawable.corner_stroke_red1:R.drawable.corner_stroke_gray1)
        .setTextColor(R.id.name,select!=helper.getLayoutPosition()?mContext.getResources().getColor(R.color.text_gray)
                :mContext.getResources().getColor(R.color.white))
        .setText(R.id.name,item.getName());
    }
}
