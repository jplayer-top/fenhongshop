package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyOrder;
import com.fanglin.fhlib.other.FHLib;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/11-上午11:27.
 * 功能描述: 极速免税店 订单详情页顶部（订单状态）
 */
public class OrderDetailTopViewHolder extends RecyclerView.ViewHolder {
    TextView tvOrderState, tvOrderSN, tvCopy, tvOrderTime, tvName;

    public OrderDetailTopViewHolder(View itemView) {
        super(itemView);
        tvOrderState = (TextView) itemView.findViewById(R.id.tvOrderState);
        tvOrderSN = (TextView) itemView.findViewById(R.id.tvOrderSN);
        tvCopy = (TextView) itemView.findViewById(R.id.tvCopy);
        tvOrderTime = (TextView) itemView.findViewById(R.id.tvOrderTime);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
    }

    public static OrderDetailTopViewHolder getHolder(Context mContext) {
        View view = View.inflate(mContext, R.layout.item_orderdtl_top, null);
        return new OrderDetailTopViewHolder(view);
    }

    public void refreshView(final DutyOrder order) {
        if (order != null) {
            tvOrderState.setText(order.getState_desc());
            tvOrderSN.setText(order.getOrder_sn());
            tvCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FHLib.copy(v.getContext(), order.getOrder_sn());
                    BaseFunc.showMsg(v.getContext(), "复制成功");
                }
            });
            tvOrderTime.setText(order.getPaymentTimeDesc(tvOrderTime.getContext()));
            tvName.setText(order.getOrderName());
        }
    }
}
