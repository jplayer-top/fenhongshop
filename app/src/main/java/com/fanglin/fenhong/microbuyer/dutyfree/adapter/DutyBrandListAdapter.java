package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.fanglin.fenhong.microbuyer.base.model.BrandMessage;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyBrandListViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.SpliterViewHolder;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/17-上午10:19.
 * 功能描述: 极速免税店 品牌搜索列表页
 */
public class DutyBrandListAdapter extends SectionedRecyclerViewAdapter<SpliterViewHolder, DutyBrandListViewHolder, SpliterViewHolder> {

    private Context context;
    private List<BrandMessage> list;

    public DutyBrandListAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<BrandMessage> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<BrandMessage> list) {
        if (list == null || list.size() == 0) return;
        if (this.list != null) {
            this.list.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    protected int getItemCountForSection(int section) {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    protected int getSectionCount() {
        if (list == null) return 0;
        return 1;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected SpliterViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return SpliterViewHolder.getHolder(context);
    }

    @Override
    protected SpliterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return SpliterViewHolder.getHolder(context);
    }

    @Override
    protected DutyBrandListViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return DutyBrandListViewHolder.getHolder(context);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(SpliterViewHolder holder, int section) {
        if (section == 0) {
            holder.vSpliter.setVisibility(View.GONE);
        } else {
            holder.vSpliter.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onBindSectionFooterViewHolder(SpliterViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(DutyBrandListViewHolder holder, int section, int position) {
        BrandMessage brandMessage = list.get(position);
        holder.refreshView(brandMessage);
    }
}
