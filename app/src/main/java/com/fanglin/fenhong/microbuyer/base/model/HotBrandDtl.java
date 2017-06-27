package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/12.
 */
public class HotBrandDtl extends BaseGoods {
    public String resource_tags;//统计用 -- lizhixin

    public HotBrandDtl () {
        super ();
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<HotBrandDtl> list = new Gson ().fromJson (data, new TypeToken<List<HotBrandDtl>> () {
                        }.getType ());
                        if (mcb != null) mcb.onHBDList (list);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onHBDError ("-1");
                    }
                } else {
                    if (mcb != null) mcb.onHBDError (data);
                }
            }
        });
    }

    /**
     * 获取品牌馆商品(get)
     *
     * num 分页数量/每次请求数量
     * curpage 当前页/第几次请求 （第一页对应数字1）
     * brand_id    品牌id
     * sort （1为销量，2为商品价格，3为推荐，4为人气）
     * order (1为升序，2为降序）
     */
    public void getList (String brand_id, int sort, int order, int curpage) {
        String url = BaseVar.API_GET_HOT_BRANDS_GOODS;
        url += "&num=" + 20;
        url += "&brand_id=" + brand_id;
        url += "&sort=" + sort;
        url += "&order=" + order;
        url += "&curpage=" + curpage;

        execute (HttpRequest.HttpMethod.GET, url, null);
    }

    private HBDModelCallBack mcb;

    public void setModelCallBack (HBDModelCallBack cb) {
        this.mcb = cb;
    }

    public interface HBDModelCallBack {
        void onHBDError (String errcode);

        void onHBDList (List<HotBrandDtl> list);
    }
}
