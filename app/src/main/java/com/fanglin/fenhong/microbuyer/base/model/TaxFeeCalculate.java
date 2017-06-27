package com.fanglin.fenhong.microbuyer.base.model;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/4/26-下午4:19.
 * 功能描述:关税计算类
 */
public class TaxFeeCalculate {
    private double xiaofeiRate = 0.3;//消费税率
    private double zengzhiRate = 0.17;//增值税率
    private double zongheRate = 0.7;//综合税率
    private double guanshui = 0;
    private double freight = 0;//运费
    private double goodsPrice;
    private int goodsNum;

    public TaxFeeCalculate(double goodsPrice, int goodsNum, double freight) {
        this.goodsPrice = goodsPrice;
        this.goodsNum = goodsNum;
        this.freight = freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public void setXiaofeiRate(double xiaofeiRate) {
        this.xiaofeiRate = xiaofeiRate;
    }

    public void setZengzhiRate(double zengzhiRate) {
        this.zengzhiRate = zengzhiRate;
    }

    public void setZongheRate(double zongheRate) {
        this.zongheRate = zongheRate;
    }

    public void setGuanshui(double guanshui) {
        this.guanshui = guanshui;
    }

    public double getTaxFee() {
        double fee;
        double xiaofeiFee = (goodsPrice * goodsNum + freight) / (1 - xiaofeiRate) * xiaofeiRate;
        double zengzhiFee = (goodsNum * goodsPrice + freight + xiaofeiFee) * zengzhiRate;
        fee = (guanshui + xiaofeiFee + zengzhiFee) * zongheRate;
        return fee;
    }
}
