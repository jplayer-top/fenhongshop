package com.fanglin.fenhong.microbuyer.common.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BannerView;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.UpdateAction;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.ActivityGoods;
import com.fanglin.fenhong.microbuyer.base.model.Adv;
import com.fanglin.fenhong.microbuyer.base.model.ChannelOneData;
import com.fanglin.fenhong.microbuyer.base.model.GoodsScheme;
import com.fanglin.fenhong.microbuyer.base.model.HomeNavData;
import com.fanglin.fenhong.microbuyer.base.model.Theme2Data;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/9/2-下午2:26.
 * 功能描述: 首页频道二
 * 建议以后有复杂类型的列表均可采用这种形式
 * 以前的列表构造形式已经过时：我的订单、首页、国家馆。。。
 */
public class ThemeTwoAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder, RecyclerView.ViewHolder> {

    public static final int TYPE_ADV_BANNER = 0;//      {广告} 轮播图
    public static final int TYPE_ADV_1LEFT_1RIGHT = 1;//{广告} 左一右一
    public static final int TYPE_ADV_1LEFT_2RIGHT = 2;//{广告} 左一右二
    public static final int TYPE_ADV_2LEFT_1RIGHT = 3;//{广告} 左二右一

    public static final int TYPE_CLASS_4COLUMN = 4;//   {分类} 一排四
    public static final int TYPE_CLASS_2COLUMN = 5;//   {分类} 一排二

    public static final int TYPE_ACTIVITY_GOODS = 6;//  {商品} 活动 图+单品
    public static final int TYPE_GOODS_HUGE = 7;//      {商品} 大样式
    public static final int TYPE_GOODS_2COLUMN = 8;//   {商品} 四宫格 一排二
    public static final int TYPE_GOODS_1COLUMN = 9;//   {商品} 普通样式 一排一个

    //附加样式
    public static final int TYPE_ADV_1COLUMN = 10;//    {广告} 一排一个

    LinearLayout.LayoutParams paramsOf1Left1Right;
    LinearLayout.LayoutParams paramsOfADVOne, paramsOfADVTwo;
    LinearLayout.LayoutParams paramsOfADVContainer, paramsOfADVOneHold;

    LinearLayout.LayoutParams paramsOfClassTwo;
    LinearLayout.LayoutParams paramsOfActivityBanner;
    LinearLayout.LayoutParams paramsOfADVSingle;


    private List<Theme2Data> list;


    private Context mContext;

    public ThemeTwoAdapter(Context mContext) {
        this.mContext = mContext;
        int w = BaseFunc.getDisplayMetrics(mContext).widthPixels;
        paramsOf1Left1Right = new LinearLayout.LayoutParams(w / 2, w / 2);

        int x = 169 * w / 375;
        int y = 111 * w / 375;
        int dx = 206 * w / 375;
        int dy = 222 * w / 375;

        paramsOfADVTwo = new LinearLayout.LayoutParams(x, y);
        paramsOfADVOne = new LinearLayout.LayoutParams(dx, dx);
        paramsOfADVContainer = new LinearLayout.LayoutParams(x, 2 * y);
        paramsOfADVOneHold = new LinearLayout.LayoutParams(dx, dy);

        int classH = 115 * w / (2 * 175);
        paramsOfClassTwo = new LinearLayout.LayoutParams(w / 2, classH);

        int activityBannerH = 662 * w / 1080;
        paramsOfActivityBanner = new LinearLayout.LayoutParams(w, activityBannerH);

        int singleAdvH = 576 * w / 1080;
        paramsOfADVSingle = new LinearLayout.LayoutParams(w, singleAdvH);

    }

    public void setList(List<Theme2Data> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<Theme2Data> lst) {
        if (list != null) {
            list.addAll(lst);
            notifyDataSetChanged();
        }
    }

    public Theme2Data getSectionData(int section) {
        return list.get(section);
    }

    @Override
    protected int getSectionItemViewType(int section, int position) {
        Theme2Data data = getSectionData(section);
        return data.getType();
    }

    @Override
    protected int getItemCountForSection(int section) {
        int viewType = getSectionItemViewType(section, 0);
        if (viewType < 4) {
            return 1;
        } else {
            Theme2Data data = getSectionData(section);
            List<Object> alist = data.getList();
            if (alist == null) return 0;
            return alist.size();
        }
    }

    @Override
    protected int getSectionCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        Theme2Data data = getSectionData(section);
        int sectionCount = data.getOriginListSize();
        int viewType = getSectionItemViewType(section, 0);
        if ((viewType == TYPE_CLASS_4COLUMN || viewType == TYPE_CLASS_2COLUMN) && sectionCount > 8) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_header_homenav_style1, null);
        return new NormalHeaderViewHolder(view);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.layout_class_spliter, null);
        return new FooterViewHolder(view);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ADV_BANNER:
                return new BannerViewHolder(View.inflate(mContext, R.layout.item_banner, null));
            case TYPE_ADV_1LEFT_1RIGHT:
                return new ADV1Left1RightViewHolder(View.inflate(mContext, R.layout.item_1left_1right, null));
            case TYPE_ADV_1LEFT_2RIGHT:
                return new ADV1Left2RightViewHolder(View.inflate(mContext, R.layout.item_1left_2right, null));
            case TYPE_ADV_2LEFT_1RIGHT:
                return new ADV2Left1RightViewHolder(View.inflate(mContext, R.layout.item_2left_1right, null));

            case TYPE_CLASS_4COLUMN:
                return new Class4ColumnViewHolder(View.inflate(mContext, R.layout.item_national_pav_classify, null));
            case TYPE_CLASS_2COLUMN:
                return new Class2ColumnViewHolder(View.inflate(mContext, R.layout.item_class_2column, null));

            case TYPE_ACTIVITY_GOODS:
                return new ActivityGoodsViewHolder(View.inflate(mContext, R.layout.item_activity_banner_goods, null));
            case TYPE_GOODS_HUGE:
                return new HugeGoodsViewHolder(View.inflate(mContext, R.layout.item_homenav_lastgoods, null));
            case TYPE_GOODS_2COLUMN:
                return new TwoColumnGoodsViewHolder(View.inflate(mContext, R.layout.item_storenewgoods_normal, null));
            case TYPE_GOODS_1COLUMN:
                return new OneColumnGoodsViewHolder(View.inflate(mContext, R.layout.item_group, null));
            case TYPE_ADV_1COLUMN:
                return new OneColumnAdvViewHolder(View.inflate(mContext, R.layout.item_class_2column, null));
            default:
                return new BannerViewHolder(View.inflate(mContext, R.layout.item_banner, null));
        }
    }


    /**
     * --赋值--
     */
    @Override
    protected void onBindSectionHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {
        NormalHeaderViewHolder normalHeaderViewHolder = (NormalHeaderViewHolder) holder;
        Theme2Data data = getSectionData(section);
        normalHeaderViewHolder.tvTitle.setText(data.getTitle());
        if (TextUtils.isEmpty(data.getTitle())) {
            normalHeaderViewHolder.LContainer.setVisibility(View.GONE);
        } else {
            normalHeaderViewHolder.LContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {
        FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
        final Theme2Data data = getSectionData(section);
        if (data.isExpanded()) {
            footerViewHolder.tvSpliter.setText(mContext.getString(R.string.lbl_disexpand));
            footerViewHolder.tvSpliter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_triangle_gray, 0);
        } else {
            footerViewHolder.tvSpliter.setText(mContext.getString(R.string.all_class));
            footerViewHolder.tvSpliter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_triangle_gray_down, 0);
        }
        footerViewHolder.tvSpliter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setExpanded(!data.isExpanded());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int section, int position) {
        int viewType = getSectionItemViewType(section, position);
        Theme2Data data = getSectionData(section);
        switch (viewType) {
            case TYPE_ADV_BANNER:
                BannerViewHolder bHolder = (BannerViewHolder) holder;
                bHolder.LBanner.removeAllViews();
                if (data.getBanner() != null) {
                    BannerView bannerView = new BannerView(mContext);
                    bannerView.setHeightPxNew(400);
                    bannerView.setShowType(FHImageViewUtil.SHOWTYPE.THEMEONE_BANNER);
                    View headerView = bannerView.getMainBannerView(data.getBanner());
                    bHolder.LBanner.addView(headerView);
                }
                break;
            case TYPE_ADV_1LEFT_1RIGHT:
                ADV1Left1RightViewHolder adv11Holder = (ADV1Left1RightViewHolder) holder;
                final List<Adv> adv11 = data.getAdv();
                if (adv11 != null && adv11.size() > 1) {
                    new FHImageViewUtil(adv11Holder.ivLeft).setImageURI(adv11.get(0).adv_pic, FHImageViewUtil.SHOWTYPE.DEFAULT);
                    new FHImageViewUtil(adv11Holder.ivRight).setImageURI(adv11.get(1).adv_pic, FHImageViewUtil.SHOWTYPE.DEFAULT);

                    onUrlClick(adv11Holder.ivLeft, adv11.get(0).adv_link);
                    onUrlClick(adv11Holder.ivRight, adv11.get(1).adv_link);
                }
                break;
            case TYPE_ADV_1LEFT_2RIGHT:
                ADV1Left2RightViewHolder adv12Holder = (ADV1Left2RightViewHolder) holder;
                final List<Adv> adv12 = data.getAdv();
                if (adv12 != null && adv12.size() > 2) {
                    new FHImageViewUtil(adv12Holder.imageView0).setImageURI(adv12.get(0).adv_pic, FHImageViewUtil.SHOWTYPE.DEFAULT);
                    new FHImageViewUtil(adv12Holder.imageView1).setImageURI(adv12.get(1).adv_pic, FHImageViewUtil.SHOWTYPE.NEWHOME_ADVRIGHT);
                    new FHImageViewUtil(adv12Holder.imageView2).setImageURI(adv12.get(2).adv_pic, FHImageViewUtil.SHOWTYPE.NEWHOME_ADVRIGHT);

                    onUrlClick(adv12Holder.imageView0, adv12.get(0).adv_link);
                    onUrlClick(adv12Holder.imageView1, adv12.get(1).adv_link);
                    onUrlClick(adv12Holder.imageView2, adv12.get(2).adv_link);
                }
                break;
            case TYPE_ADV_2LEFT_1RIGHT:
                ADV2Left1RightViewHolder adv21Holder = (ADV2Left1RightViewHolder) holder;
                final List<Adv> adv21 = data.getAdv();
                if (adv21 != null && adv21.size() > 2) {
                    new FHImageViewUtil(adv21Holder.imageView0).setImageURI(adv21.get(0).adv_pic, FHImageViewUtil.SHOWTYPE.NEWHOME_ADVRIGHT);
                    new FHImageViewUtil(adv21Holder.imageView1).setImageURI(adv21.get(1).adv_pic, FHImageViewUtil.SHOWTYPE.NEWHOME_ADVRIGHT);
                    new FHImageViewUtil(adv21Holder.imageView2).setImageURI(adv21.get(2).adv_pic, FHImageViewUtil.SHOWTYPE.DEFAULT);

                    onUrlClick(adv21Holder.imageView0, adv21.get(0).adv_link);
                    onUrlClick(adv21Holder.imageView1, adv21.get(1).adv_link);
                    onUrlClick(adv21Holder.imageView2, adv21.get(2).adv_link);
                }
                break;
            case TYPE_CLASS_4COLUMN:
                Class4ColumnViewHolder class4Holder = (Class4ColumnViewHolder) holder;
                List<ChannelOneData.ChannelClass> classes4 = data.getChanelClass();
                if (classes4 != null && position < classes4.size()) {
                    class4Holder.tvItemClassify.setText(classes4.get(position).class_name);
                    new FHImageViewUtil(class4Holder.ivItemClassify).setImageURI(classes4.get(position).class_pic, FHImageViewUtil.SHOWTYPE.DEFAULT);

                    onUrlClick(class4Holder.ivItemClassify, classes4.get(position).class_url);
                }
                break;
            case TYPE_CLASS_2COLUMN:
                Class2ColumnViewHolder class2Holder = (Class2ColumnViewHolder) holder;
                List<ChannelOneData.ChannelClass> classes2 = data.getChanelClass();
                if (classes2 != null && position < classes2.size()) {
                    new FHImageViewUtil(class2Holder.imageView).setImageURI(classes2.get(position).class_pic, FHImageViewUtil.SHOWTYPE.THEMEONE_CATEGORY);

                    onUrlClick(class2Holder.imageView, classes2.get(position).class_url);
                }
                break;
            case TYPE_ACTIVITY_GOODS:
                ActivityGoodsViewHolder activityHolder = (ActivityGoodsViewHolder) holder;
                List<HomeNavData.ActivityListWithGoods> activityList = data.getActivityGoods();
                if (activityList != null && activityList.size() > position) {
                    new FHImageViewUtil(activityHolder.imageView).setImageURI(activityList.get(position).activity_banner, FHImageViewUtil.SHOWTYPE.NEWHOME_BANNER);
                    List<ActivityGoods> goodsList = activityList.get(position).goods_list;
                    if (goodsList != null && goodsList.size() > 0) {
                        HomeNavRecycleGoodsAdapter adapter = new HomeNavRecycleGoodsAdapter(mContext);
                        adapter.setList(goodsList);
                        adapter.setActivityList(activityList.get(position));
                        activityHolder.recyclerView.setAdapter(adapter);
                        activityHolder.recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        activityHolder.recyclerView.setVisibility(View.GONE);
                    }
                }
                break;
            case TYPE_GOODS_HUGE:
                HugeGoodsViewHolder hugeHolder = (HugeGoodsViewHolder) holder;
                List<GoodsScheme> hugeGoods = data.getGoods();
                if (hugeGoods != null && hugeGoods.size() > position) {
                    final GoodsScheme goodsScheme = hugeGoods.get(position);

                    new FHImageViewUtil(hugeHolder.ivFlag).setImageURI(goodsScheme.nation_flag, FHImageViewUtil.SHOWTYPE.DEFAULT);
                    new FHImageViewUtil(hugeHolder.ivGoodsImage).setImageURI(goodsScheme.goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);
                    hugeHolder.tvFlag.setText(goodsScheme.goods_promise);
                    hugeHolder.tvPrice.setText(goodsScheme.getGoodspriceDesc());
                    hugeHolder.tvPrice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UpdateAction.add2Cart((Activity) mContext, goodsScheme.goods_id, 1, goodsScheme.goods_source);
                        }
                    });
                    boolean aBool = data.getPlaceBoolean(position);
                    if (aBool) {
                        hugeHolder.tvHeart.setText(mContext.getString(R.string.if_love_full));
                        hugeHolder.tvHeart.setTextColor(mContext.getResources().getColor(R.color.fh_red));
                    } else {
                        hugeHolder.tvHeart.setText(mContext.getString(R.string.if_love_empty));
                        hugeHolder.tvHeart.setTextColor(mContext.getResources().getColor(R.color.color_bb));
                    }
                    hugeHolder.tvGoodsName.setText(goodsScheme.goods_name);
                    hugeHolder.tvGoodsDesc.setText(goodsScheme.goods_desc);
                    hugeHolder.setLocation(section, position);
                    hugeHolder.ivGoodsImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BaseFunc.gotoGoodsDetail(mContext, goodsScheme.goods_id, goodsScheme.resource_tags, null);
                        }
                    });
                }
                break;
            case TYPE_GOODS_2COLUMN:
                TwoColumnGoodsViewHolder twoHolder = (TwoColumnGoodsViewHolder) holder;
                List<GoodsScheme> twoGoods = data.getGoods();
                if (twoGoods != null && twoGoods.size() > position) {
                    final GoodsScheme goodsScheme = twoGoods.get(position);

                    new FHImageViewUtil(twoHolder.imageView).setImageURI(goodsScheme.goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);
                    twoHolder.tvTitle.setText(goodsScheme.goods_name);
                    twoHolder.tvPrice.setText(goodsScheme.getGoodspriceDesc());
                    twoHolder.tvPriceMarket.setText(goodsScheme.getMarketpriceDesc(true));
                    twoHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BaseFunc.gotoGoodsDetail(mContext, goodsScheme.goods_id, null, null);
                        }
                    });

                    new FHImageViewUtil(twoHolder.ivNation).setImageURI(goodsScheme.nation_flag, FHImageViewUtil.SHOWTYPE.BANNER);
                    twoHolder.tvNation.setText(goodsScheme.goods_promise);
                }
                break;
            case TYPE_GOODS_1COLUMN:
                OneColumnGoodsViewHolder oneHolder = (OneColumnGoodsViewHolder) holder;
                List<GoodsScheme> oneGoods = data.getGoods();
                if (oneGoods != null && oneGoods.size() > position) {
                    final GoodsScheme goodsScheme = oneGoods.get(position);

                    new FHImageViewUtil(oneHolder.ivImage).setImageURI(goodsScheme.goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);
                    oneHolder.tvName.setText(goodsScheme.getGoodsNameForDetail(mContext));
                    oneHolder.tvPrice.setText(goodsScheme.getGoodspriceDesc());
                    oneHolder.tvMarketPrice.setText(goodsScheme.getMarketpriceDesc(true));

                    oneHolder.tvNation.setText(goodsScheme.goods_promise);
                    new FHImageViewUtil(oneHolder.ivNation).setImageURI(goodsScheme.nation_flag, FHImageViewUtil.SHOWTYPE.BANNER);

                    oneHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BaseFunc.gotoGoodsDetail(mContext, goodsScheme.goods_id, null, null);
                        }
                    });
                }
                break;
            case TYPE_ADV_1COLUMN:
                OneColumnAdvViewHolder advSingle = (OneColumnAdvViewHolder) holder;
                List<Adv> advSingleList = data.getAdv();
                if (advSingleList != null && position < advSingleList.size()) {
                    new FHImageViewUtil(advSingle.imageView).setImageURI(advSingleList.get(position).adv_pic, FHImageViewUtil.SHOWTYPE.THEMEONE_HOTGOODS_ONLYBANNER);

                    onUrlClick(advSingle.imageView, advSingleList.get(position).adv_link);
                }
                break;
            default:
                break;
        }
    }

    class NormalHeaderViewHolder extends RecyclerView.ViewHolder {
        LinearLayout LContainer;
        TextView tvTitle;

        public NormalHeaderViewHolder(View itemView) {
            super(itemView);
            LContainer = (LinearLayout) itemView.findViewById(R.id.LContainer);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView tvSpliter;

        public FooterViewHolder(View itemView) {
            super(itemView);
            tvSpliter = (TextView) itemView.findViewById(R.id.tvSpliter);
        }
    }

    //轮播  item_banner
    class BannerViewHolder extends RecyclerView.ViewHolder {
        LinearLayout LBanner;

        public BannerViewHolder(View itemView) {
            super(itemView);
            LBanner = (LinearLayout) itemView.findViewById(R.id.LBanner);
        }
    }

    //左一右一  item_1left_1right
    class ADV1Left1RightViewHolder extends RecyclerView.ViewHolder {
        ImageView ivLeft;
        ImageView ivRight;

        public ADV1Left1RightViewHolder(View itemView) {
            super(itemView);

            ivLeft = (ImageView) itemView.findViewById(R.id.ivLeft);
            ivRight = (ImageView) itemView.findViewById(R.id.ivRight);

            ivLeft.setLayoutParams(paramsOf1Left1Right);
            ivRight.setLayoutParams(paramsOf1Left1Right);
        }
    }

    //左一右二  item_1left_2right
    class ADV1Left2RightViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView0, imageView1, imageView2;
        LinearLayout LContainer, LSingle;

        public ADV1Left2RightViewHolder(View itemView) {
            super(itemView);
            imageView0 = (ImageView) itemView.findViewById(R.id.imageView0);
            imageView1 = (ImageView) itemView.findViewById(R.id.imageView1);
            imageView2 = (ImageView) itemView.findViewById(R.id.imageView2);
            LContainer = (LinearLayout) itemView.findViewById(R.id.LContainer);
            LSingle = (LinearLayout) itemView.findViewById(R.id.LSingle);

            imageView0.setLayoutParams(paramsOfADVOne);
            LSingle.setLayoutParams(paramsOfADVOneHold);

            LContainer.setLayoutParams(paramsOfADVContainer);
            imageView1.setLayoutParams(paramsOfADVTwo);
            imageView2.setLayoutParams(paramsOfADVTwo);
        }
    }

    //左二右一  item_2left_1right
    class ADV2Left1RightViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView0, imageView1, imageView2;
        LinearLayout LContainer, LSingle;

        public ADV2Left1RightViewHolder(View itemView) {
            super(itemView);
            imageView0 = (ImageView) itemView.findViewById(R.id.imageView0);
            imageView1 = (ImageView) itemView.findViewById(R.id.imageView1);
            imageView2 = (ImageView) itemView.findViewById(R.id.imageView2);
            LContainer = (LinearLayout) itemView.findViewById(R.id.LContainer);
            LSingle = (LinearLayout) itemView.findViewById(R.id.LSingle);

            LContainer.setLayoutParams(paramsOfADVContainer);
            LSingle.setLayoutParams(paramsOfADVOneHold);

            imageView0.setLayoutParams(paramsOfADVTwo);
            imageView1.setLayoutParams(paramsOfADVTwo);
            imageView2.setLayoutParams(paramsOfADVOne);
        }
    }

    //分类 一排四  item_national_pav_classify
    class Class4ColumnViewHolder extends RecyclerView.ViewHolder {
        ImageView ivItemClassify;
        TextView tvItemClassify;

        public Class4ColumnViewHolder(View itemView) {
            super(itemView);
            ivItemClassify = (ImageView) itemView.findViewById(R.id.iv_item_classify);
            tvItemClassify = (TextView) itemView.findViewById(R.id.tv_item_classify);
        }
    }

    //分类 一排二  item_class_2column
    class Class2ColumnViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public Class2ColumnViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            imageView.setLayoutParams(paramsOfClassTwo);
        }
    }

    //活动 商品 item_activity_banner_goods
    class ActivityGoodsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        RecyclerView recyclerView;

        public ActivityGoodsViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);

            imageView.setLayoutParams(paramsOfActivityBanner);

            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(manager);
        }
    }

    //巨幅 商品 item_homenav_lastgoods
    class HugeGoodsViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFlag;
        TextView tvFlag;
        ImageView ivGoodsImage;
        TextView tvIcon, tvPrice;
        TextView tvHeart;
        TextView tvGoodsName, tvGoodsDesc;

        private int section, position;

        public void setLocation(int section, int position) {
            this.section = section;
            this.position = position;
        }

        public HugeGoodsViewHolder(View itemView) {
            super(itemView);
            ivFlag = (ImageView) itemView.findViewById(R.id.ivFlag);
            tvFlag = (TextView) itemView.findViewById(R.id.tvFlag);
            ivGoodsImage = (ImageView) itemView.findViewById(R.id.ivGoodsImage);

            tvIcon = (TextView) itemView.findViewById(R.id.tvIcon);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvHeart = (TextView) itemView.findViewById(R.id.tvHeart);
            tvGoodsName = (TextView) itemView.findViewById(R.id.tvGoodsName);
            tvGoodsDesc = (TextView) itemView.findViewById(R.id.tvGoodsDesc);

            BaseFunc.setFont(tvIcon);
            BaseFunc.setFont(tvHeart);

            tvHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Theme2Data data = getSectionData(section);
                    if (data.getList() != null && position < data.getList().size()) {
                        YoYo.with(Techniques.ZoomIn).duration(200).playOn(v);
                        boolean aBool = data.getPlaceBoolean(position);
                        data.setPlaceBoolean(position, !aBool);
                        notifyDataSetChanged();

                    }
                }
            });


        }
    }

    //四宫格 一排二 商品  item_storenewgoods_normal
    class TwoColumnGoodsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView ivNation;
        TextView tvNation;
        TextView tvTitle;
        TextView tvPrice, tvPriceMarket;

        public TwoColumnGoodsViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            ivNation = (ImageView) itemView.findViewById(R.id.ivNation);

            tvNation = (TextView) itemView.findViewById(R.id.tvNation);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvPriceMarket = (TextView) itemView.findViewById(R.id.tvPriceMarket);

            tvPriceMarket.setPaintFlags(Paint.ANTI_ALIAS_FLAG | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    //普通样式  商品  item_group
    class OneColumnGoodsViewHolder extends RecyclerView.ViewHolder {
        ImageView ivGlobal;
        ImageView ivImage, ivTop;
        ImageView ivNation;
        TextView tvNation;
        TextView tvName;
        TextView tvPrice, tvMarketPrice;

        public OneColumnGoodsViewHolder(View itemView) {
            super(itemView);
            ivGlobal = (ImageView) itemView.findViewById(R.id.ivGlobal);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            ivTop = (ImageView) itemView.findViewById(R.id.ivTop);
            ivNation = (ImageView) itemView.findViewById(R.id.ivNation);

            tvNation = (TextView) itemView.findViewById(R.id.tvNation);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvMarketPrice = (TextView) itemView.findViewById(R.id.tvMarketPrice);

            ivGlobal.setVisibility(View.INVISIBLE);
            ivTop.setVisibility(View.INVISIBLE);
            tvMarketPrice.setPaintFlags(Paint.ANTI_ALIAS_FLAG | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    //广告图 一行一张  item_class_2column
    class OneColumnAdvViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public OneColumnAdvViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setLayoutParams(paramsOfADVSingle);

            imageView.setPadding(0, 0, 0, 0);
        }
    }

    public int getSpanSize(int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {

            case TYPE_CLASS_4COLUMN:
                return 1;

            case TYPE_CLASS_2COLUMN:
            case TYPE_GOODS_2COLUMN:
                return 2;

            case TYPE_ADV_BANNER:
            case TYPE_ADV_1LEFT_1RIGHT:
            case TYPE_ADV_1LEFT_2RIGHT:
            case TYPE_ADV_2LEFT_1RIGHT:
            case TYPE_ACTIVITY_GOODS:
            case TYPE_GOODS_HUGE:
            case TYPE_GOODS_1COLUMN:
            case TYPE_ADV_1COLUMN:
            default:
                return 4;
        }
    }


    /**
     * 点击广告位图片
     *
     * @param v   View
     * @param url String
     */
    private void onUrlClick(View v, final String url) {
        if (v != null && !TextUtils.isEmpty(url)) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.urlClick(mContext, url);
                }
            });
        }
    }
}
