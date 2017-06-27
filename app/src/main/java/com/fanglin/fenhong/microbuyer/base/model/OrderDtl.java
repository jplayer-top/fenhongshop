package com.fanglin.fenhong.microbuyer.base.model;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-9-28.
 * 订单详情
 */
public class OrderDtl {
    public String order_id;// 订单id
    public String order_sn;// 订单编号
    public String store_id;// 店铺id
    public String store_name;// 店铺名称
    public int order_state;// 订单状态
    public long add_time;// 订单添加时间
    public String pay_sn;// 支付单号
    public double goods_amount;// 商品金额
    //public double order_amount;// 订单金额
    public double real_amount;// 实际付款金额
    public double shipping_fee;// 运费

    public String shipping_weight;//快递重量

    //public String shipping_code;// 物流单号
    //public String e_code;//配送公司编号
    //public String e_name;// 物流公司名称

    public double duty_fee;// 关税金额
    public int country_source;// 订单国别：0:国内 | 1:韩国
    public String state_desc;// 订单状态(文字)
    public String payment_name;// 支付类型(文字)
    public long validity_pay_time;// 剩余支付时间秒数 (过期返回0)

    public ReciverInfo reciver_info;//收货信息
    //public Object invoice_info;//[ 发票信息 ]
    public List<OrderGoods> order_goods;//订单详情商品
    public String evaluation_state;//评价状态
    public int store_evaluated;//店铺评价状态 0:未评价 | 1:已评价
    public float store_desccredit = 5;// 描述相符评分  （1-5）
    public float store_servicecredit = 5;//   服务态度评分  (1-5)
    public float store_deliverycredit = 5;// 发货速度评分  (1-5)
    public String store_baidusales;//百度商桥链接，用于联系卖家

    public int refund_enabled;//是否支持退款 0 不支持  1 支持

    public int order_custom; //海关id ( 0 青岛泛亚 1郑州捷龙 ...)
    public String delete_state;//删除状态 0未删除 1放入回收站 2彻底删除

    //public String if_refund;//0  0-表示不可以  1-表示可以

    public String pd_amount;//站内余额金额
    public String coupon_amount;//优惠券金额
    public String use_limit;//满减标示

    public double free_freight;//满额包邮金额
    public GoodsDtlPromMansongRules mansong;//满额优惠

    public List payment_list;//alipay：支付宝,  wxpay：微信,  chinapay：银联

    public double coupon_on_goods_amount;// 435 可使用优惠券的商品总额
    public double coupon_off_goods_amount;// 0  不可使用优惠券的商品总额

    public double store_promotion_total;//店铺优惠的金额

    public double micro_shoper_save_money;//微店主身份能节省的钱
    public String micro_shoper_save_desc;//红人店铺身份描述

    public double getUse_limit() {
        try {
            return Double.valueOf(use_limit);
        } catch (Exception e) {
            return 0;
        }
    }

    public String getPd_amountDesc() {
        double d = getPd_amount();
        DecimalFormat df = new DecimalFormat("-¥#0.00");
        return df.format(d);
    }

    public double getPd_amount() {
        try {
            return Double.valueOf(pd_amount);
        } catch (Exception e) {
            return 0;
        }
    }

    public String getCoupon_amountDesc() {
        double d = getCoupon_amount();
        DecimalFormat df = new DecimalFormat("-¥#0.00");
        return df.format(d);
    }

    public double getCoupon_amount() {
        try {
            return Double.valueOf(coupon_amount);
        } catch (Exception e) {
            return 0;
        }
    }

    public String getShipping_weight() {
        if (TextUtils.isEmpty(shipping_weight)) {
            return shipping_weight;
        } else {
            return "(" + shipping_weight + ")";
        }
    }

    /**
     * 控制分享红包的按钮是否显示
     */
    public boolean getVisiable(boolean isRefunding) {
        /** 退货时不显示该按钮*/
        if (isRefunding) {
            return false;
        } else {
            return order_state == 20 || order_state == 30 || order_state == 40;
        }
    }


    public int getGoodsNum() {
        if (this.order_goods == null) return 0;
        int c = 0;
        for (int i = 0; i < this.order_goods.size(); i++) {
            c += this.order_goods.get(i).goods_num;
        }
        return c;
    }

    /**
     * 获取订单内商品的状态 只有所有商品都有效时订单才有效且允许支付
     */
    public int getOrderGoodsState() {
        int result = 1;
        if (order_goods != null && order_goods.size() > 0) {
            for (OrderGoods goods : order_goods) {
                if (TextUtils.equals(goods.goods_state, "1")) {
                    //商品有效
                } else {
                    //有无效商品
                    result = 0;
                    break;
                }
            }
        } else {
            result = 0;
        }
        return result;
    }

    /**
     * 是否显示红人店铺的优惠
     *
     * @return boolean
     */
    public boolean showMicroShopSave() {
        return micro_shoper_save_money > 0 && !TextUtils.isEmpty(micro_shoper_save_desc);
    }

    public Spanned getMicroShoperSaveDesc() {
        return Html.fromHtml(micro_shoper_save_desc);
    }

    public String getMicroShoperSaveMoney() {
        return "-¥" + micro_shoper_save_money;
    }

}
