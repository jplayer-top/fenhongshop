package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.baseui.viewholder.HalfScreenGoodsViewHolder;
import com.fanglin.fenhong.microbuyer.base.model.GoodsList;
import com.fanglin.fenhong.microbuyer.base.model.StoreFlow;
import com.fanglin.fenhong.microbuyer.base.model.StoreHomeInfo;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/7/20-下午3:24.
 * 功能描述:店铺按照分区方式重构
 */
public class StoreSectionAdater extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder, RecyclerView.ViewHolder> {

    public static final int TYPE_ITEM_BANNER = 2;
    public static final int TYPE_ITEM_GOODS = 3;
    public static final int TYPE_HEADER_TOP = 0;
    public static final int TYPE_HEADER_SECTION = 1;

    private Context mContext;
    LinearLayout.LayoutParams bannerParams, bgParams;

    private StoreHomeInfo info;
    private boolean hasCollected = false;
    private List<StoreFlow> list;


    public StoreSectionAdater(Context mContext) {
        this.mContext = mContext;
        int h = mContext.getResources().getDisplayMetrics().widthPixels * 230 / 375;
        bannerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
        int bgH = mContext.getResources().getDisplayMetrics().widthPixels * 400 / 790;
        bgParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, bgH);
    }

    @Override
    protected int getSectionCount() {
        if (list == null)
            return 1;
        return list.size() + 1;
    }

    public StoreFlow getSectionEntity(int section) {
        return list.get(section - 1);
    }

    public GoodsList getItem(int section, int position) {
        return getSectionEntity(section).getGoods_list().get(position);
    }


    /**
     * 队尾添加商品
     *
     * @param goodsList goods
     */
    public void addGoods(List<GoodsList> goodsList) {
        int lastSection = getSectionCount() - 1;
        StoreFlow flow = getSectionEntity(lastSection);
        if (flow != null && flow.getGoods_list() != null) {
            if (goodsList != null && goodsList.size() > 0) {
                flow.getGoods_list().addAll(goodsList);
                notifyDataSetChanged();
            }
        }
    }

    @Override
    protected int getItemCountForSection(int section) {
        if (section == 0) {
            return 0;
        }

        StoreFlow flow = getSectionEntity(section);
        if (flow.isBanner()) {
            return 1;
        } else {
            List<GoodsList> goodsList = flow.getGoods_list();
            if (goodsList == null) return 0;
            return goodsList.size();
        }
    }

    public void setInfo(StoreHomeInfo info) {
        this.info = info;
        hasCollected = info != null && info.is_collected == 1;
        notifyDataSetChanged();
    }

    public void setHasCollected(boolean hasCollected) {
        this.hasCollected = hasCollected;
        notifyDataSetChanged();
    }

    public void setList(List<StoreFlow> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER_TOP) {
            View view = View.inflate(mContext, R.layout.item_store_header, null);
            return new TopViewHolder(view);
        } else {
            View view = View.inflate(mContext, R.layout.item_store_section_title, null);
            return new TitleViewHolder(view);
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM_BANNER) {
            View view = View.inflate(mContext, R.layout.item_store_section_banner, null);
            return new BannerViewHolder(view);
        } else {
            View view = View.inflate(mContext, R.layout.item_halfscreen_goods, null);
            return new HalfScreenGoodsViewHolder(view);
        }
    }

    @Override
    protected void onBindSectionHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {
        int viewType = getSectionHeaderViewType(section);
        if (viewType == TYPE_HEADER_TOP) {
            TopViewHolder topHolder = (TopViewHolder) holder;

            if (info != null) {
                topHolder.LContainer.setVisibility(View.VISIBLE);
                topHolder.tvName.setText(info.store_name);
                new FHImageViewUtil(topHolder.ivStoreLogo).setImageURI(info.store_label, FHImageViewUtil.SHOWTYPE.DEFAULT);
                topHolder.ivCollect.setSelected(hasCollected);

                new FHImageViewUtil(topHolder.ivStoreBg).setImageURI(info.store_banner, FHImageViewUtil.SHOWTYPE.STORE_BANNER);

                if (TextUtils.isEmpty(info.store_notice)) {
                    topHolder.LSpliter.setVisibility(View.GONE);
                    topHolder.LRoll.setVisibility(View.GONE);
                } else {
                    topHolder.LSpliter.setVisibility(View.VISIBLE);
                    topHolder.LRoll.setVisibility(View.VISIBLE);

                    topHolder.tvNotice.setText(BaseFunc.formatHtml(info.store_notice));
                    topHolder.tvNotice.setMaxLines(maxLines);

                    if (topHolder.tvNotice.getLineCount() < 3) {
                        linesCount = 10;
                        topHolder.LSpliter.setVisibility(View.GONE);
                    } else {
                        linesCount = topHolder.tvNotice.getLineCount();
                        topHolder.LSpliter.setVisibility(View.VISIBLE);
                        topHolder.tvSpliter.setText(maxLines == 2 ? R.string.lbl_expand : R.string.lbl_disexpand);
                        topHolder.tvSpliter.setCompoundDrawablesWithIntrinsicBounds(0, 0, maxLines == 2 ? R.drawable.icon_triangle_gray_down : R.drawable.icon_triangle_gray, 0);
                    }

                }
            } else {
                topHolder.LContainer.setVisibility(View.GONE);
            }
        } else {
            // 普通头部
            StoreFlow flow = getSectionEntity(section);
            if (flow != null) {
                TitleViewHolder titleHolder = (TitleViewHolder) holder;
                if (flow.isBanner()) {
                    titleHolder.tvName.setVisibility(View.GONE);
                } else {
                    titleHolder.tvName.setVisibility(View.VISIBLE);
                    titleHolder.tvName.setText(flow.getContent());
                }
            }
        }
    }

    int linesCount = 10;

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int section, int position) {
        int viewType = getSectionItemViewType(section, position);
        final StoreFlow flow = getSectionEntity(section);
        if (viewType == TYPE_ITEM_BANNER) {
            BannerViewHolder bannerHolder = (BannerViewHolder) holder;
            new FHImageViewUtil(bannerHolder.ivBanner).setImageURI(flow.getContent(), FHImageViewUtil.SHOWTYPE.NEWHOME_BANNER);
            bannerHolder.ivBanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (BaseFunc.isValidUrl(flow.getGoods_url())) {
                        BaseFunc.urlClick(mContext, flow.getGoods_url());
                    }
                }
            });
        } else {
            final GoodsList goodsList = getItem(section, position);
            HalfScreenGoodsViewHolder goodsHolder = (HalfScreenGoodsViewHolder) holder;
            goodsHolder.setModelData(mContext, goodsList);
        }
    }

    @Override
    protected int getSectionHeaderViewType(int section) {
        if (section == 0) {
            return TYPE_HEADER_TOP;
        } else {
            return TYPE_HEADER_SECTION;
        }
    }

    @Override
    protected int getSectionItemViewType(int section, int position) {
        StoreFlow storeFlow = getSectionEntity(section);
        if (storeFlow.isBanner()) {
            return TYPE_ITEM_BANNER;
        } else {
            return TYPE_ITEM_GOODS;
        }
    }

    @Override
    protected boolean isSectionHeaderViewType(int viewType) {
        return viewType == TYPE_HEADER_TOP || viewType == TYPE_HEADER_SECTION;
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBanner;

        public BannerViewHolder(View itemView) {
            super(itemView);
            ivBanner = (ImageView) itemView.findViewById(R.id.ivBanner);
            ivBanner.setLayoutParams(bannerParams);
        }
    }

    private int maxLines = 2;

    class TopViewHolder extends RecyclerView.ViewHolder {
        LinearLayout LContainer;
        ImageView ivStoreLogo, ivCollect;
        TextView tvName;

        FrameLayout FStoreBg;
        ImageView ivStoreBg;

        LinearLayout LRoll;
        TextView tvNotice;
        LinearLayout LSpliter;
        TextView tvSpliter;

        public TopViewHolder(View itemView) {
            super(itemView);
            LContainer = (LinearLayout) itemView.findViewById(R.id.LContainer);
            ivStoreLogo = (ImageView) itemView.findViewById(R.id.ivStoreLogo);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            ivCollect = (ImageView) itemView.findViewById(R.id.ivCollect);

            FStoreBg = (FrameLayout) itemView.findViewById(R.id.FStoreBg);
            ivStoreBg = (ImageView) itemView.findViewById(R.id.ivStoreBg);

            tvNotice = (TextView) itemView.findViewById(R.id.tvNotice);
            LRoll = (LinearLayout) itemView.findViewById(R.id.LRoll);
            LSpliter = (LinearLayout) itemView.findViewById(R.id.LSpliter);
            tvSpliter = (TextView) itemView.findViewById(R.id.tvSpliter);

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.LSpliter:
                            maxLines = maxLines == 2 ? linesCount : 2;
                            notifyDataSetChanged();
                            break;
                        case R.id.ivCollect:
                            if (sectionListener != null) {
                                sectionListener.onCollect(hasCollected);
                            }
                            break;
                    }
                }
            };
            LSpliter.setOnClickListener(listener);
            ivCollect.setOnClickListener(listener);

            FStoreBg.setLayoutParams(bgParams);
        }
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public TitleViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
        }
    }

    private StoreSectionListener sectionListener;

    public void setSectionListener(StoreSectionListener sectionListener) {
        this.sectionListener = sectionListener;
    }

    public interface StoreSectionListener {
        void onCollect(boolean hasCollect);
    }

    public int getSpanSize(int position) {
        int viewType = getItemViewType(position);
        if (isSectionHeaderViewType(viewType) || viewType == TYPE_ITEM_BANNER)
            return 2;
        return 1;
    }

public int[] getOffsets(int position) {
    int[] res = new int[]{0, 0, 0, 0};
    int viewType = getItemViewType(position);
    if (viewType == TYPE_ITEM_GOODS) {
        //left top right bottom
        int span = 2;
        // left：第一列 6px
        if (position % span == 0) {
            res[0] = 6;
        }
        // top：第一行 6px
        if (position < span) {
            res[1] = 6;
        }
        // right: 最后一列 6px
        if (position % span == span - 1) {
            res[2] = 6;
        }
        // bottom: 最后一行 6px
    }

    if (viewType == TYPE_HEADER_SECTION) {
        res[3] = 6;
    }

    return res;
}
}



