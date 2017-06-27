package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/13.
 * 精选微店列表
 */
public class FancyMShop extends APIUtil {
    public String shop_id;//              微店id
    public String shop_name;//            微店名称
    public String shop_banner;//          微店店招图片
    public String shop_theme;//           微店主题
    public String shop_scope;//           微店介绍
    public String store_id;//             相关店铺id
    public String store_name;//           相关店铺名称
    public String shop_logo;//            微店logo
    public List<GoodsList> goods;//       精选微店经营的商品


    public FancyMShop () {
        super ();
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<FancyMShop> list = new Gson ().fromJson (data, new TypeToken<List<FancyMShop>> () {
                        }.getType ());
                        if (mcb != null) mcb.onFMSList (list);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onFMSError ("-1");
                    }
                } else {
                    if (mcb != null) mcb.onFMSError (data);
                }
            }
        });
    }

    /**
     *
     * 获取精选微店列表分类
     *
     **/
    public void getList (String cid, int num, int curpage) {
        String url = BaseVar.API_GET_MSHOP_LIST;
        if (cid != null) url += "&cid=" + cid;
        url += "&num=" + num;
        url += "&curpage=" + curpage;
        execute (HttpRequest.HttpMethod.GET, url, null);
    }


    private FMSModelCallBack mcb;

    public void setModelCallBack (FMSModelCallBack cb) {
        this.mcb = cb;
    }

    public interface FMSModelCallBack {
        void onFMSList (List<FancyMShop> list);

        void onFMSError (String errcode);
    }
}
