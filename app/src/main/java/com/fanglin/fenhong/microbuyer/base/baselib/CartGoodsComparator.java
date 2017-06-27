package com.fanglin.fenhong.microbuyer.base.baselib;

import com.fanglin.fenhong.microbuyer.base.model.GoodsinCart;

import java.util.Comparator;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/8/19-上午11:08.
 * 功能描述: 购物车商品按添加时间add_time排序
 */
public class CartGoodsComparator implements Comparator {

    private boolean isAsc;//升序

    public CartGoodsComparator(boolean isAsc) {
        this.isAsc = isAsc;
    }

    @Override
    public int compare(Object lhs, Object rhs) {
        GoodsinCart lg = (GoodsinCart) lhs;
        GoodsinCart rg = (GoodsinCart) rhs;
        if (lg.add_time == rg.add_time) {
            return 0;
        } else {
            if (lg.add_time > rg.add_time) {
                return isAsc ? 1 : -1;
            } else {
                return isAsc ? -1 : 1;
            }
        }
    }

    @Override
    public boolean equals(Object object) {
        return false;
    }
}
