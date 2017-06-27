package com.fanglin.fenhong.microbuyer.dutyfree;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreeConsumeDetail;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/12/8-上午11:51.
 * 功能描述: 极速免税 消费详情
 */
public class DutyfreeConsumeDetailActivity extends BaseFragmentActivityUI implements DutyfreeConsumeDetail.DutyfreeConsumeDetailRequestCallBack {

    DutyfreeConsumeDetail consumeDetailReq;
    private String consumeId;
    @ViewInject(R.id.LMoney)
    LinearLayout LMoney;
    @ViewInject(R.id.LDetail)
    LinearLayout LDetail;
    @ViewInject(R.id.tvLabel)
    TextView tvLabel;
    @ViewInject(R.id.tvMoney)
    TextView tvMoney;
    @ViewInject(R.id.tvType)
    TextView tvType;
    @ViewInject(R.id.tvTime)
    TextView tvTime;
    @ViewInject(R.id.tvSN)
    TextView tvSN;
    @ViewInject(R.id.tvLeft)
    TextView tvLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_consume_detail, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        consumeId = getIntent().getStringExtra("VAL");
        initView();
    }

    private void initView() {
        tvHead.setText("消费明细");
        consumeDetailReq = new DutyfreeConsumeDetail();
        consumeDetailReq.setRequestCallBack(this);
        consumeDetailReq.getData(member, consumeId);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void onDutyfreeConsumeDetail(DutyfreeConsumeDetail data) {
        if (data != null) {
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);

            GradientDrawable drawableMoney = (GradientDrawable) LMoney.getBackground();
            drawableMoney.setColor(data.getColor());

            GradientDrawable drawableDetail = (GradientDrawable) LDetail.getBackground();
            drawableDetail.setColor(data.getColor());


            tvLabel.setText(data.getConsumeLabel());
            tvMoney.setText(data.getConsumeMoney());
            tvType.setText(data.getConsumeType());
            tvTime.setText(data.getCreatetime());
            tvSN.setText(data.getConsumeSn());
            tvLeft.setText(data.getLeftMoney());
        } else {
            refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
        }
    }
}
