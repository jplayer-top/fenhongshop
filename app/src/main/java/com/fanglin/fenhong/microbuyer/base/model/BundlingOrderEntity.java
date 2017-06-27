package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;

import java.util.List;

/**
 * 订单 促销商品 优惠套装 实体类
 * Created by lizhixin on 2016/1/8.
 */
public class BundlingOrderEntity {

    public String bl_name;//优惠套装名称
    public String bl_price;//优惠套装价格
    public int bl_num;//优惠套装购买数量
    public List<Bundlings> bl_list;//优惠套装商品列表

    public String[] getImageStr () {
        if (bl_list != null && bl_list.size () > 0) {
            String[] list = new String[bl_list.size ()];
            for (int i = 0; i < bl_list.size (); i++) {
                String img = bl_list.get (i).goods_image;
                if (BaseFunc.isValidUrl(img)) {
                    list[i] = img;
                }
            }
            return list;
        } else {
            return null;
        }
    }

    /**
     * 判断套装是否有效
     */
    public int getBLState () {
        int result = 1;//有效
        for (Bundlings bundlings : bl_list) {
            if (bundlings.bl_state == 0) {
                result = 0;//只要有一个失效则整个套装变为失效
                break;
            }
        }
        return result;
    }



}
