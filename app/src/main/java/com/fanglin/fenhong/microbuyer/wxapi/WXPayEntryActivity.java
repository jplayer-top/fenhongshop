package com.fanglin.fenhong.microbuyer.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.event.DutyfreeVipEvent;
import com.fanglin.fenhong.microbuyer.base.model.PayEntity;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyTopUp;
import com.fanglin.fenhong.microbuyer.common.OrderActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.DutyPayOfflineActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.OrderListActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.adapter.VipBuyerPayAdapter;
import com.fanglin.fhlib.other.FHLib;
import com.fanglin.fhui.FHHintDialog;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wxpay.WXConstants;
import com.wxpay.WxOrderEntity;
import com.wxpay.WxPayUtil;

import java.util.Map;

import de.greenrobot.event.EventBus;

public class WXPayEntryActivity extends FHPayActivity implements IWXAPIEventHandler, DutyTopUp.DutyTopUpRequestCallBack, VipBuyerPayAdapter.VipPayListener {

    private IWXAPI api;
    //普通支付界面元素
    @ViewInject(R.id.vNormalPay)
    View vNormalPay;
    @ViewInject(R.id.ivAli)
    ImageView ivAli;
    @ViewInject(R.id.ivJD)
    ImageView ivJD;
    @ViewInject(R.id.ivWx)
    ImageView ivWx;
    @ViewInject(R.id.ivUnion)
    ImageView ivUnion;
    @ViewInject(R.id.LPayOffline)
    LinearLayout LPayOffline;
    @ViewInject(R.id.vPayoff)
    View vPayoff;
    //VIP买手支付页面元素
    @ViewInject(R.id.vTopUp)
    View vTopUp;
    MultiStateView iStateView;
    @ViewInject(R.id.tvID)
    TextView tvID;
    @ViewInject(R.id.tvMoney)
    TextView tvMoney;
    @ViewInject(R.id.ivVIPSeason)
    ImageView ivVIPSeason;
    @ViewInject(R.id.ivVIPYear)
    ImageView ivVIPYear;
    @ViewInject(R.id.recyclerView)
    RecyclerView recyclerView;


    @ViewInject(R.id.LBOT)
    LinearLayout LBOT;

    int paytype = 0;// 支付方式 0 支付宝 1京东  2微信 3银联
    int payresult = -2;//支付结果 0 成功  -1 失败 -2 取消 -3 签名失败
    Animation animIn, animOut;

    LinearLayout.LayoutParams imgParams;

    WXConstants wxconst;
    //VIP买手网络请求
    DutyTopUp dutyTopUpReq;
    DutyTopUp topUpData;

    VipBuyerPayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ViewUtils.inject(this);

        //ViewUtils在注解的时候 如果注解的是同一个对象，则只有第一个是对的，其余的为null
        iStateView = (MultiStateView) vTopUp;

        wxconst = new WXConstants(BaseVar.HOST);
        wxconst.APP_ID = getString(R.string.WX_APP_ID);
        wxconst.API_KEY = BaseVar.WXO0API;
        wxconst.MCH_ID = BaseVar.WXO0MCH;

        api = WXAPIFactory.createWXAPI(this, wxconst.APP_ID);
        api.handleIntent(getIntent(), this);

        dutyTopUpReq = new DutyTopUp();
        dutyTopUpReq.setRequestCallBack(this);

        initAnimation();

        initView();
    }

    private void initView() {
        //确保部位null
        if (payEntity == null) {
            finish();
            return;
        }
        changeStatu(0);
        int w = BaseFunc.getDisplayMetrics(mContext).widthPixels;
        int offset = getResources().getDimensionPixelOffset(R.dimen.dp_of_40);
        int imgW = (w - offset) / 2;
        int imgH = imgW * 102 / 345;
        imgParams = new LinearLayout.LayoutParams(imgW, imgH);
        ivVIPSeason.setLayoutParams(imgParams);
        ivVIPYear.setLayoutParams(imgParams);

        View eptView = iStateView.getView(MultiStateView.VIEW_STATE_EMPTY);
        if (eptView != null) {
            TextView tvEmpty = (TextView) eptView.findViewById(R.id.tvEmpty);
            tvEmpty.setText("请点击重试");
            eptView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                    dutyTopUpReq.submit(member, 10);
                }
            });
        }

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        adapter = new VipBuyerPayAdapter(mContext);
        adapter.setPayListener(this);
        recyclerView.setAdapter(adapter);

        if (payEntity.vip_pay_type > 0) {
            //VIP买手支付
            vTopUp.setVisibility(View.VISIBLE);
            iStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            vNormalPay.setVisibility(View.GONE);
            //进界面就请求数据
            dutyTopUpReq.submit(member, 10);
        } else {
            //普通支付
            vTopUp.setVisibility(View.GONE);
            vNormalPay.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(payEntity.payoff)) {
                LPayOffline.setVisibility(View.VISIBLE);
                vPayoff.setVisibility(View.VISIBLE);
            } else {
                LPayOffline.setVisibility(View.GONE);
                vPayoff.setVisibility(View.GONE);
            }
        }

        LBOT.startAnimation(animIn);
    }

    private void close() {
        LBOT.startAnimation(animOut);
    }

    private void goBack() {
        if (lastActivity != null) {
            Intent ii = new Intent(mContext, lastActivity);
            ii.putExtra("payment", paytype);
            ii.putExtra("result", payresult);
            setResult(RESULT_OK, ii);
        }
        finish();
    }

    @Override
    public void onDutyTopUpData(DutyTopUp topUp) {
        topUpData = topUp;
        if (topUpData != null) {
            iStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            tvID.setText(topUpData.getBuyerName());
            tvMoney.setText(topUpData.getPayAmountDesc());
            new FHImageViewUtil(ivVIPSeason).setImageURI(topUpData.getSeasonCardImg(), FHImageViewUtil.SHOWTYPE.VIPSEASON);
            new FHImageViewUtil(ivVIPYear).setImageURI(topUpData.getYearCardImg(), FHImageViewUtil.SHOWTYPE.VIPYEAR);
            adapter.setPayments(topUpData.getPaymentList());
        } else {
            iStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    private void changeStatu(int index) {
        ivAli.setSelected(index == 0);
        ivJD.setSelected(index == 1);
        ivWx.setSelected(index == 2);
        ivUnion.setSelected(index == 3);
        paytype = index;
    }

    @OnClick(value = {R.id.LUnionpay, R.id.LWxpay, R.id.LAlipay, R.id.LJDpay, R.id.tvSubmit, R.id.vout, R.id.LPayOffline,
            R.id.ivVIPSeason, R.id.ivVIPYear})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.LAlipay:
                changeStatu(0);
                break;
            case R.id.LWxpay:
                changeStatu(2);
                break;
            case R.id.LUnionpay:
                changeStatu(3);
                break;
            case R.id.tvSubmit:
                normalPay(paytype);
                break;
            case R.id.vout:
                //如果是购买VIP买手界面则不需要弹确认框
                if (payEntity.vip_pay_type > 0) {
                    close();
                } else {
                    confirm();
                }
                break;
            case R.id.LJDpay:
                changeStatu(1);
                break;
            case R.id.LPayOffline:
                BaseFunc.gotoActivity(mContext, DutyPayOfflineActivity.class, payEntity.payoff);
                break;

            //VIP买手点击操作
            case R.id.ivVIPSeason:
                dutyTopUpReq.submit(member, 10);
                break;
            case R.id.ivVIPYear:
                dutyTopUpReq.submit(member, 20);
                break;
        }
    }

    @Override
    public void onVipPay(String payment) {
        if (topUpData == null) return;
        if (TextUtils.isEmpty(topUpData.getErrMsg())) {
            if (TextUtils.equals("alipay", payment)) {
                aliPayAction(topUpData.getPaySn(), topUpData.getPayType());
            } else if (TextUtils.equals("wxpay", payment)) {
                wxPayAction(topUpData.getSubject(), topUpData.getPayAmount(), topUpData.getPaySn());
            } else if (TextUtils.equals("jdpay", payment)) {
                jdPayAction(topUpData.getPaySn());
            } else {
                BaseFunc.showMsg(mContext, "暂不支持该支付方式");
            }
        } else {
            BaseFunc.showMsg(mContext, topUpData.getErrMsg());
        }
    }

    @Override
    public void onBackPressed() {
        //如果是购买VIP买手界面则不需要弹确认框
        if (payEntity.vip_pay_type > 0) {
            close();
        } else {
            confirm();
        }
    }

    private void confirm() {
        final FHHintDialog fhd = new FHHintDialog(WXPayEntryActivity.this);
        fhd.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
                //如果本身就在订单的页面就不需要跳转
                if (notInOrderPage()) {
                    if (isFromDutyfree()) {
                        BaseFunc.gotoActivity(mContext, OrderListActivity.class, "1");
                    } else {
                        BaseFunc.gotoActivity(mContext, OrderActivity.class, "10");
                    }
                }
                close();
            }
        });
        fhd.setTvTitle(getString(R.string.title_of_paycancel));
        fhd.setTvContent(getString(R.string.tips_of_paycancel));
        fhd.show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX && handler != null) {
            handler.sendEmptyMessage(resp.errCode);
        }
    }


    /**
     * 调起微信支付
     */
    private void doWXPay() {
        wxPayAction(payEntity.getSubject(mContext), String.valueOf(payEntity.pay_amount), payEntity.pay_sn);
    }

    /**
     * 带参数调起微信支付
     *
     * @param subject   付款内容
     * @param payAmount 金额
     * @param paySN     支付单号
     */
    private void wxPayAction(String subject, String payAmount, String paySN) {
        if (!FHLib.isAvilible(mContext, "com.tencent.mm")) {
            BaseFunc.showMsg(mContext, getString(R.string.hint_wx_notinstall));
            payresult = -2;
            return;
        }
        WxOrderEntity order1 = new WxOrderEntity();
        order1.subject = subject;
        order1.setBody(subject);
        order1.price = payAmount;
        order1.tradeno = paySN;
        new WxPayUtil(this, order1).setConstants(wxconst).setCallBack(new WxPayUtil.WxPayCallBack() {
            @Override
            public void onStart() {
                BaseFunc.showMsg(mContext, "正在建立安全链接...");
            }

            @Override
            public void onPrePayErr(Map<String, String> result) {
                if (handler != null) handler.sendEmptyMessage(-3);
            }

            @Override
            public void onPrePayOK(Map<String, String> result) {

            }
        }).pay();
    }


    private void normalPay(int index) {
        switch (index) {
            case 0:
                if (payEntity.payment_list != null && payEntity.payment_list.contains("alipay")) {//根据API返回的允许支付方式来判断
                    doAlipay();
                } else {
                    BaseFunc.showMsg(mContext, getString(R.string.no_alipay));
                }
                break;
            case 1:
                if (payEntity.payment_list != null && payEntity.payment_list.contains("jdpay")) {//根据API返回的允许支付方式来判断
                    doJDPay();
                } else {
                    BaseFunc.showMsg(mContext, getString(R.string.no_jdpay));
                }
                break;
            case 2:
                if (payEntity.payment_list != null && payEntity.payment_list.contains("wxpay")) {//根据API返回的允许支付方式来判断
                    doWXPay();
                } else {
                    BaseFunc.showMsg(mContext, getString(R.string.no_weixinpay));
                }
                break;
            case 3:
                if (payEntity.payment_list != null && payEntity.payment_list.contains("chinapay")) {//根据API返回的允许支付方式来判断
                    doUnionPay();
                } else {
                    BaseFunc.showMsg(mContext, getString(R.string.no_unionpay));
                }
                break;
        }
    }

    private void gotoSuccess(String _paysn, double _pay_amount, int _goods_source) {
        if (isFromDutyfree()) {
            //如果来自于极速免税付款 则不跳转至付款成功页
            payresult = 0;
            close();
            return;
        }

        PayEntity entity = new PayEntity();
        entity.pay_sn = _paysn;
        entity.pay_amount = _pay_amount;
        entity.gc_area = _goods_source;
        BaseFunc.gotoPaySuccessActivity(this, entity, REQ_PAYSUCCESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PAYSUCCESS) {
            //关闭付款成功页面后的处理
            payresult = 0;
            close();
        }
    }


    /**
     * 支付结果
     *
     * @param payment String
     * @param error   0：成功 -1 失败 -2 取消支付
     * @param msg     String
     */
    @Override
    public void onPayResult(String payment, int error, String msg) {
        payresult = error;
        if (payEntity.vip_pay_type > 0) {
            paytype = getPayTypeCode(payment);
            payresult = error;
            if (error == 0) {
                //如果极速免税VIP购买或者续费成功 则发布通知
                EventBus.getDefault().post(new DutyfreeVipEvent(true));
            }
            close();
        } else {
            //支付成功页面
            super.onPayResult(payment, error, msg);
            if (error == 0) {
                gotoSuccess(payEntity.pay_sn, payEntity.pay_amount, payEntity.order_custom);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        handler = null;
    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    onPayResult(PAYTYPE_WX, PAYRESULT_SUCCESS, PAYMSG_SUCCESS);
                    break;
                case -1:
                    onPayResult(PAYTYPE_WX, PAYRESULT_ERROR, PAYMSG_ERROR);
                    break;
                case -2:
                    onPayResult(PAYTYPE_WX, PAYRESULT_CANCEL, PAYMSG_CANCEL);
                    break;
                case -3:
                    onPayResult(PAYTYPE_WX, PAYRESULT_SIGNERROR, PAYMSG_SIGNERROR);
                    break;
                default:
                    onPayResult(PAYTYPE_WX, PAYRESULT_ERROR, PAYMSG_ERROR);
                    break;
            }
            super.handleMessage(msg);
        }

    };

    /**
     * 初始化动画
     */
    private void initAnimation() {
        animIn = FHLib.createTranslationInAnimation(0);
        animIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                LBOT.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LBOT.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animOut = FHLib.createTranslationOutAnimation(0);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                goBack();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
