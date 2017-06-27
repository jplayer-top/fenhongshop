package com.fanglin.fenhong.microbuyer.base.model;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/12/1.
 */
public class CartCheckData {
    public List<CartCheck> store_cart_list;//店铺订单信息数组
    public Address address;//收货地址
    public String custom_claim;//是否需要完善海关要求的信息（如：身份证）（0 否  1 是）
    public String vat_hash;
    public String offpay_hash;
    public String address_tips;//不发货地区提醒字段
    public String offpay_hash_batch;
    public String deposit;//可用余额
    public String deposit_disable;//是否关闭余额支付（0 正常使用   1 关闭）
    public COUPON_LIST coupon_list;//现金券列表
    public boolean isChina;//区分国内跟国外

    public Object mansong;//满送
    public Object free_freight_list;//包邮
    public Object tax_list;//店铺税费 key-value
    public double coupon_on_goods_amount;// 435 可使用优惠券的商品总额
    public double coupon_off_goods_amount;// 0  不可使用优惠券的商品总额
    public List<BaseGoods> not_deliver_areas_goods_list;//不配送地区商品列表

    public double micro_shoper_save_money;//微店主身份能节省的钱
    public String micro_shoper_save_desc;//红人店铺身份描述

    public double[] getMoneyAndNumAndFeeAndTaxAndYouhui() {
        double[] data = new double[]{0, 0, 0, 0, 0};
        if (store_cart_list != null && store_cart_list.size() > 0) {
            int num = 0;
            double money = 0;
            double fee = 0;
            double tax = 0;
            double youhui = 0;
            for (int i = 0; i < store_cart_list.size(); i++) {
                /**
                 * 目前店铺优惠：
                 * 1、满送
                 * 2、满任选
                 */
                double store_youhui = store_cart_list.get(i).mansong_minus_amount + store_cart_list.get(i).manselect_minus_amount;
                youhui += store_youhui;

                num += store_cart_list.get(i).store_num;
                money += store_cart_list.get(i).goods_money;
                fee += store_cart_list.get(i).store_freight;
                if (!isChina) {
                    tax += store_cart_list.get(i).store_tax;
                }
            }
            data[0] = money;//商品总额
            data[1] = num;//商品总数
            data[2] = fee;//运费总额
            data[3] = tax;//关税总额
            data[4] = youhui;//店铺优惠金额
        }
        return data;
    }


    public class COUPON_LIST {
        public List<Bonus> available;
        public List<Bonus> unavailable;

        public String getAvailableNum() {
            if (available != null && available.size() > 0) {
                return "可用优惠券(" + available.size() + ")";
            } else {
                return "可用优惠券(0)";
            }
        }

        public String getUnavailableNum() {
            if (unavailable != null && unavailable.size() > 0) {
                return "不可用优惠券(" + unavailable.size() + ")";
            } else {
                return "不可用优惠券(0)";
            }
        }
    }

    public double getBonusMoney(Bonus bonus) {
        double bonusMoney = bonus != null ? bonus.coupon_price : 0;
        double[] fee = getMoneyAndNumAndFeeAndTaxAndYouhui();
        double nowPay = fee[0] - fee[4];// + fee[2] + fee[3];//优惠券不参与抵扣运费、关税 // fixed by Plucky 2016-1-11 17:17
        if (nowPay <= bonusMoney) {
            return nowPay;
        } else {
            return bonusMoney;
        }
    }

    public double getDepositeMoney(boolean selected, Bonus bonus) {
        if (selected) {
            double[] fee = getMoneyAndNumAndFeeAndTaxAndYouhui();
            double adeposit;
            try {
                adeposit = Double.valueOf(deposit);
            } catch (Exception e) {
                adeposit = 0;
            }
            double nowPay = fee[0] + fee[2] + fee[3] - fee[4] - getBonusMoney(bonus);
            if (nowPay <= adeposit) {
                return nowPay;
            } else {
                return adeposit;
            }
        } else {
            return 0;
        }
    }


    public String getDepositeMoneyUse(boolean selectedAmount, Bonus bonus) {
        double adeposit = getDepositeMoney(selectedAmount, bonus);
        DecimalFormat df = new DecimalFormat("-¥#0.00");
        return df.format(adeposit);
    }

    public String getBonusMoneyUse(Bonus bonus) {
        double bonusMoney = bonus != null ? bonus.coupon_price : 0;
        DecimalFormat df = new DecimalFormat("-¥#0.00");
        return df.format(bonusMoney);
    }


    public int getAvailableCount() {
        return (coupon_list != null && coupon_list.available != null) ? coupon_list.available.size() : 0;
    }

    /**
     * 显示有多少优惠券可使用
     */
    public String getBonusShow(Bonus bonus) {
        if (bonus != null) {
            /** 如果已经选中了*/
            DecimalFormat df = new DecimalFormat("¥#0.00");
            return "已选择一张面额" + df.format(bonus.coupon_price) + "优惠券";
        } else {
            int a = (coupon_list != null && coupon_list.available != null) ? coupon_list.available.size() : 0;
            return a + "张优惠券可用";
        }
    }


    /**
     * 显示有多少金额可用
     */
    public String getDepositShow() {
        double adeposit;
        try {
            adeposit = Double.valueOf(deposit);
        } catch (Exception e) {
            adeposit = 0;
        }
        DecimalFormat df = new DecimalFormat("(可用余额#0.00元)");
        return df.format(adeposit);
    }

    public boolean getDeposit_disable() {
        return TextUtils.equals("1", deposit_disable);
    }


    /**
     * 获取店铺满送活动
     */
    public GoodsDtlPromMansongRules getMansong(String store_id) {
        try {
            String str = new Gson().toJson(mansong);
            JSONObject json = new JSONObject(str);
            String man_str = json.getString(store_id);
            return new Gson().fromJson(man_str, GoodsDtlPromMansongRules.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取包邮提示语
     * free_freight_list 的结构为 { "store_id": "money_baoyou"}
     */
    public String getBaoyouDesc(String store_id) {
        if (free_freight_list != null) {
            try {
                String str = new Gson().toJson(free_freight_list);
                JSONObject json = new JSONObject(str);
                DecimalFormat df = new DecimalFormat("满#0.00元包邮，已包邮");
                return df.format(json.getDouble(store_id));
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * 获取包邮限额
     * free_freight_list 的结构为 { "store_id": "money_baoyou"}
     */
    public double getBaoyoumoney(String store_id) {
        double result;
        try {
            String str = new Gson().toJson(free_freight_list);
            JSONObject json = new JSONObject(str);
            result = json.getDouble(store_id);
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    /**
     * 返回店铺的税费
     *
     * @param store_id 店铺id
     * @return double
     */
    public double getTaxFeeOfStore(String store_id) {
        double result;
        try {
            String str = new Gson().toJson(tax_list);
            JSONObject json = new JSONObject(str);
            result = json.getDouble(store_id);
        } catch (Exception e) {
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
