package com.fanglin.fenhong.microbuyer.dutyfree.listener;

import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyWaybill;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/22-下午2:20.
 * 功能描述: 运单
 */
public interface DutyBillActionListener {
    String SUB_DELIVERY = "查看物流";
    String SUB_RECEIVE = "确认收货";

    void onDelivery(DutyWaybill bill);

    void onReceive(DutyWaybill bill);
}
