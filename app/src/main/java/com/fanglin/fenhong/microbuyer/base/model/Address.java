package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

/**
 * 作者： Created by Plucky on 15-9-20.
 */
public class Address {
    public String address_id;// 地址id
    public String name;// 收货人姓名
    public String cert_name;//    身份证姓名
    public String cert_num;//    身份证号码
    public String area_id;// 区县id
    public String city_id;// 城市id
    public String area_info;// 地区信息
    public String address;// 街道信息
    public String mob_phone;// 手机号
    public String mobile;// 手机号 --蛋疼的后端设计
    public String is_default;// 是否默认收货地址

    public String getMaskIdCard() {
        String res;
        if (!TextUtils.isEmpty(cert_num) && cert_num.length() >= 15) {
            String pre = cert_num.substring(0, 5);
            String aff = cert_num.substring(cert_num.length() - 5);
            res = "身份证:" + pre + "*****" + aff;
        } else {
            res = cert_num;
        }
        return res;
    }

    /**
     * 第一次尝试在Model 中进行数据的请求操作 --APIUtil
     * 这样MVC的职责分工更明确
     * 好像又去掉了 Added By Plucky
     */


    public Address() {

    }

    public String getAddressDesc() {
        return area_info + " " + address;
    }

    public boolean isDefault() {
        return TextUtils.equals("1", is_default);
    }

    public String getAddressDescofPick() {
        return (isDefault() ? "【默认】" : "") + area_info + " " + address;
    }
}
