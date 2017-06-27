package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/7/9.
 * modify by lizhixin on 2016/03/30 添加统计字段
 */
public class Banner extends APIUtil {
    public int index;
    public String slide_id;
    public String slide_title;
    public String link_url;
    public String image_url;
    public String sort_order;
    public String resource_tags;//统计数据，透明传输

    public Banner() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<Banner> list = new Gson().fromJson(data, new TypeToken<List<Banner>>() {
                        }.getType());
                        if (mcb != null) mcb.onBannerList(list);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onBannerError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onBannerError(data);
                }
            }
        });
    }

    public void getList(String type) {
        String url = BaseVar.API_GET_INDEX_SLIDERS;
        if (type != null) {
            url += "&type=" + type;
        }
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private BannerModelCallBack mcb;

    public void setModelCallBack(BannerModelCallBack cb) {
        this.mcb = cb;
    }

    public interface BannerModelCallBack {
        void onBannerError(String errcode);

        void onBannerList(List<Banner> list);
    }

    public static List<Banner> getTest() {
        List<Banner> banners = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Banner b = new Banner();
            b.image_url = "http://pic.fenhongshop.com/slides/20160914/57d8c4eeb347c.jpg";
            b.link_url = "http://1991th.com";
            banners.add(b);
        }
        return banners;
    }
}
