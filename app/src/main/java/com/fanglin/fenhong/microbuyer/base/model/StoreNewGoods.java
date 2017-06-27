package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-10-17.
 * 店铺上新商品
 */
public class StoreNewGoods extends APIUtil {
    public String id;// ( 0: 当前月 | 1: 上个月 )
    public String time;// 时间 (eg:2015-10)
    public List<GoodsList> goods_list;//

    public static List<GoodsList> getAllGoodsList(List<StoreNewGoods> newList) {
        if (newList != null && newList.size() > 0) {
            List<GoodsList> list = new ArrayList<>();
            for (int i = 0; i < newList.size(); i++) {
                List<GoodsList> gl = newList.get(i).goods_list;
                if (gl != null && gl.size() > 0) {
                    list.addAll(gl);
                }
            }
            return list;
        } else {
            return null;
        }
    }


    public StoreNewGoods() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<StoreNewGoods> lst = new Gson().fromJson(data, new TypeToken<List<StoreNewGoods>>() {
                        }.getType());
                        if (mcb != null) mcb.onSNGList(lst);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onSNGError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onSNGError(data);
                }
            }
        });
    }

    public void getList(String store_id) {
        if (store_id == null) {
            if (mcb != null) mcb.onSNGError("-1");
            return;
        }
        String url = BaseVar.API_GET_STORE_NEW_GOODS + "&store_id=" + store_id;
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private SNGModelCallBack mcb;

    public void setModelCallBack(SNGModelCallBack cb) {
        this.mcb = cb;
    }

    public interface SNGModelCallBack {
        void onSNGError(String errcode);

        void onSNGList(List<StoreNewGoods> list);
    }
}
