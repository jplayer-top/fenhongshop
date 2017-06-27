package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 作者： Created by Plucky on 15-9-30.
 * 供应商入驻检验信息
 */
public class JoinStep1 extends APIUtil {
    public String seller_name;
    public String store_name;
    public String charge_std;
    public int joinin_year = 0;
    public String store_class;
    public double deposit;
    public double pay_amount = 0;// 支付宝付款金额
    public List<List<String>> store_class_names;
    public List<String> store_class_deduct_rates;
    public String step;
    public String tip;
    public String order_sn;// 支付宝 订单号

    private boolean isUpdate = false;

    public JoinStep1 () {
        super ();
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {
            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    if (isUpdate) {
                        if (mcb != null) mcb.onSuccess (null);
                    } else {
                        JoinStep1 step1;
                        try {
                            step1 = new Gson ().fromJson (data, JoinStep1.class);
                        } catch (Exception e) {
                            step1 = null;
                        }
                        if (step1 != null) {
                            if (mcb != null) mcb.onSuccess (step1);
                        } else {
                            if (mcb != null) mcb.onError ("-1");
                        }
                    }
                } else {
                    if (mcb != null) mcb.onError (data);
                }
            }
        });
    }

    public void getData (Member m, String step) {
        if (m == null) return;
        isUpdate = false;
        String url = BaseVar.API_STORE_JOIN + "&mid=" + m.member_id + "&token=" + m.token;
        if (step != null) {
            url += "&step=" + step;
        }
        execute (HttpRequest.HttpMethod.GET, url, null);
    }

    private JoinStep1CallBack mcb;

    public void setModelCallBack (JoinStep1CallBack cb) {
        this.mcb = cb;
    }

    public interface JoinStep1CallBack {
        void onSuccess (JoinStep1 joinStep1);

        //-1 表示解析失败
        void onError (String errcode);
    }
}
