package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

/**
 * 获取退货退款详情 请求类 webservice
 * Created by lizhixin on 2015/11/13.
 */
public class WSReturnGoodsGetRefundDtl extends APIUtil {

    public String refund_sn;// 退货/退款编号
    public String store_name;// 店铺名称
    public String goods_num;// 商品数量
    public String refund_amount;// 退款金额
    public int refund_type;// 申请类型 1:退款 | 2:退货
    public long add_time;// 申请时间
    public long admin_time;// 退款成功的时间
    public String reason_info;// 退款/退货原因
    public String buyer_message;// 退款/退货说明
    public ArrayList<String> pic_info_format;//图片集合

    private WSReturnGoodsRefundDtlCallBack mcb;

    public WSReturnGoodsGetRefundDtl() {
        super ();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {

                if (isSuccess) {
                    try {
                        WSReturnGoodsGetRefundDtl adata = new Gson().fromJson(data, WSReturnGoodsGetRefundDtl.class);
                        if (mcb != null) mcb.onWSReturnGoodsRefundDtlSuccess(adata);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (mcb != null) mcb.onWSReturnGoodsRefundDtlError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onWSReturnGoodsRefundDtlError(data);
                }
            }
        });
    }

    public void getRefundDetail (Member m, String refund_id) {
        if (m == null) {
            if (mcb != null) mcb.onWSReturnGoodsRefundDtlError("-1");
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);
        params.addBodyParameter ("refund_id", refund_id );// 退款记录ID

        execute (HttpRequest.HttpMethod.POST, BaseVar.API_RETURN_GOODS_GET_REFUND_DETAIL, params);
    }

    public void setWSReturnGoodsInitCallBack(WSReturnGoodsRefundDtlCallBack mcb) {
        this.mcb = mcb;
    }

    public interface WSReturnGoodsRefundDtlCallBack {
        void onWSReturnGoodsRefundDtlSuccess (WSReturnGoodsGetRefundDtl data);

        void onWSReturnGoodsRefundDtlError(String errcode);
    }

}
