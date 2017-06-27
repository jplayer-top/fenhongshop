package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyOrder;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyOrderGoods;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyWaybill;
import com.fanglin.fenhong.microbuyer.dutyfree.listener.DutyBillActionListener;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.OrderDetailAddrViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.OrderDetailBottomViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.OrderDetailFooterViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.OrderDetailGoodsViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.OrderDetailNullViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.OrderDetailTopViewHolder;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/11-上午11:26.
 * 功能描述: 急速免税店 订单详情适配器
 */
public class OrderDetailAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder, OrderDetailFooterViewHolder> {

    public static final int TYPE_HEADER_ADDR = 0;
    public static final int TYPE_HEADER_NULL = 1;
    public static final int TYPE_ITEM_TOP = 2;
    public static final int TYPE_ITEM_GOODS = 3;
    public static final int TYPE_ITEM_BOTTOM = 4;

    private Context mContext;
    private DutyOrder order;

    public OrderDetailAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected int getItemCountForSection(int section) {
        int viewType = getSectionItemViewType(section, 0);
        if (viewType == TYPE_ITEM_GOODS) {
            DutyWaybill bill = getItemBill(section);
            List<DutyOrderGoods> goodsList = bill.getOrderGoods4Logic();
            if (goodsList == null) return 0;
            return goodsList.size();
        }
        return 1;
    }

    public void setOrder(DutyOrder order) {
        this.order = order;
        notifyDataSetChanged();
    }


    public DutyWaybill getItemBill(int section) {
        List<DutyWaybill> bills = order.getWaybill();
        return bills.get(section - 1);
    }

    private DutyBillActionListener listener;

    public void setListener(DutyBillActionListener listener) {
        this.listener = listener;
    }

    public DutyOrderGoods getGoods(int section, int position) {
        DutyWaybill bill = getItemBill(section);
        return bill.getOrderGoods().get(position);
    }

    @Override
    protected int getSectionCount() {
        if (order == null) return 0;
        List<DutyWaybill> bills = order.getWaybill();
        if (bills == null || bills.size() == 0) return 0;
        return bills.size() + 2;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        int viewType = getSectionItemViewType(section, 0);
        return viewType == TYPE_ITEM_GOODS;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER_ADDR) {
            return OrderDetailAddrViewHolder.getHolder(mContext);
        } else {
            View view = View.inflate(mContext, R.layout.item_orderdtl_null, null);
            return new OrderDetailNullViewHolder(view);
        }
    }

    @Override
    protected OrderDetailFooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return OrderDetailFooterViewHolder.getHolder(mContext);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM_TOP) {
            return OrderDetailTopViewHolder.getHolder(mContext);
        } else if (viewType == TYPE_ITEM_BOTTOM) {
            return OrderDetailBottomViewHolder.getHolder(mContext);
        } else {
            return OrderDetailGoodsViewHolder.getHolder(mContext);
        }
    }

    @Override
    protected void onBindSectionHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {
        int viewType = getSectionHeaderViewType(section);
        if (viewType == TYPE_HEADER_ADDR) {
            OrderDetailAddrViewHolder addrHolder = (OrderDetailAddrViewHolder) holder;
            DutyWaybill bill = getItemBill(section);
            addrHolder.setListener(listener);
            addrHolder.refreshView(bill);
        }
    }

    @Override
    protected void onBindSectionFooterViewHolder(OrderDetailFooterViewHolder holder, int section) {
        final DutyWaybill bill = getItemBill(section);
        holder.refreshView(bill);
        holder.LMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bill.setExpanded(!bill.isExpanded());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int section, int position) {
        int viewType = getSectionItemViewType(section, position);
        switch (viewType) {
            case TYPE_ITEM_TOP:
                OrderDetailTopViewHolder toper = (OrderDetailTopViewHolder) holder;
                toper.refreshView(order);
                break;
            case TYPE_ITEM_GOODS:
                OrderDetailGoodsViewHolder goodsHolder = (OrderDetailGoodsViewHolder) holder;
                DutyOrderGoods goods = getGoods(section, position);
                goodsHolder.refreshView(goods);
                break;
            case TYPE_ITEM_BOTTOM:
                OrderDetailBottomViewHolder bottomer = (OrderDetailBottomViewHolder) holder;
                bottomer.refreshView(order);
                break;
        }
    }

    @Override
    protected int getSectionHeaderViewType(int section) {
        int sectionCount = getSectionCount();
        if (section == 0 || section == sectionCount - 1) {
            return TYPE_HEADER_NULL;
        } else {
            return TYPE_HEADER_ADDR;
        }
    }

    @Override
    protected int getSectionItemViewType(int section, int position) {
        int sectionCount = getSectionCount();
        if (section == 0) {
            return TYPE_ITEM_TOP;
        } else if (section == sectionCount - 1) {
            return TYPE_ITEM_BOTTOM;
        } else {
            return TYPE_ITEM_GOODS;
        }
    }

    @Override
    protected boolean isSectionHeaderViewType(int viewType) {
        return viewType == TYPE_HEADER_ADDR || viewType == TYPE_HEADER_NULL;
    }

    public String[] getButtonDesc() {
        String[] res = new String[]{"", ""};
        if (order != null) {
            res = order.getButtonDesc(false);
        }
        return res;
    }

    public String getMoney() {
        if (order != null) {
            return order.getMoney4Dtl();
        }
        return "";
    }

    public String getNumAndFreight() {
        if (order != null) {
            return order.getNumAndFreight();
        }
        return "";
    }

    public DutyOrder getOrder() {
        return order;
    }
}
