package com.fanglin.unionpay;

import android.content.Context;
import android.content.Intent;

import com.unionpay.UPPayAssistEx;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/2/25-下午3:50.
 * 功能描述:
 */
public class FHUPPayUtil {

    /**
     * Mode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
     */
    public static final String mMode = "00";

    public FHUPPayUtil() {

    }

    /**
     * 调起支付控件
     */
    public static void pay(Context context, String tn) {
        UPPayAssistEx.startPay(context, null, null, tn, mMode);
    }

    public static void handlerActivityResult(int requestCode, int resultCode, Intent data, FHUPPayCallBack mcb) {
        if (data == null) {
            if (mcb != null) mcb.onError();
            return;
        }
        /**
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        if ("success".equalsIgnoreCase(str)) {
            if (mcb != null) mcb.onSuccess();
        } else if ("fail".equalsIgnoreCase(str)) {
            // "支付失败！";
            if (mcb != null) mcb.onError();
        } else if ("cancel".equalsIgnoreCase(str)) {
            // "用户取消了支付";
            if (mcb != null) mcb.onCancel();
        }
    }
}
