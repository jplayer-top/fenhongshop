package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyOrder;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyOrderGoods;
import com.fanglin.fenhong.microbuyer.dutyfree.listener.DutyOrderActionListener;
import com.fanglin.fenhong.microbuyer.dutyfree.OrderDetailActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.OrderListActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.OrderListFooterViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.OrderListHeaderViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.OrderListItemViewHolder;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/10-下午3:03.
 * 功能描述: 极速免税店我的订单列表
 */
public class OrderListAdapter extends SectionedRecyclerViewAdapter<OrderListHeaderViewHolder, OrderListItemViewHolder, OrderListFooterViewHolder> {

    private Context mContext;
    private List<DutyOrder> orderList;

    public OrderListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    private DutyOrderActionListener listener;

    public void setListener(DutyOrderActionListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getItemCountForSection(int section) {
        DutyOrder order = orderList.get(section);
        List<DutyOrderGoods> goodsList = order.getOrder_goods();
        if (goodsList == null)
            return 0;
        return goodsList.size();
    }

    @Override
    protected int getSectionCount() {
        if (orderList == null) return 0;
        return orderList.size();
    }

    public void setOrderList(List<DutyOrder> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }

    public void addOrderList(List<DutyOrder> list) {
        if (list == null || list.size() == 0) return;
        if (orderList != null) {
            orderList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void removeItem(int section) {
        if (orderList != null && orderList.size() > section) {
            orderList.remove(section);
            notifyDataSetChanged();
        }
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return true;
    }

    @Override
    protected OrderListHeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return OrderListHeaderViewHolder.getHolder(mContext);
    }

    @Override
    protected OrderListFooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return OrderListFooterViewHolder.getHolder(mContext);
    }

    @Override
    protected OrderListItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return OrderListItemViewHolder.getHolder(mContext);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(OrderListHeaderViewHolder holder, int section) {
        DutyOrder order = orderList.get(section);
        holder.refreshView(order);
    }

    @Override
    protected void onBindSectionFooterViewHolder(OrderListFooterViewHolder holder, int section) {
        DutyOrder order = orderList.get(section);
        holder.setListener(listener);
        holder.refreshView(order, section);
    }

    @Override
    protected void onBindItemViewHolder(OrderListItemViewHolder holder, int section, int position) {
        final DutyOrder order = orderList.get(section);
        DutyOrderGoods goods = order.getOrder_goods().get(position);
        holder.refreshView(goods);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFunc.gotoActivity4Result((Activity) mContext, OrderDetailActivity.class, order.getOrder_id(), OrderListActivity.REQPAYORDER);
            }
        });
    }
}
