package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/5/23-上午11:44.
 * 功能描述:获取验证码
 */
public class ReqSmsCode extends APIUtil {

    public ReqSmsCode() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                SmsCodeInit codeInit;
                if (isSuccess) {
                    try {
                        codeInit = new Gson().fromJson(data, SmsCodeInit.class);
                    } catch (Exception e) {
                        codeInit = null;
                    }
                } else {
                    codeInit = null;
                }
                if (mcb != null) {
                    mcb.onData(true, codeInit);
                }
            }
        });
    }

    /**
     * 获取手机验证码
     *
     * @param mobile  手机验证码
     * @param captcha 图片验证码 需要则传
     */
    public void getSmsCode(String mobile, String captcha) {
        String url = BaseVar.API_GET_SMS_CODE;
        url += "&mobile=" + mobile;
        if (!TextUtils.isEmpty(captcha)) {
            url += "&captcha=" + captcha;
        }

        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private SmsCodeInit.SCIModellCallBack mcb;

    public void setModelCallBack(SmsCodeInit.SCIModellCallBack callBack) {
        this.mcb = callBack;
    }
}
