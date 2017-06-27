package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.baseui.viewholder.HalfScreenGoodsViewHolder;
import com.fanglin.fenhong.microbuyer.base.model.ActivityGoods;
import com.fanglin.fenhong.microbuyer.base.model.ChannelOneData;
import com.fanglin.fenhong.microbuyer.base.model.GoodsScheme;
import com.fanglin.fenhong.microbuyer.base.model.HomeNavData;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.common.ActListDtlActivity;
import com.fanglin.fhui.PinnedHeaderListView.SectionedBaseAdapter;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/3/28-下午2:00.
 * 功能描述:首页样式一适配器
 */
public class ThemeOneAdapter extends SectionedBaseAdapter {

    private Context mContext;

    public static final int TYPE_HEADER_0 = 0;
    public static final int TYPE_HEADER_1 = 1;

    public static final int TYPE_CONTENT_CATEGORY = 0;
    public static final int TYPE_CONTENT_BANNER_GOODS = 1;
    public static final int TYPE_CONTENT_ONLYBANNER = 2;
    public static final int TYPE_CONTENT_LASTGOODS = 3;
    public static final int TYPE_CONTENT_DIVIDER = 4;

    private ChannelOneData channelOneData;

    public void setData(ChannelOneData channelOneData) {
        this.channelOneData = channelOneData;
    }

    LinearLayout.LayoutParams paramsHotGoodsPic, paramsActivityPic, paramsCategoryPic;

    public ThemeOneAdapter(Context context) {
        super();
        this.mContext = context;

        int screenWidth = BaseFunc.getDisplayMetrics(context).widthPixels;

        int activityBannerHeight = 460 * screenWidth / 750;//主题馆+商品组合 图片的高度
        paramsActivityPic = new LinearLayout.LayoutParams(screenWidth, activityBannerHeight);

        int hotGoodsPicHeight = 380 * screenWidth / 750;//热门好货单张图片的高度
        paramsHotGoodsPic = new LinearLayout.LayoutParams(screenWidth, hotGoodsPicHeight);

        int categoryWidth = (screenWidth - 10) / 2;
        int categoryHeight = 190 * categoryWidth / 370;//分类图片高度
        paramsCategoryPic = new LinearLayout.LayoutParams(categoryWidth, categoryHeight);
    }

    @Override
    public int getItemViewType(int section, int position) {
        switch (section) {
            case 0:
                if (position == getCountForSection(section) - 1)
                    return TYPE_CONTENT_DIVIDER;
                return TYPE_CONTENT_CATEGORY;
            case 1:
                if (position == getCountForSection(section) - 1)
                    return TYPE_CONTENT_DIVIDER;
                return TYPE_CONTENT_BANNER_GOODS;
            case 2:
                if (position == getCountForSection(section) - 1)
                    return TYPE_CONTENT_DIVIDER;
                return TYPE_CONTENT_ONLYBANNER;
            case 3:
                return TYPE_CONTENT_LASTGOODS;
            default:
                return TYPE_CONTENT_CATEGORY;
        }
    }

    @Override
    public int getItemViewTypeCount() {
        return 5;
    }

    @Override
    public int getSectionHeaderViewType(int section) {
        if (section == 1 || section == 2) {
            if (getCountForSection(section) == 1) {
                return TYPE_HEADER_0;
            } else {
                return TYPE_HEADER_1;
            }
        } else {
            return TYPE_HEADER_0;
        }
    }

    @Override
    public int getSectionHeaderViewTypeCount() {
        return 2;
    }

    @Override
    public Object getItem(int section, int position) {
        return null;
    }

    @Override
    public long getItemId(int section, int position) {
        return 0;
    }

    @Override
    public int getSectionCount() {
        if (channelOneData == null) return 0;
        return 4;
    }

    @Override
    public int getCountForSection(int section) {
        switch (section) {
            case 0:
                return BaseFunc.getRowCountOfList(channelOneData.getClassSize(), 2) + 1;
            case 1:
                return channelOneData.getZhutiCount() + 1;
            case 2:
                return channelOneData.getJustBannerCount() + 1;
            case 3:
                return channelOneData.getLastGoodsRows();
            default:
                return 0;
        }

    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(section, position);
        if (convertView == null) {
            switch (type) {
                case TYPE_CONTENT_CATEGORY:
                    convertView = View.inflate(mContext, R.layout.item_themeone_category, null);
                    break;
                case TYPE_CONTENT_BANNER_GOODS:
                    convertView = View.inflate(mContext, R.layout.item_homenav_advpic_recycleview, null);
                    break;
                case TYPE_CONTENT_ONLYBANNER:
                    convertView = View.inflate(mContext, R.layout.item_themeone_goodsbanner, null);
                    break;
                case TYPE_CONTENT_LASTGOODS:
                    convertView = View.inflate(mContext, R.layout.item_themeone_lastgoods, null);
                    break;
                case TYPE_CONTENT_DIVIDER:
                    convertView = View.inflate(mContext, R.layout.item_homenav_divider, null);
                    break;
                default:
                    convertView = View.inflate(mContext, R.layout.item_themeone_category, null);
                    break;
            }

            new ItemViewHolder(convertView, type);
        }

        ItemViewHolder holder = (ItemViewHolder) convertView.getTag();
        switch (type) {
            case TYPE_CONTENT_CATEGORY:
                ChannelOneData.ChannelClass cls0 = channelOneData.getChannerlClass(position, 0);
                ChannelOneData.ChannelClass cls1 = channelOneData.getChannerlClass(position, 1);
                bindChannelClass(holder.iV0, cls0);
                bindChannelClass(holder.iV1, cls1);
                break;
            case TYPE_CONTENT_BANNER_GOODS:
                final HomeNavData.ActivityListWithGoods activityListWithGoods = channelOneData.getZhutiWithGoods(position);
                if (activityListWithGoods != null) {
                    new FHImageViewUtil(holder.iV0).setImageURI(activityListWithGoods.activity_banner, FHImageViewUtil.SHOWTYPE.NEWHOME_BANNER);
                    holder.iV0.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (BaseFunc.isValidUrl(activityListWithGoods.activity_url)) {
                                BaseFunc.urlClick(mContext, activityListWithGoods.activity_url);
                            } else {
                                BaseFunc.gotoActivity(mContext, ActListDtlActivity.class, activityListWithGoods.activity_id);
                            }
                        }
                    });

                    LinearLayoutManager manager = new LinearLayoutManager(mContext);
                    manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    holder.recyclerView.setLayoutManager(manager);

                    List<ActivityGoods> originGoods = activityListWithGoods.goods_list;

                    if (originGoods != null && originGoods.size() > 0) {
                        HomeNavRecycleGoodsAdapter adapter = new HomeNavRecycleGoodsAdapter(mContext);
                        adapter.setList(originGoods);
                        adapter.setActivityList(activityListWithGoods);
                        holder.recyclerView.setAdapter(adapter);
                        holder.recyclerView.setVisibility(View.VISIBLE);
                        if (position == originGoods.size() - 2) {
                            holder.vline.setVisibility(View.GONE);
                        } else {
                            holder.vline.setVisibility(View.VISIBLE);
                        }

                        List<ActivityGoods> sortGoods = activityListWithGoods.getReWordAscList();
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
                                        shareData.title = activityListWithGoods.getShareTitle();
                                        shareData.content = activityListWithGoods.getShareDesc();
                                        shareData.imgs = activityListWithGoods.getShareImg();
                                        shareData.url = activityListWithGoods.activity_url;
                                        ShareData.fhShare((BaseFragmentActivity) mContext, shareData, null);
                                    }
                                });
                            } else {
                                holder.LShare.setVisibility(View.GONE);
                                holder.vDash.setVisibility(View.GONE);
                            }
                        } else {
                            holder.LShare.setVisibility(View.GONE);
                            holder.vDash.setVisibility(View.GONE);
                        }

                    } else {
                        holder.recyclerView.setVisibility(View.GONE);
                        holder.vline.setVisibility(View.VISIBLE);
                        holder.LShare.setVisibility(View.GONE);
                    }
                }
                break;
            case TYPE_CONTENT_ONLYBANNER:
                final GoodsScheme bannerGoods = channelOneData.getBannerGoods(position);
                if (bannerGoods != null) {
                    new FHImageViewUtil(holder.iV0).setImageURI(bannerGoods.goods_image, FHImageViewUtil.SHOWTYPE.THEMEONE_HOTGOODS_ONLYBANNER);
                    holder.iV0.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BaseFunc.gotoGoodsDetail(mContext, bannerGoods.goods_id, null, null);
                        }
                    });
                }
                break;
            case TYPE_CONTENT_LASTGOODS:
                GoodsScheme lastGoods0 = channelOneData.getLastGoods(position, 0);
                GoodsScheme lastGoods1 = channelOneData.getLastGoods(position, 1);
                bindLastGoods(holder.viewGoodsOne, lastGoods0);
                bindLastGoods(holder.viewGoodsTwo, lastGoods1);
                break;
        }

        return convertView;
    }

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
            holder.tvTitle.setText(getHeaderString(section));
        }

        return convertView;
    }

    class ItemViewHolder {
        ImageView iV0, iV1;
        RecyclerView recyclerView;
        View vline, viewGoodsOne, viewGoodsTwo, vDash;

        LinearLayout LShare;
        TextView tvShare, tvShareWin;

        public ItemViewHolder(View convertView, int type) {
            switch (type) {
                case TYPE_CONTENT_CATEGORY:
                    iV0 = (ImageView) convertView.findViewById(R.id.iV0);
                    iV1 = (ImageView) convertView.findViewById(R.id.iV1);
                    break;
                case TYPE_CONTENT_BANNER_GOODS:
                    iV0 = (ImageView) convertView.findViewById(R.id.iV0);
                    recyclerView = (RecyclerView) convertView.findViewById(R.id.recyclerView);
                    vline = convertView.findViewById(R.id.vline);
                    vDash = convertView.findViewById(R.id.vDash);

                    iV0.setLayoutParams(paramsActivityPic);

                    LShare = (LinearLayout) convertView.findViewById(R.id.LShare);
                    tvShare = (TextView) convertView.findViewById(R.id.tvShare);
                    tvShareWin = (TextView) convertView.findViewById(R.id.tvShareWin);
                    break;
                case TYPE_CONTENT_ONLYBANNER:
                    iV0 = (ImageView) convertView.findViewById(R.id.iV0);
                    iV0.setLayoutParams(paramsHotGoodsPic);
                    break;
                case TYPE_CONTENT_LASTGOODS:
                    viewGoodsOne = convertView.findViewById(R.id.viewGoodsOne);
                    viewGoodsTwo = convertView.findViewById(R.id.viewGoodsTwo);
                    break;
            }
            convertView.setTag(this);
        }
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

    private String getHeaderString(int section) {
        if (section == 1) return channelOneData.getHotActivityTitle();
        if (section == 2) return channelOneData.getHotGoodsTitle();
        return "";
    }


    private void bindChannelClass(ImageView iv, final ChannelOneData.ChannelClass channelClass) {
        if (channelClass != null) {
            new FHImageViewUtil(iv).setImageURI(channelClass.class_pic, FHImageViewUtil.SHOWTYPE.THEMEONE_CATEGORY);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.urlClick(mContext, channelClass.class_url);
                }
            });
            iv.setVisibility(View.VISIBLE);
        } else {
            iv.setVisibility(View.INVISIBLE);
        }
        iv.setLayoutParams(paramsCategoryPic);
    }

    private void bindLastGoods(View itemView, final GoodsScheme goodsScheme) {
        if (itemView == null) return;
        if (goodsScheme != null) {
            itemView.setVisibility(View.VISIBLE);

            HalfScreenGoodsViewHolder holder = new HalfScreenGoodsViewHolder(itemView);
            holder.setModelData(mContext, goodsScheme);
        } else {
            itemView.setVisibility(View.INVISIBLE);
        }
    }
}
