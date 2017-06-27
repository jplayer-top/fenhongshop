package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import android.text.Html;
import android.text.Spanned;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 青岛芳林信息
 * Created by Plucky on 2016/12/27.
 * 功能描述;//极速免税升级成为VIP买手
 */

public class DutyTopUp extends APIUtil {
    private String buyer_name;//M18663962851,
    private String pay_sn;//xxx,
    private String pay_amount;//123.36,
    private String season_card_img;//http://1991th.com/a.jpg,
    private String year_card_img;//http://1991th.com/b.jpg,
    private List<DutyfreePayment> payment_list;//
    private String err_msg;//不可重复支付
    private int pay_type;

    public DutyTopUp() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (requestCallBack != null) {
                    DutyTopUp result;
                    if (isSuccess) {
                        try {
                            result = new Gson().fromJson(data, DutyTopUp.class);
                            result.pay_type = pay_type;
                        } catch (Exception e) {
                            result = null;
                        }
                    } else {
                        result = null;
                    }
                    requestCallBack.onDutyTopUpData(result);
                }
            }
        });
    }

    public void submit(Member member, int pay_type) {
        this.pay_type = pay_type;
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("pay_type", String.valueOf(pay_type));
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_TOPUP, params);
    }

    // ------Getter------

    public String getPaySn() {
        return pay_sn;
    }

    public String getPayAmount() {
        return pay_amount;
    }

    public Spanned getPayAmountDesc() {
        String html = "金额：<font color='#FA2855'>" + pay_amount + "元</font>";
        return Html.fromHtml(html);
    }

    public String getSeasonCardImg() {
        return season_card_img;
    }

    public String getYearCardImg() {
        return year_card_img;
    }

    public List<DutyfreePayment> getPaymentList() {
        return payment_list;
    }

    public String getErrMsg() {
        return err_msg;
    }

    public String getSubject() {
        return "分红全球购极速免税店订单:" + pay_sn;
    }

    public int getPayType() {
        return pay_type;
    }

    public String getBuyerName() {
        return "ＩＤ：" + buyer_name;
    }

    private DutyTopUpRequestCallBack requestCallBack;

    public void setRequestCallBack(DutyTopUpRequestCallBack requestCallBack) {
        this.requestCallBack = requestCallBack;
    }

    public interface DutyTopUpRequestCallBack {
        void onDutyTopUpData(DutyTopUp topUp);
    }
}
