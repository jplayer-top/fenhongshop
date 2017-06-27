package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/11/28.
 * 现金券
 */
public class BonusData extends APIUtil {
    public List<String> coupon_counts;// ["2","0","0"], 未使用数量，已使用数量，已过期数量
    public List<Bonus> coupon_list;//现金券列表


    public BonusData() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        BonusData adata = new Gson().fromJson(data, BonusData.class);
                        if (mcb != null) mcb.onBDData(adata);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onBDError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onBDError(data);
                }
            }
        });
    }

    public void getData(Member m, int state, int curpage) {
        if (m == null) {
            if (mcb != null) mcb.onBDError("-1");
            return;
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("mid", m.member_id);
        params.addBodyParameter("token", m.token);
        params.addBodyParameter("num", String.valueOf(BaseVar.REQUESTNUM));
        params.addBodyParameter("curpage", String.valueOf(curpage));
        params.addBodyParameter("state", String.valueOf(state));
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_GET_COUPON_LIST, params);
    }

    private BDModelCallBack mcb;

    public void setModelCallBack(BDModelCallBack cb) {
        this.mcb = cb;
    }

    public interface BDModelCallBack {
        void onBDError(String errcode);

        void onBDData(BonusData data);
    }


    /*获取测试数据*/
    public static BonusData getTest() {
        BonusData data = new BonusData();
        String c = "[\"2\",\"0\",\"0\"]";
        data.coupon_counts = new Gson().fromJson(c, new TypeToken<List<String>>() {
        }.getType());
        data.coupon_list = new ArrayList<>();
        data.coupon_list.add(Bonus.getTest());
        data.coupon_list.add(Bonus.getTest());
        return data;
    }
}
