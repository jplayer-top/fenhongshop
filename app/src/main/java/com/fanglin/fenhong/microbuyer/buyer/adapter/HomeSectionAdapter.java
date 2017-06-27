package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baselib.UpdateAction;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.ActivityGoods;
import com.fanglin.fenhong.microbuyer.base.model.ActivityList;
import com.fanglin.fenhong.microbuyer.base.model.Adv;
import com.fanglin.fenhong.microbuyer.base.model.GoodsScheme;
import com.fanglin.fenhong.microbuyer.base.model.HomeNavData;
import com.fanglin.fenhong.microbuyer.base.model.HomeNavigation;
import com.fanglin.fenhong.microbuyer.base.model.HotBrands;
import com.fanglin.fenhong.microbuyer.base.model.SeqGoodsModel;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.common.ActListDtlActivity;
import com.fanglin.fenhong.microbuyer.common.FHBrowserActivity;
import com.fanglin.fenhong.microbuyer.common.GroupActivity;
import com.fanglin.fenhong.microbuyer.common.HotBrandsActivity;
import com.fanglin.fenhong.microbuyer.common.NationalPavilionActivity;
import com.fanglin.fenhong.microbuyer.common.adapter.HomeNavRecycleGoodsAdapter;
import com.fanglin.fhui.PinnedHeaderListView.SectionedBaseAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页第三次重构 首页adapter
 * Created by Plucky on 2016/3/24 15:16.
 */
public class HomeSectionAdapter extends SectionedBaseAdapter {

    private Context mContext;
    public static final int TYPE_HEADER_0 = 0;
    public static final int TYPE_HEADER_1 = 1;
    /**
     * 注意TYPE不能随便写
     */
    public static final int TYPE_CONTENT_BTNS = 0;
    public static final int TYPE_CONTENT_HOTACT = 1;
    public static final int TYPE_CONTENT_COUNTRY = 2;
    public static final int TYPE_CONTENT_BANNER_GOODS = 3;
    public static final int TYPE_CONTENT_BRAND = 4;
    public static final int TYPE_CONTENT_LASTGOODS = 5;
    public static final int TYPE_CONTENT_DIVIDER = 6;//10dp分隔线
    public static final int TYPE_CONTENT_BRAND_MORE = 7;//品牌馆加载更多

    private HomeNavData mData;
    private DecimalFormat priceFormat;
    private Typeface iconfont;

    LinearLayout.LayoutParams paramsActivityPic, paramsCountryPic, paramsBrandPic;

    private CountDownTimer countDownTimer;//秒杀倒计时 -- lizhixin

    /**
     * @param context 构造传入上下文
     */
    public HomeSectionAdapter(Context context) {
        this.mContext = context;
        priceFormat = new DecimalFormat("¥#0.00");
        iconfont = BaseFunc.geticonFontType(mContext);

        int screenWidth = BaseFunc.getDisplayMetrics(context).widthPixels;

        int activityBannerHeight = 460 * screenWidth / 750;//主题馆+商品组合 图片的高度
        paramsActivityPic = new LinearLayout.LayoutParams(screenWidth, activityBannerHeight);

        int countryWidth = (screenWidth - 10) / 2;
        int countryHeight = 180 * countryWidth / 350;//国家馆图片高度
        paramsCountryPic = new LinearLayout.LayoutParams(countryWidth, countryHeight);

        int brandHeight = 230 * countryWidth / 350;//品牌馆图片高度
        paramsBrandPic = new LinearLayout.LayoutParams(countryWidth, brandHeight);

    }

    public void setData(HomeNavData mData) {
        this.mData = mData;
        if (countDownTimer != null) {
            //每次刷新数据时要清空timer
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    public Object getItem(int section, int position) {
        return null;
    }

    @Override
    public long getItemId(int section, int position) {
        return 0;
    }

    /**
     * 区分子布局样式
     *
     * @param section  分区
     * @param position 索引
     * @return 0，1，2，3，4,5,6,7 分别从上到下
     */
    @Override
    public int getItemViewType(int section, int position) {
        switch (section) {
            case 0:
                boolean isLast = getCountForSection(section) - 1 == position;
                if (isLast) {
                    return TYPE_CONTENT_DIVIDER;
                } else {
                    return TYPE_CONTENT_BTNS;
                }
            case 1:
                if (position == 0) {
                    return TYPE_CONTENT_HOTACT;
                } else {
                    return TYPE_CONTENT_DIVIDER;
                }
            case 2:
                boolean isFinal = getCountForSection(section) - 1 == position;
                if (isFinal) {
                    return TYPE_CONTENT_DIVIDER;
                } else {
                    return TYPE_CONTENT_COUNTRY;
                }
            case 3:
                boolean last = getCountForSection(section) - 1 == position;
                if (last) {
                    return TYPE_CONTENT_DIVIDER;
                } else {
                    return TYPE_CONTENT_BANNER_GOODS;
                }
            case 4:
                boolean fin = getCountForSection(section) - 1 == position;
                if (fin) {
                    return TYPE_CONTENT_BRAND_MORE;
                } else {
                    return TYPE_CONTENT_BRAND;
                }
            case 5:
                return TYPE_CONTENT_LASTGOODS;
            default:
                return TYPE_CONTENT_BTNS;
        }
    }

    @Override
    public int getItemViewTypeCount() {
        return 8;
    }

    /**
     * 根据section区分头部视图类型
     *
     * @param section 分区
     * @return 0 代表 style0  1代码style1
     */
    @Override
    public int getSectionHeaderViewType(int section) {
        if (section == 3 || section == 4 || section == 5) {
            if (isSectionEmpty(section)) {
                return TYPE_HEADER_0;
            } else {
                return TYPE_HEADER_1;
            }
        } else {
            return TYPE_HEADER_0;
        }
    }

    /**
     * 加载更多时 单品推荐继续添加
     *
     * @param list ""
     */
    public void addRecommentGoods(List<GoodsScheme> list) {
        if (list == null || list.size() == 0) return;

        if (mData == null) return;
        if (mData.recommended_goods == null) return;
        if (mData.recommended_goods.list == null) {
            mData.recommended_goods.list = new ArrayList<>();
        }
        mData.recommended_goods.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 判断分区数据源是否为空
     *
     * @param section 分区
     * @return 根据判断控制标题的显示与隐藏
     */
    private boolean isSectionEmpty(int section) {
        switch (section) {
            case 0:
            case 2:
            case 3:
            case 4:
                return getCountForSection(section) == 1;
            case 1:
                return false;
            case 5:
                return getCountForSection(section) == 0;
            default:
                return false;
        }
    }

    @Override
    public int getSectionHeaderViewTypeCount() {
        return 2;
    }

    /**
     * @return 返回标题条目的总数
     */
    @Override
    public int getSectionCount() {
        if (mData == null) return 0;
        return 6;
    }

    /**
     * @param section 第几个标题
     * @return 返回标题下内容条目的总数
     */
    @Override
    public int getCountForSection(int section) {
        switch (section) {
            case 0:
                return BaseFunc.getRowCountOfList(mData.getBtnsSize(), 5) + 1;
            case 1:
                return 2;
            case 2:
                return BaseFunc.getRowCountOfList(mData.getCountryCount(), 2) + 1;
            case 3:
                return mData.getZhutiCount() + 1;
            case 4:
                return BaseFunc.getRowCountOfList(mData.getBrandCount(), 2) + 1;
            case 5:
                return mData.getRecommendCount();
            default:
                return 1;
        }
    }

    /**
     * 返回条目View
     *
     * @param section     分区
     * @param position    分区子件索引
     * @param convertView 视图
     * @param parent      ViewGroup
     * @return View
     */
    @Override
    public View getItemView(final int section, int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(section, position);

        if (convertView == null) {
            switch (viewType) {
                case TYPE_CONTENT_BTNS:
                    convertView = View.inflate(mContext, R.layout.item_homenav_btns, null);
                    break;
                case TYPE_CONTENT_HOTACT:
                    convertView = View.inflate(mContext, R.layout.layout_hot_activity, null);
                    break;
                case TYPE_CONTENT_COUNTRY:
                    convertView = View.inflate(mContext, R.layout.item_homenav_guojiaguan, null);
                    break;
                case TYPE_CONTENT_BANNER_GOODS:
                    convertView = View.inflate(mContext, R.layout.item_homenav_advpic_recycleview, null);
                    break;
                case TYPE_CONTENT_BRAND:
                    convertView = View.inflate(mContext, R.layout.item_homenav_pinpaiguan, null);
                    break;
                case TYPE_CONTENT_LASTGOODS:
                    convertView = View.inflate(mContext, R.layout.item_homenav_lastgoods, null);
                    break;
                case TYPE_CONTENT_DIVIDER:
                    convertView = View.inflate(mContext, R.layout.item_homenav_divider, null);
                    break;
                case TYPE_CONTENT_BRAND_MORE:
                    convertView = View.inflate(mContext, R.layout.item_homenav_loadmore, null);
                    break;
                default:
                    convertView = View.inflate(mContext, R.layout.item_homenav_btns, null);
                    break;
            }

            new ItemViewHolder(convertView, viewType);
        }

        ItemViewHolder holder = (ItemViewHolder) convertView.getTag();
        switch (viewType) {
            case TYPE_CONTENT_BTNS:
                HomeNavigation hn0 = mData.getHomeNav(position, 0);
                bindBtnsViewWithData(holder.L0, holder.iV0, holder.tV0, hn0);
                HomeNavigation hn1 = mData.getHomeNav(position, 1);
                bindBtnsViewWithData(holder.L1, holder.iV1, holder.tV1, hn1);
                HomeNavigation hn2 = mData.getHomeNav(position, 2);
                bindBtnsViewWithData(holder.L2, holder.iV2, holder.tV2, hn2);
                HomeNavigation hn3 = mData.getHomeNav(position, 3);
                bindBtnsViewWithData(holder.L3, holder.iV3, holder.tV3, hn3);
                HomeNavigation hn4 = mData.getHomeNav(position, 4);
                bindBtnsViewWithData(holder.L4, holder.iV4, holder.tV4, hn4);
                break;
            case TYPE_CONTENT_COUNTRY:
                ActivityList ctry0 = mData.getCountry(position, 0);
                bindCtryViewWithData(holder.L0, holder.iV0, ctry0);
                ActivityList ctry1 = mData.getCountry(position, 1);
                bindCtryViewWithData(holder.L1, holder.iV1, ctry1);
                break;
            case TYPE_CONTENT_HOTACT:
                if (mData.getAdv(0) != null) {
                    new FHImageViewUtil(holder.ivAdv1).setImageURI(mData.getAdv(0).adv_pic, FHImageViewUtil.SHOWTYPE.NEWHOME_ADVRIGHT);
                    holder.ivAdv1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BaseFunc.urlClick(mContext, mData.getAdv(0).adv_link);
                        }
                    });
                }
                if (mData.getAdv(1) != null) {
                    new FHImageViewUtil(holder.ivAdv2).setImageURI(mData.getAdv(1).adv_pic, FHImageViewUtil.SHOWTYPE.NEWHOME_ADVRIGHT);
                    holder.ivAdv2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BaseFunc.urlClick(mContext, mData.getAdv(1).adv_link);
                        }
                    });
                }

                //如果存在秒杀则显示秒杀 否则显示秒杀替代的广告位 广告位为API中固定写的
                if (mData.getMiaosha(0) != null && mData.getNacSecKillingGoods(0) != null) {
                    holder.viewMiaosha.setVisibility(View.VISIBLE);
                    holder.LMiaosha.setVisibility(View.GONE);
                    bindMiaoshaView(holder.viewMiaosha, mData.getNacSecKillingGoods(0), mData.getMiaosha(0).sequence_url);
                } else {
                    holder.viewMiaosha.setVisibility(View.GONE);
                    holder.LMiaosha.setVisibility(View.VISIBLE);
                    final Adv miaoshaAdv = mData.getMiaoshaAdv(0);
                    if (miaoshaAdv != null) {
                        new FHImageViewUtil(holder.ivAdvMiaosha).setImageURI(miaoshaAdv.adv_pic, FHImageViewUtil.SHOWTYPE.DEFAULT);
                        holder.LMiaosha.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                BaseFunc.urlClick(mContext, miaoshaAdv.adv_link);
                            }
                        });
                    }
                }
                break;
            case TYPE_CONTENT_BANNER_GOODS:
                final HomeNavData.ActivityListWithGoods zhutiGoods = mData.getZhutiWithGoods(position);
                if (zhutiGoods != null) {
                    new FHImageViewUtil(holder.iV0).setImageURI(zhutiGoods.activity_banner, FHImageViewUtil.SHOWTYPE.NEWHOME_BANNER);
                    holder.iV0.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (BaseFunc.isValidUrl(zhutiGoods.activity_url)) {
                                BaseFunc.urlClick(mContext, zhutiGoods.activity_url);
                            } else {
                                BaseFunc.gotoActivity(mContext, ActListDtlActivity.class, zhutiGoods.activity_id);
                            }
                        }
                    });

                    List<ActivityGoods> originGoods = zhutiGoods.goods_list;

                    if (originGoods != null && originGoods.size() > 0) {
                        LinearLayoutManager manager = new LinearLayoutManager(mContext);
                        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        holder.recyclerView.setLayoutManager(manager);
                        HomeNavRecycleGoodsAdapter adapter = new HomeNavRecycleGoodsAdapter(mContext);

                        adapter.setList(originGoods);
                        adapter.setActivityList(zhutiGoods);
                        holder.recyclerView.setAdapter(adapter);
                        holder.recyclerView.setVisibility(View.VISIBLE);
                        int countOfSec = getCountForSection(section);
                        if (position == countOfSec - 2) {
                            holder.vline.setVisibility(View.GONE);
                        } else {
                            holder.vline.setVisibility(View.VISIBLE);
                        }

                        List<ActivityGoods> sortGoods = zhutiGoods.getReWordAscList();
                        if (sortGoods != null && sortGoods.size() > 0) {
                            ActivityGoods firstGoods = sortGoods.get(0);
                            if (firstGoods.isShowReward()) {
                                holder.LShare.setVisibility(View.VISIBLE);
                                holder.vDash.setVisibility(View.VISIBLE);
                                holder.tvShareWin.setText(firstGoods.getMaxRewordRatio());
                                holder.tvShare.setText(firstGoods.getGoods_share_intro());
                                holder.LShare.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ShareData shareData = new ShareData();
                                        shareData.title = zhutiGoods.getShareTitle();
                                        shareData.content = zhutiGoods.getShareDesc();
                                        shareData.imgs = zhutiGoods.getShareImg();
                                        shareData.url = zhutiGoods.activity_url;
                                        ShareData.fhShare((BaseFragmentActivity) mContext, shareData, null);
                                    }
                                });
                            } else {
                                holder.LShare.setVisibility(View.GONE);
                                holder.vDash.setVisibility(View.GONE);
                            }
                        }

                    } else {
                        holder.recyclerView.setVisibility(View.GONE);
                        holder.LShare.setVisibility(View.GONE);
                        holder.vDash.setVisibility(View.GONE);
                        holder.vline.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case TYPE_CONTENT_BRAND_MORE:
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("VAL", "-1");
                        bundle.putString("TITLE", mData.getHeaderString(section));
                        BaseFunc.gotoActivityBundle(mContext, HotBrandsActivity.class, bundle);
                    }
                });
                break;
            case TYPE_CONTENT_BRAND:
                HotBrands brand0 = mData.getBrand(position, 0);
                bindBrandViewWithData(holder.L0, holder.iV0, brand0);
                HotBrands brand1 = mData.getBrand(position, 1);
                bindBrandViewWithData(holder.L1, holder.iV1, brand1);
                break;
            case TYPE_CONTENT_LASTGOODS:
                final GoodsScheme goodsScheme = mData.getRecommentGoodsAtPosition(position);
                if (goodsScheme != null) {
                    new FHImageViewUtil(holder.ivFlag).setImageURI(goodsScheme.nation_flag, FHImageViewUtil.SHOWTYPE.DEFAULT);
                    new FHImageViewUtil(holder.ivGoodsImage).setImageURI(goodsScheme.goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);
                    holder.tvFlag.setText(goodsScheme.goods_promise);
                    holder.tvHeart.setTypeface(iconfont);
                    holder.tvIcon.setTypeface(iconfont);
                    holder.tvPrice.setText(priceFormat.format(goodsScheme.goods_price));
                    holder.tvPrice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UpdateAction.add2Cart((Activity) mContext, goodsScheme.goods_id, 1, goodsScheme.goods_source);
                        }
                    });
                    if (goodsScheme.isCollected) {
                        holder.tvHeart.setText(mContext.getString(R.string.if_love_full));
                        holder.tvHeart.setTextColor(mContext.getResources().getColor(R.color.fh_red));
                    } else {
                        holder.tvHeart.setText(mContext.getString(R.string.if_love_empty));
                        holder.tvHeart.setTextColor(mContext.getResources().getColor(R.color.color_bb));
                    }
                    holder.tvGoodsName.setText(goodsScheme.goods_name);
                    holder.tvGoodsDesc.setText(goodsScheme.goods_desc);
                    holder.tvHeart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            YoYo.with(Techniques.ZoomIn).duration(200).playOn(v);
                            goodsScheme.isCollected = !goodsScheme.isCollected;
                            notifyDataSetChanged();
                        }
                    });
                    View.OnClickListener goodsClick = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BaseFunc.gotoGoodsDetail(mContext, goodsScheme.goods_id, goodsScheme.resource_tags, null);
                        }
                    };
                    holder.ivGoodsImage.setOnClickListener(goodsClick);
                    holder.tvGoodsName.setOnClickListener(goodsClick);
                    holder.tvGoodsDesc.setOnClickListener(goodsClick);
                    holder.tvFlag.setOnClickListener(goodsClick);
                    holder.ivFlag.setOnClickListener(goodsClick);

                    if (goodsScheme.isShowReward()) {
                        holder.LShare.setVisibility(View.VISIBLE);
                        holder.tvShareDesc.setText(goodsScheme.getFanli());
                        holder.tvShareWin.setText(goodsScheme.getGoodsRewardMoney());
                        holder.LShare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ShareData shareData = new ShareData();
                                shareData.title = goodsScheme.share_title;
                                shareData.content = goodsScheme.share_describe;
                                shareData.imgs = goodsScheme.share_img;
                                shareData.url = String.format(BaseVar.SHARE_GOODS_DTL, goodsScheme.goods_id);
                                shareData.price = goodsScheme.goods_price;
                                shareData.deductMoney = goodsScheme.getGoods_reward_money();

                                ShareData.fhShare((BaseFragmentActivity) mContext, shareData, null);
                            }
                        });
                    } else {
                        holder.LShare.setVisibility(View.GONE);
                        holder.tvShareDesc.setText("");
                    }
                }
                break;
        }
        return convertView;
    }

    /**
     * 返回标题View
     *
     * @param section     int
     * @param convertView View
     * @param parent      ViewGroup
     * @return View
     */
    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        int viewType = getSectionHeaderViewType(section);
        if (convertView == null) {
            if (viewType == TYPE_HEADER_1) {
                convertView = View.inflate(mContext, R.layout.item_header_homenav_style1, null);
            } else {
                convertView = View.inflate(mContext, R.layout.item_header_homenav_style0, null);
            }
            new HeaderViewHolder(convertView, viewType);
        }
        HeaderViewHolder holder = (HeaderViewHolder) convertView.getTag();
        if (viewType == TYPE_HEADER_1) {
            holder.tvTitle.setText(mData.getHeaderString(section));
        }

        return convertView;
    }

    class HeaderViewHolder {
        private TextView tvTitle;

        public HeaderViewHolder(View convertView, int type) {
            if (type == TYPE_HEADER_1) {
                tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            }
            convertView.setTag(this);
        }
    }

    class ItemViewHolder {
        ImageView ivFlag, ivGoodsImage;
        ImageView ivAdv1, ivAdv2, ivAdvMiaosha;
        TextView tvFlag, tvIcon, tvPrice, tvGoodsName, tvGoodsDesc, tvHeart;
        LinearLayout L0, L1, L2, L3, L4, LMiaosha;
        ImageView iV0, iV1, iV2, iV3, iV4;
        TextView tV0, tV1, tV2, tV3, tV4;
        RecyclerView recyclerView;
        View vline, viewMiaosha, vDash;

        LinearLayout LShare;
        TextView tvShare, tvShareWin;
        TextView tvShareDesc;

        TextView tvShareIcon;

        public ItemViewHolder(View convertView, int type) {
            switch (type) {
                case TYPE_CONTENT_BTNS:
                    L0 = (LinearLayout) convertView.findViewById(R.id.L0);
                    iV0 = (ImageView) convertView.findViewById(R.id.iV0);
                    tV0 = (TextView) convertView.findViewById(R.id.tV0);

                    L1 = (LinearLayout) convertView.findViewById(R.id.L1);
                    iV1 = (ImageView) convertView.findViewById(R.id.iV1);
                    tV1 = (TextView) convertView.findViewById(R.id.tV1);

                    L2 = (LinearLayout) convertView.findViewById(R.id.L2);
                    iV2 = (ImageView) convertView.findViewById(R.id.iV2);
                    tV2 = (TextView) convertView.findViewById(R.id.tV2);

                    L3 = (LinearLayout) convertView.findViewById(R.id.L3);
                    iV3 = (ImageView) convertView.findViewById(R.id.iV3);
                    tV3 = (TextView) convertView.findViewById(R.id.tV3);

                    L4 = (LinearLayout) convertView.findViewById(R.id.L4);
                    iV4 = (ImageView) convertView.findViewById(R.id.iV4);
                    tV4 = (TextView) convertView.findViewById(R.id.tV4);
                    break;
                case TYPE_CONTENT_HOTACT:
                    ivAdv1 = (ImageView) convertView.findViewById(R.id.ivAdv1);
                    ivAdv2 = (ImageView) convertView.findViewById(R.id.ivAdv2);
                    viewMiaosha = convertView.findViewById(R.id.viewMiaosha);
                    LMiaosha = (LinearLayout) convertView.findViewById(R.id.LMiaosha);
                    ivAdvMiaosha = (ImageView) convertView.findViewById(R.id.ivAdvMiaosha);
                    break;
                case TYPE_CONTENT_COUNTRY:
                    L0 = (LinearLayout) convertView.findViewById(R.id.L0);
                    iV0 = (ImageView) convertView.findViewById(R.id.iV0);

                    L1 = (LinearLayout) convertView.findViewById(R.id.L1);
                    iV1 = (ImageView) convertView.findViewById(R.id.iV1);

                    iV0.setLayoutParams(paramsCountryPic);
                    iV1.setLayoutParams(paramsCountryPic);
                    break;
                case TYPE_CONTENT_BANNER_GOODS:
                    iV0 = (ImageView) convertView.findViewById(R.id.iV0);
                    recyclerView = (RecyclerView) convertView.findViewById(R.id.recyclerView);
                    vline = convertView.findViewById(R.id.vline);

                    iV0.setLayoutParams(paramsActivityPic);

                    LShare = (LinearLayout) convertView.findViewById(R.id.LShare);
                    tvShare = (TextView) convertView.findViewById(R.id.tvShare);
                    tvShareWin = (TextView) convertView.findViewById(R.id.tvShareWin);
                    vDash = convertView.findViewById(R.id.vDash);
                    break;
                case TYPE_CONTENT_BRAND:
                    L0 = (LinearLayout) convertView.findViewById(R.id.L0);
                    iV0 = (ImageView) convertView.findViewById(R.id.iV0);

                    L1 = (LinearLayout) convertView.findViewById(R.id.L1);
                    iV1 = (ImageView) convertView.findViewById(R.id.iV1);
                    break;
                case TYPE_CONTENT_LASTGOODS:
                    ivFlag = (ImageView) convertView.findViewById(R.id.ivFlag);
                    ivGoodsImage = (ImageView) convertView.findViewById(R.id.ivGoodsImage);
                    tvFlag = (TextView) convertView.findViewById(R.id.tvFlag);
                    tvIcon = (TextView) convertView.findViewById(R.id.tvIcon);
                    tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
                    tvGoodsName = (TextView) convertView.findViewById(R.id.tvGoodsName);
                    tvGoodsDesc = (TextView) convertView.findViewById(R.id.tvGoodsDesc);
                    tvHeart = (TextView) convertView.findViewById(R.id.tvHeart);

                    LShare = (LinearLayout) convertView.findViewById(R.id.LShare);
                    tvShareIcon = (TextView) convertView.findViewById(R.id.tvShareIcon);
                    tvShareWin = (TextView) convertView.findViewById(R.id.tvShareWin);
                    tvShareDesc = (TextView) convertView.findViewById(R.id.tvShareDesc);

                    tvShareIcon.setTypeface(iconfont);
                    break;

            }
            convertView.setTag(this);
        }

    }


    private void bindBtnsViewWithData(LinearLayout L, ImageView iV, TextView tV, final HomeNavigation hn) {
        if (hn != null) {
            L.setVisibility(View.VISIBLE);
            new FHImageViewUtil(iV).setImageURI(hn.nav_icon, FHImageViewUtil.SHOWTYPE.DEFAULT);
            tV.setText(hn.getNavTitle(mContext));
            L.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.urlClick(mContext, hn.nav_url);
                }
            });
        } else {
            L.setVisibility(View.INVISIBLE);
        }
    }

    private void bindCtryViewWithData(LinearLayout L, ImageView iV, final ActivityList al) {
        if (al != null) {
            L.setVisibility(View.VISIBLE);
            new FHImageViewUtil(iV).setImageURI(al.activity_pic, FHImageViewUtil.SHOWTYPE.COUNTRY);
            L.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (BaseFunc.isValidUrl(al.activity_url)) {
                        BaseFunc.urlClick(mContext, al.activity_url);
                    } else {
                        Intent intent = new Intent(mContext, NationalPavilionActivity.class);
                        intent.putExtra("activity_id", al.activity_id);
                        mContext.startActivity(intent);
                    }
                }
            });
        } else {
            L.setVisibility(View.INVISIBLE);
        }
    }

    private void bindBrandViewWithData(LinearLayout L, ImageView iV, final HotBrands brand) {
        if (brand != null) {
            L.setVisibility(View.VISIBLE);
            new FHImageViewUtil(iV).setImageURI(brand.brand_banner, FHImageViewUtil.SHOWTYPE.NEWHOME_BRAND);
            L.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (BaseFunc.isValidUrl(brand.brand_url)) {
                        BaseFunc.urlClick(mContext, brand.brand_url);
                    } else {
                        BaseFunc.gotoActivity(mContext, GroupActivity.class, brand.brand_id);
                    }
                }
            });
        } else {
            L.setVisibility(View.INVISIBLE);
        }
        iV.setLayoutParams(paramsBrandPic);
    }

    private void bindMiaoshaView(View viewMiaosha, final SeqGoodsModel goods, final String sequence_url) {
        if (viewMiaosha == null || goods == null)
            return;
        ImageView imageView = (ImageView) viewMiaosha.findViewById(R.id.imageView);
        TextView tvLabel = (TextView) viewMiaosha.findViewById(R.id.tvLabel);
        TextView tvMarketPrice = (TextView) viewMiaosha.findViewById(R.id.tvMarketPrice);
        final TextView tvReadyToRob = (TextView) viewMiaosha.findViewById(R.id.tvReadyToRob);
        final TextView tvCountDown = (TextView) viewMiaosha.findViewById(R.id.tvCountDown);

        new FHImageViewUtil(imageView).setImageURI(goods.goods_pic, FHImageViewUtil.SHOWTYPE.DEFAULT);

        tvLabel.setText(goods.goods_name);
        tvMarketPrice.setText(priceFormat.format(goods.goods_price));
        tvMarketPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        if (goods.countdown > 0) {
            /**
             * 有开始时间倒计时 首先显示
             */
            tvCountDown.setVisibility(View.VISIBLE);
            tvReadyToRob.setVisibility(View.VISIBLE);
            tvReadyToRob.setText(mContext.getString(R.string.ready_to_rob));
            if (countDownTimer == null) {
                countDownTimer = new CountDownTimer(goods.countdown * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        long[] leftTime = BaseFunc.getD_H_M_S(millisUntilFinished / 1000);
                        tvCountDown.setText(String.format(mContext.getString(R.string.time_standard), formatTime(leftTime[1]), formatTime(leftTime[2]), formatTime(leftTime[3])));
                    }

                    @Override
                    public void onFinish() {
                        /**
                         * 当开始时间倒计时结束后直接显示结束时间倒计时
                         */
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        countDownTimer = showCountDown(goods.countdown_end * 1000, tvCountDown, tvReadyToRob);
                        tvReadyToRob.setText(mContext.getString(R.string.rob_to_the_end));
                    }
                }.start();
            }
        } else if (goods.countdown_end > 0) {
            tvCountDown.setVisibility(View.VISIBLE);
            tvReadyToRob.setVisibility(View.VISIBLE);
            tvReadyToRob.setText(mContext.getString(R.string.rob_to_the_end));
            /**
             * 结束时间倒计时 这里只有在初始时才实例化一次，否则页面滚动回来后倒计时又会重新开始
             */
            if (countDownTimer == null) {
                countDownTimer = showCountDown(goods.countdown_end * 1000, tvCountDown, tvReadyToRob);
            }

        } else {
            /**
             * 没有倒计时，直接隐藏
             */
            tvReadyToRob.setVisibility(View.GONE);
            tvCountDown.setVisibility(View.GONE);
        }

        if (BaseFunc.isValidUrl(sequence_url)) {
            viewMiaosha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.gotoActivity(mContext, FHBrowserActivity.class, sequence_url);
                }
            });
        }
    }

    /**
     * 显示秒杀倒计时
     *
     * @param countdown    剩余时间 s
     * @param tvCountDown  更新的控件
     * @param tvReadyToRob 最后隐藏的控件
     */
    private CountDownTimer showCountDown(long countdown, final TextView tvCountDown, final TextView tvReadyToRob) {
        return new CountDownTimer(countdown, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long[] leftTime = BaseFunc.getD_H_M_S(millisUntilFinished / 1000);
                tvCountDown.setText(String.format(mContext.getString(R.string.time_standard), formatTime(leftTime[1]), formatTime(leftTime[2]), formatTime(leftTime[3])));
            }

            @Override
            public void onFinish() {
                tvCountDown.setVisibility(View.GONE);
                if (tvReadyToRob != null)
                    tvReadyToRob.setVisibility(View.GONE);
            }
        }.start();
    }

    /**
     * 对于时间数字，一位的前面补0
     *
     * @param time time
     * @return time
     */
    private String formatTime(long time) {
        return (time < 10) ? ("0" + time) : String.valueOf(time);
    }

}
