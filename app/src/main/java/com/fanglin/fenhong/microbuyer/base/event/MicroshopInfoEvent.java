package com.fanglin.fenhong.microbuyer.base.event;

import com.fanglin.fenhong.microbuyer.base.model.MicroshopInfo;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/6-下午2:53.
 * 功能描述: 我的微店信息EventBus
 *
 */
public class MicroshopInfoEvent {
    private MicroshopInfo microshopInfo;

    public MicroshopInfoEvent(MicroshopInfo info) {
        this.microshopInfo = info;
    }

    public MicroshopInfo getMicroshopInfo() {
        return microshopInfo;
    }
}
