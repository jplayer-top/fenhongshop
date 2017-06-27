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
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.event.CartPaySuccess;
import com.fanglin.fenhong.microbuyer.base.event.DutyAddShowEvent;
import com.fanglin.fenhong.microbuyer.base.model.PayEntity;
import com.fanglin.fenhong.microbuyer.base.model.PopupInfo;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyCartCheck;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyPayOffline;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutygenOrderData;
import com.fanglin.fenhong.microbuyer.dutyfree.adapter.DutyfreeCartCheckAdapter;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyCartCheckCalculateViewHolder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
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
 * author:Created by Plucky on 2016/11/16-下午5:00.
 * 功能描述: 极速免税店 核对订单
 */
public class DutyfreeCartCheckActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener, DutyCartCheck.DutyCartCheckRequestCallBack, DutyCartCheckCalculateViewHolder.DutyCartCheckCalculateListener {

    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;
    @ViewInject(R.id.tvNum)
    TextView tvNum;
    @ViewInject(R.id.tvMoney)
    TextView tvMoney;
    @ViewInject(R.id.tvCalculate)
    TextView tvCalculate;

    LayoutBalanceTips layoutBalanceTips;

    DutyfreeCartCheckAdapter adapter;
    DutyCartCheck cartCheckReq;
    String cartInfo, from;

    public static final int REQPAYCODE = 0x001;
    private boolean cleanLocalStorage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        redTop = true;
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_dutyfree_cartcheck, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        cartInfo = getIntent().getStringExtra("CART_INFO");
        from = getIntent().getStringExtra("FROM");
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        tvHead.setText("订单确认");
        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshRecycleView.setOnRefreshListener(this);

        layoutBalanceTips = new LayoutBalanceTips(this);

        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new DutyfreeCartCheckAdapter(mContext);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                tvMoney.setText(adapter.getTotalPriceDesc());
                tvNum.setText(adapter.getAllNumDesc());
            }
        });
        adapter.setCalculateListener(this);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);

        cartCheckReq = new DutyCartCheck();
        cartCheckReq.setRequestCallBack(this);
        onRefresh(pullToRefreshRecycleView);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    @OnClick(value = {R.id.tvCalculate})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tvCalculate:
                if (!adapter.hasChecked()) {
                    BaseFunc.showMsg(mContext, "您必须同意授权分红全球购申报海关购买商品");
                    return;
                }
                if (!adapter.checkBind()) {
                    //Obl-change
                    BaseFunc.showMsg(mContext, "请确保已添加收货地址!");
                    return;
                }
                genOrder();
                break;
        }
    }

    private void genOrder() {
        String cartInfo = adapter.getCartInfo();
        int selected = adapter.getBalanceSelected();
        new DutyfreeBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {
                tvCalculate.setEnabled(false);
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    DutygenOrderData orderData = DutygenOrderData.getDataByJson(data);
                    if (orderData != null) {
                        double payAmount = orderData.getPay_amount();//实际交易金额
                        if (payAmount > 0) {
                            PayEntity entity = new PayEntity();
                            entity.pay_sn = orderData.getPay_sn();//交易单编号
                            entity.lastClassName = DutyfreeCartCheckActivity.class.getName();
                            entity.gc_area = 1;
                            entity.pay_amount = orderData.getPay_amount();//实际交易金额
                            entity.payment_list = orderData.getPayment_list();//支付方式
                            DutyPayOffline offline = orderData.getPay_offline();
                            if (offline != null) {
                                offline.setOrderSN(orderData.getOrder_sn());
                                offline.setPayAmount(orderData.getPayAmountDesc());
                                entity.payoff = offline.toJson();
                            }
                            BaseFunc.gotoPayActivity(DutyfreeCartCheckActivity.this, entity, REQPAYCODE);
                        } else {
                            /**
                             * 如果使用余额支付全部抵扣了 则不需要调用第三方支付 直接成功
                             * url 为Android预留字段 如果返回则跳转至该页面
                             */
                            String url = orderData.getOrderUrl();
                            if (!TextUtils.isEmpty(url)) {
                                BaseFunc.urlClick(mContext, url);
                            }
                            EventBus.getDefault().post(new CartPaySuccess(true));
                            finish();
                        }
                    }
                } else {
                    tvCalculate.setEnabled(true);
                }
            }
        }).genOrder(member, from, cartInfo, selected);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (member == null) return;
        cartCheckReq.getData(member, cartInfo, from, 0);
    }

    @Override
    public void onRequestCallBack(DutyCartCheck data) {
        pullToRefreshRecycleView.onRefreshComplete();
        //只有初次进入才清除缓存
        if (cleanLocalStorage) {
            CartCheckCache.removeLocalGoodsList();
            CartCheckCache.removeLocalAddress();
        }

        if (data != null) {
            PopupInfo info = data.getPopupInfo();
            if (info != null) {
                layoutBalanceTips.refreshData(info);
                layoutBalanceTips.show();
            }
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
            CartCheckCache.addGoodsList(data.getGoodsList());
            adapter.setCartCheck(data);

            if (!cleanLocalStorage) {
                //如果没有清除缓存 则需要刷新缓存的地址
                adapter.refreshCartData4BindAddress();
            }
        } else {
            refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
            adapter.setCartCheck(null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQPAYCODE:
                //如果支付成功了--获取取消支付，退出订单确认页
                finish();
                break;
        }
    }

    @Override
    public void onUserBalanceClick() {
        if (member == null) return;
        cleanLocalStorage = false;//在使用余额时不清除本地缓存的关联地址
        int isSelected = adapter.getBalanceSelected();
        isSelected = isSelected == 0 ? 1 : 0;
        cartCheckReq.getData(member, cartInfo, from, isSelected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.refreshCartData4BindAddress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void getAddress(DutyAddShowEvent event) {
        if (event.isSuccess) {
            adapter.showAddress(event);
        }
    }
}
