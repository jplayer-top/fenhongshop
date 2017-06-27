package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;
import android.text.Spanned;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/23.
 */
public class CartCheckGoods {
    public String goods_id;
    public boolean is_mansong;//标示是否为满送商品
    public String mansong_desc;//满多少件送

    public String goods_name;// 商品名称,
    public double goods_price;// 商品价格,
    public String goods_image;// 商品图片,
    public int goods_num;// 商品数量,
    public double hs_rate;// 商品关税税率 （小数，如0.5 表示 50%关税）

    public List<Bundlings> bl_list;//优惠套装
    public GoodsDtlPromXianshi xianshi;//限时折扣
    public MiaoshaTag seckilling;//秒杀标签

    public boolean is_freegift;//标示是否为免费送的商品
    public List<BaseGoods> gifts;//免费送的商品
    public int is_activity;// 是否参加平台活动（如优惠券红包）（0 否   1 是）


    public List<CartCheckGoods> convertGift2CartCheckGoodsList() {
        if (gifts != null && gifts.size() > 0) {
            List<CartCheckGoods> res = new ArrayList<>();
            for (int i = 0; i < gifts.size(); i++) {
                CartCheckGoods checkGoods = new CartCheckGoods();
                BaseGoods gift = gifts.get(i);
                checkGoods.goods_id = gift.goods_id;
                checkGoods.goods_image = gift.goods_image;
                checkGoods.goods_name = gift.goods_name;
                checkGoods.goods_num = gift.goods_num;
                checkGoods.goods_price = gift.goods_price;
                checkGoods.is_freegift = true;
                res.add(checkGoods);
            }
            return res;
        } else {
            return null;
        }
    }

    public List<String> getImages() {
        if (bl_list != null && bl_list.size() > 0) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < bl_list.size(); i++) {
                String img = bl_list.get(i).goods_image;
                if (BaseFunc.isValidUrl(img)) {
                    list.add(img);
                }
            }
            return list;
        } else {
            return null;
        }
    }

    public String[] getImageStr() {
        if (bl_list != null && bl_list.size() > 0) {
            String[] list = new String[bl_list.size()];
            for (int i = 0; i < bl_list.size(); i++) {
                String img = bl_list.get(i).goods_image;
                if (BaseFunc.isValidUrl(img)) {
                    list[i] = img;
                }
            }
            return list;
        } else {
            return null;
        }
    }

    public String getActivityDesc(Context c) {
        //Modified By Plucky 秒杀商品不可使用优惠券
        if (is_activity == 0 || seckilling != null) {
            return c.getString(R.string.fmt_goods_use_bonus);
        } else {
            return null;
        }
    }

    public Spanned getBundlingActivityDesc(Context c) {
        if (bl_list != null && bl_list.size() > 0) {
            String res = c.getString(R.string.fmt_bundling_use_bonus);
            for (int i = 0; i < bl_list.size(); i++) {
                Bundlings blgoods = bl_list.get(i);
                if (blgoods != null && blgoods.is_activity == 0) {
                    return BaseFunc.fromHtml(res);
                }
            }
            return null;
        } else {
            return null;
        }
    }
}
