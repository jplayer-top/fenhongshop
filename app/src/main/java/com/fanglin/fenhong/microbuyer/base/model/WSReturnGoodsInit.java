package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

/**
 * 退货申请页面 初始化数据请求类 webservice
 * Created by lizhixin on 2015/11/13.
 */
public class WSReturnGoodsInit extends APIUtil {

    public ArrayList<RefundReason> reason;
    public double goods_pay_price;//最多退款金额
    public int goods_num;//最多退款数量
    public String coupon_deduct;//优惠券按比例抵扣的金额
    public LastRefund last_refund;//上次提交的退款申请单

    private WSReturnGoodsInitCallBack mcb;

    public WSReturnGoodsInit() {
        super ();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {

                if (isSuccess) {
                    try {
                        WSReturnGoodsInit adata = new Gson().fromJson(data, WSReturnGoodsInit.class);
                        if (mcb != null) mcb.onWSReturnGoodsInitSuccess(adata);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (mcb != null) mcb.onWSReturnGoodsInitError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onWSReturnGoodsInitError(data);
                }
            }
        });
    }

    public void getInitData (Member m, String orderId, String rec_id) {
        if (m == null) {
            if (mcb != null) mcb.onWSReturnGoodsInitError("-1");
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);
        params.addBodyParameter ("order_id", orderId);
        if (!TextUtils.isEmpty(rec_id))
        params.addBodyParameter ("rec_id", rec_id); // 订单商品表编号（可选，传0或不传返回订单所有，默认0）

        execute (HttpRequest.HttpMethod.POST, BaseVar.API_RETURN_GOODS_INIT, params);
    }

    public void setWSReturnGoodsInitCallBack(WSReturnGoodsInitCallBack mcb) {
        this.mcb = mcb;
    }

    public interface WSReturnGoodsInitCallBack {
        void onWSReturnGoodsInitError(String errcode);

        void onWSReturnGoodsInitSuccess (WSReturnGoodsInit data);
    }

}
