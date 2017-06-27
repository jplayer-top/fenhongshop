package com.fanglin.fenhong.microbuyer.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alipay.AliPayUtils;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.model.PayEntity;
import com.fanglin.fenhong.microbuyer.base.model.ResultModel;
import com.fanglin.fenhong.microbuyer.common.FHBrowserActivity;
import com.fanglin.fenhong.microbuyer.common.OrderActivity;
import com.fanglin.fenhong.microbuyer.common.OrderDtlActivity;
import com.fanglin.fenhong.microbuyer.common.OrderMergeActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.DutyfreeCartCheckActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.OrderDetailActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.OrderListActivity;
import com.fanglin.unionpay.FHUPPayCallBack;
import com.fanglin.unionpay.FHUPPayUtil;
import com.google.gson.Gson;

/**
 * 青岛芳林信息
 * Created by Plucky on 2016/12/28.
 * 功能描述: 支付类
 */

public class FHPayActivity extends BaseFragmentActivity {

    public Class lastActivity;
    public PayEntity payEntity;

    private static final int REQJDPAY = 111;
    public static final int REQ_PAYSUCCESS = 110;

    public static final String PAYTYPE_JD = "jdpay";
    public static final String PAYTYPE_WX = "wxpay";
    public static final String PAYTYPE_ALI = "alipay";
    public static final String PAYTYPE_UNION = "chinapay";

    //支付结果 0 成功  -1 失败 -2 取消 -3 签名失败
    public static final int PAYRESULT_SUCCESS = 0;
    public static final String PAYMSG_SUCCESS = "支付成功";
    public static final int PAYRESULT_ERROR = -1;
    public static final String PAYMSG_ERROR = "支付失败";
    public static final int PAYRESULT_CANCEL = -2;
    public static final String PAYMSG_CANCEL = "取消支付";
    public static final int PAYRESULT_SIGNERROR = -3;
    public static final String PAYMSG_SIGNERROR = "签名失败!请通过其他途径支付!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            String val = getIntent().getStringExtra("VAL");
            payEntity = new Gson().fromJson(val, PayEntity.class);
            lastActivity = Class.forName(payEntity.lastClassName);
        } catch (Exception e) {
            payEntity = null;
        }
        if (payEntity == null) {
            BaseFunc.showMsg(mContext, getString(R.string.hint_order_invalid));
            finish();
        }
    }

    /**
     * 调起阿里支付
     */
    public void doAlipay() {
        aliPayAction(payEntity.pay_sn, payEntity.vip_pay_type);
    }

    /**
     * 带参数调起阿里支付
     *
     * @param paySN   支付单号
     * @param payType vip支付类型  10季卡 20年卡
     */
    public void aliPayAction(String paySN, int payType) {
        if (member == null) return;
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    new AliPayUtils(mContext, data).setPayCallBack(new AliPayUtils.PayCallBack() {
                        @Override
                        public void onStart() {
                            BaseFunc.showMsg(mContext, "正在建立安全链接...");
                        }

                        @Override
                        public void onEnd(boolean isSuccess, String msg) {
                            if (isSuccess) {
                                onPayResult(PAYTYPE_ALI, PAYRESULT_SUCCESS, PAYMSG_SUCCESS);
                            } else {
                                if (TextUtils.equals("6001", msg)) {
                                    onPayResult(PAYTYPE_ALI, PAYRESULT_CANCEL, PAYMSG_CANCEL);
                                } else {
                                    onPayResult(PAYTYPE_ALI, PAYRESULT_ERROR, msg);
                                }
                            }
                        }
                    }).pay();
                }
            }
        }).getAlipayInfo(member, paySN, payType);
    }

    /**
     * 支付结果
     *
     * @param payment String
     * @param error   0：成功 -1 失败 -2 取消支付
     * @param msg     String
     */
    public void onPayResult(String payment, int error, String msg) {
        BaseFunc.showMsg(mContext, msg);
    }


    /**
     * 调起京东支付
     */
    public void doJDPay() {
        jdPayAction(payEntity.pay_sn);
    }

    /**
     * 带参数京东支付
     *
     * @param paySN 支付单号
     */
    public void jdPayAction(String paySN) {
        if (member == null) {
            BaseFunc.gotoLogin(this);
            return;
        }

        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {

                if (isSuccess) {
                    ResultModel resultModel = getFHReuslt(data);
                    if (resultModel != null && !resultModel.errorEmpty()) {
                        //如果返回了
                        if (TextUtils.isEmpty(resultModel.getMsg())) {
                            onPayResult(PAYTYPE_JD, PAYRESULT_ERROR, PAYMSG_ERROR);
                        } else {
                            onPayResult(PAYTYPE_JD, PAYRESULT_ERROR, resultModel.getMsg());
                        }
                    } else {
                        Intent intent = new Intent(mContext, FHBrowserActivity.class);
                        intent.putExtra("VAL", data);
                        intent.putExtra("TYPE", true);
                        startActivityForResult(intent, REQJDPAY);
                    }
                } else {
                    onPayResult(PAYTYPE_JD, PAYRESULT_ERROR, PAYMSG_ERROR);
                }
            }
        }).setNormalRequest(true).jdPay(member, paySN);
    }

    /**
     * 调起银联支付
     */
    public void doUnionPay() {
        unionPayAction(payEntity.pay_sn, payEntity.pay_amount);
    }

    /**
     * 带参数调起银联支付
     *
     * @param paySN     支付单号
     * @param payAmount 支付金额
     */
    public void unionPayAction(String paySN, double payAmount) {
        if (member == null) return;
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {
                BaseFunc.showMsg(mContext, "正在建立安全链接...");
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    FHUPPayUtil.pay(mContext, data);
                } else {
                    onPayResult(PAYTYPE_UNION, PAYRESULT_ERROR, "银联支付单出错:" + data);
                }
            }
        }).genUnionPayTn(member.member_id, member.token, paySN, payAmount);
    }


    /**
     * 如果是分红商城API的返回信息 则不是京东的支付页面
     *
     * @param data 支付回调的信息
     * @return resultModel
     */
    private ResultModel getFHReuslt(String data) {
        try {
            return new Gson().fromJson(data, ResultModel.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断是否已经在订单页面：订单列表或者订单详情
     *
     * @return boolean
     */
    public boolean notInOrderPage() {
        return (lastActivity != null && lastActivity != OrderActivity.class && lastActivity != OrderDtlActivity.class && lastActivity != OrderMergeActivity.class && lastActivity != OrderListActivity.class && lastActivity != OrderDetailActivity.class);
    }

    /**
     * 是否来自急速免税店的支付
     *
     * @return boolean
     */
    public boolean isFromDutyfree() {
        return lastActivity == DutyfreeCartCheckActivity.class || lastActivity == OrderListActivity.class || lastActivity == OrderDetailActivity.class;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQJDPAY) {
            if (data != null && resultCode == RESULT_OK) {
                int aError = data.getIntExtra("JD_ERROR", 2);
                String aMsg = data.getStringExtra("JD_MSG");
                if (aError == 0) {
                    onPayResult(PAYTYPE_JD, PAYRESULT_SUCCESS, PAYMSG_SUCCESS);
                } else {
                    if (!TextUtils.isEmpty(aMsg)) {
                        onPayResult(PAYTYPE_JD, PAYRESULT_ERROR, aMsg);
                    } else {
                        onPayResult(PAYTYPE_JD, PAYRESULT_ERROR, PAYMSG_ERROR);
                    }
                }
            } else {
                onPayResult(PAYTYPE_JD, PAYRESULT_CANCEL, PAYMSG_CANCEL);
            }
        } else if (requestCode != REQ_PAYSUCCESS) {
            FHUPPayUtil.handlerActivityResult(requestCode, resultCode, data, new FHUPPayCallBack() {
                @Override
                public void onSuccess() {
                    onPayResult(PAYTYPE_UNION, PAYRESULT_SUCCESS, PAYMSG_SUCCESS);
                }

                @Override
                public void onError() {
                    onPayResult(PAYTYPE_UNION, PAYRESULT_ERROR, PAYMSG_ERROR);
                }

                @Override
                public void onCancel() {
                    onPayResult(PAYTYPE_UNION, PAYRESULT_CANCEL, PAYMSG_CANCEL);
                }
            });
        }
    }

    // 支付方式 0 支付宝 1京东  2微信 3银联
    public int getPayTypeCode(String type) {
        if (TextUtils.equals(PAYTYPE_ALI, type)) {
            return 0;
        } else if (TextUtils.equals(PAYTYPE_WX, type)) {
            return 2;
        } else if (TextUtils.equals(PAYTYPE_JD, type)) {
            return 1;
        } else if (TextUtils.equals(PAYTYPE_UNION, type)) {
            return 3;
        }

        return 0;
    }
}
