package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 作者： Created by Plucky on 2015/10/20.
 */
public class ReceiveOrder extends APIUtil {

    public ReceiveOrder () {
        super ();
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (mcb != null) mcb.onError (isSuccess);
            }
        });
    }

    public void receive_order (Context c, Member m, String order_id, ROModelCallBack cb) {
        mcb = cb;
        if (m == null) {
            BaseFunc.gotoLogin (c);
            if (mcb != null) mcb.onError (false);
            return;
        }
        if (order_id == null) {
            if (mcb != null) mcb.onError (false);
            return;
        }

        RequestParams params = new RequestParams ();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);
        params.addBodyParameter ("order_id", order_id);
        execute (HttpRequest.HttpMethod.POST, BaseVar.API_RECEIVE_ORDER, params);

    }

    private ROModelCallBack mcb;

    public void setModelCallBack (ROModelCallBack cb) {
        this.mcb = cb;
    }

    public interface ROModelCallBack {
        void onError (boolean flag);
    }
}
