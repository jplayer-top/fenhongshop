package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/12/8-下午1:41.
 * 功能描述:极速免税 消费单详情
 */
public class DutyfreeConsumeDetail extends DutyfreeConsume {

    public DutyfreeConsumeDetail() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (requestCallBack != null) {
                    if (isSuccess) {
                        DutyfreeConsumeDetail detail;
                        try {
                            detail = new Gson().fromJson(data, DutyfreeConsumeDetail.class);
                        } catch (Exception e) {
                            detail = null;
                        }
                        requestCallBack.onDutyfreeConsumeDetail(detail);
                    } else {
                        requestCallBack.onDutyfreeConsumeDetail(null);
                    }
                }
            }
        });
    }

    public void getData(Member member, String consume_id) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("consume_id", consume_id);

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_CONSUME_DETAIL, params);
    }

    private DutyfreeConsumeDetailRequestCallBack requestCallBack;

    public void setRequestCallBack(DutyfreeConsumeDetailRequestCallBack requestCallBack) {
        this.requestCallBack = requestCallBack;
    }

    public interface DutyfreeConsumeDetailRequestCallBack {
        void onDutyfreeConsumeDetail(DutyfreeConsumeDetail data);
    }
}
