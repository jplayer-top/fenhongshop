package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;
import android.text.Spanned;
import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.google.gson.internal.LinkedTreeMap;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/25.
 */
public class OrderGoods {
    public String rec_id;//订单商品表编号
    public String goods_id;//商品编号
    public String goods_name;//商品名称
    public double goods_price;//商品价格
    public int goods_num;//商品数量
    public String goods_image;//
    public double goods_pay_price;//商品实际成交价
    public int evaluation_state;//商品评价状态：0:未评价 | 1:已评价
    public String if_refund;//0  0-表示不可以  1-表示可以

    public String store_id;//
    public String store_name;//
    public LinkedTreeMap goods_spec;//商品规格
    public String order_id;//订单编号

    public String goods_state;//商品状态 1: 正常
    public String goods_type;//商品类型 1:默认 | 2:团购商品 | 3:限时折扣 | 4:组合套装 | 5:赠品
    public BundlingOrderEntity bundling;//优惠套装实体类
    public GoodsDtlPromXianshi xianshi;//限时折扣实体类

    public int is_activity;// 是否参加平台活动（如优惠券红包）（0 否   1 是）


    /**
     * 是否显示 退款/退货 按钮
     */
    public boolean ifRefund(int order_state) {
        if (order_state <= 20) {
            return false;
        } else {
            return TextUtils.equals(if_refund, "1") && (order_state == 40);
        }
    }

    public String getActivityDesc(Context c) {
        if (is_activity == 0) {
            return c.getString(R.string.fmt_goods_use_bonus);
        } else {
            return null;
        }
    }

    public Spanned getBundlingActivityDesc(Context c) {
        if (bundling != null) {
            String res = c.getString(R.string.fmt_bundling_use_bonus);
            List<Bundlings> bl_list = bundling.bl_list;
            if (bl_list != null && bl_list.size() > 0) {
                for (int i = 0; i < bl_list.size(); i++) {
                    Bundlings blgoods = bl_list.get(i);
                    if (blgoods != null && blgoods.is_activity == 0) {
                        return BaseFunc.fromHtml(res);
                    }
                }
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * 是否为套装
     *
     * @return true false
     */
    public boolean isBundling() {
        return bundling != null && bundling.getImageStr() != null && bundling.getImageStr().length > 0;
    }

}
