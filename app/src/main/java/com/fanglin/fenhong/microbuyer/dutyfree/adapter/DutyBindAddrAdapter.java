package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyAddress;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyCartProduct;
import com.fanglin.fenhong.microbuyer.dutyfree.CartCheckCache;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyCartCheckAddressViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyCartCheckHeaderViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.SpliterViewHolder;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/16-下午7:40.
 * 功能描述: 关联地址
 */
public class DutyBindAddrAdapter extends SectionedRecyclerViewAdapter<DutyCartCheckHeaderViewHolder, DutyCartCheckAddressViewHolder, SpliterViewHolder> {

    private Context mContext;
    private DutyCartProduct product;
    private List<DutyAddress> addrList;
    private int totalNum;

    public DutyBindAddrAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setAddrList(List<DutyAddress> addrList) {
        this.addrList = addrList;
        notifyDataSetChanged();
    }

    public void setProduct(DutyCartProduct product) {
        this.product = product;
        notifyDataSetChanged();
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    @Override
    protected int getItemCountForSection(int section) {
        if (addrList == null) return 0;
        return addrList.size();
    }

    public boolean hasBinded() {
        return getItemCountForSection(0) > 0;
    }

    @Override
    protected int getSectionCount() {
        if (product == null) return 0;
        return 1;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return true;
    }

    @Override
    protected DutyCartCheckHeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return DutyCartCheckHeaderViewHolder.getHolder(mContext, R.dimen.dp_of_10);
    }

    @Override
    protected SpliterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return SpliterViewHolder.getHolder(mContext, R.dimen.dp_of_50);
    }

    @Override
    protected DutyCartCheckAddressViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return DutyCartCheckAddressViewHolder.getHolder(mContext);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(DutyCartCheckHeaderViewHolder holder, int section) {
        holder.tvAddr.setVisibility(View.GONE);
        holder.vLine.setBackgroundResource(R.color.com_bg);
        holder.refreshView(product);
    }

    @Override
    protected void onBindSectionFooterViewHolder(SpliterViewHolder holder, int section) {
        holder.vSpliter.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onBindItemViewHolder(DutyCartCheckAddressViewHolder holder, int section, final int position) {
        int count = getItemCountForSection(section);
        if (position == count - 1) {
            holder.vLine.setVisibility(View.GONE);
        } else {
            holder.vLine.setVisibility(View.VISIBLE);
        }
        final DutyAddress address = addrList.get(position);
        holder.setTotalNum(totalNum);
        holder.refreshView(address);
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasDeleted = CartCheckCache.removeBindedAddress(address);
                if (hasDeleted) {
                    addrList.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
    }

    public boolean canBind() {
        if (addrList == null || addrList.size() == 0) return true;
        int num = 0;
        for (DutyAddress addr : addrList) {
            num += addr.getSelectNum();
        }
        return num < totalNum;
    }
}
