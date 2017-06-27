package com.fanglin.fenhong.microbuyer.dutyfree;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.event.DutyfreeVipEvent;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreeUCenter;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/12/7-下午3:41.
 * 功能描述: 急速免税店个人中心 显示钱包及其他
 */
public class DutyfreePersonalActivity extends BaseFragmentActivity implements DutyfreeUCenter.DutyfreeUCenterListener, DutyPayDialog.DutyPayDialogListener {

    @ViewInject(R.id.ivBanner)
    ImageView ivBanner;
    @ViewInject(R.id.multiStateView)
    MultiStateView multiStateView;

    @ViewInject(R.id.ivRank)
    ImageView ivRank;
    @ViewInject(R.id.tvAccount)
    TextView tvAccount;
    @ViewInject(R.id.tvMoney)
    TextView tvMoney;
    @ViewInject(R.id.tvAccountDays)
    TextView tvAccountDays;
    @ViewInject(R.id.tvInviteMoney)
    TextView tvInviteMoney;
    @ViewInject(R.id.tvInviteNum)
    TextView tvInviteNum;
    @ViewInject(R.id.tvCharge)
    TextView tvCharge;

    @ViewInject(R.id.tvToPayNum)
    TextView tvToPayNum;
    @ViewInject(R.id.tvToSendNum)
    TextView tvToSendNum;
    @ViewInject(R.id.tvToReceiveNum)
    TextView tvToReceiveNum;
    @ViewInject(R.id.tvMessage)
    TextView tvMessage;
    @ViewInject(R.id.tvMsgNum)
    TextView tvMsgNum;
    DutyfreeUCenter uCenterReq;
    public static final int REQ_PAY = 110;
    DutyPayDialog payDialog;

    String clickUrl, inviteUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dutyfree_personal);
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        int h = BaseFunc.getDisplayMetrics(mContext).widthPixels * 463 / 750;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
        ivBanner.setLayoutParams(params);

        uCenterReq = new DutyfreeUCenter();
        uCenterReq.setuCenterListener(this);

        uCenterReq.getData(member);
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        payDialog = new DutyPayDialog(this);
        payDialog.setDutyPayListener(this);

        BaseFunc.setFont(tvMessage);

        tvMessage.setTypeface(iconfont);
    }

    @OnClick(value = {R.id.LBalance,
            R.id.tvShare, R.id.ivBack, R.id.tvCharge,
            R.id.LOrder, R.id.LInviteMoney,
            R.id.tvToPay, R.id.tvToSend, R.id.tvToReceive,
            R.id.ivRank, R.id.tvMessage})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.LBalance:
            case R.id.ivRank:
                if (!TextUtils.isEmpty(clickUrl)) {
                    BaseFunc.urlClick(mContext, clickUrl);
                }
                break;
            case R.id.LInviteMoney:
                BaseFunc.gotoActivity(mContext, DutyfreePersonalInviteActivity.class, "4");
                break;
            case R.id.tvShare:
                if (BaseFunc.isValidUrl(inviteUrl)) {
                    BaseFunc.urlClick(this, inviteUrl);
                }
                break;
            case R.id.tvCharge:
                doPay();
                break;
            case R.id.LOrder:
                BaseFunc.gotoActivity(mContext, OrderListActivity.class, "0");
                break;
            case R.id.tvToPay:
                BaseFunc.gotoActivity(mContext, OrderListActivity.class, "1");
                break;
            case R.id.tvToSend:
                BaseFunc.gotoActivity(mContext, OrderListActivity.class, "2");
                break;
            case R.id.tvToReceive:
                BaseFunc.gotoActivity(mContext, OrderListActivity.class, "3");
                break;

            case R.id.tvMessage:  //消息跳转
                BaseFunc.gotoActivity(mContext, DutyfreePersonalMessagesActivity.class, "5");
                break;
        }
    }

    private void doPay() {
        BaseFunc.topUp(this, REQ_PAY);
    }

    @Override
    public void onDutyfreeUCenterData(DutyfreeUCenter data) {
        if (data != null) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);

            new FHImageViewUtil(ivRank).setImageURI(data.getRank_img(), FHImageViewUtil.SHOWTYPE.BANNER);
            tvAccount.setText(data.getAccount());
            tvMoney.setText(data.getAccount_desc());
            tvAccountDays.setText(data.getAccount_days());
            tvInviteMoney.setText(data.getInvite_money());
            tvInviteNum.setText(data.getInvite_num());

            if (TextUtils.isEmpty(data.getButton_label())) {
                tvCharge.setVisibility(View.INVISIBLE);
            } else {
                tvCharge.setVisibility(View.VISIBLE);
                tvCharge.setText(data.getButton_label());
            }

            clickUrl = data.getAccount_url();
            inviteUrl = data.getInvite_url();


            String pay = data.getUnpaiedCountDesc();
            if (TextUtils.equals("0", pay)) {
                tvToPayNum.setVisibility(View.INVISIBLE);
            } else {
                tvToPayNum.setVisibility(View.VISIBLE);
                tvToPayNum.setText(pay);
            }

            String send = data.getUnsentCountDesc();
            if (TextUtils.equals("0", send)) {
                tvToSendNum.setVisibility(View.INVISIBLE);
            } else {
                tvToSendNum.setVisibility(View.VISIBLE);
                tvToSendNum.setText(send);
            }

            String receive = data.getUnreceivedCountDesc();
            if (TextUtils.equals("0", receive)) {
                tvToReceiveNum.setVisibility(View.INVISIBLE);
            } else {
                tvToReceiveNum.setVisibility(View.VISIBLE);
                tvToReceiveNum.setText(receive);
            }

            if (TextUtils.isEmpty(data.getRead_count())) {
                tvMsgNum.setVisibility(View.GONE);
            } else {
                tvMsgNum.setVisibility(View.VISIBLE);
                tvMsgNum.setText(data.getRead_count());
            }

        } else {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_PAY:
                if (resultCode == RESULT_OK && data != null) {
                    int result = data.getIntExtra("result", -1);
                    payDialog.refreshView(result == 0);
                    payDialog.show();
                }
                break;
        }
    }

    @Override
    public void onSubmitClick(boolean isSuccess) {
        if (isSuccess) {
            finish();
        } else {
            doPay();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handleDutyfreeVipEvent(DutyfreeVipEvent event) {
        if (event != null && event.isVip()) {
            uCenterReq.getData(member);
        }
    }
}
