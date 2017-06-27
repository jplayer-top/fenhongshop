package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 意见反馈 请求类 webservice
 * Created by lizhixin on 2015/11/17.
 */
public class WSFeedBack extends APIUtil {

    private WSFeedBackCallBack mcb;

    public WSFeedBack() {
        super ();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {

                if (isSuccess) {
                    /*try {
                        WSFeedBack adata = new Gson().fromJson(data, WSFeedBack.class);
                        if (mcb != null) mcb.onWSFeedBackSuccess(adata);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (mcb != null) mcb.onWSFeedBackError("-1");
                    }*/
                    if (mcb != null) mcb.onWSFeedBackSuccess(data);
                } else {
                    if (mcb != null) mcb.onWSFeedBackError(data);
                }
            }
        });
    }

    public void submit (Member m, String feedBack) {
        if (m == null) {
            if (mcb != null) mcb.onWSFeedBackError("-1");
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);
        params.addBodyParameter ("feedback", feedBack);// 反馈意见内容

        execute (HttpRequest.HttpMethod.POST, BaseVar.API_FEED_BACK, params);
    }

    public void setWSFeedBackCallBack(WSFeedBackCallBack mcb) {
        this.mcb = mcb;
    }

    public interface WSFeedBackCallBack {
        void onWSFeedBackSuccess (String data);

        void onWSFeedBackError(String errcode);
    }


}
