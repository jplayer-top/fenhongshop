package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 获取退货退款地址 请求类 webservice
 * Created by lizhixin on 2015/11/14.
 */
public class WSReturnGoodsGetRefundAddr extends APIUtil {

    public String consignee;// 收货人
    public String tel;// 联系电话
    public String address;// 收货地址
    public String express_id;// 物流公司ID
    public String express_code;// 物流公司编号
    public String express_name;// 物流公司
    public String express_no;// 物流单号

    private WSReturnGoodsRefundAddrCallBack mcb;

    public WSReturnGoodsGetRefundAddr() {
        super ();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {

                if (isSuccess) {
                    try {
                        WSReturnGoodsGetRefundAddr adata = new Gson().fromJson(data, WSReturnGoodsGetRefundAddr.class);
                        if (mcb != null) mcb.onWSReturnGoodsRefundAddrSuccess(adata);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (mcb != null) mcb.onWSReturnGoodsRefundAddrError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onWSReturnGoodsRefundAddrError(data);
                }
            }
        });
    }

    public void getRefundAddress (Member m, String refund_id) {
        if (m == null) {
            if (mcb != null) mcb.onWSReturnGoodsRefundAddrError("-1");
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);
        params.addBodyParameter ("refund_id", refund_id);// 退款记录ID

        execute (HttpRequest.HttpMethod.POST, BaseVar.API_RETURN_GOODS_GET_REFUND_ADDR, params);
    }

    public void setWSReturnGoodsRefundAddrCallBack(WSReturnGoodsRefundAddrCallBack mcb) {
        this.mcb = mcb;
    }

    public interface WSReturnGoodsRefundAddrCallBack {
        void onWSReturnGoodsRefundAddrSuccess (WSReturnGoodsGetRefundAddr data);

        void onWSReturnGoodsRefundAddrError(String errcode);
    }

}
