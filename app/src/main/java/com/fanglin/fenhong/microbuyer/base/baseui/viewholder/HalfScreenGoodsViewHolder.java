package com.fanglin.fenhong.microbuyer.base.baseui.viewholder;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.BaseGoods;
import com.fanglin.fenhong.microbuyer.base.model.GoodsList;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/9/30-上午11:25.
 * 功能描述: 1/2屏 商品样式（所谓四宫格）
 */
public class HalfScreenGoodsViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView, ivTop;
    public ImageView ivNation;
    public TextView tvNation;
    public TextView tvTitle;
    public TextView tvPrice, tvMarketPrice;
    public ImageView ivFlagGlobal;

    //奖金相关
    public TextView tvRewardRatio, tvRewordMoney, tvShare;
    public FrameLayout FReword;

    public HalfScreenGoodsViewHolder(View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.imageView);
        ivTop = (ImageView) itemView.findViewById(R.id.ivTop);
        ivNation = (ImageView) itemView.findViewById(R.id.ivNation);

        tvNation = (TextView) itemView.findViewById(R.id.tvNation);
        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
        tvMarketPrice = (TextView) itemView.findViewById(R.id.tvMarketPrice);

        ivFlagGlobal = (ImageView) itemView.findViewById(R.id.ivFlagGlobal);

        tvMarketPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        //奖金相关
        tvRewardRatio = (TextView) itemView.findViewById(R.id.tvRewardRatio);
        tvRewordMoney = (TextView) itemView.findViewById(R.id.tvRewordMoney);
        tvShare = (TextView) itemView.findViewById(R.id.tvShare);

        FReword = (FrameLayout) itemView.findViewById(R.id.FReword);
    }

    private String resource_tags;

    public void setResource_tags(String resource_tags) {
        this.resource_tags = resource_tags;
    }

    public void setModelData(final Context mContext, final BaseGoods goods) {
        if (goods != null) {
            new FHImageViewUtil(imageView).setImageURI(goods.goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);
            ivTop.setImageResource(goods.getGoodsSaleState());
            new FHImageViewUtil(ivNation).setImageURI(goods.nation_flag, FHImageViewUtil.SHOWTYPE.BANNER);
            tvNation.setText(goods.goods_promise);
            tvTitle.setText(goods.getGoodsNameForDetail(mContext));
            tvPrice.setText(goods.getGoodspriceDesc());
            tvMarketPrice.setText(goods.getMarketpriceDesc(true));

            if (goods.goods_source > 0) {
                ivFlagGlobal.setVisibility(View.VISIBLE);
            } else {
                ivFlagGlobal.setVisibility(View.INVISIBLE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.gotoGoodsDetail(mContext, goods.goods_id, resource_tags, null);
                }
            });

            tvRewardRatio.setText(goods.getFanliDesc());
            if (goods.isShowReward()) {
                tvRewardRatio.setVisibility(View.VISIBLE);
                FReword.setVisibility(View.VISIBLE);
                tvRewordMoney.setText(goods.getGoodsRewardMoney());
                tvShare.setText(goods.getGoods_share_intro());
            } else {
                tvRewardRatio.setVisibility(View.GONE);
                FReword.setVisibility(View.GONE);
            }

            FReword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareData shareData = new ShareData();
                    shareData.title = goods.share_title;
                    shareData.content = goods.share_describe;
                    shareData.imgs = goods.share_img;
                    shareData.url = String.format(BaseVar.SHARE_GOODS_DTL, goods.goods_id);
                    shareData.price = goods.goods_price;
                    shareData.deductMoney = goods.getGoods_reward_money();

                    ShareData.fhShare((BaseFragmentActivity) mContext, shareData, null);
                }
            });
        }

    }
}
