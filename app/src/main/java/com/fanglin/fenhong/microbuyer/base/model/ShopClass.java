package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-10-12.
 * 精品微店分类
 */
public class ShopClass extends APIUtil {

    public String sc_id;
    public String sc_name;
    public String sc_pic;
    public String sc_sort;
    public String sc_parent_id;
    public String if_show;
    public List<ShopClass> subclass;//

    public ShopClass() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<ShopClass> lst = new Gson().fromJson(data, new TypeToken<List<ShopClass>>() {
                        }.getType());
                        if (mcb != null) mcb.onSCList(lst);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onSCError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onSCError(data);
                }
            }
        });
    }

    public void getList(String pid) {
        String url = BaseVar.API_GET_SHOP_CLASS;
        if (pid != null) {
            url += "&pid=" + pid;
        }

        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private ShopClassModelCallBack mcb;

    public void setModelCallBack(ShopClassModelCallBack cb) {
        this.mcb = cb;
    }

    public interface ShopClassModelCallBack {
        void onSCList(List<ShopClass> lst);

        void onSCError(String errcode);
    }
}
