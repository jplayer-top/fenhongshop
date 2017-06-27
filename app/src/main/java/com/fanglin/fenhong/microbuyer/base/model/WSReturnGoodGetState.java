package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 获取退货状态 webservice
 * Created by lizhixin on 2015/11/13.
 * 参数 state_id:
 *  1 申请退款退货成功

    2 卖家不同意,退货单流程完结

    3 卖家同意,买家填写发货信息

    4 买家已发货,等待卖家收货 (显示物流信息)

    5 收货期限到了,卖家选择了未收到,退货单流程完结

    6 卖家收到货,等待平台审核

    7 平台审核通过,退款到买家余额账户
 */
public class WSReturnGoodGetState extends APIUtil {

    // state_id = 1
    public long countdown; // 倒计时秒数
    public String store_name; // 店铺名称
    public String store_phone; // 店铺电话
    public String store_qq; // 店铺qq

    // state_id = 2 或 3
    public String seller_message; // 卖家反馈留言
    public long seller_time;
    public int is_finish;//是否被卖家拒绝，0 否  1 是 ，是的话要说明退款单流程完结，隐藏重新申请按钮。

    // state_id = 4
    public String express_code; //  快递代码 （用于快递100接口）
    public String express_no; // 快递单号 （用于快递100接口)

    // state_id = 7
    public String admin_message; // 平台审核意见

    private WSReturnGoodsGetStateCallBack mcb;

    public WSReturnGoodGetState() {
        super ();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {

                if (isSuccess) {
                    try {
                        WSReturnGoodGetState adata = new Gson().fromJson(data, WSReturnGoodGetState.class);
                        if (mcb != null) mcb.onWSReturnGoodsGetStateSuccess(adata);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (mcb != null) mcb.onWSReturnGoodsGetStateError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onWSReturnGoodsGetStateError(data);
                }
            }
        });
    }

    public void getRefundState (Member m, String refund_id, int state_id, String orderId) {
        if (m == null) {
            if (mcb != null) mcb.onWSReturnGoodsGetStateError("-1");
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);
        params.addBodyParameter("order_id", orderId);//订单Id，不是必填项，作为用于查询卖家拒绝后是否显示重新申请按钮的参数
        params.addBodyParameter ("refund_id", refund_id);// 退单id
        params.addBodyParameter ("state_id", String.valueOf(state_id)); //进度状态id : 1-退款提交成功

        execute (HttpRequest.HttpMethod.POST, BaseVar.API_RETURN_GOODS_GET_STATE, params);
    }

    public void setWSReturnGoodsGetStateCallBack(WSReturnGoodsGetStateCallBack mcb) {
        this.mcb = mcb;
    }

    public interface WSReturnGoodsGetStateCallBack {
        void onWSReturnGoodsGetStateSuccess (WSReturnGoodGetState data);

        void onWSReturnGoodsGetStateError(String errcode);
    }

}
