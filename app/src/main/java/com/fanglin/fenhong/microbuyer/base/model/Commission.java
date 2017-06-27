package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;
import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.R;

/**
 * 我的团队 list item
 */
public class Commission {
    private String buyer_name;//购买者姓名
    private String order_sn;//订单编号
    private String goods_name;//显示的类型名称
    private String order_state;//订单状态（直接显示就可以)
    private String goods_price;//商品价格
    private String deduct_price;//推广奖金比例（已经带了+ -号）也已经计算完成
    private String goods_num;//订单的商品数量
    private String order_date;//订单时间戳
    private String change_date;// 状态更改时间 (目前用作结算时间， ！可能为空 ！ 之前的结算时间没有存)
    private String order_state_code;// 订单状态码 0:已取消 | 10:未付款 | 20:已付款 | 30:已发货 | 40:已收货 | 50:已结算

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getOrder_state() {
        return order_state + "";
    }

    public void setOrder_state(String order_state) {
        this.order_state = order_state;
    }

    public String getGoods_price() {
        return goods_price + "";
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getDeduct_price() {
        return deduct_price + "";
    }

    public void setDeduct_price(String deduct_price) {
        this.deduct_price = deduct_price;
    }

    public String getGoods_num() {
        return goods_num + "";
    }

    public String getGoodsNum4Display(Context context) {
        return context.getString(R.string.multiply) + getGoods_num();
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getChange_date() {
        return change_date;
    }

    public void setChange_date(String change_date) {
        this.change_date = change_date;
    }

    public String getOrder_state_code() {
        return order_state_code;
    }

    public int getOrderStateBackground(Context mContext) {
        if (TextUtils.equals("0", order_state_code)) {
            return R.drawable.shape_commission_gray_corner;
        } else if (TextUtils.equals("50", order_state_code)) {
            return R.drawable.shape_commission_lv_corner;
        } else {
            return R.drawable.shape_commission_lan_corner;
        }
    }

    public void setOrder_state_code(String order_state_code) {
        this.order_state_code = order_state_code;
    }
}
