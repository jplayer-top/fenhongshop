package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

/**
 * 国家馆 分类和广告 请求webservice
 * Created by lizhixin on 2015/11/5.
 */
public class WSNatPavGetActivityDetail extends APIUtil {

    public String activity_id;
    public String activity_title;
    public String activity_desc;
    public String share_title;
    public String share_desc;
    public String share_img;
    public String activity_banner;
    public ArrayList<NationalPavClassifyEntity> activity_classes;//分类
    public ArrayList<NationalPavAdvEntity> activity_advs;//分类
    public String resource_tags;//统计数据，透明传输

    private WSGetActivityDetailCallBack mcb;

    public WSNatPavGetActivityDetail() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {

                if (isSuccess) {
                    try {
                        WSNatPavGetActivityDetail adata = new Gson().fromJson(data, WSNatPavGetActivityDetail.class);
                        if (mcb != null) mcb.onWSGetActivityDetailSuccess(adata);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (mcb != null) mcb.onWSGetActivityDetailError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onWSGetActivityDetailError(data);
                }
            }
        });
    }

    public void getActivityDetail (Member m, String activity_id) {

        String url = BaseVar.API_GET_ACTIVITY_DTL + "&activity_id=" + activity_id + "&type=guojiaguan";

        execute (HttpRequest.HttpMethod.GET, url, null);
    }

    public void setWSGetActivityDetailCallBack(WSGetActivityDetailCallBack mcb) {
        this.mcb = mcb;
    }

    public interface WSGetActivityDetailCallBack {
        void onWSGetActivityDetailError(String errcode);

        void onWSGetActivityDetailSuccess (WSNatPavGetActivityDetail data);
    }

}
