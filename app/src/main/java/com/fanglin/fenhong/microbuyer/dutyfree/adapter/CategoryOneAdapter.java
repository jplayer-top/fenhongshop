package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.CategoryBanner;
import com.fanglin.fenhong.microbuyer.base.model.Banner;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.BaseProduct;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreeCategory;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.HomeCategory;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.BannerViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.HalfGoodsViewHolder;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/13-下午2:31.
 * 功能描述: 极速免税店一级分类向下页面适配器
 */
public class CategoryOneAdapter extends RecyclerView.Adapter {

    public static final int TYPE_BANNER = 0;
    public static final int TYPE_CATEGORY = 1;
    public static final int TYPE_GOODS = 2;

    private Context mContext;
    private List<Banner> banners;//Banner图
    private List<DutyfreeCategory> categories;//分类
    private List<BaseProduct> products;//商品

    public CategoryOneAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(HomeCategory data) {
        if (data != null) {
            this.banners = data.getBanners();
            this.categories = data.getCategory();
            this.products = data.getGoodsList();
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
    public int getItemCount() {
        if (products == null)
            return 2;
        return products.size() + 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER) {
            return BannerViewHolder.getHolder(mContext, false);
        } else if (viewType == TYPE_CATEGORY) {
            View view = View.inflate(mContext, R.layout.item_category_container, null);
            return new CategoryViewHolder(view);
        } else {
            return HalfGoodsViewHolder.getHolder(mContext);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        holder.itemView.setTag(position);//传入分区

        if (viewType == TYPE_BANNER) {
            BannerViewHolder bannerHolder = (BannerViewHolder) holder;
            bannerHolder.refreshView(banners);
        } else if (viewType == TYPE_CATEGORY) {
            CategoryViewHolder categoryHolder = (CategoryViewHolder) holder;
            categoryHolder.LContainer.removeAllViews();
            if (categories != null && categories.size() > 0) {
                CategoryBanner categoryBanner = new CategoryBanner(mContext);
                categoryHolder.LContainer.addView(categoryBanner.getView(categories));
            }
        } else {
            HalfGoodsViewHolder goodsHolder = (HalfGoodsViewHolder) holder;
            int index = position - 2;//商品index
            goodsHolder.refreshView(index, products.get(index));
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        } else if (position == 1) {
            return TYPE_CATEGORY;
        } else {
            return TYPE_GOODS;
        }
    }


    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        LinearLayout LContainer;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            LContainer = (LinearLayout) itemView.findViewById(R.id.LContainer);
        }
    }

    public int getSpanSize(int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_GOODS) {
            return 1;
        }
        return 2;
    }
}

