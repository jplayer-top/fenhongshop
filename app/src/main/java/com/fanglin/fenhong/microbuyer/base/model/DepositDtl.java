package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;
import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/11/1.
 */
public class
DepositDtl extends APIUtil {
    public String pdc_sn;// 提现单号
    public double pdc_amount;// 提现金额
    public String pdc_bank_name;// 开户行
    public String pdc_bank_no;// 银行卡号
    public String pdc_bank_user;// 户主姓名
    public String pdc_add_time;// 申请时间
    public String pdc_payment_time;// 支付时间
    public int pdc_payment_state;// 提现支付状态 （0 审核中 1 支付完成）


    public DepositDtl() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<DepositDtl> list = new Gson().fromJson(data, new TypeToken<List<DepositDtl>>() {
                        }.getType());
                        if (mcb != null) mcb.onDDList(list);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onDDError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onDDError(data);
                }
            }
        });
    }

    public void getList(Member m) {
        if (m == null) {
            if (mcb != null) mcb.onDDError("-1");
            return;
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("mid", m.member_id);
        params.addBodyParameter("token", m.token);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DEPOSITDTL, params);
    }

    private DDModelCallBack mcb;

    public void setModelCallBack(DDModelCallBack cb) {
        this.mcb = cb;
    }

    public interface DDModelCallBack {
        void onDDError(String errcode);

        void onDDList(List<DepositDtl> list);
    }

    public String getStatus() {
        if (pdc_payment_state == 0) {
            return "审核中";
        } else if (pdc_payment_state == 1) {
            return "已到账";
        } else {
            return "审核失败";
        }
    }

    public int getStatusColor(Context c) {
        if (pdc_payment_state == 0) {
            return c.getResources().getColor(R.color.weibo);
        } else if (pdc_payment_state == 1) {
            return c.getResources().getColor(R.color.color_lv);
        } else {
            return c.getResources().getColor(R.color.fh_red);
        }
    }

    public String getTitle() {
        String affix;
        if (!TextUtils.isEmpty(pdc_bank_no) && pdc_bank_no.length() > 4 && !TextUtils.equals(pdc_bank_name, "支付宝")) {
            affix = pdc_bank_no.substring(pdc_bank_no.length() - 4);
            affix = "(尾号:" + affix + ")";
        } else {
            affix = "(" + pdc_bank_no + ")";
        }
        return pdc_bank_user + affix;
    }
}
