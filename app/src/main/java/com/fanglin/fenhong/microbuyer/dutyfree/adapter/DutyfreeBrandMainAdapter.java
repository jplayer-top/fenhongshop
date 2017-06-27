package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fanglin.fenhong.microbuyer.base.model.Banner;
import com.fanglin.fenhong.microbuyer.base.model.BrandMessage;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.BaseProduct;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.HomeBrand;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.BannerViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyBrandViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.HalfGoodsViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.SpliterViewHolder;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/14-上午11:56.
 * 功能描述:极速免税店 品牌首页适配器
 */
public class DutyfreeBrandMainAdapter extends SectionedRecyclerViewAdapter<SpliterViewHolder, RecyclerView.ViewHolder, RecyclerView.ViewHolder> {

    public static final int TYPE_BANNER = 0;
    public static final int TYPE_BRAND = 1;
    public static final int TYPE_GOODS = 2;

    private Context mContext;
    private List<Banner> banners;//Banner图
    private List<BrandMessage> brands;//品牌
    private List<BaseProduct> products;//商品

    public DutyfreeBrandMainAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected int getItemCountForSection(int section) {
        int viewType = getSectionItemViewType(section, 0);
        if (viewType == TYPE_BANNER) {
            return 1;
        } else if (viewType == TYPE_BRAND) {
            if (brands == null) return 0;
            return brands.size();
        } else {
            if (products == null) return 0;
            return products.size();
        }
    }

    public void setData(HomeBrand data) {
        if (data != null) {
            banners = data.getBanners();
            brands = data.getGoodsBrand();
            products = data.getGoodsList();
            notifyDataSetChanged();
        }
    }

    public void addProducts(List<BaseProduct> products) {
        if (this.products == null) return;
        if (products != null && products.size() > 0) {
            this.products.addAll(products);
            notifyDataSetChanged();
        }
    }

    @Override
    protected int getSectionCount() {
        return 3;
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
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER) {
            return BannerViewHolder.getHolder(mContext, false);
        } else if (viewType == TYPE_BRAND) {
            return DutyBrandViewHolder.getHolder(mContext);
        } else {
            return HalfGoodsViewHolder.getHolder(mContext);
        }
    }

    @Override
    protected void onBindSectionHeaderViewHolder(SpliterViewHolder holder, int section) {
        int viewType = getSectionItemViewType(section, 0);
        if (viewType == TYPE_BANNER) {
            holder.vSpliter.setVisibility(View.GONE);
        } else {
            holder.vSpliter.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int section, int position) {
        int viewType = getSectionItemViewType(section, position);
        if (viewType == TYPE_BANNER) {
            BannerViewHolder bannerHolder = (BannerViewHolder) holder;
            bannerHolder.refreshView(banners);
        } else if (viewType == TYPE_BRAND) {
            DutyBrandViewHolder brandHolder = (DutyBrandViewHolder) holder;
            brandHolder.refreshView(position, brands);
        } else {
            HalfGoodsViewHolder goodsHolder = (HalfGoodsViewHolder) holder;
            BaseProduct product = products.get(position);
            goodsHolder.refreshView(position, product);
        }
    }

    @Override
    protected int getSectionItemViewType(int section, int position) {
        if (section == 0) {
            return TYPE_BANNER;
        } else if (section == 1) {
            return TYPE_BRAND;
        } else {
            return TYPE_GOODS;
        }
    }

    public int getSpanSize(int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_GOODS) {
            return 1;
        } else if (viewType == TYPE_BRAND) {
            return 1;
        }
        return 2;
    }
}
