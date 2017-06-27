package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.R;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/3/10-下午9:25.
 * 功能描述:商品列表、购物车、
 */
public class MiaoshaTag {
    public String tag;// 手机秒杀,
    public double price;// 55.00,
    public int top_limit;// 限购数量 (注意：0 代表不限购)
    public double origin_price;// 未满足秒杀条件的原价
    public long countdown;//
    public int killing_stock;// 秒杀库存数
    public int purchased_num;// 已购数量

    public String share_title;//分享标题
    public String share_desc;// 分享描述
    public String share_img;// 分享图片

    public String getMiaoshaDesc() {
        if (top_limit > 0) {
            if (top_limit > 1) {
                return "  购买1-" + top_limit + "件时享受秒杀优惠";
            } else {
                return "  购买1件时享受秒杀优惠";
            }
        } else {
            return "";
        }
    }

    /**
     * @param buy_num        选择购买数
     * @param normal_storage 普通库存
     * @param normal_price   普通价格
     * @return 返回秒杀UI显示逻辑
     */
    public Miaosha4Display getDisplay(int buy_num,
                                      int normal_storage,
                                      double normal_price) {
        Miaosha4Display mdisplay = new Miaosha4Display();
        if (killing_stock > 0) {
            if (top_limit > 0) {
                //可购数量
                boolean isFlag = top_limit < killing_stock;
                int realLimt = isFlag ? top_limit : killing_stock;

                int canBuyNum = realLimt - purchased_num;
                if (canBuyNum > 0) {
                    if (buy_num <= canBuyNum) {
                        mdisplay.isNormalStorage = false;
                        mdisplay.storageLimit = killing_stock;
                        mdisplay.storageShow = killing_stock;
                        mdisplay.price = price;
                        mdisplay.ifshowtag = true;
                        mdisplay.drawableLeft = R.drawable.img_miaosha;
                        mdisplay.showtag1 = "限购" + canBuyNum + "件";
                        mdisplay.showtag2 = "";
                    } else {
                        if (canBuyNum >= normal_storage) {
                            mdisplay.isNormalStorage = false;
                            mdisplay.storageLimit = canBuyNum;//可能比较绕 目的是为了防止 逻辑死循环
                            mdisplay.storageShow = killing_stock;
                            mdisplay.price = price;
                            mdisplay.ifshowtag = true;
                            mdisplay.drawableLeft = R.drawable.img_miaosha;
                            mdisplay.showtag1 = "";
                            mdisplay.showtag2 = "普通库存不足";
                        } else {
                            mdisplay.isNormalStorage = true;
                            mdisplay.storageLimit = normal_storage;
                            mdisplay.storageShow = normal_storage;
                            mdisplay.price = normal_price;
                            mdisplay.ifshowtag = true;
                            mdisplay.drawableLeft = 0;
                            mdisplay.showtag1 = "";
                            mdisplay.showtag2 = isFlag ? "超出限购数量不享受优惠价" : "超出秒杀库存不享受优惠价";
                        }
                    }
                } else {
                    mdisplay.isNormalStorage = true;
                    mdisplay.storageLimit = normal_storage;
                    mdisplay.storageShow = normal_storage;
                    mdisplay.price = normal_price;
                    mdisplay.ifshowtag = true;
                    mdisplay.drawableLeft = 0;
                    mdisplay.showtag1 = "";
                    mdisplay.showtag2 = "已超过限购数量不享受优惠价";
                }
            } else {
                //不限购
                if (buy_num <= killing_stock) {
                    mdisplay.isNormalStorage = false;
                    mdisplay.storageLimit = killing_stock;
                    mdisplay.storageShow = killing_stock;
                    mdisplay.price = price;
                    mdisplay.ifshowtag = true;
                    mdisplay.drawableLeft = R.drawable.img_miaosha;
                    mdisplay.showtag1 = "";
                    mdisplay.showtag2 = "";
                } else {
                    if (killing_stock >= normal_storage) {
                        mdisplay.isNormalStorage = false;
                        mdisplay.storageLimit = killing_stock;
                        mdisplay.storageShow = killing_stock;
                        mdisplay.price = price;
                        mdisplay.ifshowtag = true;
                        mdisplay.drawableLeft = R.drawable.img_miaosha;
                        mdisplay.showtag1 = "";
                        mdisplay.showtag2 = "";
                    } else {
                        mdisplay.isNormalStorage = true;
                        mdisplay.storageLimit = normal_storage;
                        mdisplay.storageShow = normal_storage;
                        mdisplay.price = normal_price;
                        mdisplay.ifshowtag = true;
                        mdisplay.drawableLeft = 0;
                        mdisplay.showtag1 = "";
                        mdisplay.showtag2 = "大于秒杀库存不享受优惠价";
                    }
                }
            }
        } else {
            mdisplay.isNormalStorage = true;
            mdisplay.storageLimit = normal_storage;
            mdisplay.storageShow = normal_storage;
            mdisplay.price = normal_price;
            mdisplay.ifshowtag = true;
            mdisplay.drawableLeft = 0;
            mdisplay.showtag1 = "";
            mdisplay.showtag2 = "秒杀商品已抢光";
        }
        return mdisplay;
    }

    public class Miaosha4Display {
        public int storageLimit;//购物车和详情购买时 最大限制数
        public int storageShow;//库存显示数
        public boolean ifshowtag;//是否显示秒杀标签
        public String showtag1;//秒杀标签提示1灰色
        public String showtag2;//秒杀标签提示红色
        public double price;// 价格显示
        public int drawableLeft;
        public boolean isNormalStorage;//标示是否为普通库存
    }
}
