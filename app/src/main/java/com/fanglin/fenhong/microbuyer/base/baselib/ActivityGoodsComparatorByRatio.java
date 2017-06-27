package com.fanglin.fenhong.microbuyer.base.baselib;

import com.fanglin.fenhong.microbuyer.base.model.ActivityGoods;

import java.util.Comparator;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/10/9-下午4:43.
 * 功能描述:首页比较两个商品的佣金比例
 */
public class ActivityGoodsComparatorByRatio implements Comparator {

    private boolean isAsc;//升序

    public ActivityGoodsComparatorByRatio(boolean isAsc) {
        this.isAsc = isAsc;
    }

    @Override
    public int compare(Object lhs, Object rhs) {
        float reword0 = ((ActivityGoods) lhs).getRewordRatio();
        float reword1 = ((ActivityGoods) rhs).getRewordRatio();
        if (reword0 == reword1) {
            return 0;
        } else {
            if (reword0 > reword1) {
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
