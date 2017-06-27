package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.Favorites;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/9/27-下午4:56.
 * 功能描述:品牌收藏适配器
 */
public class BrandFavAdapter extends FavoritesAdapter {

    public BrandFavAdapter(Context c) {
        super(c);
    }

    @Override
    public void onBindViewHolder(FavViewHolder holder, final int position) {
        BrandViewHolder brandHolder = (BrandViewHolder) holder;
        final Favorites item = getItem(position);
        brandHolder.tvBrandName.setText(item.brand_name);
        brandHolder.tvBrandDesc.setText(item.brand_temp_describe);
        new FHImageViewUtil(brandHolder.ivBrand).setImageURI(item.brand_pic, FHImageViewUtil.SHOWTYPE.DEFAULT);

        brandHolder.tvAction.setSelected(true);
        brandHolder.tvAction.setText("已关注");

        brandHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mcb != null) {
                    mcb.onItemClick(6, position);
                }
            }
        });

        brandHolder.tvAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (focusClickListener != null) {
                    focusClickListener.onFocusClick(item.brand_id);
                }
            }
        });
    }

    @Override
    public BrandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_fav_brand, null);
        return new BrandViewHolder(view);
    }

    class BrandViewHolder extends FavViewHolder {
        ImageView ivBrand;
        TextView tvAction, tvBrandDesc, tvBrandName;

        public BrandViewHolder(View itemView) {
            super(itemView);
            ivBrand = (ImageView) itemView.findViewById(R.id.ivBrand);

            tvBrandName = (TextView) itemView.findViewById(R.id.tvBrandName);
            tvBrandDesc = (TextView) itemView.findViewById(R.id.tvBrandDesc);
            tvAction = (TextView) itemView.findViewById(R.id.tvAction);
        }
    }

    public interface FocusClickListener {
        void onFocusClick(String ids);
    }

    private FocusClickListener focusClickListener;

    public void setFocusClickListener(FocusClickListener focusClickListener) {
        this.focusClickListener = focusClickListener;
    }
}
