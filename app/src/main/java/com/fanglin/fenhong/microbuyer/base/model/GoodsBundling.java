package com.fanglin.fenhong.microbuyer.base.model;

import java.util.List;

/**
 * 促销商品套装列表 实体类
 * Created by lizhixin on 2015/12/31.
 */
public class GoodsBundling{

    public List<BundlingItem> goods_list;//套装商品列表
    public double cost_price;//总原价
    public double current_price;//总现价
    public double freight;//运费
    public String bl_id;//套装ID,用于加入购物车

    public int position;//下标,套装几
}
