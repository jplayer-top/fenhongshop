package com.fanglin.unionpay;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/2/25-下午4:01.
 * 功能描述:
 */
public interface FHUPPayCallBack {
    void onSuccess();
    void onError();
    void onCancel();
}
