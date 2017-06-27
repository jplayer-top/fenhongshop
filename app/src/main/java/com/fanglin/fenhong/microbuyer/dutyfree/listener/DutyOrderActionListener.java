package com.fanglin.fenhong.microbuyer.dutyfree.listener;

import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyOrder;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/21-下午5:48.
 * 功能描述: 极速免税店 订单操作
 */
public interface DutyOrderActionListener {

    String DELETE = "删除订单";
    String CANCEL = "取消订单";
    String PAY = "付款";
    String DETAIL = "查看详情";
    String RECEIVE = "一键收货";

    void onDelete(DutyOrder order, int section);

    void onCancel(DutyOrder order, int section);

    void onPay(DutyOrder order, int section);

    void onReceive(DutyOrder order, int section);

    void onDetail(DutyOrder order, int section);
}
