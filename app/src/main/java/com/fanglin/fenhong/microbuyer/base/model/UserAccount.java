package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 作者： Created by Plucky on 2015/11/30.
 * 我的余额
 */
public class UserAccount extends APIUtil {
    public double allsum_money;// 83.2906  累计收入
    public double pdc_amount;// 53.00  现金提现
    public double available_predeposit;// 3202.11 账户余额


    public UserAccount () {
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        UserAccount account = new Gson ().fromJson (data, UserAccount.class);
                        if (mcb != null) mcb.onUADate (account);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onUAError ("-1");
                    }
                } else {
                    if (mcb != null) mcb.onUAError ("-1");
                }
            }
        });
    }

    public void getData (Member m) {
        if (m == null) {
            if (mcb != null) mcb.onUAError ("-1");
            return;
        }
        RequestParams params = new RequestParams ();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);

        execute (HttpRequest.HttpMethod.POST, BaseVar.API_GET_USER_ACCOUNT, params);
    }


    private UAModelCallBack mcb;

    public void setModelCallBack (UAModelCallBack cb) {
        this.mcb = cb;
    }

    public interface UAModelCallBack {
        void onUAError (String errcode);

        void onUADate (UserAccount account);
    }

}
