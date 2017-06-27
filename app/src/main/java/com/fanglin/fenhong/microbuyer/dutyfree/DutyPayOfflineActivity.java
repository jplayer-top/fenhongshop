package com.fanglin.fenhong.microbuyer.dutyfree;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyPayOffline;
import com.fanglin.fenhong.microbuyer.common.FHBrowserActivity;
import com.fanglin.fhlib.other.FHLib;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/22-下午8:44.
 * 功能描述: 极速免税店线下支付
 */
public class DutyPayOfflineActivity extends BaseFragmentActivityUI {

    @ViewInject(R.id.tvCompany)
    TextView tvCompany;
    @ViewInject(R.id.tvBankNO)
    TextView tvBankNO;
    @ViewInject(R.id.tvBankName)
    TextView tvBankName;
    @ViewInject(R.id.tvOrderSN)
    TextView tvOrderSN;
    @ViewInject(R.id.tvMoney)
    TextView tvMoney;
    @ViewInject(R.id.tvEml)
    TextView tvEml;

    DutyPayOffline payOffline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        redTop = true;
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_dutypay_offline, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        try {
            String val = getIntent().getStringExtra("VAL");
            payOffline = new Gson().fromJson(val, DutyPayOffline.class);
        } catch (Exception e) {
            payOffline = null;
        }
        initView();
    }

    private void initView() {
        tvHead.setText("企业账户");
        if (payOffline == null) {
            BaseFunc.showMsg(mContext, "异常数据");
            finish();
            return;
        }
        tvCompany.setText(payOffline.getCompany());
        tvBankNO.setText(payOffline.getBank_no());
        tvBankName.setText(payOffline.getBank_name());
        tvOrderSN.setText(payOffline.getOrderSN());
        tvMoney.setText(payOffline.getPayAmount());
        tvEml.setText(payOffline.getEmail());
    }

    @OnClick(value = {R.id.tvCompany, R.id.tvBankNO, R.id.tvBankName, R.id.tvOrderSN, R.id.ivService})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tvCompany:
            case R.id.tvBankNO:
            case R.id.tvBankName:
            case R.id.tvOrderSN:
                String text = ((TextView) view).getText().toString();
                FHLib.copy(mContext, text);
                BaseFunc.showMsg(mContext, getString(R.string.already_copy));
                break;
            case R.id.ivService:
                if (BaseFunc.isValidUrl(payOffline.getService())) {
                    BaseFunc.gotoActivity(mContext, FHBrowserActivity.class, payOffline.getService());
                } else {
                    BaseFunc.showMsg(mContext, "客服不在线!");
                }
                break;
        }
    }
}
