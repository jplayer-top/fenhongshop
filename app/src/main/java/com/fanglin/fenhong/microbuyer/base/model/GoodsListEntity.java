package com.fanglin.fenhong.microbuyer.base.model;

/**
 * 青岛芳林信息
 * Created by Plucky on 2016/12/20.
 * 功能描述: 商品列表传值类
 */

public class GoodsListEntity {
    private String key;//
    private int is_own;//
    private String gc_id;//

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getIsOwn() {
        return is_own;
    }

    public void setIsOwn(int is_own) {
        this.is_own = is_own;
    }

    public String getGcId() {
        return gc_id;
    }

    public void setGcId(String gc_id) {
        this.gc_id = gc_id;
    }
}
