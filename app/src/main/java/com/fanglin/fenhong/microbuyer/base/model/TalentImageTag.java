package com.fanglin.fenhong.microbuyer.base.model;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/13-上午11:42.
 * 功能描述;//时光图片上的标签
 */
public class TalentImageTag {
    private String location;//30%,65%,
    private TalentImageTagLine line1;//
    private TalentImageTagLine line2;//
    private TalentImageTagLine line3;//
    private String goods_id;//19872,
    private String short_name;//cloud9马油膏,
    private String goods_price;//¥84.00,
    private String goods_origin;//韩国
    private String goods_image;//商品图片

    //附加字段
    private int sort;//排序
    private int editingTag;//如果点击编辑的标签则大于0

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public TalentImageTagLine getLine1() {
        return line1;
    }

    public void setLine1(TalentImageTagLine line1) {
        this.line1 = line1;
    }

    public TalentImageTagLine getLine2() {
        return line2;
    }

    public void setLine2(TalentImageTagLine line2) {
        this.line2 = line2;
    }

    public TalentImageTagLine getLine3() {
        return line3;
    }

    public void setLine3(TalentImageTagLine line3) {
        this.line3 = line3;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getGoods_origin() {
        return goods_origin;
    }

    public void setGoods_origin(String goods_origin) {
        this.goods_origin = goods_origin;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getGoods_image() {
        return goods_image;
    }

    public void setGoods_image(String goods_image) {
        this.goods_image = goods_image;
    }

    public int getEditingTag() {
        return editingTag;
    }

    public void setEditingTag(int editingTag) {
        this.editingTag = editingTag;
    }
}

