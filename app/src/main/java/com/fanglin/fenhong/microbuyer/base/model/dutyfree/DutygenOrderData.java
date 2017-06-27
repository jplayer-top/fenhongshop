package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/22-下午1:05.
 * 功能描述: 急速免税店 生成订单后的数据结构
 */
public class DutygenOrderData {
    private String pay_sn;
    private String order_sn;
    private double pay_amount;
    private List payment_list;
    private DutyPayOffline pay_offline;
    private int is_selected;// 1代表此单使用了余额支付
    private double pd_remain_money;// 45000 账户余额
    private double pd_pay_money;//5000 此单余额支付的金额

    private String order_url;//预留字段 如果返回则跳转至该页面

    public double getPay_amount() {
        return pay_amount;
    }

    public String getPayAmountDesc() {
        DecimalFormat decimalFormat = new DecimalFormat("¥#0.00");
        return decimalFormat.format(pay_amount);
    }

    public String getPay_sn() {
        return pay_sn;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public List getPayment_list() {
        return payment_list;
    }

    public DutyPayOffline getPay_offline() {
        return pay_offline;
    }

    public static DutygenOrderData getDataByJson(String json) {
        try {
            return new Gson().fromJson(json, DutygenOrderData.class);
        } catch (Exception e) {
            return null;
        }
    }

    public String getOrderUrl() {
        return order_url;
    }
}
