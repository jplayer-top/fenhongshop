package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import com.fanglin.fenhong.microbuyer.dutyfree.listener.DutyBillActionListener;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author;//Created by Plucky on 2016/11/21-下午1;//34.
 * 功能描述;// 极速免税店 运单
 */
public class DutyWaybill extends DutyAddress {
    private String waybill_id;// "397",
    private String waybill_sn;// "",
    private String merge_sn;// "",
    private String order_sn;// "8000000000041401",
    private String buyer_id;// "1113182",
    private String waybill_type;//
    private String waybill_url;//物流跟踪页面地址
    private int waybill_state;//运单状态 0:未付款 10:待发货 20:待收货 30:已收货 40:支付异常（金额不符）

    private List<DutyOrderGoods> goods_list;//
    private boolean isExpanded;
    private int all_product_num;//

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getExpandDesc() {
        return isExpanded ? "收起" : "查看更多";
    }

    public List<DutyOrderGoods> getOrderGoods4Logic() {
        if (goods_list != null && goods_list.size() > 0 && !isExpanded) {
            return goods_list.subList(0, 1);
        } else {
            return goods_list;
        }
    }

    public String getAllProductNumDesc() {
        return "x" + all_product_num;
    }

    public List<DutyOrderGoods> getOrderGoods() {
        return goods_list;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public String getMerge_sn() {
        return merge_sn;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public String getWaybill_id() {
        return waybill_id;
    }

    public String getWaybill_sn() {
        return waybill_sn;
    }

    public String getWaybill_type() {
        return waybill_type;
    }

    //0:未付款 10:待发货 20:待收货 30:已收货 40:支付异常（金额不符）
    public String[] getBillButtonDesc() {
        String[] str = new String[]{"", ""};
        switch (waybill_state) {
            case 10:
                str[0] = DutyBillActionListener.SUB_DELIVERY;
                str[1] = "";
                break;
            case 20:
                //待收货
                str[0] = DutyBillActionListener.SUB_DELIVERY;
                str[1] = DutyBillActionListener.SUB_RECEIVE;
                break;
            case 30:
                str[0] = DutyBillActionListener.SUB_DELIVERY;
                str[1] = "";
                break;
        }
        return str;
    }

    public String getWaybill_url() {
        return waybill_url;
    }
}
