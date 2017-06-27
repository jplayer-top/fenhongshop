package com.fanglin.fenhong.microbuyer.base.model;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/11/15.
 * 生成订单
 * modify by lizhixin on 2016/2/1
 */
public class OrderGen {
   public String pay_sn;       //交易单编号
   public double  pay_amount;  //交易金额
   public int order_custom; //海关id ( 0 青岛泛亚 1郑州捷龙 ...)
   public List payment_list;//alipay：支付宝,  wxpay：微信,  chinapay：银联
}
