package com.fanglin.fenhong.microbuyer.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.OrderList;
import com.fanglin.fenhong.microbuyer.base.model.PayEntity;
import com.fanglin.fenhong.microbuyer.common.adapter.IconAnimator;
import com.fanglin.fenhong.microbuyer.common.adapter.OrderMergeAdapter;
import com.fanglin.fhui.FHHintDialog;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import java.util.List;
import it.carlom.stikkyheader.core.StikkyHeaderBuilder;

/**
 * 作者： Created by Plucky on 2015/10/13.
 */
public class OrderMergeActivity extends BaseFragmentActivityUI implements OrderList.OrderModelCallBack, PullToRefreshBase.OnRefreshListener {

    @ViewInject(R.id.pullToRefreshListView)
    PullToRefreshListView pullToRefreshListView;
    @ViewInject(R.id.mheader)
    LinearLayout mheader;
    @ViewInject(R.id.LBanner)
    LinearLayout LBanner;

    @ViewInject(R.id.tv_name)
    TextView tv_name;
    @ViewInject(R.id.tv_phone)
    TextView tv_phone;
    @ViewInject(R.id.tv_addr)
    TextView tv_addr;
    @ViewInject(R.id.tv_money)
    TextView tv_money;
    @ViewInject(R.id.tv_red)
    TextView tv_red;


    OrderList orderList;
    String pay_sn;
    int state;
    OrderMergeAdapter adapter;

    //取消订单
    LayoutOrderCancel layoutOrderCancel;
    private static final int REQPAYCODE = 123;
    @ViewInject(R.id.tv_msg)
    TextView tv_msg;
    @ViewInject(R.id.LActiton)
    LinearLayout LActiton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_ordermerge, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        try {
            OrderList ol = new Gson().fromJson(getIntent().getStringExtra("VAL"), OrderList.class);
            pay_sn = ol.pay_sn;
            state = ol.order_state;
        } catch (Exception e) {
            pay_sn = null;
            state = -1;
        }

        if (pay_sn == null || state == -1) {
            BaseFunc.showMsg(mContext, getString(R.string.invalid_data));
            finish();
            return;
        }
        initView();
    }

    private void initView() {
        setHeadTitle(R.string.order_merge);
        tv_red.setEnabled(false);
        tv_msg.setVisibility(View.GONE);
        BaseFunc.setFont(LBanner);
        tv_name.setTypeface(null);
        tv_addr.setTypeface(null);

        LActiton.setVisibility(View.GONE);

        layoutOrderCancel = new LayoutOrderCancel(mContext);
        layoutOrderCancel.setCallBack(new LayoutOrderCancel.OrderCancelCallBack() {
            @Override
            public void onSuccess() {
                BaseFunc.showMsg(mContext, getString(R.string.op_success));
                onRefresh(pullToRefreshListView);
            }

            @Override
            public void onError() {
                BaseFunc.showMsg(mContext, getString(R.string.op_error));
            }
        });

        pullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
        pullToRefreshListView.setOnRefreshListener(this);
        pullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshListView.setShowIndicator(false);

        StikkyHeaderBuilder.stickTo(pullToRefreshListView.getRefreshableView()).setHeader(mheader).minHeightHeaderDim(R.dimen.line_width_1).animator(new IconAnimator(LBanner)).build();

        orderList = new OrderList();
        orderList.setModelCallBack(this);
        adapter = new OrderMergeAdapter(mContext);
        adapter.setCallBack(new OrderMergeAdapter.OrderMergeAdapterCallBack() {
            @Override
            public void onCancel(int position) {
                /** 取消订单*/
                OrderList alist = adapter.getItem(position);
                if (alist == null) return;
                if (alist.getCoupon_amount() > 0) {
                    confirmCancelWhenUseBonus(alist.order_id);
                } else {
                    layoutOrderCancel.show(member, alist.order_id);
                }
            }
        });
        pullToRefreshListView.setAdapter(adapter);
        onRefresh(pullToRefreshListView);
    }

    private void confirmCancelWhenUseBonus(final String orderid) {
        if (member == null) return;

        final FHHintDialog fhd = new FHHintDialog(OrderMergeActivity.this);
        fhd.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
                layoutOrderCancel.show(member, orderid);
            }
        });
        fhd.setTvContent(getString(R.string.tips_cancel_order_usebonus));
        fhd.show();
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (member == null) return;
        orderList.get_order_list(member.member_id, member.token, pay_sn, state, 1);
    }

    /*数据处理结果*/
    @Override
    public void onOrderError(String errcode) {
        pullToRefreshListView.onRefreshComplete();
        finish();
    }

    @Override
    public void onOrderList(List<OrderList> list) {
        pullToRefreshListView.onRefreshComplete();
        adapter.setList(list);
        adapter.notifyDataSetChanged();
        if (list != null && list.size() > 0) {

            final OrderList o = list.get(0);
            String str;
            if (o.order_state > 10) {//未付款
                str = String.format(getString(R.string.count_ordermoney), String.valueOf(o.all_real_amount));
            } else {
                str = String.format(getString(R.string.count_ordermoney_topay), String.valueOf(o.all_real_amount));
            }
            tv_money.setText(str);

            if (o.reciver_info != null) {
                tv_name.setText(o.reciver_info.reciver_name);
                tv_phone.setText(o.reciver_info.mob_phone);
                tv_addr.setText(o.reciver_info.address);
            }

            parseOrder(o);

            int goods_valid = 1;//默认有效
            for (OrderList order : list) {
                if (order.getOrderGoodsState() == 0) {
                    //存在无效商品
                    goods_valid = 0;
                    break;
                }
            }
            final int finalGoods_valid = goods_valid;

            tv_red.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPay(o.pay_sn, o.all_real_amount, o.country_source, o.order_custom, finalGoods_valid, o.payment_list);
                }
            });
        } else {
            finish();
        }
    }

    private void parseOrder(OrderList order) {
        if (order != null) {
            /*只有是待付款的订单 且在有效期的订单 才能支付*/
            if (order.order_state == 10) {
                LActiton.setVisibility(View.VISIBLE);
                if (order.validity_pay_time > 0) {
                    tv_msg.setVisibility(View.GONE);
                    tv_red.setEnabled(true);
                } else {
                    tv_msg.setVisibility(View.VISIBLE);
                    tv_red.setEnabled(false);
                }
            } else {
                LActiton.setVisibility(View.GONE);
                tv_msg.setVisibility(View.GONE);
            }
        } else {
            LActiton.setVisibility(View.GONE);
            tv_msg.setVisibility(View.GONE);
            tv_red.setEnabled(false);
        }
    }

    private void onPay(String pay_sn, double pay_amount, int country_source, int order_custom, int goods_valid, List payment_list) {
        if (goods_valid == 0) {
            //提示并结束支付
            BaseFunc.showMsg(mContext, getString(R.string.order_checkout_valid));
            return;
        }

        PayEntity entity = new PayEntity();
        entity.pay_sn = pay_sn;//交易单编号
        entity.lastClassName = this.getClass().getName();
        entity.gc_area = country_source;
        entity.order_custom = order_custom;//海关编码
        entity.pay_amount = pay_amount;//交易金额
        entity.payment_list = payment_list;
        BaseFunc.gotoPayActivity(this, entity, REQPAYCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQPAYCODE:
                onRefresh(pullToRefreshListView);
                break;
        }
    }
}
