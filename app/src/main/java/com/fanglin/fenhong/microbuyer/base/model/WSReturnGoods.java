package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 *  单商品 退货申请提交 webservice
 * Created by admin on 2015/11/5.
 */
public class WSReturnGoods extends APIUtil {

    private WSReturnGoodsModelCallBack mcb;

    public WSReturnGoods() {
        super ();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {

                if (isSuccess) {
                   if (mcb != null) mcb.onWSReturnGoodsSuccess(data);

                } else {
                    if (mcb != null) mcb.onWSReturnGoodsError(data);
                }
            }
        });
    }

    public void submit (Member m, int type, String orderId, String rec_Id, String refund_amount, String goods_num, String reason_id, String buyer_message, String pic_info) {
        if (m == null) {
            if (mcb != null) mcb.onWSReturnGoodsError("-1");
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);
        params.addBodyParameter ("refund_type", String.valueOf (type));//申请类型   1为退款 | 2为退货
        params.addBodyParameter ("order_id", orderId);
        params.addBodyParameter ("rec_id", rec_Id);
        params.addBodyParameter ("refund_amount", refund_amount);
        params.addBodyParameter ("goods_num", goods_num);
        params.addBodyParameter ("reason_id", reason_id);
        params.addBodyParameter ("buyer_message", buyer_message);
        params.addBodyParameter ("pic_info", pic_info);//  多个用逗号隔开

        execute (HttpRequest.HttpMethod.POST, BaseVar.API_RETURN_GOODS_SUBMIT, params);
    }

    public void setWSReturnGoodsModelCallBack(WSReturnGoodsModelCallBack mcb) {
        this.mcb = mcb;
    }

    public interface WSReturnGoodsModelCallBack {
        void onWSReturnGoodsError(String errcode);

        void onWSReturnGoodsSuccess (String data);
    }

}
