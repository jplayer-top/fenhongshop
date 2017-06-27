package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
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
 * author:Created by Plucky on 2016/11/14-下午1:42.
 * 功能描述: 急速免税店 四宫格商品 ViewHolder
 */
public class HalfGoodsViewHolder extends RecyclerView.ViewHolder {
    ImageView ivImage;
    TextView tvName, tvDesc, tvPrice;
    FrameLayout FVIP;
    TextView tvVIP, tvFlag;

    private String goodsId;
    private String goodsUrl;

    public HalfGoodsViewHolder(View itemView) {
        super(itemView);
        ivImage = (ImageView) itemView.findViewById(R.id.ivImage);

        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
        tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);

        FVIP = (FrameLayout) itemView.findViewById(R.id.FVIP);
        tvVIP = (TextView) itemView.findViewById(R.id.tvVIP);
        tvFlag = (TextView) itemView.findViewById(R.id.tvFlag);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaseFunc.isValidUrl(goodsUrl)) {
                    BaseFunc.urlClick(v.getContext(), goodsUrl);
                } else {
                    BaseFunc.gotoActivity(v.getContext(), DFGoodsDetailActivity.class, goodsId);
                }
            }
        });
    }

    public static HalfGoodsViewHolder getHolder(Context mContext) {
        View view = View.inflate(mContext, R.layout.item_dutyfree_half_goods, null);
        return new HalfGoodsViewHolder(view);
    }

    public void refreshView(int goodsIndex, BaseProduct product) {

        boolean isFirstLine = goodsIndex < 2;
        boolean isFirstCol = goodsIndex % 2 == 0;

        if (isFirstCol) {
            if (isFirstLine) {
                itemView.setBackgroundResource(R.drawable.duty_goods_second_quadrant_top);
            } else {
                itemView.setBackgroundResource(R.drawable.duty_goods_second_quadrant_normal);
            }
        } else {
            if (isFirstLine) {
                itemView.setBackgroundResource(R.drawable.duty_goods_first_quadrant_top);
            } else {
                itemView.setBackgroundResource(R.drawable.duty_goods_first_quadrant_normal);
            }
        }

        if (product == null) return;
        goodsId = product.getProductId();
        goodsUrl = product.getGoods_url();
        Context mContext = tvPrice.getContext();

        new FHImageViewUtil(ivImage).setImageURI(product.getProductImgUrl(), FHImageViewUtil.SHOWTYPE.DEFAULT);
        tvName.setText(product.getBrandName());
        tvDesc.setText(product.getProductName());


        if (product.showItem()) {
            FVIP.setVisibility(View.VISIBLE);
            tvVIP.setText(product.getItemPrice());
            tvFlag.setText(product.getItemLabel());

            tvPrice.setText(product.getPriceGray());
            tvPrice.setTextColor(mContext.getResources().getColor(R.color.color_99));
            tvPrice.setGravity(Gravity.LEFT);
            tvPrice.setTextSize(11);
        } else {
            FVIP.setVisibility(View.GONE);

            tvPrice.setText(product.getPrice4Show());
            tvPrice.setTextColor(mContext.getResources().getColor(R.color.fh_red));
            tvPrice.setGravity(Gravity.CENTER);
            tvPrice.setTextSize(14);
        }
    }
}
