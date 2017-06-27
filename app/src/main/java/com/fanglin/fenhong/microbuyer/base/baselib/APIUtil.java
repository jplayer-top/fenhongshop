package com.fanglin.fenhong.microbuyer.base.baselib;

import android.os.Build;
import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.BuildConfig;
import com.fanglin.fenhong.microbuyer.base.event.WifiConnectDealEvent;
import com.fanglin.fhlib.other.FHLog;
import com.fanglin.fhlib.other.FHSSLSocketFactory;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

import java.security.KeyStore;

import de.greenrobot.event.EventBus;

/**
 * 作者： Created by Plucky on 2015/7/6.
 * 来分红基础API 请求类
 */
public class APIUtil {
    private FHAPICallBack callback;
    private boolean ifShowMsg = true;
    private int conTimeOut;
    public boolean normalRequest = false;//为了扩展普通的请求

    public APIUtil() {

    }

    /**
     * APIUtil 封装的目的是为了实现:输入参数--输出对象
     * onStart 访问开始，onEnd 访问完成
     */
    public interface FHAPICallBack {

        void onStart(String data);

        void onEnd(boolean isSuccess, String data);
    }

    public APIUtil setCallBack(FHAPICallBack cb) {
        callback = cb;
        return this;
    }

    public APIUtil setIfShowMsg(boolean ifShowMsg) {
        this.ifShowMsg = ifShowMsg;
        return this;
    }

    public APIUtil setConTimeOut(int conTimeOut) {
        this.conTimeOut = conTimeOut;
        return this;
    }

    public void execute(HttpRequest.HttpMethod method, String url, RequestParams params) {
        HttpUtils hUtils;
        if (conTimeOut > 1000) {
            //loading页面的超时时间为5秒
            hUtils = new HttpUtils(conTimeOut);
        } else {
            //默认时间为20秒
            hUtils = new HttpUtils(20000);
        }

        hUtils.configCurrentHttpCacheExpiry(100);

        if (Build.VERSION.SDK_INT < 21) {
            try {
                //解决Android4.4.2 以下机型无法请求https的问题（默认不支持TLS1.2，需手动开启，api 20+ 才能支持 ）
                KeyStore clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                FHSSLSocketFactory factory = new FHSSLSocketFactory(clientKeyStore);
                hUtils.configSSLSocketFactory(factory);
            } catch (Exception e) {
                FHLog.d("APIUtil", e.getMessage());
            }
        }

        url = url.replace(" ", "");

        /**
         * 在URL中添加设备号 Added By Plucky
         */
        url += "&deviceid=" + VarInstance.getInstance().getDeviceID();
        url += "&market=" + VarInstance.getInstance().getMarket();

        FHLog.d("APIUtil", "url:" + url);

        if (params != null) FHLog.d("APIUtil", "params:" + new Gson().toJson(params));

        final String finalUrl = url;
        hUtils.send(method, url, params, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                if (callback != null) {
                    callback.onStart(null);
                }
            }

            /**
             *  -1数据解析异常 -2取消 -3网络链接失败 -4parseError处理完显示错误信息后，子类不再显示 -5 请求超时
             */
            @Override
            public void onCancelled() {
                super.onCancelled();
                if (callback != null) {
                    callback.onEnd(false, "-2");
                }
            }

            /**
             * 服务端反馈的数据格式为JSON格式：{"result": "数据","error":"0","msg":"消息"}
             */
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                EventBus.getDefault().post(new WifiConnectDealEvent());
                if (responseInfo == null) {
                    //确保返回不为null
                    if (callback != null) {
                        callback.onEnd(false, "-1");
                        FHLog.d("APIUtil", "responseInfo is null");
                    }
                    return;
                }

                // 请求成功之后置为默认状态
                VarInstance.getInstance().setHasShowServerError(false);

                FHLog.d("APIUtil", responseInfo.result);//打印成功信息

                if (normalRequest) {
                    /**
                     * 普通Url 请求时，如快递100 因为返回数据格式与分红商城不一致,直接返回即可，无须进行逻辑判断
                     */
                    if (callback != null) {
                        callback.onEnd(true, responseInfo.result);
                    }
                    return;
                }
                try {
                    String respon = responseInfo.result;

                    /**
                     * 直接使用存在无序问题
                     */
                    JSONObject json = new JSONObject(respon);
                    String err = json.getString("error");
                    String res = json.getString("result");
                    String amsg = null;
                    if (json.has("msg")) {
                        //后端不一定能返回msg
                        amsg = json.getString("msg");
                    }


                    /**
                     * msg 不一定有返回,错误信息时有返回
                     */


                    if (TextUtils.equals(err, "0")) {
                        if (!TextUtils.isEmpty(amsg)) {
                            VarInstance.getInstance().showMsg(amsg);
                        }
                        if (callback != null)
                            callback.onEnd(true, res);
                    } else {
                        Boolean b = VarInstance.getInstance().parseError(err, amsg, ifShowMsg);
                        if (callback != null)
                            callback.onEnd(false, b ? err : "-4");
                    }
                } catch (Exception e) {
                    /**
                     * 出现非法数据
                     */
                    if (callback != null) {
                        callback.onEnd(false, "-1");
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                FHLog.e("APIUtil", "url:" + finalUrl + "msg：" + s);
                if (callback != null) {
                    if (TextUtils.equals("java.net.SocketTimeoutException", s)) {
                        VarInstance.getInstance().parseError("-5", null, true);
                        callback.onEnd(false, "-5");
                    } else {
                        if (s != null && s.contains("java.net.UnknownHostException")) {
                            callback.onEnd(false, "-4");
                        } else {
                            VarInstance.getInstance().parseError("-3", null, true);
                            callback.onEnd(false, "-3");
                        }
                    }

                }
            }
        });
    }
}
