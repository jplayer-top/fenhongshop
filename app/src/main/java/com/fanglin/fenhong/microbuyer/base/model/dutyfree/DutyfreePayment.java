package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

/**
 * 青岛芳林信息
 * Created by Plucky on 2016/12/30.
 * 功能描述: 极速免税店支付方式
 */

public class DutyfreePayment {
    private String payment;//alipay wxpay jdpay
    private String msg;//如果返回则说明不能支付

    public String getPayment() {
        return payment;
    }

    public String getMsg() {
        return msg;
    }
}
