package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 作者： Created by Plucky on 2015/11/3.
 */
public class DeductMoney extends APIUtil {
    public double freeze_deduct;//  预计到账金额
    public double available_predeposit;//  可用金额

    public DeductMoney () {
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        DeductMoney dMoney = new Gson ().fromJson (data, DeductMoney.class);
                        if (mcb != null) mcb.onDMData (dMoney);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onDMError ("-1");
                    }
                } else {
                    if (mcb != null) mcb.onDMError (data);
                }
            }
        });
    }

    public void getData (Member m) {
        if (m == null) {
            if (mcb != null) mcb.onDMError ("-1");
            return;
        }
        RequestParams params = new RequestParams ();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);
        execute (HttpRequest.HttpMethod.POST, BaseVar.API_GET_DEDUCTMONEY, params);
    }

    private DMModelCallBack mcb;

    public void setModelCallBack (DMModelCallBack cb) {
        this.mcb = cb;
    }

    public interface DMModelCallBack {
        void onDMData (DeductMoney dMoney);

        void onDMError (String errcode);
    }
}
