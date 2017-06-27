package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/21-下午1:45.
 * 功能描述: 极速免税店订单商品
 */
public class DutyOrderGoods extends BaseProduct {
    private int product_num;
    private String product_pay_price;

    private String activity_desc;//活动标示

    public String getProductNumDesc() {
        return "x" + product_num;
    }

    public String getActivityDesc() {
        return activity_desc;
    }

}
