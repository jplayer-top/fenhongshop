package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyOrder;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/10-下午3:45.
 * 功能描述:订单列表头部ViewHolder
 */
public class OrderListHeaderViewHolder extends RecyclerView.ViewHolder {

    TextView tvName, tvStates;

    public OrderListHeaderViewHolder(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvStates = (TextView) itemView.findViewById(R.id.tvStates);
    }

    public static OrderListHeaderViewHolder getHolder(Context context) {
        View view = View.inflate(context, R.layout.item_orderlist_header, null);
        return new OrderListHeaderViewHolder(view);
    }

    public void refreshView(DutyOrder order) {
        if (order != null) {
            tvStates.setText(order.getState_desc());
        }
    }
}
