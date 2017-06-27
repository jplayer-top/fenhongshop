package com.fanglin.fenhong.microbuyer.dutyfree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.DutyfreeBO;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.PayEntity;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyOrder;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyOrderListRequest;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyPayOffline;
import com.fanglin.fenhong.microbuyer.dutyfree.adapter.OrderListAdapter;
import com.fanglin.fenhong.microbuyer.dutyfree.listener.DutyOrderActionListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/10-下午1:42.
 * 功能描述: 极速免税订单列表
 */
public class OrderListActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener2, DutyOrderListRequest.DutyOrderListRequestCallBack, DutyOrderActionListener {

    public static final int REQPAYORDER = 109;
    public static final int REQPAYCODE = 110;
    @ViewInject(R.id.tvAll)
    TextView tvAll;
    @ViewInject(R.id.vAll)
    View vAll;
    @ViewInject(R.id.tvPay)
    TextView tvPay;
    @ViewInject(R.id.vPay)
    View vPay;
    @ViewInject(R.id.tvDelivery)
    TextView tvDelivery;
    @ViewInject(R.id.vDelivery)
    View vDelivery;
    @ViewInject(R.id.tvRecept)
    TextView tvRecept;
    @ViewInject(R.id.vRecept)
    View vRecept;
    @ViewInject(R.id.multiStateView)
    MultiStateView multiStateView;
    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;
    @ViewInject(R.id.btnBackTop)
    Button btnBackTop;

    int white, red, black;
    OrderListAdapter adapter;
    DutyOrderListRequest orderListRequest;
    int curpage, state = 5, index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        redTop = true;
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_dutyfree_orderlist, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        String val = getIntent().getStringExtra("VAL");
        if (BaseFunc.isInteger(val)) {
            index = Integer.valueOf(val);
        }
        initView();
    }

    private void initView() {
        white = getResources().getColor(R.color.white);
        red = getResources().getColor(R.color.fh_red);
        black = getResources().getColor(R.color.color_33);

        tvHead.setText("我的订单");
        onPageChange(index);

        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshRecycleView.setOnRefreshListener(this);
        pullToRefreshRecycleView.setBackUpView(btnBackTop);

        adapter = new OrderListAdapter(mContext);
        adapter.setListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(manager);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);

        orderListRequest = new DutyOrderListRequest();
        orderListRequest.setRequestCallBack(this);
        onPullDownToRefresh(pullToRefreshRecycleView);
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    @OnClick(value = {R.id.tvAll, R.id.tvPay, R.id.tvDelivery, R.id.tvRecept, R.id.btnBackTop})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tvAll:
                onPageChange(0);
                onPullDownToRefresh(pullToRefreshRecycleView);
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                break;
            case R.id.tvPay:
                onPageChange(1);
                onPullDownToRefresh(pullToRefreshRecycleView);
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                break;
            case R.id.tvDelivery:
                onPageChange(2);
                onPullDownToRefresh(pullToRefreshRecycleView);
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                break;
            case R.id.tvRecept:
                onPageChange(3);
                onPullDownToRefresh(pullToRefreshRecycleView);
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                break;
            case R.id.btnBackTop:
                pullToRefreshRecycleView.getRefreshableView().scrollTo(0, 0);
                break;
        }
    }

    private void onPageChange(int index) {
        switch (index) {
            case 0:
                state = 5;
                break;
            case 1:
                state = 2;
                break;
            case 2:
                state = 1;
                break;
            case 3:
                state = 3;
                break;
        }
        tvAll.setTextColor(index == 0 ? red : black);
        vAll.setBackgroundColor(index == 0 ? red : white);

        tvPay.setTextColor(index == 1 ? red : black);
        vPay.setBackgroundColor(index == 1 ? red : white);

        tvDelivery.setTextColor(index == 2 ? red : black);
        vDelivery.setBackgroundColor(index == 2 ? red : white);

        tvRecept.setTextColor(index == 3 ? red : black);
        vRecept.setBackgroundColor(index == 3 ? red : white);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        if (member == null) return;
        curpage = 1;
        orderListRequest.getList(member, state, curpage);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (member == null) return;
        curpage++;
        orderListRequest.getList(member, state, curpage);
    }

    @Override
    public void onDutyOrderList(List<DutyOrder> list) {
        pullToRefreshRecycleView.onRefreshComplete();
        if (list != null && list.size() > 0) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            if (curpage == 1) {
                pullToRefreshRecycleView.resetPull(PullToRefreshBase.Mode.BOTH);
                adapter.setOrderList(list);
            } else {
                adapter.addOrderList(list);
            }
        } else {
            if (curpage == 1) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            } else {
                pullToRefreshRecycleView.showNoMore();
                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            }
        }
    }

    @Override
    public void onCancel(DutyOrder order, int section) {
        if (member == null || order == null) return;
        new DutyfreeBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    onPullDownToRefresh(pullToRefreshRecycleView);
                }
            }
        }).cancelOrder(member, order.getOrder_id());
    }

    @Override
    public void onDelete(DutyOrder order, final int section) {
        if (member == null || order == null) return;
        new DutyfreeBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    adapter.removeItem(section);
                }
            }
        }).delOrder(member, order.getOrder_id());

    }

    @Override
    public void onPay(DutyOrder order, int section) {
        if (member == null || order == null) return;
        PayEntity entity = new PayEntity();
        entity.pay_sn = order.getPay_sn();//交易单编号
        entity.lastClassName = OrderListActivity.class.getName();
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

    @Override
    public void onReceive(DutyOrder order, int section) {
        if (member == null || order == null) return;
        new DutyfreeBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    onPullDownToRefresh(pullToRefreshRecycleView);
                }
            }
        }).receiveAllOrder(member, order.getOrder_id());
    }

    @Override
    public void onDetail(DutyOrder order, int section) {
        if (order == null) return;
        BaseFunc.gotoActivity4Result(this, OrderDetailActivity.class, order.getOrder_id(), REQPAYORDER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQPAYCODE:
                onPullDownToRefresh(pullToRefreshRecycleView);
                break;
            case REQPAYORDER:
                onPullDownToRefresh(pullToRefreshRecycleView);
                break;
        }
    }
}
