package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;

import java.text.DecimalFormat;

/**
 * 作者： Created by Plucky on 2015/10/6.
 * App个人中心的数字提醒 UCenter
 */
public class PersonalNum {
    public String goods_favorite_count;// 商品收藏数量
    public String store_favorite_count;// 店铺收藏数量
    public String brand_favorite_count;// 品牌收藏数量
    public String microshop_favorite_count;// 微店收藏数量
    public String talent_favorite_count;// 达人收藏数量
    public String time_favorite_count;// 时光收藏数量
    public String wait_paid_order_count;// 待付款订单数量
    public String wait_send_order_count;// 待发货订单数量
    public String wait_receive_order_count;// 待收货订单数量
    public String wait_evaluate_order_count;// 待评价订单数量
    public String service_order_count;// 退款/售后订单数量

    public String member_nickname;// 昵称
    public String member_avatar;// 头像

    public int member_degree;//0或者1 会员身份 0是正常会员 1是分享达人
    public int member_type;// 0代表 非买手 10代表 普通 买手 20代表 vip 买手
    public String sharer_name;//推荐人名称(分享人名称)

    public int shop_type;// 微店类型  2是365店铺(大V红人店铺) 3是666店铺(皇冠红人店铺) 否则没有店

    public String member_points;// 我的积分
    public String member_balance;//我的余额
    public String coupon_count;// 优惠券数量
    public String bank_card_count;// 银行卡数量
    public String batch_id;// 红包组id (第一次登录个人中心才返回)
    public String bag_name;// 红包名称 (第一次登录个人中心才返回)

    public int newcoming_coupon_count;//新到的优惠券数量 如果没有新到的券 则此字段不返回

    public int is_jump;//是否跳转
    public String jump_url;//跳转链接

    public String qrcode;//个人中心我的二维码
    public String qrcode_description;//分享二维码时同时分享的内容

    public PersonalNum() {
        goods_favorite_count = "0";
        store_favorite_count = "0";
        brand_favorite_count = "0";
        microshop_favorite_count = "0";
        talent_favorite_count = "0";
        time_favorite_count = "0";
    }

    public boolean jump2Url() {
        return is_jump > 0 && BaseFunc.isValidUrl(jump_url);
    }


    public int get_member_points() {
        if (TextUtils.isEmpty(member_points)) return 0;
        if (TextUtils.isDigitsOnly(member_points)) {
            return Integer.valueOf(member_points);
        }
        return 0;
    }

    public double get_member_balance() {
        if (TextUtils.isEmpty(member_balance)) return 0;
        try {
            return Double.valueOf(member_balance);
        } catch (Exception e) {
            return 0;
        }
    }

    public String get_member_balanceDesc() {
        double d = get_member_balance();
        if (d > 10000) {
            DecimalFormat df = new DecimalFormat("#0.00万");
            return df.format(d / 10000);
        } else {
            DecimalFormat df = new DecimalFormat("#0.00");
            return df.format(d);
        }
    }

    public int get_coupon_count() {
        if (TextUtils.isEmpty(coupon_count)) return 0;
        if (TextUtils.isDigitsOnly(coupon_count)) {
            return Integer.valueOf(coupon_count);
        }
        return 0;
    }

    public int get_bank_card_count() {
        if (TextUtils.isEmpty(bank_card_count)) return 0;
        if (TextUtils.isDigitsOnly(bank_card_count)) {
            return Integer.valueOf(bank_card_count);
        }
        return 0;
    }


    public int get_wait_paid_order() {
        if (TextUtils.isEmpty(wait_paid_order_count)) return 0;
        if (TextUtils.isDigitsOnly(wait_paid_order_count)) {
            return Integer.valueOf(wait_paid_order_count);
        }
        return 0;
    }

    public int get_wait_send_order() {
        if (TextUtils.isEmpty(wait_send_order_count)) return 0;
        if (TextUtils.isDigitsOnly(wait_send_order_count)) {
            return Integer.valueOf(wait_send_order_count);
        }
        return 0;
    }

    public int get_wait_receive_order() {
        if (TextUtils.isEmpty(wait_receive_order_count)) return 0;
        if (TextUtils.isDigitsOnly(wait_receive_order_count)) {
            return Integer.valueOf(wait_receive_order_count);
        }
        return 0;
    }

    public int get_wait_evaluate_order() {
        if (TextUtils.isEmpty(wait_evaluate_order_count)) return 0;
        if (TextUtils.isDigitsOnly(wait_evaluate_order_count)) {
            return Integer.valueOf(wait_evaluate_order_count);
        }
        return 0;
    }

    public int get_service_order() {
        if (TextUtils.isEmpty(service_order_count)) return 0;
        if (TextUtils.isDigitsOnly(service_order_count)) {
            return Integer.valueOf(service_order_count);
        }
        return 0;
    }

    public String getShopTypeName() {
        switch (shop_type) {
            case 2:
                return "大V红人店铺";
            case 3:
                return "皇冠红人店铺";
            default:
                return "红人店铺";
        }
    }
}
