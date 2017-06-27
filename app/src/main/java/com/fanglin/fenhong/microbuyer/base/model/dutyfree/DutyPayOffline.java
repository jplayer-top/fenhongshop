package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import com.google.gson.Gson;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/22-下午9:13.
 * 功能描述:极速免税线下支付
 */
public class DutyPayOffline {
    private String company;
    private String bank_no;
    private String bank_name;
    private String email;
    private String service;

    private String orderSN;
    private String payAmount;

    public String getOrderSN() {
        return orderSN;
    }

    public void setOrderSN(String orderSN) {
        this.orderSN = orderSN;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getBank_name() {
        return bank_name;
    }

    public String getBank_no() {
        return bank_no;
    }

    public String getCompany() {
        return company;
    }

    public String getEmail() {
        return email;
    }

    public String getService() {
        return service;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
