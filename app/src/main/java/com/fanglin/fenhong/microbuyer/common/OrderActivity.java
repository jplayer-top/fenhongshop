package com.fanglin.fenhong.microbuyer.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.listener.OrderCallBack;
import com.fanglin.fenhong.microbuyer.base.baselib.VarInstance;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.OrderList;
import com.fanglin.fenhong.microbuyer.base.model.PayEntity;
import com.fanglin.fenhong.microbuyer.base.model.ReceiveOrder;
import com.fanglin.fenhong.microbuyer.common.adapter.OrderSectionAdapter;
import com.fanglin.fhlib.other.FHLib;
import com.fanglin.fhui.FHHintDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/24.
 * modify by lizhixin on 2016/03/11
 */
public class OrderActivity extends BaseFragmentActivityUI implements OrderCallBack, OrderList.OrderModelCallBack, LayoutEvaluateTips.EvaluateTipsCallBack, PullToRefreshBase.OnRefreshListener2 {
    public static final int REQDTLCODE = 125;
    private static final int REQPAYCODE = 123;
    private static final int REQEVACODE = 124;

    @ViewInject(R.id.LGrp)
    LinearLayout LGrp;
    int index = 0;
    int curpage = 0;
    int state = 0;

    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;

    @ViewInject(R.id.LNoresult)
    LinearLayout LNoresult;
    OrderSectionAdapter adapter;
    OrderList orderList;
    LayoutEvaluateTips layoutEvaluateTips;

    LayoutMoreVertical layoutMoreVertical;//更多按钮弹框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_orders, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);

        try {
            String val = getIntent().getStringExtra("VAL");
            parseIndex(val);
        } catch (Exception e) {
            index = 0;
        }
        initView();
    }

    private void parseIndex(String val) {
        if (TextUtils.equals(val, "0")) {
            index = 0;
        }
        if (TextUtils.equals(val, "10")) {
            index = 1;
        }
        if (TextUtils.equals(val, "20")) {
            index = 2;
        }
        if (TextUtils.equals(val, "30")) {
            index = 3;
        }
        if (TextUtils.equals(val, "100")) {
            index = 4;
        }
    }

    private void parseState(int index) {
        switch (index) {
            case 0:
                state = 0;
                break;
            case 1:
                state = 10;
                break;
            case 2:
                state = 20;
                break;
            case 3:
                state = 30;
                break;
            case 4:
                state = 100;
                break;
        }
    }

    /**
     * 初始化
     */
    private void initView() {
        setHeadTitle(R.string.orders);

        //初始化更多按钮弹框
        layoutMoreVertical = new LayoutMoreVertical(vBottomLine);

        enableIvMore(0);

        adapter = new OrderSectionAdapter(mContext);
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(new LinearLayoutManager(mContext));
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);
        adapter.setCallBack(this);

        layoutEvaluateTips = new LayoutEvaluateTips(mContext);
        layoutEvaluateTips.setCallBack(this);

        orderList = new OrderList();
        orderList.setModelCallBack(this);

        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshRecycleView.setOnRefreshListener(this);

        changeStatu(index);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        pullToRefreshRecycleView.setVisibility(View.VISIBLE);
        LNoresult.setVisibility(View.GONE);
        curpage = 1;
        getData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (msgnum != null && msgnum.getTotalNum() > 0) {
            enableMsgDot(true);
            layoutMoreVertical.setMsgNum(msgnum.getTotalNum());
        } else {
            enableMsgDot(false);
            layoutMoreVertical.setMsgNum(0);
        }
    }

    /**
     * 确认收货代理
     */
    @Override
    public void onLETEnd(final String order_id) {

        new ReceiveOrder().receive_order(mContext, member, order_id, new ReceiveOrder.ROModelCallBack() {
            @Override
            public void onError(boolean flag) {
                if (flag) {
                    onPullDownToRefresh(pullToRefreshRecycleView);
                    LayoutEvaluateTips.confirm2Evaluate(mContext, order_id);
                } else {
                    BaseFunc.showMsg(mContext, getString(R.string.op_error));
                }
            }
        });
    }

    /**
     * 取消确认收货对话框
     */
    @Override
    public void onLETCancel() {
    }

    private void changeStatu(int index) {
        for (int i = 0; i < 5; i++) {
            LGrp.getChildAt(i).setSelected(false);
        }
        LGrp.getChildAt(index).setSelected(true);
        parseState(index);
        adapter.setList(null);
        pullToRefreshRecycleView.getRefreshableView().getAdapter().notifyDataSetChanged();
        onPullDownToRefresh(pullToRefreshRecycleView);
    }


    @OnClick(value = {R.id.LAll, R.id.LPay, R.id.LRelease, R.id.LDelivery, R.id.LEvaluate, R.id.LNoresult})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.LAll:
                changeStatu(0);
                break;
            case R.id.LPay:
                changeStatu(1);
                break;
            case R.id.LRelease:
                changeStatu(2);
                break;
            case R.id.LDelivery:
                changeStatu(3);
                break;
            case R.id.LEvaluate:
                changeStatu(4);
                break;
            case R.id.LNoresult:
                onPullDownToRefresh(pullToRefreshRecycleView);
                break;
            default:
                break;
        }
    }

    @Override
    public void onivMoreClick() {
        super.onivMoreClick();
        layoutMoreVertical.show();
    }

    private void getData() {
        if (member == null) return;
        FHLib.EnableViewGroup(LGrp, false);//禁止点击--请求过程中禁止访问
        orderList.get_order_list(member.member_id, member.token, null, state, curpage);
    }

    @Override
    public void onOrderError(String errcode) {
        pullToRefreshRecycleView.onRefreshComplete();
        refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        if (curpage > 1) {
            pullToRefreshRecycleView.getLoadingLayoutProxy().setPullLabel(getString(R.string.pullup_error_tips));
        } else {
            //显示无商品的提示
            pullToRefreshRecycleView.setVisibility(View.GONE);
            adapter.setList(null);
            pullToRefreshRecycleView.getRefreshableView().getAdapter().notifyDataSetChanged();
            LNoresult.setVisibility(View.VISIBLE);
        }
        FHLib.EnableViewGroup(LGrp, true);//执行完成之后,允许点击
    }

    @Override
    public void onOrderList(final List<OrderList> list) {
        pullToRefreshRecycleView.onRefreshComplete();
        refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        FHLib.EnableViewGroup(LGrp, true);//执行完成之后,允许点击
        if (curpage > 1) {

            if (list != null && list.size() > 0) {
                adapter.addList(list);
                adapter.notifyDataSetChanged();
                pullToRefreshRecycleView.onAppendData();
            } else {
                pullToRefreshRecycleView.getLoadingLayoutProxy().setPullLabel(getString(R.string.pullup_error_tips));
            }
        } else {
            if (list != null && list.size() > 0) {
                adapter.setList(list);
                adapter.notifyDataSetChanged();
                pullToRefreshRecycleView.setVisibility(View.VISIBLE);
                LNoresult.setVisibility(View.GONE);
            } else {
                //显示无商品的提示
                pullToRefreshRecycleView.setVisibility(View.GONE);
                LNoresult.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQPAYCODE:
                onPullDownToRefresh(pullToRefreshRecycleView);
                break;
            case REQEVACODE:
                onPullDownToRefresh(pullToRefreshRecycleView);
                break;
            case REQDTLCODE:
                onPullDownToRefresh(pullToRefreshRecycleView);
                break;
        }
    }

    @Override
    public void onPay(int position) {
        PayEntity entity=new PayEntity();
        entity.pay_sn= adapter.getItem(position).pay_sn;//交易单编号
        entity.lastClassName= OrderActivity.class.getName();
        entity.gc_area= adapter.getItem(position).country_source;
        entity.order_custom= adapter.getItem(position).order_custom;//海关编码
        entity.pay_amount= adapter.getItem(position).getPayMoney();//实际交易金额
        entity.payment_list= adapter.getItem(position).payment_list;//支付方式
        BaseFunc.gotoPayActivity(this,entity, REQPAYCODE);
    }

    @Override
    public void onDelete(final int position) {
        if (member == null) return;

        final FHHintDialog fhd = new FHHintDialog(OrderActivity.this);
        fhd.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
                    @Override
                    public void onStart(String data) {
                    }

                    @Override
                    public void onEnd(boolean isSuccess, String data) {
                        if (isSuccess) {
                            onPullDownToRefresh(pullToRefreshRecycleView);
                            VarInstance.getInstance().showMsg(R.string.delete_success);
                        }
                    }
                }).delete_order(member.member_id, member.token, adapter.getItem(position).order_id);
            }
        });
        fhd.setTvContent(getString(R.string.tips_delete_order));
        fhd.show();
    }

    @Override
    public void onDelivery(int position) {
        BaseFunc.gotoActivity(mContext, FHExpressActivity.class, adapter.getItem(position).order_id);
    }

    @Override
    public void onSubmit(int position) {
        layoutEvaluateTips.show(adapter.getItem(position).order_id);
    }

    @Override
    public void onEvaluate(int position, int type) {
        if (type == 1) {
            //追加评价
            BaseFunc.gotoActivity4Result(this, GoodsEvaluateActivity.class, adapter.getItem(position).order_id, REQEVACODE);
        } else {
            //去评价
            BaseFunc.gotoActivity4Result(this, EvaluateBeforeActivity.class, adapter.getItem(position).order_id, REQEVACODE);
        }
    }

    @Override
    public void onCancel(int position) {

    }
}
