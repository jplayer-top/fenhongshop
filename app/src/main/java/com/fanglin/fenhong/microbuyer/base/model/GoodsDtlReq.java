package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fhlib.other.FHLog;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 作者： Created by Plucky on 2015/11/15.
 */
public class GoodsDtlReq extends APIUtil {
    public GoodsDtlReq() {
        setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    GoodsDetail detail = new Gson().fromJson(data, GoodsDetail.class);
                    if (detail != null) {
                        FHLog.d("GoodsDetail", "detail is not null");
                        if (mcb != null) mcb.onGDMCData(detail);
                    } else {
                        FHLog.d("GoodsDetail", "detail is null");
                        if (mcb != null) mcb.onGDMCError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onGDMCError(data);
                }
            }
        });
    }

    /**
     *  获取商品详情(get)
     *  goods_id    商品id
     *
     */
    public void get_goods_detail(String goods_id, Member member, String talent_deductid) {
        String url = BaseVar.API_GET_GOODS_DTL + "&goods_id=" + goods_id;
        if (member != null) {
            url += "&mid=" + member.member_id;
        }
        if (!TextUtils.isEmpty(talent_deductid)) {
            url += "&talent_deductid=" + talent_deductid;
        }

        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private GoodsDtlModelCallBack mcb;

    public void setModelCallBack(GoodsDtlModelCallBack cb) {
        this.mcb = cb;
    }

    public interface GoodsDtlModelCallBack {
        void onGDMCError(String errcode);

        void onGDMCData(GoodsDetail dtl);
    }
}
