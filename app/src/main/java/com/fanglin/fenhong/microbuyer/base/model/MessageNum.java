package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;
import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.R;

/**
 * 作者： Created by Plucky on 15-10-26.
 */
public class MessageNum {
    public int goods_activity_num;//优惠促销   12
    public String goods_activity_msg;//优惠促销最新一条消息
    public int order_notice_num;//订单提醒     13
    public String order_notice_msg;//订单提醒最新一条消息
    public int delivery_notice_num;//物流通知  14
    public String delivery_notice_msg;//物流通知最新一条消息
    public int income_num;//我的钱包           15
    public String income_msg;//我的钱包最新一条消息
    public int sys_msg_num;//系统通知          9
    public String sys_msg;//系统通知最新一条消息
    public int team_num;//我的团队             16
    public String team_msg;//我的团队最新一条消息
    public int msg17_num;//商品提醒             17
    public String msg17_msg;//商品提醒最新一条消息

    public int msg18Num;//我的回复             17
    public String msg18Msg;//我的回复 最新一条消息

    public int msg19Num;//新的粉丝             17
    public String msg19Msg;//新的粉丝 最新一条消息

    public String get_goods_activity_msg(Context c) {
        if (TextUtils.isEmpty(goods_activity_msg)) {
            return c.getString(R.string.more_goods_activity);
        }
        return goods_activity_msg;
    }

    public String get_order_notice_msg(Context c) {
        if (TextUtils.isEmpty(order_notice_msg)) {
            return c.getString(R.string.more_goods_notice);
        }
        return order_notice_msg;
    }

    public String get_delivery_notice_msg(Context c) {
        if (TextUtils.isEmpty(delivery_notice_msg)) {
            return c.getString(R.string.more_delivery_notice);
        }
        return delivery_notice_msg;
    }

    public String get_income_msg(Context c) {
        if (TextUtils.isEmpty(income_msg)) {
            return c.getString(R.string.more_income_notice);
        }
        return income_msg;
    }

    public String get_sys_msg(Context c) {
        if (TextUtils.isEmpty(sys_msg)) {
            return c.getString(R.string.more_sys_notice);
        }
        return sys_msg;
    }

    public String get_team_msg(Context c) {
        if (TextUtils.isEmpty(team_msg)) {
            return c.getString(R.string.more_team_notice);
        }
        return team_msg;
    }

    public String get_msg17_msg(Context c) {
        if (TextUtils.isEmpty(msg17_msg)) {
            return c.getString(R.string.more_msg17);
        }
        return msg17_msg;
    }

    public String getMsg18Msg(Context c) {
        if (TextUtils.isEmpty(msg18Msg)) {
            return c.getString(R.string.msg_18_hint);
        }
        return msg18Msg;
    }

    public String getMsg19Msg(Context c) {
        if (TextUtils.isEmpty(msg19Msg)) {
            return c.getString(R.string.msg_19_hint);
        }
        return msg19Msg;
    }

    public int getTotalNum() {
        return goods_activity_num + order_notice_num + delivery_notice_num + income_num + sys_msg_num + team_num + msg17_num + msg18Num + msg19Num;
    }
}
