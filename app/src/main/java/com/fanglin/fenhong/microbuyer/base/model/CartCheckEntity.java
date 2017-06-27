package com.fanglin.fenhong.microbuyer.base.model;

/**
 * 青岛芳林信息
 * Created by Plucky on 2016/12/20.
 * 功能描述;//进入核对购物车传值
 */

public class CartCheckEntity {
    private String goodsId_num;//140|1,
    private int goods_source;//0,
    private int if_cart;//0,
    private String resource_tags;//XXXXX,
    private String pintuan_id;//8,
    private String pintuan_group_id;//,
    private String pintuan_parent_id;//

    public String getGoodsIdNum() {
        return goodsId_num;
    }

    public void setGoodsIdNum(String goodsId_num) {
        this.goodsId_num = goodsId_num;
    }

    public int getGoodsSource() {
        return goods_source;
    }

    public void setGoodsSource(int goods_source) {
        this.goods_source = goods_source;
    }

    public int getIfCart() {
        return if_cart;
    }

    public void setIfCart(int if_cart) {
        this.if_cart = if_cart;
    }

    public String getResourceTags() {
        return resource_tags;
    }

    public void setResourceTags(String resource_tags) {
        this.resource_tags = resource_tags;
    }

    public String getPintuanId() {
        return pintuan_id;
    }

    public void setPintuanId(String pintuan_id) {
        this.pintuan_id = pintuan_id;
    }

    public String getPintuanGroupId() {
        return pintuan_group_id;
    }

    public void setPintuanGroupId(String pintuan_group_id) {
        this.pintuan_group_id = pintuan_group_id;
    }

    public String getPintuanParentId() {
        return pintuan_parent_id;
    }

    public void setPintuanParentId(String pintuan_parent_id) {
        this.pintuan_parent_id = pintuan_parent_id;
    }
}
