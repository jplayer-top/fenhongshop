package com.fanglin.fenhong.microbuyer.base.model;

/**
 * Author: Created by lizhixin on 2016/2/22 15:51.
 */
public class EvaluatingGoodsEntity {
    public String goods_id;
    public String goods_name;//   商品名称
    public String goods_image;//    商品图片
    public float geval_scores;// 上一次的评价分数
    public String geval_content;// 上一次的评价内容
    public int store_evaluated;//  店铺评价是否完成  （0 显示店铺评价的三排星星   1 隐藏店铺评价）
    public int geval_isanonymous = 1;// 是否匿名 0:否 | 1:是

    public String comment;//用来存储品论内容
    public float stars;//用来存储商品评价星数

    //店铺评价相关
    public float stars_desc = 5;//  店铺描述 默认星级  5星
    public float stars_service = 5;//  店铺服务 默认星级  5星
    public float stars_release = 5;//  店铺发货 默认星级  5星

}
