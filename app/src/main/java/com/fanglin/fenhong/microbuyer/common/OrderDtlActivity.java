package com.fanglin.fenhong.microbuyer.common;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.OrderDtl;
import com.fanglin.fenhong.microbuyer.base.model.OrderList;
import com.fanglin.fenhong.microbuyer.base.model.PayEntity;
import com.fanglin.fenhong.microbuyer.base.model.PaySuccessBonus;
import com.fanglin.fenhong.microbuyer.base.model.ReceiveOrder;
import com.fanglin.fenhong.microbuyer.common.adapter.OrderDtlAdapter;
import com.fanglin.fenhong.microbuyer.microshop.LayoutOrderSuccess;
import com.fanglin.fhlib.other.CountDown;
import com.fanglin.fhlib.other.FHLib;
import com.fanglin.fhui.FHHintDialog;
import com.google.gson.Gson;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 作者： Created by Plucky on 15-9-28.
 * modify by lizhixin on 2016/03/11
 */
public class OrderDtlActivity extends BaseFragmentActivityUI implements LayoutEvaluateTips.EvaluateTipsCallBack, PaySuccessBonus.PSBModelCallBack {

    private static final int REQPAYCODE = 123;

    @ViewInject(R.id.rcv)
    RecyclerView rcv;
    @ViewInject(R.id.tv_money)
    TextView tv_money;
    @ViewInject(R.id.tv_num)
    TextView tv_num;
    @ViewInject(R.id.tv_freight)
    TextView tv_freight;
    @ViewInject(R.id.tv_yellow)
    TextView tv_yellow;
    @ViewInject(R.id.tv_red)
    TextView tv_red;
    @ViewInject(R.id.LActiton)
    LinearLayout LActiton;
    @ViewInject(R.id.btn_bonus)
    Button btn_bonus;
    OrderDtl orderDtl;
    OrderDtlAdapter adapter;
    LayoutOrderCancel layoutOrderCancel;
    RecyclerViewHeader rcvheader;
    CountDown cd;
    LayoutEvaluateTips layoutEvaluateTips;
    //标记是否是退货列表
    String sdesc, scode;
    int desccolor;
    LayoutOrderSuccess layoutOrderSuccess;
    PaySuccessBonus paySuccessBonusReq;
    LayoutMoreVertical layoutMoreVertical;//更多按钮弹框
    private String order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_orderdtl, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        try {
            order_id = getIntent().getStringExtra("VAL");
            if (getIntent().hasExtra("STATUES_DESC") && getIntent().hasExtra("STATUES_COLOR") && getIntent().hasExtra("STATUES_CODE")) {
                scode = getIntent().getStringExtra("STATUES_CODE");
                sdesc = getIntent().getStringExtra("STATUES_DESC");
                String scolor = getIntent().getStringExtra("STATUES_COLOR");
                desccolor = Color.parseColor(scolor);
            }
        } catch (Exception e) {
            sdesc = null;
            desccolor = Color.parseColor("#333");
        }
        initView();
    }

    private void initView() {
        LActiton.setVisibility(View.GONE);
        rcv.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new OrderDtlAdapter(mContext);
        rcv.setAdapter(adapter);

        rcvheader = RecyclerViewHeader.fromXml(mContext, R.layout.layout_orderdtl_header);
        rcvheader.attachTo(rcv);
        rcvheader.setVisibility(View.GONE);

        setHeadTitle(R.string.order_dtl);

        //初始化更多按钮弹框
        layoutMoreVertical = new LayoutMoreVertical(vBottomLine);

        enableIvMore(0);

        layoutOrderCancel = new LayoutOrderCancel(mContext);
        layoutOrderCancel.setCallBack(new LayoutOrderCancel.OrderCancelCallBack() {
            @Override
            public void onSuccess() {
                BaseFunc.showMsg(mContext, getString(R.string.op_success));
                finish();
            }

            @Override
            public void onError() {

            }
        });

        layoutEvaluateTips = new LayoutEvaluateTips(mContext);
        layoutEvaluateTips.setCallBack(this);

        paySuccessBonusReq = new PaySuccessBonus();
        paySuccessBonusReq.setModelCallBack(this);
        paySuccessBonusReq.setIfShowMsg(false);
        layoutOrderSuccess = new LayoutOrderSuccess(btn_bonus);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void onPSBData(PaySuccessBonus PSBData) {
        if (PSBData != null) {
            btn_bonus.setVisibility(View.VISIBLE);
            btn_bonus.setTag(PSBData);
        } else {
            btn_bonus.setVisibility(View.INVISIBLE);
            btn_bonus.setTag(null);
        }
    }

    @Override
    public void onPSBError(String errcode) {
        btn_bonus.setVisibility(View.INVISIBLE);
        btn_bonus.setTag(null);
    }

    @OnClick(value = {R.id.btn_bonus})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bonus:
                if (layoutOrderSuccess != null) {
                    if (btn_bonus.getTag() != null) {
                        PaySuccessBonus psb = (PaySuccessBonus) btn_bonus.getTag();
                        layoutOrderSuccess.show(psb);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getdata();
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
                    getdata();
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

    @Override
    public void finish() {
        super.finish();
        if (cd != null) cd.cancel();
    }

    private void getdata() {
        if (member == null || order_id == null) return;
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
                    try {
                        orderDtl = new Gson().fromJson(data, OrderDtl.class);
                        adapter.setList(orderDtl);
                        rcv.getAdapter().notifyDataSetChanged();
                        attachHeader(orderDtl);
                        parseButton();
                        calculate();
                    } catch (Exception e) {
                        //
                    }
                } else {
                    refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
                }
            }
        }).get_order_detail(member.member_id, member.token, order_id);
    }

    private void attachHeader(OrderDtl dtl) {
        if (dtl == null || dtl.reciver_info == null) return;
        rcvheader.setVisibility(View.VISIBLE);
        TextView tv_order_state = (TextView) rcvheader.findViewById(R.id.tv_order_state);
        TextView tv_order_id = (TextView) rcvheader.findViewById(R.id.tv_order_id);
        TextView tv_order_id_copy = (TextView) rcvheader.findViewById(R.id.tv_order_id_copy);
        final TextView tv_red_header = (TextView) rcvheader.findViewById(R.id.tv_red);
        final TextView tv_right_header = (TextView) rcvheader.findViewById(R.id.tv_right);

        if (!TextUtils.isEmpty(sdesc)) {
            if (TextUtils.equals("7", scode)) {
                tv_order_state.setText(sdesc);
                tv_order_state.setTextColor(desccolor);
            } else {
                tv_order_state.setText(getString(R.string.customer_service_ing));
            }
        } else {
            tv_order_state.setText(Html.fromHtml(dtl.state_desc));
        }

        tv_order_id.setText(dtl.order_sn);
        tv_order_id_copy.setOnClickListener(new View.OnClickListener() {
            private String order_sn;

            View.OnClickListener init(String order_sn) {
                this.order_sn = order_sn;
                return this;
            }

            @Override
            public void onClick(View v) {
                FHLib.copy(mContext, order_sn);
                BaseFunc.showMsg(mContext, getString(R.string.already_copy));
            }
        }.init(dtl.order_sn));

        if (dtl.order_state == 10) {
            tv_red_header.setVisibility(View.GONE);//
            if (dtl.validity_pay_time > 0) {
                if (cd != null) {
                    /** 如果是重返界面 则释放并重新创建*/
                    cd.cancel();
                    cd = null;
                }
                cd = new CountDown(dtl.validity_pay_time);
                cd.start(new CountDown.CountDownListener() {
                    @Override
                    public void onChange(long atime) {
                        String fmt = getString(R.string.tips_order);
                        fmt = String.format(fmt, BaseFunc.getCNTimeByTimeStamp(atime));
                        tv_right_header.setText(Html.fromHtml(fmt));
                    }

                    @Override
                    public void onStop() {
                        tv_right_header.setText(getString(R.string.tips_order_auto_cancel));
                        tv_red.setEnabled(false);
                    }
                });
            } else {
                tv_right_header.setText(getString(R.string.tips_order_auto_cancel));
                tv_red.setEnabled(false);
            }
        } else {
            if (cd != null) {
                /** 如果是重返界面 则释放并重新创建*/
                cd.cancel();
                cd = null;
            }
            tv_red_header.setVisibility(View.VISIBLE);
            tv_red_header.setText(dtl.payment_name);
            tv_right_header.setText(FHLib.getTimeStrByTimestamp(dtl.add_time));
        }

        TextView tv_name = (TextView) rcvheader.findViewById(R.id.tv_name);
        TextView tv_phone = (TextView) rcvheader.findViewById(R.id.tv_phone);
        TextView tv_addr = (TextView) rcvheader.findViewById(R.id.tv_addr);

        tv_name.setText(dtl.reciver_info.reciver_name);
        tv_phone.setText(BaseFunc.getMaskMobile(dtl.reciver_info.mob_phone));
        tv_addr.setText(dtl.reciver_info.address);

        BaseFunc.setFont(rcvheader);
        tv_name.setTypeface(null);
        tv_addr.setTypeface(null);

        //处理红包显示及分享逻辑 -- 只有付款完成之后的订单才显示该按钮 --退货不显示该按钮
        if (dtl.getVisiable(!TextUtils.isEmpty(sdesc))) {
            paySuccessBonusReq.getData(member, dtl.pay_sn);
        } else {
            btn_bonus.setVisibility(View.GONE);
        }
    }

    private void calculate() {
        if (orderDtl == null) return;
        String fmt;
        double money = orderDtl.real_amount;//实际交易金额
        if (orderDtl.order_state > 10) {//未付款
            fmt = String.format(getString(R.string.count_ordermoney), String.valueOf(money));
        } else {
            fmt = String.format(getString(R.string.count_ordermoney_topay), String.valueOf(money));
        }

        int num = orderDtl.getGoodsNum();
        double freight = orderDtl.shipping_fee;
        tv_money.setText(fmt);
        tv_num.setText(String.format(getString(R.string.count_num), num));
        tv_freight.setText(String.format(getString(R.string.count_fee), String.valueOf(freight)));
    }


    /**
     * 根据订单状态,处理两个按钮的业务逻辑
     */
    private void parseButton() {
        if (orderDtl == null) return;
        LActiton.setVisibility(View.VISIBLE);
        int order_state = orderDtl.order_state;
        long _validity_pay_time = orderDtl.validity_pay_time;
        String _evaluation_state = orderDtl.evaluation_state;

        switch (order_state) {
            case 0://已取消
                tv_yellow.setVisibility(View.VISIBLE);
                tv_yellow.setSelected(false);
                tv_yellow.setText(OrderList.getOrderStateDesc(order_state, _validity_pay_time, _evaluation_state)[1]);
                tv_red.setVisibility(View.GONE);
                tv_yellow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDelete(orderDtl.order_id);
                    }
                });
                break;
            case 10://未付款
                tv_yellow.setVisibility(View.VISIBLE);
                tv_yellow.setSelected(false);
                tv_red.setVisibility(View.VISIBLE);
                tv_yellow.setText(OrderList.getOrderStateDesc(order_state, _validity_pay_time, _evaluation_state)[1]);
                tv_red.setText(OrderList.getOrderStateDesc(order_state, _validity_pay_time, _evaluation_state)[2]);
                tv_yellow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCancel(orderDtl);
                    }
                });
                tv_red.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPay();

                    }
                });
                break;
            case 20://已付款
                tv_yellow.setVisibility(View.GONE);
                tv_red.setVisibility(View.VISIBLE);
                tv_red.setText(OrderList.getOrderStateDesc(order_state, _validity_pay_time, _evaluation_state)[2]);
                tv_red.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDelivery();
                    }
                });
                break;
            case 30://已发货
                tv_yellow.setVisibility(View.VISIBLE);
                tv_yellow.setSelected(true);
                tv_red.setVisibility(View.VISIBLE);
                tv_yellow.setText(OrderList.getOrderStateDesc(order_state, _validity_pay_time, _evaluation_state)[1]);
                tv_red.setText(OrderList.getOrderStateDesc(order_state, _validity_pay_time, _evaluation_state)[2]);
                tv_yellow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDelivery();
                    }
                });
                tv_red.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSubmit(orderDtl.order_id);
                    }
                });
                break;
            case 40://已收货
                tv_yellow.setVisibility(View.VISIBLE);
                tv_yellow.setText(OrderList.getOrderStateDesc(order_state, _validity_pay_time, _evaluation_state)[1]);
                tv_red.setText(OrderList.getOrderStateDesc(order_state, _validity_pay_time, _evaluation_state)[2]);
                if (TextUtils.equals(orderDtl.evaluation_state, "0")) {
                    tv_yellow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onDelivery();
                        }
                    });
                    tv_red.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BaseFunc.gotoActivity(mContext, EvaluateBeforeActivity.class, order_id);//去评价
                        }
                    });
                } else if (TextUtils.equals(orderDtl.evaluation_state, "1")) {
                    tv_yellow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onDelivery();
                        }
                    });
                    tv_red.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BaseFunc.gotoActivity(mContext, GoodsEvaluateActivity.class, order_id);//追加评价
                        }
                    });
                } else {
                    tv_yellow.setText(getString(R.string.delete_order));
                    tv_yellow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onDelete(orderDtl.order_id);
                        }
                    });
                    tv_red.setText(getString(R.string.express_info));
                    tv_red.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onDelivery();//查看物流
                        }
                    });
                }
                break;
            case 50://已失败
                tv_yellow.setVisibility(View.VISIBLE);
                tv_yellow.setText(OrderList.getOrderStateDesc(order_state, _validity_pay_time, _evaluation_state)[1]);
                tv_red.setVisibility(View.GONE);
                tv_yellow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDelete(orderDtl.order_id);
                    }
                });
                break;
        }

        if (!TextUtils.equals("0", orderDtl.delete_state)) {
            tv_yellow.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQPAYCODE:
                getdata();
                break;
        }
    }

    private void onPay() {
        if (orderDtl == null) return;

        if (orderDtl.getOrderGoodsState() == 0) {
            //存在无效商品, 给予提示并结束支付
            BaseFunc.showMsg(mContext, getString(R.string.order_checkout_valid));
            return;
        }

        PayEntity entity = new PayEntity();
        entity.pay_sn = orderDtl.pay_sn;//交易单编号
        entity.lastClassName = this.getClass().getName();
        entity.gc_area = orderDtl.country_source;
        entity.order_custom = orderDtl.order_custom;//海关编码
        entity.pay_amount = orderDtl.real_amount;//实际交易金额
        entity.payment_list = orderDtl.payment_list;//支付方式
        BaseFunc.gotoPayActivity(this, entity, REQPAYCODE);
    }

    private void onDelete(final String orderid) {
        if (member == null) return;

        final FHHintDialog fhd = new FHHintDialog(OrderDtlActivity.this);
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
                            BaseFunc.showMsg(mContext, getString(R.string.op_success));
                            finish();
                        }
                    }
                }).delete_order(member.member_id, member.token, orderid);
            }
        });
        fhd.setTvContent(getString(R.string.tips_delete_order));
        fhd.show();
    }

    private void onDelivery() {
        if (orderDtl == null) return;

        BaseFunc.gotoActivity(mContext, FHExpressActivity.class, order_id);
    }

    private void onSubmit(String orderid) {
        layoutEvaluateTips.show(orderid);
    }

    private void onCancel(OrderDtl orderDtl) {
        if (orderDtl == null) return;
        if (orderDtl.getUse_limit() > 0) {
            confirmCancelWhenUseBonus(orderDtl.order_id);
        } else {
            layoutOrderCancel.show(member, orderDtl.order_id);
        }
    }

    private void confirmCancelWhenUseBonus(final String orderid) {
        if (member == null) return;

        final FHHintDialog fhd = new FHHintDialog(OrderDtlActivity.this);
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
    public void onivMoreClick() {
        super.onivMoreClick();
        layoutMoreVertical.show();
    }
}
