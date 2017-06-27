package com.fanglin.fenhong.microbuyer.base.baselib;

import android.content.Context;
import android.text.TextUtils;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fhlib.other.FHLib;
import java.io.File;

/**
 * 作者： Created by Plucky on 2015/11/3.
 * 常量单例
 * 目的为了不传Context --只传一次
 */
public class VarInstance {
    private static Context mContext;
    private static String chanel = "qq", deviceId = "864516020754610";
    private static VarInstance instance;
    private static boolean hasExecLogin = false;//是否已经执行登录
    private static boolean hasShowServerError = false;//用户判断是否已经提示过服务器连接失败

    private VarInstance() {

    }

    public void setHasExecLogin(boolean hasExecuteLogin) {
        hasExecLogin = hasExecuteLogin;
    }

    public void setHasShowServerError(boolean flag) {
        hasShowServerError = flag;
    }

    public void showMsg(int resId) {
        if (mContext == null) return;
        BaseFunc.showMsg(mContext, mContext.getString(resId));
    }

    public void showMsg(String msg) {
        if (mContext == null) return;
        BaseFunc.showMsg(mContext, msg);
    }

    public String getDeviceID() {
        return deviceId;
    }

    public String getMarket() {
        return chanel;
    }

    public static void init(Context c, String aChanel, String aDeviceId) {
        mContext = c;
        chanel = aChanel;
        deviceId = aDeviceId;
        instance = new VarInstance();
    }

    public static VarInstance getInstance() {
        if (instance == null) {
            instance = new VarInstance();
        }
        return instance;
    }

    public void refreshMedia(File imageFile) {
        FHLib.refreshMedia(mContext, imageFile);
    }

    /**
     * @param errocode 错误码
     * @param msg      服务端返回的信息
     * @return 如果为true 则将错误码返回值子模块  主要用来区分网络请求
     */
    public boolean parseError(String errocode, String msg, boolean showMsg) {
        if (mContext == null) return true;
        if (!TextUtils.isEmpty(msg)) {
            //如果有Msg则显示msg
            if (showMsg)
                BaseFunc.showMsg(mContext, msg);
            return true;
        }
        if (TextUtils.equals(errocode, "1000002")) {
            return true;
        }
        if (TextUtils.equals(errocode, "1000010")) {
            if (showMsg)
                BaseFunc.showMsg(mContext, mContext.getString(R.string.perr_of_1000010));
            return true;
        }
        if (TextUtils.equals(errocode, "1000011")) {
            if (showMsg)
                BaseFunc.showMsg(mContext, mContext.getString(R.string.perr_of_1000011));
            return true;
        }
        if (TextUtils.equals("0010", errocode)) {
            if (!hasExecLogin) {
                if (showMsg)
                    BaseFunc.showMsg(mContext, mContext.getString(R.string.tips_token_error));
                BaseFunc.gotoLogin(mContext);
                setHasExecLogin(true);
            }
            return false;
        }
        if (TextUtils.equals("0009", errocode)) {
            if (!hasExecLogin) {
                if (showMsg)
                    BaseFunc.showMsg(mContext, mContext.getString(R.string.tips_token_out_of_data));
                BaseFunc.gotoLogin(mContext);
                setHasExecLogin(true);
            }
            return false;
        }


        if (TextUtils.equals("-1", errocode)) {
            if (showMsg)
                BaseFunc.showMsg(mContext, mContext.getString(R.string.invalid_data));
            return false;
        }

        if (TextUtils.equals("-2", errocode)) {
            if (showMsg)
                BaseFunc.showMsg(mContext, mContext.getString(R.string.op_cancel));
            return false;
        }

        if (TextUtils.equals("-3", errocode)) {
            if (!hasShowServerError) {
                if (showMsg)
                    BaseFunc.showMsg(mContext, mContext.getString(R.string.network_err));
                setHasShowServerError(true);
            }

            return false;
        }

        if (TextUtils.equals("-5", errocode)) {
            if (showMsg)
                BaseFunc.showMsg(mContext, mContext.getString(R.string.network_time_out));
            return false;
        }

        if (TextUtils.equals(errocode, "1000015")) {
            return true;
        }

        return true;
    }

}
