package com.yxw.cn.carpenterrepair.activity;

import android.widget.TextView;

import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.entity.NoticeBean;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import butterknife.BindView;

/**
 * 查看图片
 */
public class MsgDetailActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_content)
    TextView mTvContent;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_msg_detail;
    }

    @Override
    public void initView() {
        super.initView();
        titleBar.setTitle("消息详情");
        NoticeBean noticeBean = (NoticeBean) getIntent().getSerializableExtra("data");
        if (noticeBean!=null){
            mTvTitle.setText(noticeBean.getTitle());
            mTvTime.setText(noticeBean.getCreateTime());
            mTvContent.setText(noticeBean.getContent());
        }
    }

}