package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 作者： Created by Plucky on 15-10-17.
 */
public class StoreHomeNum extends APIUtil {
    public int all_goods_number = 0;// 全部商品数量
    public int new_goods_number = 0;//  上新商品数量

    public StoreHomeNum() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {

                    try {
                        StoreHomeNum num = new Gson().fromJson(data, StoreHomeNum.class);
                        if (mcb != null) mcb.onSHNData(num);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onSHNError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onSHNError(data);
                }
            }
        });
    }

    public void getData(String store_id) {
        if (store_id == null) {
            if (mcb != null) mcb.onSHNError("-1");
            return;
        }
        String url = BaseVar.API_GET_STORE_HOME_NUM + "&store_id=" + store_id;
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private SHNModelCallBack mcb;

    public void setModelCallBack(SHNModelCallBack cb) {
        mcb = cb;
    }

    public interface SHNModelCallBack {
        void onSHNError(String errcode);

        void onSHNData(StoreHomeNum num);
    }
}
