package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.baseui.viewholder.HalfScreenGoodsViewHolder;
import com.fanglin.fenhong.microbuyer.base.model.GoodsList;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.base.model.StoreNewGoods;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/7/13-下午3:56.
 * 功能描述: 店铺新品适配器
 */
public class StoreNewGoodsAdapter extends SectionedRecyclerViewAdapter<StoreNewGoodsAdapter.HeaderViewHolder, RecyclerView.ViewHolder, RecyclerView.ViewHolder> {

    private Context mContext;
    private List<StoreNewGoods> list;

    public final static int TYPE_ITEM_NORMAL = 0;
    public final static int TYPE_ITEM_BIG = 1;


    public StoreNewGoodsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List<StoreNewGoods> list) {
        this.list = list;
    }

    @Override
    protected int getSectionCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        List<GoodsList> goods_list = list.get(section).goods_list;
        if (goods_list == null)
            return 0;
        return goods_list.size();
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_storenewgoods_header, null);
        return new HeaderViewHolder(view);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_ITEM_BIG) {
            view = View.inflate(mContext, R.layout.item_storenewgoods_big, null);
            return new BigItemViewHolder(view);
        } else {
            view = View.inflate(mContext, R.layout.item_halfscreen_goods, null);
            return new HalfScreenGoodsViewHolder(view);
        }

    }

    public StoreNewGoods getSectionEntity(int section) {
        return list.get(section);
    }

    public GoodsList getItem(int section, int position) {
        return getSectionEntity(section).goods_list.get(position);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(HeaderViewHolder holder, int section) {
        StoreNewGoods newGoods = getSectionEntity(section);
        holder.tvTime.setText(newGoods.time);
    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int section, int position) {
        //解决分区
        holder.itemView.setTag(position);

        final GoodsList goodsList = getItem(section, position);
        View.OnClickListener listener = new View.OnClickListener() {
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
        if (holder instanceof HalfScreenGoodsViewHolder) {
            HalfScreenGoodsViewHolder goodsHolder = (HalfScreenGoodsViewHolder) holder;
            goodsHolder.setModelData(mContext, goodsList);
        } else {
            BigItemViewHolder bigHolder = (BigItemViewHolder) holder;
            bigHolder.tvTitle.setText(goodsList.getGoodsNameForDetail(mContext));
            new FHImageViewUtil(bigHolder.imageView).setImageURI(goodsList.goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);
            bigHolder.tvNation.setText(goodsList.nation_name);
            new FHImageViewUtil(bigHolder.ivNation).setImageURI(goodsList.nation_flag, FHImageViewUtil.SHOWTYPE.DEFAULT);
            bigHolder.tvPrice.setText(goodsList.getGoodspriceDesc());
            bigHolder.tvPriceMarket.setText(goodsList.getMarketpriceDesc(true));
            bigHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.gotoGoodsDetail(mContext, goodsList.goods_id, null, null);
                }
            });

            bigHolder.tvRewardRatio.setText(goodsList.getFanliDesc());
            if (goodsList.isShowReward()) {
                bigHolder.LShare.setVisibility(View.VISIBLE);
                bigHolder.tvRewardRatio.setVisibility(View.VISIBLE);
                bigHolder.tvShareWin.setText(goodsList.getGoodsRewardMoney());
                bigHolder.LShare.setOnClickListener(listener);
            } else {
                bigHolder.LShare.setVisibility(View.GONE);
                bigHolder.tvRewardRatio.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected int getSectionItemViewType(int section, int position) {
        int sCount = getItemCountForSection(section);
        if (sCount == 1) {
            return TYPE_ITEM_BIG;
        } else {
            return TYPE_ITEM_NORMAL;
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        }
    }

    public int getSpanSize(int position) {
        int viewType = getItemViewType(position);

        if (viewType == TYPE_ITEM_BIG || isSectionHeaderPosition(position)) {
            return 2;
        }
        return 1;
    }

    class BigItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView ivNation;
        TextView tvNation;
        TextView tvTitle;
        TextView tvPrice, tvPriceMarket;

        //奖金相关
        LinearLayout LShare;
        TextView tvShareIcon, tvShareWin, tvRewardRatio;

        public BigItemViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            ivNation = (ImageView) itemView.findViewById(R.id.ivNation);
            tvNation = (TextView) itemView.findViewById(R.id.tvNation);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvPriceMarket = (TextView) itemView.findViewById(R.id.tvPriceMarket);

            tvPriceMarket.setPaintFlags(Paint.ANTI_ALIAS_FLAG | Paint.STRIKE_THRU_TEXT_FLAG);

            tvShareWin = (TextView) itemView.findViewById(R.id.tvShareWin);
            tvShareIcon = (TextView) itemView.findViewById(R.id.tvShareIcon);
            tvRewardRatio = (TextView) itemView.findViewById(R.id.tvRewardRatio);
            LShare = (LinearLayout) itemView.findViewById(R.id.LShare);
            BaseFunc.setFont(tvShareIcon);
        }
    }

    public int[] getOffsets(int position, int index) {
        int[] res = new int[]{0, 0, 0, 0};
        int viewType = getItemViewType(position);
        if (viewType == TYPE_ITEM_NORMAL) {
            //left top right bottom
            int span = 2;
            // left：第一列 6px
            if (index % span == 0) {
                res[0] = 6;
            }
            // top：第一行 6px
            if (index < span) {
                res[1] = 6;
            }
            // right: 最后一列 6px
            if (index % span == span - 1) {
                res[2] = 6;
            }
        }
        return res;
    }
}
