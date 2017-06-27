package com.fanglin.fenhong.microbuyer.base.event;

import com.fanglin.fenhong.microbuyer.base.model.CartAction;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/5/18-上午9:07.
 * 功能描述:预计将购物车数量替换为EventBus（以前是广播的形式）
 * 暂不实现，存在单个加入购物车的操作
 */
public class CartActionEvent {
    private CartAction cartAction;

    public CartActionEvent(CartAction action) {
        this.cartAction = action;
    }

    public CartAction getCartAction() {
        return cartAction;
    }
}
