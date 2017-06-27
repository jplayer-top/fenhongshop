package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.baseui.viewholder.HalfScreenGoodsViewHolder;
import com.fanglin.fenhong.microbuyer.base.model.FancyShopInfo;
import com.fanglin.fenhong.microbuyer.base.model.GoodsList;
import com.fanglin.fhui.CircleImageView;

import java.util.List;

/**
 * Created by Plucky on 15-10-11.
 * 微店商品适配器
 */
public class FancyShopDtlGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_CONTENT = 1;

    private Context mContext;
    private List<GoodsList> list;
    private FancyShopInfo info;

    public FancyShopDtlGoodsAdapter(Context c) {
        this.mContext = c;
        setHasStableIds(false);
    }

    public void setList(List<GoodsList> list) {
        this.list = list;
    }

    public void setInfo(FancyShopInfo info) {
        this.info = info;
    }

    public void addList(List<GoodsList> list) {
        if (list != null && list.size() > 0) {
            this.list.addAll(list);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        return TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        if (list == null) return 1;
        return list.size() + 1;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CONTENT) {
            View view = View.inflate(mContext, R.layout.item_halfscreen_goods, null);
            return new HalfScreenGoodsViewHolder(view);
        } else {
            View view = View.inflate(mContext, R.layout.layout_fancy_header, null);
            return new HeaderViewHolder(view);
        }


    }

    public GoodsList getItem(int position) {
        return list.get(position - 1);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder pholder, final int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_CONTENT) {
            final GoodsList goodsList = getItem(position);
            HalfScreenGoodsViewHolder goodsHolder = (HalfScreenGoodsViewHolder) pholder;
            goodsHolder.setModelData(mContext, goodsList);
        } else {
            FancyShopDtlGoodsAdapter.HeaderViewHolder holder = (FancyShopDtlGoodsAdapter.HeaderViewHolder) pholder;
            if (info != null) {
                holder.tvStoreName.setText(info.shop_name);
                new FHImageViewUtil(holder.hearIcon).setImageURI(info.shop_logo, FHImageViewUtil.SHOWTYPE.AVATAR);
            }
        }
    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        ImageView fiv_pic;
        FrameLayout FBanner;
        CircleImageView hearIcon;
        TextView tvStoreName;
        TextView tv_subtitle;
        TextView tv_vip;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            fiv_pic = (ImageView) itemView.findViewById(R.id.fiv_pic);
            FBanner = (FrameLayout) itemView.findViewById(R.id.FBanner);
            hearIcon = (CircleImageView) itemView.findViewById(R.id.sdv);
            tvStoreName = (TextView) itemView.findViewById(R.id.tv_title);
            tv_subtitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
            tv_vip = (TextView) itemView.findViewById(R.id.tv_vip);
            BaseFunc.setFont(tv_vip);
            tv_subtitle.setVisibility(View.INVISIBLE);
        }
    }

    public int[] getOffsets(int pos) {
        //left top right bottom
        int[] res = new int[]{0, 0, 0, 0};
        int viewType = getItemViewType(pos);
        int goodsPos = pos - 1;
        if (viewType == TYPE_CONTENT) {
            int span = 2;
            // left：第一列 6px
            if (goodsPos % span == 0) {
                res[0] = 6;
            }
            // top：第一行 6px
            if (goodsPos < span) {
                res[1] = 6;
            }
            // right: 最后一列 6px
            if (goodsPos % span == span - 1) {
                res[2] = 6;
            }
            // bottom: 最后一行 6px
            int count = list != null ? list.size() : 0;
            int line = (count % span == 0) ? count / span : count / span + 1;
            int lastIndex = (line - 1) * span - 1;
            if (goodsPos > lastIndex) {
                res[3] = 6;
            }
        }

        return res;
    }
}
