package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;

import com.fanglin.fenhong.microbuyer.R;
import com.google.gson.Gson;

import java.util.List;

/**
 * 青岛芳林信息
 * Created by Plucky on 2016/12/20.
 * 功能描述:支付所需信息
 */

public class PayEntity {
    public String pay_sn;//支付单号
    public String lastClassName;//上个页面
    public int gc_area;//0 国内 1 国外
    public int order_custom;//海关编码
    public double pay_amount;//交易金额
    public double youhui;//使用优惠券和余额抵扣的金额
    public List payment_list;//支付方式
    public String payoff;//线下支付
    public int vip_pay_type;//vip

    public String getString() {
        return new Gson().toJson(this);
    }

    public String getSubject(Context context) {
        String subject;
        if (gc_area == 0) {
            subject = context.getString(R.string.chinaorder) + pay_sn;
        } else {
            subject = context.getString(R.string.globalorder) + pay_sn;
        }
        return subject;
    }

    public String getBody() {
        return pay_sn + "_" + pay_amount;
    }
}
