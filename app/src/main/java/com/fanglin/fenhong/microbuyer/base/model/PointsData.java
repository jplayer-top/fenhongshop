package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/29.
 * 我的积分页面数据
 */
public class PointsData extends APIUtil {

    public String allpoints;
    public List<Points> points_list;

    public PointsData ( ) {
        super ();
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        PointsData adata = new Gson ().fromJson (data, PointsData.class);
                        if (mcb != null) mcb.onPDData (adata);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onPDError ("-1");
                    }
                } else {
                    if (mcb != null) mcb.onPDError (data);
                }
            }
        });
    }

    public void getList (Member m, int start) {
        if (m == null) {
            if (mcb != null) mcb.onPDError ("-1");
            return;
        }
        RequestParams params = new RequestParams ();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);
        params.addBodyParameter ("start", String.valueOf (start));
        params.addBodyParameter ("num", String.valueOf (BaseVar.REQUESTNUM));

        execute (HttpRequest.HttpMethod.POST, BaseVar.API_GET_POINTS, params);
    }

    private PointsDataModelCallBack mcb;

    public void setModelCallBack (PointsDataModelCallBack cb) {
        this.mcb = cb;
    }

    public interface PointsDataModelCallBack {
        void onPDError (String errcode);

        void onPDData (PointsData data);
    }
}
