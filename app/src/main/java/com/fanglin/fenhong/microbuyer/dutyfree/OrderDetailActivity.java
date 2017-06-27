package com.fanglin.fenhong.microbuyer.dutyfree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.DutyfreeBO;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.PayEntity;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyOrder;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyOrderDtlRequest;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyPayOffline;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyWaybill;
import com.fanglin.fenhong.microbuyer.common.FHBrowserActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.adapter.OrderDetailAdapter;
import com.fanglin.fenhong.microbuyer.dutyfree.listener.DutyBillActionListener;
import com.fanglin.fenhong.microbuyer.dutyfree.listener.DutyOrderActionListener;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/11-上午11:23.
 * 功能描述: 极速免税店订单详情
 */
public class OrderDetailActivity extends BaseFragmentActivityUI implements DutyOrderDtlRequest.DutyOrderDtlRequestCallback, DutyBillActionListener {

    @ViewInject(R.id.recyclerView)
    RecyclerView recyclerView;
    @ViewInject(R.id.tvLeft)
    TextView tvLeft;
    @ViewInject(R.id.tvRight)
    TextView tvRight;
    @ViewInject(R.id.tvMoney)
    TextView tvMoney;
    @ViewInject(R.id.tvNumFreight)
    TextView tvNumFreight;

    OrderDetailAdapter adapter;
    public static final int REQPAYCODE = 0;

    DutyOrderDtlRequest dtlRequest;

    String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        redTop = true;
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_dutyfree_orderdtl, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        orderId = getIntent().getStringExtra("VAL");
        initView();
    }

    private void initView() {
        tvHead.setText("我的订单");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new OrderDetailAdapter(mContext);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                String[] str = adapter.getButtonDesc();
                if (!TextUtils.isEmpty(str[0])) {
                    tvLeft.setVisibility(View.VISIBLE);
                    tvLeft.setText(str[0]);
                } else {
                    tvLeft.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(str[1])) {
                    tvRight.setVisibility(View.VISIBLE);
                    tvRight.setText(str[1]);
                } else {
                    tvRight.setVisibility(View.GONE);
                }

                tvMoney.setText(adapter.getMoney());
                tvNumFreight.setText(adapter.getNumAndFreight());
            }
        });
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);

        dtlRequest = new DutyOrderDtlRequest();
        dtlRequest.setRequestCallback(this);

        doRequest();
    }

    private void doRequest() {
        if (member == null) return;
        dtlRequest.getData(member, orderId);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void onDutyOrderDtlData(DutyOrder data) {
        if (data != null) {
            adapter.setOrder(data);
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        } else {
            refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    @OnClick(value = {R.id.tvLeft, R.id.tvRight})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tvLeft:
            case R.id.tvRight:
                if (view instanceof TextView) {
                    DutyOrder order = adapter.getOrder();
                    String text = ((TextView) view).getText().toString();
                    if (TextUtils.equals(text, DutyOrderActionListener.DELETE)) {
                        onDelete(order);
                    }
                    if (TextUtils.equals(text, DutyOrderActionListener.CANCEL)) {
                        onCancel(order);
                    }
                    if (TextUtils.equals(text, DutyOrderActionListener.PAY)) {
                        onPay(order);
                    }
                    if (TextUtils.equals(text, DutyOrderActionListener.RECEIVE)) {
                        onReceive(order);
                    }
                }
                break;
        }
    }

    public void onCancel(DutyOrder order) {
        if (member == null || order == null) return;
        new DutyfreeBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    doRequest();
                }
            }
        }).cancelOrder(member, order.getOrder_id());
    }


    public void onDelete(DutyOrder order) {
        if (member == null || order == null) return;
        new DutyfreeBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    finish();
                }
            }
        }).delOrder(member, order.getOrder_id());

    }

    public void onPay(DutyOrder order) {
        if (member == null || order == null) return;
        PayEntity entity = new PayEntity();
        entity.pay_sn = order.getPay_sn();//交易单编号
        entity.lastClassName = OrderDetailActivity.class.getName();
        entity.gc_area = 1;
        entity.pay_amount = order.getReality_price();//实际交易金额
        entity.payment_list = order.getPayment_list();//支付方式

        DutyPayOffline offline = order.getPay_offline();
        if (offline != null) {
            offline.setOrderSN(order.getOrder_sn());
            offline.setPayAmount(order.getPayPriceDesc());
            entity.payoff = offline.toJson();
        }
        BaseFunc.gotoPayActivity(this, entity, REQPAYCODE);
    }

    public void onReceive(DutyOrder order) {
        if (member == null || order == null) return;
        new DutyfreeBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    doRequest();
                }
            }
        }).receiveAllOrder(member, order.getOrder_id());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQPAYCODE:
                doRequest();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        setResult(RESULT_OK);
    }

    @Override
    public void onDelivery(DutyWaybill bill) {
        if (bill == null) return;
        if (!TextUtils.isEmpty(bill.getWaybill_url())) {
            BaseFunc.gotoActivity(mContext, FHBrowserActivity.class, bill.getWaybill_url());
        } else {
            BaseFunc.showMsg(mContext, "正在发货!");
        }
    }

    @Override
    public void onReceive(DutyWaybill bill) {
        if (member == null || bill == null) return;
        new DutyfreeBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    doRequest();
                }
            }
        }).receiveBill(member, bill.getWaybill_id());
    }
}
