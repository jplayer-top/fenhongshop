package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.BrandMessage;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/25-下午1:19.
 * 功能描述: 品牌聚合页
 */
public class BrandMessageDetail extends BrandMessage {

    public BrandMessageDetail() {

        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (callbackRequest != null) {
                    if (isSuccess) {
                        BrandMessage message;
                        try {
                            message = new Gson().fromJson(data, BrandMessage.class);
                        } catch (Exception e) {
                            message = null;
                        }
                        callbackRequest.onBrandMessageDetail(message);
                    } else {
                        callbackRequest.onBrandMessageDetail(null);
                    }
                }
            }
        });
    }

    /**
     * @param member   会员
     * @param brand_id 品牌id
     * @param curpage  当前页码
     * @param sort     排序字段 1：综合 2：销量 3：价格 4：人气(预留)
     * @param order    排序顺序 1：asc升序 2：desc降序
     */
    public void getData(Member member, String brand_id, int curpage, int sort, int order) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("brand_id", brand_id);
        params.addBodyParameter("num", String.valueOf(BaseVar.REQUESTNUM));
        params.addBodyParameter("curpage", String.valueOf(curpage));
        params.addBodyParameter("sort", String.valueOf(sort));
        params.addBodyParameter("order", String.valueOf(order));
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_BRANDMESSAGEDETAIL, params);
    }


    private BrandMessageDtlCallbackRequest callbackRequest;

    public void setCallbackRequest(BrandMessageDtlCallbackRequest callbackRequest) {
        this.callbackRequest = callbackRequest;
    }

    public interface BrandMessageDtlCallbackRequest {
        void onBrandMessageDetail(BrandMessage data);
    }
}
