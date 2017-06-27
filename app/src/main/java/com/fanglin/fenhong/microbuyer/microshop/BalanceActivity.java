package com.fanglin.fenhong.microbuyer.microshop;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.UserAccount;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.text.DecimalFormat;

/**
 * 作者： Created by Plucky on 2015/11/26.
 * 我的余额
 */
public class BalanceActivity extends BaseFragmentActivityUI implements UserAccount.UAModelCallBack {

    @ViewInject(R.id.tv_all_money)
    TextView tv_all_money;
    @ViewInject(R.id.tv_history_money)
    TextView tv_history_money;
    @ViewInject(R.id.tv_avaliable_money)
    TextView tv_avaliable_money;
    @ViewInject(R.id.tv_desc)
    TextView tv_desc;

    private DecimalFormat df;
    UserAccount userAccountReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_balance, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        df = new DecimalFormat("#0.00");
        initView();
    }

    private void initView() {
        setHeadTitle(R.string.balance);
        enableTvMore(R.string.detail,false);

        tv_desc.setText(Html.fromHtml(getString(R.string.deposit_tips_iconfont)));
        BaseFunc.setFont(tv_desc);
        refreshMoney(0, 0, 0);
        userAccountReq = new UserAccount();
        userAccountReq.setModelCallBack(this);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userAccountReq.getData(member);
    }

    @Override
    public void onUADate(UserAccount account) {
        refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        if (account != null) {
            refreshMoney(account.available_predeposit, account.allsum_money, account.pdc_amount);
        } else {
            refreshMoney(0, 0, 0);
        }
    }

    @Override
    public void onUAError(String errcode) {
        refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        refreshMoney(0, 0, 0);
    }

    private void refreshMoney(double all, double history, double avaliable) {
        tv_all_money.setText(df.format(all));
        tv_history_money.setText(df.format(history));
        tv_avaliable_money.setText(df.format(avaliable));
    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();
        BaseFunc.gotoActivity(mContext, DepositeDtlActivity.class, null);
    }

    @OnClick(value = {R.id.tv_submit, R.id.tv_desc})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                BaseFunc.gotoActivity(mContext, DepositeActivity.class, null);
                break;
            case R.id.tv_desc:

                break;
        }
    }
}
