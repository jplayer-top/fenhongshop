package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/21-上午10:37.
 * 功能描述: 在编辑完购物车后会返回的内容
 */
public class DutyCartEditResult {
    private double goods_all_price;//商品总额
    private String goods_promotion_desc;//活动标签
    private double goods_promotion_amount;//活动价
    private int total_num;//总商品数
    private double total_price;//合计金额
    private String import_label;//进口商品
    private String import_desc;//9折优惠
    private String import_money;//-¥5.2.1
    private DutyCartTips disparity;

    private String taxes;//税费
    private String freight;//¥0.00

    private String balance_label;//余额抵扣
    private String balance_desc;//(可用余额¥5000.00)
    private String balance_money;//-¥5000.00

    private String item_label;//优惠方式
    private String item_desc;// 限时优惠
    private String item_money;// -$0.07

    public double getGoods_all_price() {
        return goods_all_price;
    }

    public void setGoods_all_price(double goods_all_price) {
        this.goods_all_price = goods_all_price;
    }

    public double getGoods_promotion_amount() {
        return goods_promotion_amount;
    }

    public void setGoods_promotion_amount(double goods_promotion_amount) {
        this.goods_promotion_amount = goods_promotion_amount;
    }

    public String getGoods_promotion_desc() {
        return goods_promotion_desc;
    }

    public void setGoods_promotion_desc(String goods_promotion_desc) {
        this.goods_promotion_desc = goods_promotion_desc;
    }

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int total_num) {
        this.total_num = total_num;
    }

    public double getTotal_price() {
        return total_price;
    }

    public String getImportDesc() {
        return import_desc;
    }

    public String getImportLabel() {
        return import_label;
    }

    public String getImportMoney() {
        return import_money;
    }

    public DutyCartTips getDisparity() {
        return disparity;
    }

    public String getFreight() {
        return freight;
    }

    public String getTaxes() {
        return taxes;
    }

    public String getBalanceDesc() {
        return balance_desc;
    }

    public String getBalanceLabel() {
        return balance_label;
    }

    public String getBalanceMoney() {
        return balance_money;
    }

    public String getItemLabel() {
        return item_label;
    }

    public String getItemDesc() {
        return item_desc;
    }

    public String getItemMoney() {
        return item_money;
    }

}
