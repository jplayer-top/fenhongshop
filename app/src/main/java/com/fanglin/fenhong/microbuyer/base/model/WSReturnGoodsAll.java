package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 全部商品 退货申请提交 webservice
 * Created by admin on 2015/11/5.
 */
public class WSReturnGoodsAll extends APIUtil {

    private WSReturnGoodsAllModelCallBack mcb;

    public WSReturnGoodsAll () {
        super ();
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {

                if (isSuccess) {
                    if (mcb != null) mcb.onWSReturnGoodsAllSuccess (data);
                } else {
                    if (mcb != null) mcb.onWSReturnGoodsAllError (data);
                }
            }
        });
    }

    public void submit (Member m, String orderId, String buyer_message, String pic_info, String rec_id) {
        if (m == null) {
            if (mcb != null) mcb.onWSReturnGoodsAllError ("-1");
            return;
        }
        RequestParams params = new RequestParams ();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);
        params.addBodyParameter ("order_id", orderId);
        if (TextUtils.isEmpty (rec_id)) {
            params.addBodyParameter ("rec_id", rec_id);
        }
        params.addBodyParameter ("buyer_message", buyer_message);//  原因
        params.addBodyParameter ("pic_info", pic_info);//  多个用逗号隔开

        execute (HttpRequest.HttpMethod.POST, BaseVar.API_RETURN_GOODS_SUBMIT_ALL, params);
    }

    public void setWSReturnGoodsAllCallBack (WSReturnGoodsAllModelCallBack mcb) {
        this.mcb = mcb;
    }

    public interface WSReturnGoodsAllModelCallBack {
        void onWSReturnGoodsAllSuccess (String data);

        void onWSReturnGoodsAllError (String errcode);
    }

}
