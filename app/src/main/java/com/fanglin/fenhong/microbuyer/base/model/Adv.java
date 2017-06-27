package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 作者： Created by Plucky on 15-10-11.
 * 商城广告系统
 * modify by lizhixin on 2016/03/30 添加统计字段
 */
public class Adv extends APIUtil {
    public String adv_title;//  广告标题
    public String adv_pic;//  广告图片
    public String adv_link;// 广告链接
    public String resource_tags;//统计数据，透明传输

    public Adv() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<Adv> lst = new Gson().fromJson(data, new TypeToken<List<Adv>>() {
                        }.getType());
                        if (mcb != null) mcb.onAdvList(lst);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onAdvError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onAdvError(data);
                }
            }
        });
    }

    public void getList(int area, String type) {
        String url = BaseVar.API_GET_ADV_LIST;
        url += "&area=" + area;
        url += "&type=" + type;//
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private AdvModelCallBack mcb;

    public void setModelCallBack(AdvModelCallBack cb) {
        this.mcb = cb;
    }

    public interface AdvModelCallBack {
        void onAdvError(String errcode);

        void onAdvList(List<Adv> list);
    }

}
