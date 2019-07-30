package com.yxw.cn.carpenterrepair.activity;

import android.widget.TextView;

import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.SpConstant;
import com.yxw.cn.carpenterrepair.entity.NoticeBean;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.SpUtil;
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
        boolean isRead = SpUtil.getBoolean("msg"+noticeBean.getNoticeId(),false);
        if (!isRead){
            SpConstant.UNREAD_MSG_COUNT--;
            SpUtil.putBoolean("msg"+noticeBean.getNoticeId(),true);
            EventBusUtil.post(MessageConstant.GET_MSG_COUNT);
        }
        mTvTitle.setText(noticeBean.getTitle());
        mTvTime.setText(noticeBean.getCreateTime());
        mTvContent.setText(noticeBean.getContent());
    }

}