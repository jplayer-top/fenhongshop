package com.fanglin.dutyfree;

import android.text.TextUtils;

import com.fanglin.fhlib.other.FHLog;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.x;

/**
 * 青岛芳林信息
 * Created by Plucky on 2017/1/4.
 * 功能描述:测试HTTPS
 */

public class APIUtil {
    public static final String APIPOST = "POST";
    public static final String APIGET = "GET";
    private FHAPICallBack callback;
    public boolean normalRequest = false;//为了扩展普通的请求

    public APIUtil() {

    }

    /**
     * APIUtil 封装的目的是为了实现:输入参数--输出对象
     * onStart 访问开始，onEnd 访问完成
     */
    public interface FHAPICallBack {
        void onEnd(boolean isSuccess, String data);
    }

    public APIUtil setCallBack(FHAPICallBack cb) {
        callback = cb;
        return this;
    }

    public void execute(String method, FHRequestParams params) {
        HttpMethod hMethod = TextUtils.equals(APIGET, method) ? HttpMethod.GET : HttpMethod.POST;
        x.http().request(hMethod, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (TextUtils.isEmpty(result)) {
                    //确保返回不为null
                    if (callback != null) {
                        callback.onEnd(false, "-1");
                        FHLog.d("APIUtil", "responseInfo is null");
                    }
                    return;
                }
                if (normalRequest) {
                    /**
                     * 普通Url 请求时，如快递100 因为返回数据格式与分红商城不一致,直接返回即可，无须进行逻辑判断
                     */
                    if (callback != null) {
                        callback.onEnd(true, result);
                    }
                    return;
                }
                try {
                    /**
                     * 直接使用存在无序问题
                     */
                    JSONObject json = new JSONObject(result);
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

                        }
                        if (callback != null)
                            callback.onEnd(true, res);
                    } else {
                        if (callback != null) {

                        }
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
            public void onError(Throwable ex, boolean isOnCallback) {
                if (callback != null) {
                    callback.onEnd(false, "-3");
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                // -1数据解析异常 -2取消 -3网络链接失败 -4parseError处理完显示错误信息后，子类不再显示 -5 请求超时
                if (callback != null) {
                    callback.onEnd(false, "-2");
                }
            }

            @Override
            public void onFinished() {

            }
        });
    }
}