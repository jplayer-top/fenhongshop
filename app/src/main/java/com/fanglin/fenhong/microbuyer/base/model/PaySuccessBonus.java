package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 作者： Created by Plucky on 15-11-29.
 * 支付成功红包组
 */
public class PaySuccessBonus extends APIUtil {
    public String batch_id;//    红包组id
    public String batch_name;//    红包组名称
    public int bag_num;//    红包数量
    public int num_out;//    红包被抢数量
    public long add_time;//    获取该红包组的时间戳

    public String share_title;//分享标题
    public String share_desc;//分享描述
    public String share_img;//分享配图
    public String share_url;//wap分享链接


    public PaySuccessBonus () {
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        PaySuccessBonus psb = new Gson ().fromJson (data, PaySuccessBonus.class);
                        if (mcb != null) mcb.onPSBData (psb);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onPSBError ("-1");
                    }
                } else {
                    if (mcb != null) mcb.onPSBError (data);
                }
            }
        });
    }

    /**
     *  mid    会员id
     *  token    登录令牌
     *  pay_sn    支付单号
     */
    public void getData (Member m, String pay_sn) {
        if (m == null || TextUtils.isEmpty (pay_sn)) {
            if (mcb != null) mcb.onPSBError ("-1");
            return;
        }

        RequestParams params = new RequestParams ();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);
        params.addBodyParameter ("pay_sn", pay_sn);
        execute (HttpRequest.HttpMethod.POST, BaseVar.API_PAID_COUPON_BATCH, params);
    }

    private PSBModelCallBack mcb;

    public void setModelCallBack (PSBModelCallBack cb) {
        this.mcb = cb;
    }

    public interface PSBModelCallBack {
        void onPSBError (String errcode);

        void onPSBData (PaySuccessBonus PSBData);
    }
}
