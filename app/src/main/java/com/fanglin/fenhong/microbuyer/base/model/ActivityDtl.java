package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/10.
 * modify by lizhixin on 2013/03/30
 */
public class ActivityDtl extends APIUtil {

    public String activity_id;//
    public String activity_title;//
    public List<ActivitySlides> activity_slides;//
    public String activity_desc;//
    public List<ActivityFloor> activity_content;//

    public String share_title;// 分享标题,
    public String share_desc;//  分享描述,
    public String share_img;// 分享图片
    public String resource_tags;//统计数据，透明传输 -- lizhixin


    public List<ActivityGoods> getActGoodsList () {
        List<ActivityGoods> alist = new ArrayList<> ();
        if (activity_content != null && activity_content.size () > 0) {
            for (int i = 0; i < activity_content.size (); i++) {
                List<ActivityGoods> floor_goods = activity_content.get (i).floor_goods;
                if (floor_goods != null && floor_goods.size () > 0) {
                    alist.addAll (floor_goods);
                }
            }
        }
        return alist;
    }

    public ActivityDtl () {
        super ();
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        ActivityDtl adata = new Gson ().fromJson (data, ActivityDtl.class);
                        if (mcb != null) mcb.onData (adata);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onError ("-1");
                    }
                } else {
                    if (mcb != null) mcb.onError (data);
                }
            }
        });
    }

    public void get_activity_detail (String activity_id) {
        if (activity_id == null) return;
        String url = BaseVar.API_GET_ACTIVITY_DTL;
        url += "&activity_id=" + activity_id;
        execute (HttpRequest.HttpMethod.GET, url, null);
    }

    private ActivityDtlModelCallBack mcb;

    public void setModelCallBack (ActivityDtlModelCallBack cb) {
        this.mcb = cb;
    }

    public interface ActivityDtlModelCallBack {
        void onData (ActivityDtl data);

        void onError (String errcode);
    }

    public List<Banner> getBanners () {
        if (activity_slides == null || activity_slides.size () == 0) {
            return null;
        }
        List<Banner> baners = new ArrayList<> ();
        for (int i = 0; i < activity_slides.size (); i++) {
            Banner banner = new Banner ();
            banner.slide_id = i + "";
            banner.index = i;
            banner.image_url = activity_slides.get (i).pic;
            banner.slide_title = i + "";
            banner.link_url = activity_slides.get (i).link;
            baners.add (banner);
        }

        return baners;
    }

   /**
    *
    * 内
    * 部
    * 类
    *
    */

    /** 活动轮播图*/
    public class ActivitySlides {
        public String pic;
        public String link;
    }
}
