package com.yxw.cn.carpenterrepair.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.entity.OrderType;
import com.yxw.cn.carpenterrepair.util.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/6/24.
 */
public class OrderTypeAdapter extends BaseAdapter {
    private Context mContext;
    public List<OrderType> mData = new ArrayList<OrderType>();
    public OrderTypeAdapter(Context context, List<OrderType> htList) {
        mContext = context;
        mData = htList;
    }

    public int getCount() {
        return mData==null?0:mData.size();
    }

    public boolean areAllItemsEnabled() {
        return false;
    }

    public OrderType getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order_type, null);
            viewHolder = new ViewHolder();
            viewHolder.ivCover = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_order_type);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final OrderType orderType = mData.get(position);
        viewHolder.tvTitle.setText(orderType.getName());
        viewHolder.ivCover.setImageResource(orderType.getDrawableId());
        return convertView;
    }

    private class ViewHolder {
        public ImageView ivCover;
        public TextView tvTitle;
    }

}
