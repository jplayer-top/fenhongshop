package com.fanglin.fenhong.microbuyer.base.model;

/**
 * 青岛芳林信息
 * Created by Plucky on 2016/12/19.
 * 功能描述: 上传图片信息
 */

public class UploadImgInfo {
    private boolean crop;//是否裁剪
    private int outputX;//输出 长
    private int outputY;//输出 宽
    private int aspectX;//裁剪比例 长
    private int aspectY;//裁剪比例 宽

    public boolean isCrop() {
        return crop;
    }

    public int getOutputX() {
        return outputX;
    }

    public int getOutputY() {
        return outputY;
    }

    public int getAspectX() {
        return aspectX;
    }

    public int getAspectY() {
        return aspectY;
    }
}
