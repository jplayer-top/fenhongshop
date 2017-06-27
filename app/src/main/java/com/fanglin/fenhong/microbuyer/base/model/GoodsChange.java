package com.fanglin.fenhong.microbuyer.base.model;

/**
 * 作者： Created by Plucky on 2015/11/13.
 * 商品详情中规格选择的时候
 */
public class GoodsChange {
    public String goods_image;
    public double goods_price;
    public int goods_storage;
    public String goods_id;

    //限制折扣
    public GoodsDtlPromXianshi xianshi;

    public MiaoshaTag seckilling;//秒杀
}
