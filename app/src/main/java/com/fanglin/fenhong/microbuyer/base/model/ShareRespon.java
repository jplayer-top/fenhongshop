package com.fanglin.fenhong.microbuyer.base.model;

import com.google.gson.Gson;

/**
 * 作者： Created by Plucky on 15-10-4.
 * 针对wap分享的回调
 */
public class ShareRespon {
    public int error;//0 success 1 取消 2 失败
    public Object result;//返回的内容
    public Object data;//額外數據

    public String getString() {
        return new Gson().toJson(this);
    }
}
