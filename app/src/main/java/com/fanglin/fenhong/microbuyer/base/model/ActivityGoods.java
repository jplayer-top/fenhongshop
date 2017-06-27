package com.fanglin.fenhong.microbuyer.base.model;

/**
 * 作者： Created by Plucky on 2015/10/10.
 * modify by lizhixin on 2015/10/10
 * 楼层商品
 */
public class ActivityGoods extends BaseGoods {
    public String store_id;//
    public String activity_floor;//

    private String resource_tags;//统计

    public String getResource_tags() {
        return resource_tags;
    }
}