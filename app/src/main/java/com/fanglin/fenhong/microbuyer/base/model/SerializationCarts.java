package com.fanglin.fenhong.microbuyer.base.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/12/31.
 * 序列化的购物车数据
 */
public class SerializationCarts {
    public JSONObject store_cart_list;//购物车商品列表
    public JSONObject mansong;//满送
    public JSONObject free_freight_list;//包邮
    public JSONObject custom_purchase_limit;//
    public JSONObject manselect_info;//满任选

    /**
     * 包邮金额
     */
    public double getFreeFreight(String store_id) {
        if (free_freight_list != null) {
            try {
                return free_freight_list.getDouble(store_id);
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    /**
     * 店铺是否存在优惠活动
     */
    public boolean hasActivity(String store_id) {
        List<GoodsDtlPromMansongRules> ms = getMansong(store_id);
        RenxuanRule renxuanRule = getRenXuan(store_id);
        return (getFreeFreight(store_id) > 0) || (ms != null && ms.size() > 0) || (renxuanRule != null);
    }

    public static SerializationCarts parseData(String data) {
        SerializationCarts serializationCarts;
        try {
            JSONObject json = new JSONObject(data);

            serializationCarts = new SerializationCarts();
            serializationCarts.store_cart_list = json.has("store_cart_list") ? json.getJSONObject("store_cart_list") : null;
            serializationCarts.mansong = json.has("mansong") ? json.getJSONObject("mansong") : null;
            serializationCarts.free_freight_list = json.has("free_freight_list") ? json.getJSONObject("free_freight_list") : null;
            serializationCarts.custom_purchase_limit = json.has("custom_purchase_limit") ? json.getJSONObject("custom_purchase_limit") : null;
            serializationCarts.manselect_info = json.has("manselect_info") ? json.getJSONObject("manselect_info") : null;
        } catch (Exception e) {
            serializationCarts = null;
        }

        return serializationCarts;
    }

    /**
     * 获取店铺满送活动
     */
    public List<GoodsDtlPromMansongRules> getMansong(String store_id) {
        try {
            String man_str = mansong.getString(store_id);
            return new Gson().fromJson(man_str, new TypeToken<List<GoodsDtlPromMansongRules>>() {
            }.getType());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 店铺购买限制
     *
     * @param store_id store_id
     * @return -1 无限制 >0 限制
     */
    public double getStoreLimit(String store_id) {
        if (custom_purchase_limit != null) {
            try {
                return custom_purchase_limit.getDouble(store_id);
            } catch (Exception e) {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public RenxuanRule getRenXuan(String store_id) {
        if (manselect_info != null) {
            try {
                String json = manselect_info.getString(store_id);
                return new Gson().fromJson(json, RenxuanRule.class);
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }
}
