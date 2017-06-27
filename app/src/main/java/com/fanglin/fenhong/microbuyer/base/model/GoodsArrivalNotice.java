package com.fanglin.fenhong.microbuyer.base.model;

/**
 * 商品到货提醒实体类
 * Author: Created by lizhixin on 2016/4/6 15:57.
 */
public class GoodsArrivalNotice extends BaseGoods {
    private String goods_salenum;//销量
    private boolean checked;//是否被选中
    private boolean display;//是否显示

    public String getGoods_salenum() {
        return goods_salenum;
    }

    public void setGoods_salenum(String goods_salenum) {
        this.goods_salenum = goods_salenum;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }
}
