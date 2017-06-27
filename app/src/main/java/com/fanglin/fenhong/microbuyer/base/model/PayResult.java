package com.fanglin.fenhong.microbuyer.base.model;

import com.google.gson.Gson;

/**
 *  支付回调信息
 *  Created by Plucky on 2015/10/15.
 */
public class PayResult {
    public int payment;//payment 0 支付宝    1 微信支付     2 银联支付
    public int result;//result  0 支付成功  -1 支付失败    -2 取消支付
    public String msg;//支付信息 数据异常时会提示

    public PayResult() {
        payment = 0;
        result = -1;
    }

    public String getString() {
        return new Gson().toJson(this);
    }
}
