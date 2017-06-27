package com.fanglin.fenhong.microbuyer.base.model;

import java.util.List;

/**
 * 青岛芳林信息
 * Created by Plucky on 2016/12/19.
 * 功能描述:支付信息
 */

public class PayInfo {
    private String pay_sn;
    private int country_source;
    private double pay_amount;
    private List payment_list;


    public String getPaySn() {
        return pay_sn;
    }

    public int getCountrySource() {
        return country_source;
    }

    public String getCountrySourceDesc(){
        return String.valueOf(country_source);
    }

    public double getPayAmount() {
        return pay_amount;
    }

    public List getPaymentList() {
        return payment_list;
    }
}
