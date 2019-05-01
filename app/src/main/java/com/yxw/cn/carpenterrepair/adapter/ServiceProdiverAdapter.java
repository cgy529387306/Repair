package com.yxw.cn.carpenterrepair.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yxw.cn.carpenterrepair.R;

import java.util.List;


/**
 * Created by necer on 2017/6/7.
 */
public class ServiceProdiverAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ServiceProdiverAdapter(List data) {
        super(R.layout.item_service_provider, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_name,item);
    }
}




