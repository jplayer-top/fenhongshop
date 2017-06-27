package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/8/10-下午10:13.
 * 功能描述:分红商城接口Model
 */
public class ResultModel {
    private String error;
    private String msg;
    private String result;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean errorEmpty() {
        return TextUtils.isEmpty(error);
    }
}
