package com.yxw.cn.carpenterrepair.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.entity.UserOrder;

import java.util.List;

/**
 * Created by CY on 2018/11/25
 */
public class UserOrderPicAdapter extends BaseQuickAdapter<UserOrder.ListBean.PicListBean, BaseViewHolder> {


    public UserOrderPicAdapter(List<UserOrder.ListBean.PicListBean> data) {
        super(R.layout.item_order_pic, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserOrder.ListBean.PicListBean item) {
        Glide.with(mContext).load(item.getUrl()).into((ImageView) helper.getView(R.id.iv_photo));
    }
}
