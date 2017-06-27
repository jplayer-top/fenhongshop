package com.fanglin.fenhong.microbuyer.base.model;

/**
 * Created by admin on 2015/12/2.
 */
public class FHExpress {

    public String	state_info;// 物流状态（20:已付款, 30:已发货, 40:已收货）
    public String   country_flag;// 国内/国外（0:国内, 1:国外）
    public Express100   foreign_express_info;//国外物流信息
    public Express100   home_express_info;// 国内物流信息




}
