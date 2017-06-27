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
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.ActivityFloor;
import com.fanglin.fenhong.microbuyer.base.model.ActivityGoods;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-10-11.
 * 楼层
 */
public class ActDtlGoodsAdapter extends SectionedRecyclerViewAdapter<ActDtlGoodsAdapter.HeaderViewHolder, ActDtlGoodsAdapter.ItemViewHolder, ActDtlGoodsAdapter.FooterViewHolder> {

    private Context mContext;
    private List<ActivityFloor> list;

    public ActDtlGoodsAdapter(Context c) {
        this.mContext = c;
    }

    public void setList(List<ActivityFloor> list) {
        this.list = list;
    }


    public ActivityGoods getItem(int section, int position) {
        return list.get(section).floor_goods.get(position);
    }


    @Override
    protected int getSectionCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        List<ActivityGoods> goods = list.get(section).floor_goods;
        if (goods == null) return 0;
        return goods.size();
    }

    @Override
    protected ItemViewHolder onCreateItemViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext, R.layout.item_actdtl_goods, null);
        return new ItemViewHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(ItemViewHolder holder, int section, final int position) {
        final ActivityGoods agoods = getItem(section, position);

        //商品标题 + 标签
        holder.tv_title.setText(agoods.getGoodsNameForDetail(mContext));
        String nation_desc = agoods.goods_promise;
        holder.tv_nation_desc.setText(nation_desc);

        //已下架、已售罄等透明图片
        holder.ivTop.setImageResource(agoods.getGoodsSaleState());

        new FHImageViewUtil(holder.ivFlagNation).setImageURI(agoods.nation_flag, FHImageViewUtil.SHOWTYPE.DEFAULT);

        DecimalFormat df = new DecimalFormat("¥#0.00");
        holder.tv_price.setText(df.format(agoods.goods_price));
        holder.tv_price_market.setText(df.format(agoods.goods_marketprice));

        new FHImageViewUtil(holder.sdv).setImageURI(agoods.goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);

        //全球购标志
        if (agoods.goods_source > 0) {
            holder.ivFlagGlobal.setVisibility(View.VISIBLE);
        } else {
            holder.ivFlagGlobal.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFunc.gotoGoodsDetail(mContext, agoods.goods_id, null, null);
            }
        });
    }


    private View headerViewAboveSectionone;

    public void setHeaderViewAboveSectionone(View view) {
        this.headerViewAboveSectionone = view;
    }

    @Override
    protected void onBindSectionHeaderViewHolder(HeaderViewHolder viewHolder, int i) {
        if (headerViewAboveSectionone != null) {
            if (i == 0) {
                viewHolder.LHeaderTop.removeAllViews();
                /**
                 * 防止复用导致的错误
                 * The specified child already has a parent. You must call removeView() on the child's parent first.
                 */
                ViewGroup viewGroup = (ViewGroup) headerViewAboveSectionone.getParent();
                if (viewGroup != null) viewGroup.removeAllViewsInLayout();

                viewHolder.LHeaderTop.addView(headerViewAboveSectionone);
            } else {
                viewHolder.LHeaderTop.removeAllViews();
            }
        }

        viewHolder.tv_header.setText(list.get(i).floor_name);
    }

    @Override
    protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext, R.layout.item_section_label, null);
        return new HeaderViewHolder(view);
    }

    @Override
    protected boolean hasFooterInSection(int i) {
        return false;
    }

    @Override
    protected FooterViewHolder onCreateSectionFooterViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext, R.layout.item_section_label, null);
        return new FooterViewHolder(view);
    }

    @Override
    protected void onBindSectionFooterViewHolder(FooterViewHolder footerViewHolder, int i) {
        footerViewHolder.tv_header.setText(list.get(i).floor_name);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        /** 商品显示Item*/
        public TextView tv_title;
        public ImageView sdv, ivFlagGlobal, ivFlagNation, ivTop;
        public TextView tv_price, tv_price_market, tv_nation_desc;


        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_nation_desc = (TextView) itemView.findViewById(R.id.tv_nation_desc);
            sdv = (ImageView) itemView.findViewById(R.id.sdv);
            ivFlagGlobal = (ImageView) itemView.findViewById(R.id.iv_flag_global);
            ivFlagNation = (ImageView) itemView.findViewById(R.id.iv_flag_nation);
            ivTop = (ImageView) itemView.findViewById(R.id.iv_top);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_price_market = (TextView) itemView.findViewById(R.id.tv_price_market);

            tv_price_market.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_header;
        public LinearLayout LHeaderTop;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            tv_header = (TextView) itemView.findViewById(R.id.tv_header);
            LHeaderTop = (LinearLayout) itemView.findViewById(R.id.LHeaderTop);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_header;

        public FooterViewHolder(View itemView) {
            super(itemView);
            tv_header = (TextView) itemView.findViewById(R.id.tv_header);
        }
    }
}
