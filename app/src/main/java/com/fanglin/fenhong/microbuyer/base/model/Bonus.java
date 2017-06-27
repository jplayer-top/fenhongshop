package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import com.fanglin.fhlib.other.FHLib;

import java.text.DecimalFormat;

/**
 * 作者： Created by Plucky on 2015/11/28.
 * 单张现金券
 */
public class Bonus {
    public String coupon_id;//现金券id,
    public String coupon_sn;//现金券编码,
    public double coupon_price;// 现金券面额,
    public long get_time;// 领取时间戳,
    public long use_time;// 使用时间戳 （未使用为0）,
    public int is_own;// 是否仅限自营店 （0 否 1 是）,
    public double use_limit;// 满多少金额使用,
    public String coupon_name;// 现金券名称,
    public long end_time;// 到期时间戳
    public boolean isSelected;//是否选中

    public String store_id;// 店铺ID (如果不为NULL,表示限单店铺）,
    public String store_name;// 限单店铺的店铺名称,

    /*是否即将过期*/
    public boolean isAlmostOutOfDate() {
        long leave = end_time - get_time;
        return leave <= 60 * 60 * 24 && leave > 0;
    }

    public String getUseDesc() {
        if (use_limit > 0) {
            DecimalFormat df = new DecimalFormat("#0.00");
            return "满" + df.format(use_limit) + "可以使用";
        } else {
            return "无门槛现金券";
        }
    }

    public String getStoreDesc() {
        //is_own 无关
        if (!TextUtils.isEmpty(store_name)) {
            return "仅限" + store_name + "使用";
        } else {
            return "仅限自营店使用";
        }
    }

    public String getHongBao() {
        return coupon_price + "元红包";
    }

    public String getDeadlineTime() {
        return "有效期至:" + FHLib.getTimeStrByTimestampDay(end_time);
    }

    public static Bonus getTest() {
        Bonus bonus = new Bonus();
        bonus.coupon_price = 50.00;
        bonus.is_own = 1;
        bonus.coupon_name = "15元现金券";
        bonus.use_limit = 134.2;
        bonus.get_time = 1448796110;
        bonus.end_time = 1449980010;
        return bonus;
    }
}
