package com.alipay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

public class AliPayUtils {

    private Context mContext;
    private Activity mActivity = null;
    private static final int PAYSUCCESS = 1;
    private static final int PAYERROR = 2;
    private PayCallBack callBack = null;
    private String PayCode;
    private String signPayInfo;

    public AliPayUtils(Context c, String signPayInfo) {
        this.mContext = c;
        this.signPayInfo = signPayInfo;
    }

    public AliPayUtils setActivity(Activity act) {
        this.mActivity = act;
        return this;
    }

    public AliPayUtils setPayCallBack(PayCallBack callback) {
        this.callBack = callback;
        return this;
    }

    public interface PayCallBack {
        void onStart();

        void onEnd(boolean isSuccess, String msg);
    }

    public void pay() {
        if (TextUtils.isEmpty(signPayInfo)) {
            if (callBack != null) {
                callBack.onEnd(false, "orderEntity is null");
            }
            return;
        }

        if (mActivity == null) {
            mActivity = (Activity) mContext;
        }

        if (callBack != null) {
            callBack.onStart();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PayTask alipay = new PayTask(mActivity);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(signPayInfo, true);
                    PayResult payResult = new PayResult(result);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    // String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    PayCode = resultStatus;
                    if (TextUtils.equals(resultStatus, "9000")) {
                        handler.sendEmptyMessage(PAYSUCCESS);
                    } else {
                        handler.sendEmptyMessage(PAYERROR);
                    }

                } catch (Exception e) {
                    handler.sendEmptyMessage(PAYERROR);
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PAYSUCCESS:
                    if (callBack != null) {
                        callBack.onEnd(true, PayCode);
                    }
                    break;
                case PAYERROR:
                    if (callBack != null) {
                        callBack.onEnd(false, PayCode);
                    }
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };
}
