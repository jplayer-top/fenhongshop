package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 作者： Created by Plucky on 15-10-23.
 * modify by lizhixin on 2016/02/22
 */
public class EvaluatingGoods extends APIUtil {

    public String store_id;
    public List<EvaluatingGoodsEntity> order_goods;

    public EvaluatingGoods() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        EvaluatingGoods goods = new Gson().fromJson(data, EvaluatingGoods.class);
                        if (mcb != null) mcb.onEGData(goods);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onEGError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onEGError(data);
                }
            }
        });
    }

    public void getData(Member m, String order_id) {
        if (m == null || TextUtils.isEmpty(order_id)) return;

        RequestParams params = new RequestParams();
        params.addBodyParameter("mid", m.member_id);
        params.addBodyParameter("token", m.token);
        params.addBodyParameter("order_id", order_id);

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_GET_ORDER_EVALUATING_GOODS, params);
    }

    private EGModelCallBack mcb;

    public void setModelCallBack(EGModelCallBack cb) {
        this.mcb = cb;
    }

    public interface EGModelCallBack {
        void onEGData(EvaluatingGoods goods);

        void onEGError(String errcode);
    }
}
