package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/10/31-上午10:50.
 * 功能描述: 内置浏览器Ajax数据
 */
public class FHAjaxData {
    private String url;//
    private String method;//POST GET
    private Object data;//请求参数

    public JSONObject getData() {
        String str = new Gson().toJson(data);
        if (TextUtils.isEmpty(str)) {
            return null;
        } else {
            try {
                return new JSONObject(str);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }
}
