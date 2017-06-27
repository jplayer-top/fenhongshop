package com.fanglin.fenhong.microbuyer.base.model;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/21.
 * 用于计算购物车信息
 */
public class CalculateData {
    public String store_id;//当对某一个分区进行统计时 会传入店铺id
    public double free_freight_limit;//满多少包邮
    public List<GoodsDtlPromMansongRules> manlist;//满送活动

    public double money = 0;
    public double youhui = 0;//店铺优惠的钱
    public int num = 0;
    public double taxfee = 0;
    public int count_selected_id = 0;//所有选中的id
    public int count_all_id = 0;//所有id
    public String selected_id;//123,124,125
    public String selected_id_num;//123|1,124|1,125|2

    public int xnum = 0;//N元任选数量

    public boolean isEditMode = false;

    public boolean getAllChecked() {
        return count_all_id == count_selected_id;
    }


    /**
     * 存在一个被选中的id
     *
     * @return true false
     */
    public boolean hasOneChecked() {
        return selected_id != null && selected_id.length() > 0;
    }
}
