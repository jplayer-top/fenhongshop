package com.fanglin.fenhong.microbuyer.base.event;

import com.fanglin.fenhong.microbuyer.base.model.LinkGoods;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/16-下午3:57.
 * 功能描述: 关联商品操作本地数据库EventBus事件
 */
public class LinkGoods4ImageTagEvent {
    private boolean isAdd;//关联 !取消关联
    private LinkGoods goods;//

    /**
     * ------ 方案一 ------
     */
    public LinkGoods4ImageTagEvent(boolean isAdd, LinkGoods goods) {
        this.isAdd = isAdd;
        this.goods = goods;
        LinkGoodsEvent.opListGoodsByOne(isAdd, goods);
    }

    public LinkGoods getGoods() {
        return goods;
    }



    /**
     * 是否为添加
     *
     * @return boolean
     */
    public boolean isAdd() {
        return isAdd;
    }

}
