package com.fanglin.fenhong.microbuyer.base.model;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/18.
 */
public class Carts {
    public String store_id;
    public String store_name;
    public int store_source = 0;//0 china 1 korea 2 japan
    public int goods_num = 0;
    public List<GoodsinCart> subitems;


    /**
     * 统计店铺优惠的优惠金额
     * 包括：满任选
     */
    public double store_youhui;

    public boolean hasActivity;
    public double storeFreeFreight;
    public List<GoodsDtlPromMansongRules> mangSongRule;
    public RenxuanRule renxuanRule;
}
