package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.mapandlocate.baiduloc.FHLocation;
import com.fanglin.fenhong.microbuyer.FHApp;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fhlib.other.FHLog;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/12/14.
 * 分红全球购GPS定位
 */
public class GPSLocation extends APIUtil {
    public String member_id;//
    public String token;//   登录令牌
    public String member_mobile;//
    public String member_nickname;//

    public String member_pos;// 会员定位标识,格式：会员id_定位序号，目前最多存3个常用地位

    public String country;//
    public String province;//
    public String city;//
    public String street;//
    public String address;// 地标
    public double latitude;//
    public double longitude;//

    public GPSLocation () {
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    FHLog.d ("GPSLocation", "定位记录成功:" + data);
                } else {
                    FHLog.d ("GPSLocation", "定位记录失败:" + data);
                }
            }
        });
    }

    public void record (FHApp app, Member member) {
        if (member == null || app == null) return;
        List<FHLocation> gps = FHCache.getGPS (app);
        if (gps == null || gps.size () == 0) return;
        FHLocation recordLocation = gps.get (0);
        if (recordLocation == null) return;

        member_id = member.member_id;
        token = member.token;
        member_mobile = member.member_mobile;
        member_nickname = member.member_nickname;

        member_pos = member_id + "_1";

        country = recordLocation.country;
        province = recordLocation.province;
        city = recordLocation.city;
        street = recordLocation.street;
        address = recordLocation.address;
        latitude = recordLocation.latitude;
        longitude = recordLocation.longitude;


        String json = new Gson ().toJson (this);
        FHLog.d ("GPSLocation", json);
        RequestParams params = new RequestParams ();
        params.addBodyParameter ("info", json);
        execute (HttpRequest.HttpMethod.POST, BaseVar.API_GPS, params);
    }
}
