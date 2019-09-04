package com.yxw.cn.carpenterrepair.activity;

import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.NoticeBean;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.Helper;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.HashMap;

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
        mTvTitle.setText(noticeBean.getTitle());
        mTvTime.setText(noticeBean.getCreateTime());
        mTvContent.setText(noticeBean.getContent());
        doRead(noticeBean);
    }

    private void doRead(NoticeBean noticeBean){
        if (noticeBean!=null && Helper.isNotEmpty(noticeBean.getNoticeId())){
            HashMap<String, Object> map = new HashMap<>();
            OkGo.<ResponseData<Object>>post(UrlConstant.GET_NOTICE_READ+noticeBean.getNoticeId())
                    .upJson(gson.toJson(map))
                    .execute(new JsonCallback<ResponseData<Object>>() {
                        @Override
                        public void onSuccess(ResponseData<Object> response) {
                            EventBusUtil.post(MessageConstant.GET_MSG_COUNT);
                        }
                    });
        }
    }

}