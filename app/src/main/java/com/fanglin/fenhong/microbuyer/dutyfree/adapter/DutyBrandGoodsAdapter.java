package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.fanglin.fenhong.microbuyer.base.model.dutyfree.BaseProduct;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.BrandGoodsViewHolder;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/19-下午12:05.
 * 功能描述: 极速免税店 品牌商品
 */
public class DutyBrandGoodsAdapter extends RecyclerView.Adapter<BrandGoodsViewHolder> {

    private Context mContext;
    private List<BaseProduct> products;

    public DutyBrandGoodsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getItemCount() {
        if (products == null)
            return 0;
        return products.size();
    }

    public void setProducts(List<BaseProduct> products) {
        this.products = products;
    }

    public BaseProduct getItem(int position) {
        return products.get(position);
    }

    @Override
    public BrandGoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return BrandGoodsViewHolder.getHolder(mContext);
    }

    @Override
    public void onBindViewHolder(BrandGoodsViewHolder holder, int position) {
        holder.refreshView(getItem(position));
    }
}
