package com.fanglin.fenhong.microbuyer.base.event;

/**
 * 青岛芳林信息
 * Created by Plucky on 2017/1/3.
 * 功能描述: 极速免税店 人员身份变化后通知事件
 * 如：充值成功后、购买成为VIP后
 */

public class DutyfreeVipEvent {
    private boolean isVip;

    public DutyfreeVipEvent(boolean isVip) {
        this.isVip = isVip;
    }

    public boolean isVip() {
        return isVip;
    }
}
