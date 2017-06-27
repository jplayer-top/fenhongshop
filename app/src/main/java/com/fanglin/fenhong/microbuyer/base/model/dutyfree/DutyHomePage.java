package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/22-下午10:14.
 * 功能描述: 极速免税店首页
 */
public class DutyHomePage extends APIUtil {
    private String search_tips;
    private String guide_url;
    private String ucenter_msg;//如果返回信息就说明不能进入我的钱包页面
    private boolean order_visible;//订单按钮是否显示  true显示
    private boolean wallet_visible;//钱包按钮是否显示

    public String getGuide_url() {
        return guide_url;
    }

    public String getSearch_tips() {
        return search_tips;
    }

    public String getUcenter_msg() {
        return ucenter_msg;
    }

    public boolean isOrder_visible() {
        return order_visible;
    }

    public boolean isWallet_visible() {
        return wallet_visible;
    }

    public DutyHomePage() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (dutyHomePageReqCallBack != null) {
                    if (isSuccess) {
                        DutyHomePage page;
                        try {
                            page = new Gson().fromJson(data, DutyHomePage.class);
                        } catch (Exception e) {
                            page = null;
                        }
                        dutyHomePageReqCallBack.onDutyHomeData(page);
                    } else {
                        dutyHomePageReqCallBack.onDutyHomeData(null);
                    }
                }
            }
        });
    }

    private DutyHomePageReqCallBack dutyHomePageReqCallBack;

    public void setDutyHomePageReqCallBack(DutyHomePageReqCallBack dutyHomePageReqCallBack) {
        this.dutyHomePageReqCallBack = dutyHomePageReqCallBack;
    }

    public interface DutyHomePageReqCallBack {
        void onDutyHomeData(DutyHomePage data);
    }

    public void getData(Member member) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_HOMEPAGE, params);
    }
}
