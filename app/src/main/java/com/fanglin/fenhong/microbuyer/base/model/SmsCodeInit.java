package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/5/20-下午5:59.
 * 功能描述:手机验证码页面初始化
 */
public class SmsCodeInit extends APIUtil {
    private int if_captcha;// 0,     是否显示图形验证码 0 否 1 是
    private int sms_available;// 1,  短信验证码是否可用  0 否  1 是
    private int countdown;// 60,     下次获取验证码倒计时
    private int mobile_correct;// 1  手机格式是否正确 0 否 1 是

    private int last_countdown;

    public SmsCodeInit() {
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
                    mcb.onData(false, codeInit);
                }

            }
        });
    }


    public void getData(String mobile) {
        String url = String.format(BaseVar.API_SMS_CODE_INIT, mobile);
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private SCIModellCallBack mcb;

    public void setModelCallBack(SCIModellCallBack callBack) {
        this.mcb = callBack;
    }

    public interface SCIModellCallBack {
        /**
         * @param fromSmsCode 是否是通过获取验证码得到的数据
         * @param data        验证码初始化数据
         */
        void onData(boolean fromSmsCode, SmsCodeInit data);
    }

    private int getMobile_correct() {
        return mobile_correct;
    }

    public boolean isMobileCorrect() {
        return getMobile_correct() == 1;
    }

    public void setMobile_correct(int mobile_correct) {
        this.mobile_correct = mobile_correct;
    }

    private int getIf_captcha() {
        return if_captcha;
    }

    public boolean isShowCaptcha() {
        return getIf_captcha() == 1;
    }

    public void setIf_captcha(int if_captcha) {
        this.if_captcha = if_captcha;
    }

    private int getSms_available() {
        return sms_available;
    }

    public boolean isSmsAvailable() {
        return getSms_available() == 1;
    }

    public void setSms_available(int sms_available) {
        this.sms_available = sms_available;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public int getLast_countdown() {
        return last_countdown;
    }

    public void setLast_countdown(int last_countdown) {
        this.last_countdown = last_countdown;
    }
}
