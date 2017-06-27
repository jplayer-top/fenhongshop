package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/8.
 * modify by lizhixin on 2015/12/17
 */
public class GoodsList extends BaseGoods {

    public float evaluation_stars;//评价星级
    public int evaluation_count;//评价数量
    public String talent_deductid;//推广达人memberId

    public GoodsList () {
        super ();
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<GoodsList> lst = new Gson ().fromJson (data, new TypeToken<List<GoodsList>> () {
                        }.getType ());
                        if (mcb != null) mcb.onGLMCList (lst);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onGLMCError ("-1");
                    }
                } else {
                    if (mcb != null) mcb.onGLMCError (data);
                }
            }
        });
    }

    /**
     *  获取商城商品(get)
     *  GoodsFilter
     *  is_sale    是否仅显示可售商品  （ 0  否   1 是 ）
     */
    public void get_goods (GoodsFilter gf) {
        String url = BaseVar.API_GET_GOODS;

        url += "&num=" + gf.num;

        url += "&curpage=" + gf.curpage;

        if (!TextUtils.isEmpty (gf.gc_id)) {
            url += "&gc_id=" + gf.gc_id;
        }

        if (!TextUtils.isEmpty (gf.sid)) {
            url += "&sid=" + gf.sid;
        }

        if (!TextUtils.isEmpty (gf.bid)) {
            url += "&bid=" + gf.bid;
        }

        if (!TextUtils.isEmpty (gf.key)) {
            url += "&key=" + gf.key;
        }

        if (!TextUtils.isEmpty (gf.scid)) {
            url += "&scid=" + gf.scid;
        }

        if (!TextUtils.isEmpty (gf.is_sale)) {
            url += "&is_sale=" + gf.is_sale;
        }

        if (gf.sort > -1) {
            url += "&sort=" + gf.sort;
        }

        if (gf.order > -1) {
            url += "&order=" + gf.order;
        }

        if (gf.gc_deep > -1) {
            url += "&gc_deep=" + gf.gc_deep;
        }

        if (gf.is_own > 0) {
            url += "&is_own=" + gf.is_own;
        }

        execute (HttpRequest.HttpMethod.GET, url, null);
    }

    private GoodsListModelCallBack mcb;


    public void setModelCallBack (GoodsListModelCallBack cb) {
        this.mcb = cb;
    }

    public interface GoodsListModelCallBack {
        void onGLMCList (List<GoodsList> list);

        void onGLMCError (String errcode);
    }

    /**
     *
     * A
     * B
     * C
     * D
     * E
     * F
     * G
     * H
     * I
     *
     */
    public static List<GoodsList> getTestData () {
        List<GoodsList> l = new ArrayList<> ();
        for (int i = 0; i < 15; i++) {
            GoodsList g = new GoodsList ();
            g.goods_name = "测试商品+" + "商品名称";
            g.goods_price = 12.45;
            g.goods_marketprice = 23.65;
            g.goods_image = BaseVar.DEFAULT_BANNER;
            g.goods_source = 0;
            l.add (g);
        }
        return l;
    }


}
