package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.fanglin.fenhong.microbuyer.base.model.dutyfree.BaseProduct;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.HalfGoodsViewHolder;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/15-上午9:33.
 * 功能描述: 极速免税店 商品列表页
 */
public class DutyfreeGoodsListAdapter extends RecyclerView.Adapter<HalfGoodsViewHolder> {
    private Context mContext;
    private List<BaseProduct> products;

    public void setProducts(List<BaseProduct> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public void addProducts(List<BaseProduct> products) {
        if (products == null || products.size() == 0) return;
        if (this.products != null) {
            this.products.addAll(products);
            notifyDataSetChanged();
        }
    }


    public DutyfreeGoodsListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getItemCount() {
        if (products == null)
            return 0;
        return products.size();
    }

    @Override
    public HalfGoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return HalfGoodsViewHolder.getHolder(mContext);
    }

    @Override
    public void onBindViewHolder(HalfGoodsViewHolder holder, int position) {
        BaseProduct product = products.get(position);
        holder.refreshView(position, product);
    }
}
