package com.fanglin.fenhong.microbuyer.base.baseui.viewholder;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.GoodsList;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.buyer.adapter.GoodsListAdapter;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/9/30-下午4:59.
 * 功能描述: 单行商品适配器
 */
public class FullScreenGoodsViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView, ivTop;
    public ImageView ivNation;
    public TextView tvNation;
    public TextView tvTitle;
    public TextView tvPrice, tvMarketPrice;
    public ImageView ivFlagGlobal;

    //奖金相关
    public TextView tvRewardRatio, tvRewordMoney, tvShare;
    public LinearLayout LShare;

    public FullScreenGoodsViewHolder(View itemView) {
        super(itemView);

        ivFlagGlobal = (ImageView) itemView.findViewById(R.id.ivFlagGlobal);
        ivTop = (ImageView) itemView.findViewById(R.id.ivTop);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);

        ivNation = (ImageView) itemView.findViewById(R.id.ivNation);

        tvNation = (TextView) itemView.findViewById(R.id.tvNation);
        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
        tvMarketPrice = (TextView) itemView.findViewById(R.id.tvMarketPrice);


        tvMarketPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        //奖金相关
        tvRewardRatio = (TextView) itemView.findViewById(R.id.tvRewardRatio);
        tvRewordMoney = (TextView) itemView.findViewById(R.id.tvRewordMoney);
        tvShare = (TextView) itemView.findViewById(R.id.tvShare);

        LShare = (LinearLayout) itemView.findViewById(R.id.LShare);
    }

    public void setModelData(final Context mContext, final int position, final GoodsList goodsList, final GoodsListAdapter.GoodsListCallBack listener) {
        if (goodsList != null) {
            tvTitle.setText(goodsList.getGoodsName(mContext));
            tvPrice.setText(goodsList.getGoodspriceDesc());
            tvMarketPrice.setText(goodsList.getMarketpriceDesc(true));

            tvNation.setText(goodsList.goods_promise);
            new FHImageViewUtil(ivNation).setImageURI(goodsList.nation_flag, FHImageViewUtil.SHOWTYPE.BANNER);

            //全球购标志
            if (goodsList.goods_source > 0) {
                ivFlagGlobal.setVisibility(View.VISIBLE);
            } else {
                ivFlagGlobal.setVisibility(View.GONE);
            }

            ivTop.setImageResource(goodsList.getGoodsSaleState());
            ivTop.setVisibility(View.VISIBLE);

            new FHImageViewUtil(imageView).setImageURI(goodsList.goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(goodsList, position);
                    }
                }
            });

            //关于手机专享及秒杀价的显示
            if (goodsList.seckilling_flag == 1) {
                //手机秒杀价
                tvPrice.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_miaosha, 0, 0);
            } else {
                //手机专享价
                if (goodsList.sole_flag == 1) {
                    tvPrice.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_price_only_on_mobile, 0, 0);
                } else {
                    tvPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }

            if (goodsList.isShowReward()) {
                LShare.setVisibility(View.VISIBLE);
                tvRewordMoney.setText(goodsList.getGoodsRewardMoney());
                tvRewardRatio.setText(goodsList.getFanliDesc());
                tvShare.setText(goodsList.getGoods_share_intro());
            } else {
                LShare.setVisibility(View.GONE);
            }

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareData shareData = new ShareData();
                    shareData.title = goodsList.share_title;
                    shareData.content = goodsList.share_describe;
                    shareData.imgs = goodsList.share_img;
                    shareData.url = String.format(BaseVar.SHARE_GOODS_DTL, goodsList.goods_id);
                    shareData.price = goodsList.goods_price;
                    shareData.deductMoney = goodsList.getGoods_reward_money();

                    ShareData.fhShare((BaseFragmentActivity) mContext, shareData, null);
                }
            };

            LShare.setOnClickListener(clickListener);
        }
    }
}
