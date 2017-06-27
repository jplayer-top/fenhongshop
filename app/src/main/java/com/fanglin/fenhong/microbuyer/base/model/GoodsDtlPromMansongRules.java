package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;
import android.text.Spanned;
import android.text.TextUtils;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import java.util.List;

/**
 * 商品促销 满额优惠 优惠列表 实体类
 * Created by lizhixin on 2016/1/5.
 */
public class GoodsDtlPromMansongRules {

    public String egt_amount;//满额
    public String minus_amount;//立减 （有才返回）
    public BaseGoods gift;//满赠,这里直接用商品基础类，因属性相同 （有才返回）

    /**
     * 拼接显示标题
     */
    public Spanned getManjianTitle(Context mContext) {
        //拼接字符串 3段
        String str = mContext.getString(R.string.prom_text_man);
        if (!TextUtils.isEmpty(egt_amount)) {
            str += BaseFunc.truncDouble(getEgt_amount(), 1);
        }

        if (!TextUtils.isEmpty(minus_amount)) {
            str += mContext.getString(R.string.prom_text_jian) + BaseFunc.truncDouble(getMinus_amount(), 1);
        }

        if (gift != null) {
            str += mContext.getString(R.string.prom_text_zeng) + gift.goods_name;
        }

        return BaseFunc.fromHtml(str);
    }

    public String getManjian(Context mContext) {
        String str = "满";
        if (!TextUtils.isEmpty(egt_amount)) {
            str += BaseFunc.truncDouble(getEgt_amount(), 1);
        }
        if (!TextUtils.isEmpty(minus_amount)) {
            str += "减" + BaseFunc.truncDouble(getMinus_amount(), 1);
        }
        return str;
    }

    public String getMansong(Context mContext) {
        String str = "满";
        if (!TextUtils.isEmpty(egt_amount)) {
            str += BaseFunc.truncDouble(getEgt_amount(), 1);
        }
        if (gift != null) {
            str += "赠 " + gift.goods_name;
        }
        return str;
    }

    public double getEgt_amount() {
        try {
            return Double.parseDouble(egt_amount);
        } catch (Exception e) {
            return 0;
        }
    }

    public double getMinus_amount() {
        try {
            return Double.parseDouble(minus_amount);
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     * 根据当前金额计算满送区间
     */
    public static String getLeft(Context mContext, List<GoodsDtlPromMansongRules> manlist, double total) {


        if (manlist != null && manlist.size() > 0) {
            for (int i = 0; i < manlist.size(); i++) {
                GoodsDtlPromMansongRules rules = manlist.get(i);
                double left = rules.getEgt_amount() - total;
                if (left > 0) {
                    String fmt = mContext.getString(R.string.count_num_left_manjian);
                    return String.format(fmt, left);
                }
            }
            return "";
        } else {
            return "";
        }
    }

    public static String getRules(Context mContext, List<GoodsDtlPromMansongRules> manlist) {
        StringBuilder sb = new StringBuilder();
        if (manlist != null && manlist.size() > 0) {
            for (int i = 0; i < manlist.size(); i++) {
                GoodsDtlPromMansongRules rules = manlist.get(i);
                if (i > 0) sb.append("\n");
                sb.append(rules.getManjianTitle(mContext));

            }
        }
        return sb.toString();
    }

    public CartCheckGoods convertGift2CartCheckGoods(String man_desc) {
        if (gift != null) {
            CartCheckGoods checkGoods = new CartCheckGoods();
            checkGoods.goods_id = gift.goods_id;
            checkGoods.goods_image = gift.goods_image;
            checkGoods.goods_name = gift.goods_name;
            checkGoods.goods_num = gift.goods_num;
            checkGoods.goods_price = gift.goods_price;
            checkGoods.is_mansong = true;
            checkGoods.mansong_desc = man_desc;
            checkGoods.is_activity = gift.is_activity;
            return checkGoods;
        } else {
            return null;
        }
    }
}
