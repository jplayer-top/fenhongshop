package com.fanglin.fenhong.microbuyer.base.baselib;

import com.fanglin.fhlib.other.FHLog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/10/9-上午11:07.
 * 功能描述: 文件下载类
 */
public class FHDownloadUtils extends RequestCallBack<File> {
    private String[] list;
    private int executedNum;// 已经下载数
    private int totalNum;// 正常的下载链接数

    public FHDownloadUtils(String... url) {
        this.list = url;
    }

    public void download() {
        if (list != null && list.length > 0) {
            for (String url : list) {
                if (BaseFunc.isValidUrl(url)) {
                    totalNum++;
                    int lastIdx = url.lastIndexOf("/");
                    String fileName = url.substring(lastIdx + 1);
                    String filePath = BaseFunc.getFileAbsPath("FHApp/image", fileName);
                    new HttpUtils().download(url, filePath, this);
                }
            }
        }
    }

    @Override
    public void onFailure(HttpException e, String s) {
        executedNum++;
        FHLog.d("Plucky", "FHDownloadUtils - onFailure:" + s);
    }

    @Override
    public void onSuccess(ResponseInfo<File> responseInfo) {
        executedNum++;
        if (executedNum == totalNum) {
            VarInstance.getInstance().showMsg("文件保存成功!");
        }
        VarInstance.getInstance().refreshMedia(responseInfo.result);
    }

    @Override
    public void onLoading(long total, long current, boolean isUploading) {
        super.onLoading(total, current, isUploading);
        FHLog.d("Plucky", "FHDownloadUtils - onLoading:" + current + "/" + total);
    }
}
