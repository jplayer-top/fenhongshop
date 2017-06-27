package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fhlib.other.FHLog;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/21-下午7:47.
 * 功能描述: 极速免税店 订单详情请求
 */
public class DutyOrderDtlRequest extends DutyOrder {

    public DutyOrderDtlRequest() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (requestCallback != null) {
                    if (isSuccess) {
                        DutyOrder order;
                        try {
                            order = new Gson().fromJson(data, DutyOrder.class);
                        } catch (Exception e) {
                            order = null;
                            FHLog.d("Plucky", e.getMessage());
                        }
                        requestCallback.onDutyOrderDtlData(order);
                    } else {
                        requestCallback.onDutyOrderDtlData(null);
                    }
                }
            }
        });
    }


    private DutyOrderDtlRequestCallback requestCallback;

    public void setRequestCallback(DutyOrderDtlRequestCallback requestCallback) {
        this.requestCallback = requestCallback;
    }

    public interface DutyOrderDtlRequestCallback {
        void onDutyOrderDtlData(DutyOrder data);
    }

    public void getData(Member member, String order_id) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }

        params.addBodyParameter("order_id", order_id);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_ORDERDTL, params);
    }
}
