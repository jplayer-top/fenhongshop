package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;

/**
 * 作者： Created by Plucky on 15-9-10.
 */
public class GoodsFilter {
    public int num = BaseVar.REQUESTNUM;//分页数量/每次请求数量
    public int curpage = 1;//当前页/第几次请求
    public String gc_id;//分类id （可选）
    public String sid;//店铺id （可选）
    public String bid;//品牌id （可选）
    public String key;//商品关键字 （可选）
    public int sort = -1;//（1为销量，2为商品价格，3为推荐，4为人气）
    public int order = -1;//(1为升序，2为降序）
    public int gc_deep = -1;//分类层级 （可选，默认为3）
    public String scid;
    public int is_own = -1;//是否仅显示自营商品 （ 否 不必传   是 传 1 ）
    public String is_sale;//是否仅显示可售商品  （ 0  否   1 是 ）
}
