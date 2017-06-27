package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fanglin.fenhong.microbuyer.base.model.BrandMessage;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.BaseProduct;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyBrandDtlPindedViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyBrandDtlTopViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.HalfGoodsViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.SpliterViewHolder;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/24-下午1:29.
 * 功能描述: 极速免税店 品牌聚合页
 */
public class DutyBrandDtlAdapter extends SectionedRecyclerViewAdapter<SpliterViewHolder, RecyclerView.ViewHolder, SpliterViewHolder> implements DutyBrandDtlTopViewHolder.BrandTopChangeListener {

    public static final int TYPE_TOP = 0;
    public static final int TYPE_PINED = 1;
    public static final int TYPE_GOODS = 2;

    private Context mContext;
    private BrandMessage brandMessage;
    private List<BaseProduct> goodsList;
    private int index, sort;
    private DutyBrandDtlPindedViewHolder.PindedHeaderListener pindedHeaderListener;

    public DutyBrandDtlAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected int getItemCountForSection(int section) {
        int viewType = getSectionItemViewType(section, 0);
        if (viewType == TYPE_GOODS) {
            if (goodsList == null)
                return 0;
            return goodsList.size();
        }
        return 1;
    }

    @Override
    protected int getSectionCount() {
        return 3;
    }

    public void setIndexAndSort(int index, int sort) {
        this.index = index;
        this.sort = sort;
        notifyDataSetChanged();
    }

    public void setBrandMessage(BrandMessage brandMessage) {
        this.brandMessage = brandMessage;
        if (brandMessage != null) {
            goodsList = brandMessage.getGoodslist();
        }
        notifyDataSetChanged();
    }

    public void addGoodsList(List<BaseProduct> list) {
        if (list == null || list.size() == 0) return;
        if (goodsList != null) {
            goodsList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public BaseProduct getItem(int index) {
        if (goodsList != null && goodsList.size() > index) {
            return goodsList.get(index);
        }
        return null;
    }

    public void setPindedHeaderListener(DutyBrandDtlPindedViewHolder.PindedHeaderListener pindedHeaderListener) {
        this.pindedHeaderListener = pindedHeaderListener;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected SpliterViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return SpliterViewHolder.getHolder(mContext);
    }

    @Override
    protected SpliterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return SpliterViewHolder.getHolder(mContext);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TOP) {
            return DutyBrandDtlTopViewHolder.getHolder(mContext);
        } else if (viewType == TYPE_PINED) {
            return DutyBrandDtlPindedViewHolder.getHolder(mContext);
        } else {
            return HalfGoodsViewHolder.getHolder(mContext);
        }
    }

    @Override
    protected void onBindSectionHeaderViewHolder(SpliterViewHolder holder, int section) {
        holder.vSpliter.setVisibility(View.GONE);
    }

    @Override
    protected void onBindSectionFooterViewHolder(SpliterViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int section, int position) {
        int viewType = getSectionItemViewType(section, position);
        if (viewType == TYPE_TOP) {
            DutyBrandDtlTopViewHolder toper = (DutyBrandDtlTopViewHolder) holder;
            toper.setChangeListener(this);
            toper.refreshView(brandMessage);
        } else if (viewType == TYPE_PINED) {
            DutyBrandDtlPindedViewHolder piner = (DutyBrandDtlPindedViewHolder) holder;
            piner.setHeaderListener(pindedHeaderListener);
            piner.refreshView(index, sort);
        } else {
            HalfGoodsViewHolder goodser = (HalfGoodsViewHolder) holder;
            goodser.refreshView(position, getItem(position));
        }
    }

    @Override
    protected int getSectionItemViewType(int section, int position) {
        return section;
    }

    public int getSpanSize(int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_GOODS) {
            return 1;
        }
        return 2;
    }

    @Override
    public void onTopHeightChange() {
        notifyDataSetChanged();
    }
}
