package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;

/**
 * 作者： Created by Plucky on 2015/9/16.
 * modify by lizhixin on 2015/12/22
 */
public class GoodsFavAdapter extends FavoritesAdapter {

    public boolean isHistory = false;

    public GoodsFavAdapter(Context c, boolean isHistory) {
        super(c);
        this.isHistory = isHistory;
    }

    @Override
    public GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_fav_goods, null);
        return new GoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavViewHolder holder, final int position) {
        GoodsViewHolder gholder = (GoodsViewHolder) holder;
        new FHImageViewUtil(gholder.sdv).setImageURI(getItem(position).goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);
        gholder.tv_price_fhmall.setText(getItem(position).getGoodspriceDesc());
        gholder.tvPriceMarket.setText(getItem(position).getMarketpriceDesc(true));

        //商品名称 + 标签
        gholder.tv_title.setText(getItem(position).getGoodsName(mContext));
        BaseFunc.setFont(gholder.tv_title);

        //国旗及描述行
        gholder.tv_subtitle.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(getItem(position).nation_name) && !TextUtils.isEmpty(getItem(position).nation_flag)) {
            gholder.ll_nation.setVisibility(View.VISIBLE);
        } else {
            gholder.ll_nation.setVisibility(View.GONE);
        }

        gholder.tv_nation_desc.setText(getItem(position).goods_promise);
        new FHImageViewUtil(gholder.iv_nation_flag).setImageURI(getItem(position).nation_flag, FHImageViewUtil.SHOWTYPE.DEFAULT);

        //全球购标志
        if (getItem(position).goods_source > 0) {
            gholder.iv_flag_global.setVisibility(View.VISIBLE);
        } else {
            gholder.iv_flag_global.setVisibility(View.GONE);
        }

        //已下架，已售罄，一抢光等透明图片
        gholder.iv_top.setImageResource(getItem(position).getGoodsSaleState());

        if (isHistory) {
            gholder.tv_memo.setVisibility(View.INVISIBLE);
        } else {
            gholder.tv_memo.setVisibility(View.INVISIBLE);
        }

        gholder.cb.setClickable(false);
        gholder.cb.setVisibility(isShowChk ? View.VISIBLE : View.GONE);
        gholder.cb.setChecked(getItem(position).isSelected);

        gholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowChk) {
                    getItem(position).isSelected = !getItem(position).isSelected;
                    notifyDataSetChanged();
                    return;
                }
                if (mcb != null) {
                    if (isHistory) {
                        mcb.onItemClick(3, position);
                    } else {
                        mcb.onItemClick(0, position);
                    }
                }
            }
        });
    }

    class GoodsViewHolder extends FavViewHolder {
        public CheckBox cb;
        public ImageView sdv;
        public TextView tv_title, tv_subtitle;
        public TextView tv_price_fhmall, tvPriceMarket;
        public TextView tv_memo;
        public LinearLayout ll_nation;//国旗 行
        public ImageView iv_nation_flag;//国旗图标
        public TextView tv_nation_desc;//国旗旁边的描述
        public ImageView iv_flag_global;//商品图片左上角的全球购标志
        public ImageView iv_top;//已售罄、已下架等透明图片

        public GoodsViewHolder(View itemView) {
            super(itemView);
            cb = (CheckBox) itemView.findViewById(R.id.cb);
            sdv = (ImageView) itemView.findViewById(R.id.sdv);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_subtitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
            tv_price_fhmall = (TextView) itemView.findViewById(R.id.tv_price_fhmall);
            tvPriceMarket = (TextView) itemView.findViewById(R.id.tvPriceMarket);
            tv_memo = (TextView) itemView.findViewById(R.id.tv_memo);
            ll_nation = (LinearLayout) itemView.findViewById(R.id.ll_nation);
            iv_nation_flag = (ImageView) itemView.findViewById(R.id.iv_nation_flag);
            tv_nation_desc = (TextView) itemView.findViewById(R.id.tv_nation_desc);
            iv_flag_global = (ImageView) itemView.findViewById(R.id.iv_flag_global);
            iv_top = (ImageView) itemView.findViewById(R.id.iv_top);

            tvPriceMarket.setVisibility(View.VISIBLE);
            tvPriceMarket.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }
}
