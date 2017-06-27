package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;
import android.text.Spanned;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/12/31.
 * 优惠套装
 */
public class Bundlings {
    public String goods_id;// 67,
    public String goods_name;// 雄风浮动三头旋转式电动剃须刀鼻毛器三合一套装,
    public double goods_price;// 优惠后的价格,
    public String goods_image;// 图片,
    public double down_price;// 优惠金额
    public double shop_price;// 商城原价
    public double bl_goods_price;//套装单价;
    public int bl_state;//套餐状态 1:有效 | 0:无效（套装内任意商品如果有停售、预售、预约、售罄状态则为0）

    public int is_activity;// 是否参加平台活动（如优惠券红包）（0 否   1 是）

    public String nation_name;//  国家地区名称
    public String nation_flag;//  国家地区旗帜图片
    public String goods_promise;//商品保证标语

    /*套装总原价*/
    public static double getListOriginal(List<Bundlings> list) {
        if (list != null && list.size() > 0) {
            double result = 0;
            for (int i = 0; i < list.size(); i++) {
                result += list.get(i).bl_goods_price + list.get(i).down_price;
            }
            return result;
        } else {
            return 0;
        }
    }

    /*套装总省价格*/
    public static double getListDownPrice(List<Bundlings> list) {
        if (list != null && list.size() > 0) {
            double result = 0;
            for (int i = 0; i < list.size(); i++) {
                result += list.get(i).down_price;
            }
            return result;
        } else {
            return 0;
        }
    }

    public String getActivityDesc(Context c) {
        if (is_activity == 0) {
            return c.getString(R.string.fmt_goods_use_bonus);
        } else {
            return null;
        }
    }
}
