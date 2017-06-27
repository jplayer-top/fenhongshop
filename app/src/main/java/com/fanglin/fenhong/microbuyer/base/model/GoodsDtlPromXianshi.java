package com.fanglin.fenhong.microbuyer.base.model;

/**
 * 商品详情促销之限时折扣
 * Created by lizhixin on 2015/12/30.
 */
public class GoodsDtlPromXianshi {

    public String tag;
    public String title;
    public String remark;
    public double price;//促销价
    public double origin_price;// 未满足折扣条件的原价
    public int lower_limit;//最低购买件数
    public long countdown;//倒计时的秒数

    /*返回最低购买多少件才能参与限时促销的提示*/
    public String getLowerLimitText() {
        if (lower_limit > 0)
            return lower_limit + "件起";
        return "";
    }

    public String getLowerLimitTextOfGoodsDtl() {
        return "(" + getLowerLimitText() + ")";
    }

}
