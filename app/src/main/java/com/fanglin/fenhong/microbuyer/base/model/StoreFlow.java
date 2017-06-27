package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;
import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/7/21-上午11:57.
 * 功能描述: 新版店铺首页商品+广告组合
 */
public class StoreFlow extends APIUtil {
    private String type_id;//   类型id (1是广告图，2是商品列表)
    private String content;//   广告图/列表名称
    private List<GoodsList> goods_list;//商品列表
    private String goods_url;// 广告图链接

    public StoreFlow() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                List<StoreFlow> list;
                if (isSuccess) {
                    try {
                        list = new Gson().fromJson(data, new TypeToken<List<StoreFlow>>() {
                        }.getType());
                    } catch (Exception e) {
                        list = null;
                    }
                } else {
                    list = null;
                }
                if (callBack != null) {
                    callBack.onList(list);
                }
            }
        });
    }

    public void getStoreFlow(String storeId) {
        String url = BaseVar.API_GET_STORE_FLOW;
        url += "&store_id=" + storeId;
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    public interface StoreFlowModelCallBack {
        void onList(List<StoreFlow> list);
    }

    private StoreFlowModelCallBack callBack;

    public void setCallBack(StoreFlowModelCallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * getter&setter
     */
    public String getType_id() {
        return type_id;
    }

    public String getContent() {
        return content;
    }

    public List<GoodsList> getGoods_list() {
        return goods_list;
    }

    public String getGoods_url() {
        return goods_url;
    }

    public boolean isBanner() {
        return TextUtils.equals("1", type_id);
    }


    public static List<StoreFlow> getTestData(Context context) {
        try {
            String json = context.getString(R.string.store_flow);
            return new Gson().fromJson(json, new TypeToken<List<StoreFlow>>() {
            }.getType());
        } catch (Exception e) {
            return null;
        }
    }
}
