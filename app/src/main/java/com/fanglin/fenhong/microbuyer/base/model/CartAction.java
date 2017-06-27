package com.fanglin.fenhong.microbuyer.base.model;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/5/18-上午9:08.
 * 功能描述:
 */
public class CartAction {
    private boolean justChangeNum = false;
    private int num;//23  购物车数量
    private int is_global;//1-- 海外  0--国内

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getIs_global() {
        return is_global;
    }

    public boolean isJustChangeNum() {
        return justChangeNum;
    }

    public void setJustChangeNum(boolean justChangeNum) {
        this.justChangeNum = justChangeNum;
    }

    public void setIs_global(int is_global) {
        this.is_global = is_global;
    }
}
