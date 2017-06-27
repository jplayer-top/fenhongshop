package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fhlib.ypyun.api.UpYunUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/5/23-下午1:52.
 * 功能描述:用户注册
 */
public class RegisterReq extends APIUtil {
    public RegisterReq() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                Member member;
                if (isSuccess) {
                    try {
                        member = new Gson().fromJson(data, Member.class);
                    } catch (Exception e) {
                        member = null;
                    }
                } else {
                    member = null;
                }

                if (mcb != null) {
                    mcb.onData(member);
                }
            }
        });
    }


    /**
     * 会员注册(post)
     * mobile 手机号
     * code 手机验证码
     * password 登录密码 MD5加密
     */
    public void register(String mobile, String code, String password) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("mobile", mobile);
        params.addBodyParameter("code", code);
        params.addBodyParameter("password", UpYunUtils.signature(password));

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_REGISTER, params);
    }

    private RRModelCallBack mcb;

    public void setModelCallBack(RRModelCallBack callBack) {
        this.mcb = callBack;
    }

    public interface RRModelCallBack {
        void onData(Member member);
    }
}
