package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/23.
 */
public class CartCheck {
    public String store_id;//   店铺id
    public String store_name;//    店铺名称
    public double store_freight;//    店铺运费
    public String shipping_weight;//快递重量
    public List<CartCheckGoods> goods_list;//    商品数组

    public double manselect_minus_amount;//此店铺满任选活动的优惠金额 如果没有活动 则无此字段

    /**
     * 服务端不给计算的字段
     */
    public int store_num = 0;//店铺商品总数
    public double goods_money;//商品总额
    public double store_tax;//关税总额

    //店铺满送的信息--由客户端计算得到的
    public double mansong_minus_amount;
    public CartCheckGoods mansong_goods;
    public String mansong_desc;

    public String getShipping_weight() {
        if (TextUtils.isEmpty(shipping_weight)) {
            return shipping_weight;
        } else {
            return "(" + shipping_weight + ")";
        }
    }
}
