package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import android.content.Context;
import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;

import java.text.DecimalFormat;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/10-上午9:16.
 * 功能描述: 商品基本属性
 */
public class BaseProduct extends APIUtil {
    private String product_id;// "1"
    private String product_name;// "伊思蜗牛霜 60ML"
    private String product_img_url;// "http://chimg.lottedfs.com/image/product/m/2711806490_01_m.jpg"

    private String member_price_rmb;// "296.56"
    private String product_desc_display;// 商品说明

    private String brand_name;//品牌名称
    private String goods_url;//预留字段  如果返回则进入该页面 否则进入原生商品详情页

    //去掉 仅返回 item_price item_label
    private String item_price;
    private String item_label;

    private double real_price_dollar;// "49.00"
    private double real_price_rmb;//人民币价格

    public String getPriceDollar4Show() {
        DecimalFormat decimalFormat = new DecimalFormat("$#0.00");
        return decimalFormat.format(real_price_dollar);
    }

    public String getPriceGray() {
        return getPriceDollar4Show() + getPriceRmb4Detail();
    }


    public String getPriceRmb4Show() {
        DecimalFormat decimalFormat = new DecimalFormat("¥#0.00");
        return decimalFormat.format(real_price_rmb);
    }

    public String getPriceRmb4Detail() {
        return "(约" + getPriceRmb4Show() + ")";
    }

    public String getProductName() {
        return product_name;
    }

    public String getProductImgUrl() {
        return product_img_url;
    }

    public String getProductId() {
        return product_id;
    }

    public String getPrice4Show() {
        String dollar = getPriceDollar4Show();
        String rmb = getPriceRmb4Detail();
        return dollar + rmb;
    }

    public String getBrandName() {
        return brand_name;
    }

    public boolean isVip(Context mContext) {
        int mType = FHCache.getMemberType(mContext);
        return mType >= 20;
    }

    public String getGoods_url() {
        return goods_url;
    }

    public boolean showItem() {
        return !TextUtils.isEmpty(item_label) && !TextUtils.isEmpty(item_price);
    }

    public String getItemLabel() {
        return item_label;
    }

    public String getItemPrice() {
        return item_price;
    }
}
