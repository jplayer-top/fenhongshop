package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fanglin.fenhong.microbuyer.base.model.dutyfree.BaseProduct;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyCategoryDetail;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreeCategory;
import com.fanglin.fenhong.microbuyer.dutyfree.listener.FloorViewHolderListener;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.BannerViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyfloorHeaderViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.FloorViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.HalfGoodsViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.SpliterViewHolder;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/17-下午5:20.
 * 功能描述: 极速免税店 分类详情页
 */
public class DutyCategoryDetailAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder, SpliterViewHolder> {

    public final static int TYPE_ITEM_BANNER_CONTAINER = 0;
    public final static int TYPE_ITEM_HORIZONAL_CONTAINER = 1;
    public final static int TYPE_ITEM_GOODS = 2;

    public final static int TYPE_HEADER_NULL = 3;
    public final static int TYPE_HEADER_FLOOR = 4;

    private int index;

    private Context mContext;
    private DutyCategoryDetail detail;
    private List<DutyfreeCategory> categoryList;

    public DutyCategoryDetailAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected int getItemCountForSection(int section) {
        int viewType = getSectionHeaderViewType(section);
        if (viewType == TYPE_HEADER_NULL) {
            return 1;
        }
        DutyfreeCategory category = getCategory(section);
        List<BaseProduct> goodsList = category.getGoodsList();
        if (goodsList == null) return 0;
        return goodsList.size();
    }

    public void setDetail(DutyCategoryDetail detail) {
        this.detail = detail;
        if (this.detail != null) {
            categoryList = this.detail.getCategory();
        }
        notifyDataSetChanged();
    }

    public List<DutyfreeCategory> getCategoryList() {
        return categoryList;
    }

    public DutyfreeCategory getCategory(int section) {
        return categoryList.get(section - 2);
    }

    public BaseProduct getProduct(int section, int position) {
        DutyfreeCategory category = getCategory(section);
        List<BaseProduct> goodsList = category.getGoodsList();
        return goodsList.get(position);
    }

    @Override
    protected int getSectionCount() {
        if (detail == null) return 0;

        if (categoryList == null) return 2;
        return categoryList.size() + 2;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER_NULL) {
            return SpliterViewHolder.getHolder(mContext);
        } else {
            return DutyfloorHeaderViewHolder.getHolder(mContext);
        }
    }

    public void setIndex(int index) {
        this.index = index;
        notifyDataSetChanged();
    }

    @Override
    protected SpliterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return SpliterViewHolder.getHolder(mContext);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM_BANNER_CONTAINER) {
            return BannerViewHolder.getHolder(mContext, false);
        } else if (viewType == TYPE_ITEM_HORIZONAL_CONTAINER) {
            return FloorViewHolder.getHolder(mContext);
        } else {
            return HalfGoodsViewHolder.getHolder(mContext);
        }
    }

    @Override
    protected void onBindSectionHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {
        int viewType = getSectionHeaderViewType(section);
        if (viewType == TYPE_HEADER_NULL) {
            SpliterViewHolder spliter = (SpliterViewHolder) holder;
            spliter.vSpliter.setVisibility(View.GONE);
        } else {
            DutyfloorHeaderViewHolder floorer = (DutyfloorHeaderViewHolder) holder;
            DutyfreeCategory category = getCategory(section);
            floorer.refreshView(category.getTypeName());
            holder.itemView.setTag(section);
        }
    }

    @Override
    protected void onBindSectionFooterViewHolder(SpliterViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int section, int position) {
        int viewType = getSectionItemViewType(section, position);
        if (viewType == TYPE_ITEM_BANNER_CONTAINER) {
            BannerViewHolder bannerHolder = (BannerViewHolder) holder;
            bannerHolder.refreshView(detail.getBanners());
        } else if (viewType == TYPE_ITEM_HORIZONAL_CONTAINER) {
            FloorViewHolder floorer = (FloorViewHolder) holder;
            floorer.setType(1);
            floorer.setFloorListener(floorListener);
            floorer.refreshView(categoryList);
            floorer.changePositon(index);
        } else {
            HalfGoodsViewHolder goodsHolder = (HalfGoodsViewHolder) holder;
            BaseProduct product = getProduct(section, position);
            goodsHolder.refreshView(position, product);
        }
    }

    @Override
    protected int getSectionHeaderViewType(int section) {
        if (section > 1) {
            return TYPE_HEADER_FLOOR;
        }

        return TYPE_HEADER_NULL;
    }

    @Override
    protected int getSectionItemViewType(int section, int position) {
        if (section == 0) {
            return TYPE_ITEM_BANNER_CONTAINER;
        } else if (section == 1) {
            return TYPE_ITEM_HORIZONAL_CONTAINER;
        } else {
            return TYPE_ITEM_GOODS;
        }
    }

    @Override
    protected boolean isSectionHeaderViewType(int viewType) {
        return TYPE_HEADER_FLOOR == viewType || TYPE_HEADER_NULL == viewType;
    }

    @Override
    public boolean isSectionHeaderPosition(int position) {
        return super.isSectionHeaderPosition(position);
    }

    //获取
    public int getPositionOfSection(int index) {
        if (categoryList == null || categoryList.size() == 0)
            return 0;
        int position = 0;
        for (int i = 0; i < index; i++) {
            List<BaseProduct> goodsList = categoryList.get(i).getGoodsList();
            int goodsSize = 0;
            if (goodsList != null && goodsList.size() > 0) {
                goodsSize = goodsList.size();
            }
            position += goodsSize + 1;
        }
        return position + 4;
    }

    public int getSpanSize(int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_ITEM_GOODS) {
            return 1;
        }
        return 2;
    }

    private FloorViewHolderListener floorListener;

    public void setFloorListener(FloorViewHolderListener floorListener) {
        this.floorListener = floorListener;
    }
}
