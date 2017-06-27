package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/13.
 */
public class FancyShopGoods extends APIUtil {

    public String shop_id;// 微店id
    public int curpage = 1;// 当前页码/第几次请求 (可选，默认为1)
    public int num = 20;// 获取的记录数 (可选，默认为10)
    public String cid;//分类id (可选, 不传返回全部商品，传0返回未归类的商品，传值返回特定分类的商品)
    public String key;//商品名关键字 (可选)
    public int sort = 3;//（1为上架时间，2为商品价格，3为推荐，4为点击次数）
    public int order = 1;//升降序（1为上，2为降）


    public FancyShopGoods () {
        super ();
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<GoodsList> list = new Gson ().fromJson (data, new TypeToken<List<GoodsList>> () {
                        }.getType ());
                        if (mcb != null) mcb.onFSGList (list);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onFSGError ("-1");
                    }
                } else {
                    if (mcb != null) mcb.onFSGError (data);
                }
            }
        });
    }


    public void getList () {
        if (shop_id == null) {
            if (mcb != null) mcb.onFSGError ("-1");
            return;
        }
        String url = BaseVar.API_GET_MSHOP_GOODS;
        url += "&shop_id=" + shop_id;
        url += "&curpage=" + curpage;
        url += "&num=" + num;
        if (cid != null) {
            url += "&cid=" + cid;
        }
        if (key != null) {
            url += "&key=" + key;
        }
        url += "&sort=" + sort;
        url += "&order=" + order;

        execute (HttpRequest.HttpMethod.GET, url, null);
    }

    private FSGModelCallBack mcb;

    public void setModelCallBack (FSGModelCallBack cb) {
        this.mcb = cb;
    }

    public interface FSGModelCallBack {
        void onFSGList (List<GoodsList> list);

        void onFSGError (String errcode);
    }
}
