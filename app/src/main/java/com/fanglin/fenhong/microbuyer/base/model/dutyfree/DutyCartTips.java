package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/12/2-上午11:35.
 * 功能描述: 购物车关于折扣及凑单的提示
 */
public class DutyCartTips {
    private String disparity_name;// "折扣"
    private String disparity_price;// "再购26229元立享8折哦~"
    private String disparity_url;// "www.fenhongshop.com"

    public String getDisparityName() {
        return disparity_name;
    }

    public String getDisparityPrice() {
        return disparity_price;
    }

    public String getDisparityUrl() {
        return disparity_url;
    }

    public void setDisparityName(String disparity_name) {
        this.disparity_name = disparity_name;
    }

    public void setDisparityPrice(String disparity_price) {
        this.disparity_price = disparity_price;
    }

    public void setDisparityUrl(String disparity_url) {
        this.disparity_url = disparity_url;
    }
}
