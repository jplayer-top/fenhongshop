package com.fanglin.fenhong.microbuyer.base.listener;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/5/24-下午5:18.
 * 功能描述:订单列表按钮操作的回调
 */
public interface OrderCallBack {
    void onPay(int position);

    void onDelete(int position);

    void onDelivery(int position);

    void onSubmit(int position);

    void onEvaluate(int position, int type);//type==1,追加评价 否则 去评价

    void onCancel(int position);
}
