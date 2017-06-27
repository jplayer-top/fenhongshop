package com.fanglin.fenhong.microbuyer.base.model;

/**
 * 青岛芳林信息
 * Created by Plucky on 2016/12/19.
 * 功能描述: 二维码生成信息
 */

public class QrcodeInfo {
    private String qrtext;
    private String preFix;
    private int width;
    private int height;

    public String getQrtext() {
        return qrtext;
    }

    public String getPreFix() {
        return preFix;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
