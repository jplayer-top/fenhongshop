package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.BaseProduct;
import com.fanglin.fenhong.microbuyer.dutyfree.DFGoodsDetailActivity;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/19-下午12:15.
 * 功能描述: 品牌商品
 */
public class BrandGoodsViewHolder extends RecyclerView.ViewHolder {
    ImageView ivImage;
    TextView tvName, tvPrice;

    public BrandGoodsViewHolder(View itemView) {
        super(itemView);
        ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
    }

    public static BrandGoodsViewHolder getHolder(Context context) {
        View view = View.inflate(context, R.layout.item_dutyfree_brand_goods, null);
        return new BrandGoodsViewHolder(view);
    }

    public void refreshView(final BaseProduct product) {
        new FHImageViewUtil(ivImage).setImageURI(product.getProductImgUrl(), FHImageViewUtil.SHOWTYPE.DEFAULT);
        tvName.setText(product.getProductName());
        tvPrice.setText(product.getPrice4Show());
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFunc.gotoActivity(v.getContext(), DFGoodsDetailActivity.class, product.getProductId());
            }
        });
    }
}
