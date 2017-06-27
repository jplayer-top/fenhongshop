package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/20-下午4:38.
 * 功能描述: 图片标签三条线
 */
public class TalentImageTagLine {
    private String value;//Laneige兰芝保湿乳液, 属性值
    private int x;//83, （以location为原点）
    private int y;//0,
    private String direction;//R 方向 L:左 | R:右

    public String getValue() {
        return value;
    }

    public boolean isLeft() {
        return TextUtils.equals("L", direction);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public static String getDirectionByBool(boolean aLeft) {
        return aLeft ? "L" : "R";
    }
}
