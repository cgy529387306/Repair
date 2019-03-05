package com.yxw.cn.carpenterrepair.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.entity.OrderUpload;

import java.util.List;

/**
 * Created by CY on 2018/11/29
 */
public class OrderUploadAdapter extends BaseMultiItemQuickAdapter<OrderUpload, BaseViewHolder> {

    public OrderUploadAdapter(List data) {
        super(data);
        addItemType(OrderUpload.UPLOAD, R.layout.item_order_upload);
        addItemType(OrderUpload.ADD, R.layout.item_order_upload_add);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderUpload item) {
        switch (helper.getItemViewType()) {
            case OrderUpload.UPLOAD:
                helper.addOnClickListener(R.id.iv_del);
                Glide.with(mContext).load(item.getPath()).into((ImageView) helper.getView(R.id.iv_photo));
                break;
            case OrderUpload.ADD:
                helper.addOnClickListener(R.id.rl_card_1);
                break;
        }
    }
}
