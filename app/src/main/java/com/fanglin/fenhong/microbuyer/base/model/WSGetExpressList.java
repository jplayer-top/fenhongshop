package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 获取物流公司列表 请求类 webservice
 * Created by lizhixin on 2015/11/24.
 */
public class WSGetExpressList extends APIUtil {

    private WSExpressCallBack mcb;

    public WSGetExpressList() {
        super ();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {

                if (isSuccess) {
                    if (mcb != null) mcb.onWSExpressSuccess(data);
                } else {
                    if (mcb != null) mcb.onWSExpressError(data);
                }
            }
        });
    }

    public void getExpressList () {

        execute (HttpRequest.HttpMethod.POST, BaseVar.API_GET_EXPRESS, null);
    }

    public void setWSExpressCallBack(WSExpressCallBack mcb) {
        this.mcb = mcb;
    }

    public interface WSExpressCallBack {
        void onWSExpressSuccess(String data);

        void onWSExpressError(String errcode);
    }

}
