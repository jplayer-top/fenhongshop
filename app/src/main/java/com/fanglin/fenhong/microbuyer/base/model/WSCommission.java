package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

/**
 * 我的团队列表请求webservice
 * Created by admin on 2015/11/5.
 */
public class WSCommission extends APIUtil {

    public ArrayList<Commission> deduct_list;
    public double freeze_deduct;//入账中金额
    public double available_predeposit;//可提现收入
    public double user_acount;//可用余额

    private WSCommissionModelCallBack mcb;

    public WSCommission() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {

                if (isSuccess) {
                    try {
                        WSCommission adata = new Gson().fromJson(data, WSCommission.class);
                        if (mcb != null) mcb.onWSTeamSuccess(adata);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (mcb != null) mcb.onWSTeamError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onWSTeamError(data);
                }
            }
        });
    }

    public void getList (Member m, int start, int type,int from) {
        if (m == null) {
            if (mcb != null) mcb.onWSTeamError("-1");
            return;
        }

        /**
         * 订单状态筛选 -- 50:已经处理；60:未处理; 70:已失效 可为空，为空的时候取所有数据
         */
        Integer state;
        if (type == 0) {
            state = 0;
        } else if (type == 1) {
            state = 60;
        } else if (type == 2) {
            state = 50;
        } else {
            state = 70;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);
        if (state > 0)
        params.addBodyParameter ("state", String.valueOf (state));//订单状态筛选 -- 50:已经处理；60:未处理.可为空，为空的时候取所有数据
        params.addBodyParameter ("start", String.valueOf (start));
        params.addBodyParameter ("num", String.valueOf (BaseVar.REQUESTNUM));
        params.addBodyParameter ("from", String.valueOf (from));

        execute (HttpRequest.HttpMethod.POST, BaseVar.API_GET_COMMISSION, params);
    }

    public void setWSCommissionModelCallBack(WSCommissionModelCallBack mcb) {
        this.mcb = mcb;
    }

    public interface WSCommissionModelCallBack {
        void onWSTeamError(String errcode);

        void onWSTeamSuccess (WSCommission data);
    }

}
