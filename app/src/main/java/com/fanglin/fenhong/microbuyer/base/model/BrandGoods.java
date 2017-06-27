package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/7/19-上午11:39.
 * 功能描述: 品牌聚合页商品
 */
public class BrandGoods extends BaseGoods {

    public BrandGoods() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<ActivityGoods> list = new Gson().fromJson(data, new TypeToken<List<ActivityGoods>>() {
                        }.getType());
                        if (callBack != null) {
                            callBack.onBrandGoodsList(list);
                        }
                    } catch (Exception e) {
                        if (callBack != null) {
                            callBack.onBrandGoodsList(null);
                        }
                    }
                } else {
                    if (callBack != null) {
                        callBack.onBrandGoodsList(null);
                    }
                }
            }
        });
    }

    /**
     * @param brandId brand_id  品牌id
     * @param curpage 当前页  num        每次请求数量
     * @param sort    1为销量，2为商品价格，3为推荐，4为人气）
     * @param order   1为升序，2为降序
     */
    public void getGoodsList(String brandId, int curpage, int sort, int order) {
        String url = BaseVar.API_GET_BRAND_GOODS;
        url += "&brand_id=" + brandId;
        url += "&num=" + BaseVar.REQUESTNUM;
        url += "&curpage=" + curpage;
        url += "&sort=" + sort;
        if (order > 0) {
            url += "&order=" + order;
        }
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    /**
     * 请求品牌默认商品
     *
     * @param brandId String
     */
    public void getDefaultBrandGoods(String brandId) {
        String url = BaseVar.API_GET_BRAND_GOODS;
        url += "&brand_id=" + brandId;
        url += "&num=8";
        url += "&curpage=" + 1;
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private BrandGoodsModelCallBack callBack;

    public void setModelCall(BrandGoodsModelCallBack callback) {
        this.callBack = callback;
    }

    public interface BrandGoodsModelCallBack {
        void onBrandGoodsList(List<ActivityGoods> list);
    }
}
