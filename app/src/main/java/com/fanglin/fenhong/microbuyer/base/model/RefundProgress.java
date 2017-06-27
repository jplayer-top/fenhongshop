package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 作者： Created by Plucky on 2015/11/25.
 */
public class RefundProgress extends APIUtil {
    public int refund_type;//是否需要退货 （1 不需要  2 需要）
    public int refund_state;//状态 ：1-7

    public RefundProgress () {
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        RefundProgress rp = new Gson ().fromJson (data, RefundProgress.class);
                        if (mcb != null) mcb.onRPData (rp);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onRPError ("-1");
                    }
                } else {
                    if (mcb != null) mcb.onRPError (data);
                }
            }
        });
    }

    public void getData (Member m, String refund_id) {
        if (m == null || TextUtils.isEmpty (refund_id)) {
            if (mcb != null) mcb.onRPError ("-1");
            return;
        }

        RequestParams params = new RequestParams ();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);
        params.addBodyParameter ("refund_id", refund_id);

        execute (HttpRequest.HttpMethod.POST, BaseVar.API_GET_REFUND_PROGRESS, params);
    }


    private RPModelCallBack mcb;

    public void setModelCallBack (RPModelCallBack cb) {
        this.mcb = cb;
    }

    public interface RPModelCallBack {
        void onRPError (String errcode);

        void onRPData (RefundProgress data);
    }
}
