package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;
import android.text.TextUtils;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 作者： Created by Plucky on 15-10-23.
 * modify by lizhixin on 2016/02/23
 * 提交商品評論
 */
public class EvaluateAGoods extends APIUtil {
    public Member member;// mid会员id  --token登录令牌
    public String order_id;//   订单id
    public int is_anonymous = 1;// 是否匿名  （1 是  0 或不传为否)
    public int is_append;// 是否为追加评价 0:否 | 1:是

    public List<EvaSubGoodsEntity> evaluate_goods;// 评价商品信息
    public float store_desccredit;// 店铺描述相符
    public float store_servicecredit;// 店铺服务态度
    public float store_deliverycredit;//  店铺发货速度

    public EvaluateAGoods() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (mcb != null) mcb.onEAGEnd(isSuccess, data);
            }
        });
    }

    public void evaluate_goods(Context c) {
        if (member == null) {
            BaseFunc.gotoLogin(c);
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("mid", member.member_id);
        params.addBodyParameter("token", member.token);
        params.addBodyParameter("order_id", order_id);
        params.addBodyParameter("is_append", String.valueOf(is_append));
        params.addBodyParameter("evaluate_goods", new Gson().toJson(evaluate_goods));
        if (is_append == 0) {
            params.addBodyParameter("is_anonymous", String.valueOf(is_anonymous));
            params.addBodyParameter("store_desccredit", String.valueOf(store_desccredit));
            params.addBodyParameter("store_servicecredit", String.valueOf(store_servicecredit));
            params.addBodyParameter("store_deliverycredit", String.valueOf(store_deliverycredit));
        }

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_EVALUATE_GOODS_BATCH, params);
    }

    private EAGModelCallBack mcb;

    public void setModelCallBack(EAGModelCallBack cb) {
        this.mcb = cb;
    }

    public interface EAGModelCallBack {
        void onEAGEnd(boolean isSuccess, String data);
    }

}
