package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyAddress;
import com.fanglin.fenhong.microbuyer.dutyfree.CartCheckCache;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyAddrListViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.SpliterViewHolder;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/16-下午9:08.
 * 功能描述:
 */
public class DutyAddrAdapter extends SectionedRecyclerViewAdapter<SpliterViewHolder, DutyAddrListViewHolder, SpliterViewHolder> {

    private Context context;
    private List<DutyAddress> list;

    public DutyAddrAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<DutyAddress> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    protected int getItemCountForSection(int section) {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    protected int getSectionCount() {
        return 1;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return true;
    }

    @Override
    protected SpliterViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return SpliterViewHolder.getHolder(context);
    }

    @Override
    protected SpliterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return SpliterViewHolder.getHolder(context, R.dimen.dp_of_50);
    }

    @Override
    protected DutyAddrListViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return DutyAddrListViewHolder.getHolder(context);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(SpliterViewHolder holder, int section) {

    }

    @Override
    protected void onBindSectionFooterViewHolder(SpliterViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(DutyAddrListViewHolder holder, int section, int position) {
        final DutyAddress address = list.get(position);
        holder.refreshView(address);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (DutyAddress dutyAddress : list) {
                    if (dutyAddress.isSelected()) {
                        dutyAddress.setSelected(false);
                    }
                }
                address.setSelected(!address.isSelected());
                notifyDataSetChanged();
            }
        });
    }

    public int getSelectedNum() {
        if (list == null || list.size() == 0) return 0;
        int num = 0;
        for (DutyAddress addr : list) {
            if (addr.isSelected()) {
                num++;
            }
        }
        return num;
    }

    public boolean onBindAddress(String cartID) {
        if (list == null || list.size() == 0) return false;
        boolean hasOne = false;
        for (DutyAddress addr : list) {
            if (addr.isSelected()) {
                hasOne = true;
                CartCheckCache.bindAddrOfGoods(addr, cartID);
            }
        }
        return hasOne;
    }
}
