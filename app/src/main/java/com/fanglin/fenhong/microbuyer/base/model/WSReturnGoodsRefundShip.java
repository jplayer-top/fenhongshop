package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 提交发货 请求类 webservice
 * Created by lizhixin on 2015/11/13.
 */
public class WSReturnGoodsRefundShip extends APIUtil {

    private WSReturnGoodsShipCallBack mcb;

    public WSReturnGoodsRefundShip() {
        super ();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {

                if (isSuccess) {
                    if (mcb != null) mcb.onWSReturnGoodsShipSuccess(data);
                } else {
                    if (mcb != null) mcb.onWSReturnGoodsShipError(data);
                }
            }
        });
    }

    public void submitShip (Member m, String refund_id, String express_id, String invoice_no) {
        if (m == null) {
            if (mcb != null) mcb.onWSReturnGoodsShipError("-1");
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);
        params.addBodyParameter ("refund_id", refund_id);// 退货单id
        params.addBodyParameter ("express_id", express_id); //  快递公司id  (从获取快递列表接口获得）
        params.addBodyParameter ("invoice_no", invoice_no);// 快递单号

        execute (HttpRequest.HttpMethod.POST, BaseVar.API_RETURN_GOODS_REFUND_SHIP, params);
    }

    public void setWSReturnGoodsShipCallBack(WSReturnGoodsShipCallBack mcb) {
        this.mcb = mcb;
    }

    public interface WSReturnGoodsShipCallBack {
        void onWSReturnGoodsShipSuccess (String data);

        void onWSReturnGoodsShipError(String errcode);
    }

}
