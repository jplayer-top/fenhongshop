package com.fanglin.fenhong.microbuyer.common.listener;

import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.base.model.UploadImgInfo;
/**
 * 青岛芳林信息
 * Created by Plucky on 2016/12/16.
 * 功能描述: 分红浏览器回调方法
 */

public interface FHBrowserListener {
    /**
     * WAP回调
     */
    void onEECrossResult(String func, String data);

    /**
     * 非回调给WAP 仅通知Activity执行事件
     */

    void doEnableShareButtonAction(ShareData shareData);
    void doGoHomeAction(int index);
    void doReloadAction();
    void doFinishAction();
    void doJDPayAction(int error, String msg);
    void doUploadAction(UploadImgInfo info);
}
