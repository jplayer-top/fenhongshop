package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.baseui.viewholder.HalfScreenGoodsViewHolder;
import com.fanglin.fenhong.microbuyer.base.model.ActivityGoods;
import com.fanglin.fenhong.microbuyer.base.model.BrandMessage;
import com.hb.views.PinnedSectionListView;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/7/13-上午10:02.
 * 功能描述:聚合页适配器
 */
public class GroupAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    public static final int TYPE_HEADER_TOP = 0;
    public static final int TYPE_HEADER_PINED = 1;
    public static final int TYPE_ITEM = 2;

    private Context mContext;

    private int currentIndex = 0;//0综合 1销量 2价格 3人气
    private int currentOrder = 1;//(1为升序，2为降序）

    private int maxLines = 2;
    private int drawablePadding = 0;

    private BrandMessage brandMessage;
    private List<ActivityGoods> list;

    private LinearLayout.LayoutParams bgParams;

    public GroupAdapter(Context mContext) {
        this.mContext = mContext;
        int w = mContext.getResources().getDisplayMetrics().widthPixels;
        drawablePadding = mContext.getResources().getDimensionPixelOffset(R.dimen.dp_of_13);

        int bgH = w * 360 / 750;
        bgParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, bgH);
    }

    public void setBrandMessage(BrandMessage brandMessage) {
        this.brandMessage = brandMessage;
        notifyDataSetChanged();
    }

    public void refreshCollectStatus(int is_favorites) {
        if (brandMessage != null) {
            brandMessage.setIs_favorites(is_favorites);
            notifyDataSetChanged();
        }
    }

    public void setList(List<ActivityGoods> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<ActivityGoods> addList) {
        if (addList != null && addList.size() > 0) {
            if (list != null) {
                list.addAll(addList);
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == TYPE_HEADER_PINED;
    }

    @Override
    public int getCount() {
        if (list == null)
            return 2;
        int row = BaseFunc.getRowCountOfList(list.size(), 2);
        return row + 2;
    }

    @Override
    public ActivityGoods getItem(int index) {
        if (list != null && index < list.size()) {
            return list.get(index);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (convertView == null) {
            switch (viewType) {
                case TYPE_HEADER_TOP:
                    convertView = View.inflate(mContext, R.layout.item_group_header_top, null);
                    new TopViewHolder(convertView);
                    break;
                case TYPE_HEADER_PINED:
                    convertView = View.inflate(mContext, R.layout.item_group_header_pinned, null);
                    new PinnedViewHolder(convertView);
                    break;
                case TYPE_ITEM:
                    convertView = View.inflate(mContext, R.layout.item_themeone_lastgoods, null);
                    new ItemViewHolder(convertView);
                    break;
            }
        }

        switch (viewType) {
            case TYPE_HEADER_TOP:
                TopViewHolder topViewHolder = (TopViewHolder) convertView.getTag();

                if (brandMessage != null) {
                    new FHImageViewUtil(topViewHolder.ivBanner).setImageURI(brandMessage.getBrand_page_pic(), FHImageViewUtil.SHOWTYPE.GRP_BANNER);

                    new FHImageViewUtil(topViewHolder.circleImageView).setImageURI(brandMessage.getBrand_pic(), FHImageViewUtil.SHOWTYPE.DEFAULT);
                    topViewHolder.tvName.setText(brandMessage.getBrand_name());

                    if (BaseFunc.isValidUrl(brandMessage.getCountry_pic())) {
                        topViewHolder.ivNation.setVisibility(View.VISIBLE);
                        new FHImageViewUtil(topViewHolder.ivNation).setImageURI(brandMessage.getCountry_pic(), FHImageViewUtil.SHOWTYPE.BANNER);
                    } else {
                        topViewHolder.ivNation.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(brandMessage.getCountry_name())) {
                        topViewHolder.tvNation.setVisibility(View.VISIBLE);
                        topViewHolder.tvNation.setText(brandMessage.getCountry_name());
                    } else {
                        topViewHolder.tvNation.setVisibility(View.GONE);
                    }


                    topViewHolder.tvNum.setText(brandMessage.getGoods_storage());
                    topViewHolder.tvDesc.setText(brandMessage.getBrand_intro());
                    topViewHolder.tvDesc.setMaxLines(maxLines);

                    if (topViewHolder.tvDesc.getLineCount() < 3) {
                        topViewHolder.tvDesc.setCompoundDrawablePadding(0);
                        topViewHolder.tvDesc.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    } else {
                        topViewHolder.tvDesc.setCompoundDrawablePadding(drawablePadding);
                        topViewHolder.tvDesc.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, maxLines == 2 ? R.drawable.icon_triangle_gray_down : R.drawable.icon_triangle_gray);
                    }

                    boolean hasCollected = brandMessage.hasCollected();
                    topViewHolder.tvCollect.setSelected(hasCollected);
                    topViewHolder.tvCollect.setText(hasCollected ? mContext.getString(R.string.lbl_focused) : mContext.getString(R.string.lbl_focus_txt));
                } else {
                    convertView.setVisibility(View.GONE);
                }
                break;
            case TYPE_HEADER_PINED:
                PinnedViewHolder pinnedViewHolder = (PinnedViewHolder) convertView.getTag();
                pinnedViewHolder.tvDefault.setSelected(currentIndex == 0);
                pinnedViewHolder.tvSales.setSelected(currentIndex == 1);
                pinnedViewHolder.LPrice.setSelected(currentIndex == 2);

                pinnedViewHolder.tvUp.setSelected(currentIndex == 2 && currentOrder == 1);
                pinnedViewHolder.tvDown.setSelected(currentIndex == 2 && currentOrder == 2);

                pinnedViewHolder.tvPopular.setSelected(currentIndex == 3);
                break;
            case TYPE_ITEM:
                ItemViewHolder itemViewHolder = (ItemViewHolder) convertView.getTag();
                int index0 = BaseFunc.getIndexOfList(position - 2, 0, 2);
                int index1 = BaseFunc.getIndexOfList(position - 2, 1, 2);

                final ActivityGoods groupGoods0 = getItem(index0);
                final ActivityGoods groupGoods1 = getItem(index1);

                if (groupGoods0 != null) {
                    itemViewHolder.viewGoodsOne.setVisibility(View.VISIBLE);
                    HalfScreenGoodsViewHolder halfHolder0 = new HalfScreenGoodsViewHolder(itemViewHolder.viewGoodsOne);
                    halfHolder0.setModelData(mContext, groupGoods0);
                } else {
                    itemViewHolder.viewGoodsOne.setVisibility(View.INVISIBLE);
                }

                if (groupGoods1 != null) {
                    itemViewHolder.viewGoodsTwo.setVisibility(View.VISIBLE);
                    HalfScreenGoodsViewHolder halfHolder1 = new HalfScreenGoodsViewHolder(itemViewHolder.viewGoodsTwo);
                    halfHolder1.setModelData(mContext, groupGoods1);
                } else {
                    itemViewHolder.viewGoodsTwo.setVisibility(View.INVISIBLE);
                }
                break;
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER_TOP;
        } else if (position == 1) {
            return TYPE_HEADER_PINED;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    class TopViewHolder {
        ImageView ivBanner;
        ImageView circleImageView;
        TextView tvName;
        ImageView ivNation;
        TextView tvNation;
        TextView tvNum;
        TextView tvDesc;
        TextView tvCollect;

        public TopViewHolder(View view) {
            ivBanner = (ImageView) view.findViewById(R.id.ivBanner);
            circleImageView = (ImageView) view.findViewById(R.id.circleImageView);
            tvName = (TextView) view.findViewById(R.id.tvName);
            ivNation = (ImageView) view.findViewById(R.id.ivNation);
            tvNation = (TextView) view.findViewById(R.id.tvNation);
            tvNum = (TextView) view.findViewById(R.id.tvNum);
            tvDesc = (TextView) view.findViewById(R.id.tvDesc);
            tvCollect = (TextView) view.findViewById(R.id.tvCollect);

            tvDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    maxLines = maxLines == 2 ? 10 : 2;
                    notifyDataSetChanged();
                }
            });
            tvCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (groupClickListener != null) {
                        groupClickListener.onCollect(brandMessage.getBrand_id(), brandMessage.hasCollected());
                    }
                }
            });

            tvDesc.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (!hasDraw) {
                        notifyDataSetChanged();
                        hasDraw = true;
                    }
                }
            });

            ivBanner.setLayoutParams(bgParams);
            view.setTag(this);
        }
    }

    private boolean hasDraw = false;

    class PinnedViewHolder {
        TextView tvDefault;
        TextView tvSales;
        LinearLayout LPrice;
        TextView tvUp, tvDown;
        TextView tvPopular;

        public PinnedViewHolder(View view) {
            tvDefault = (TextView) view.findViewById(R.id.tvDefault);
            tvSales = (TextView) view.findViewById(R.id.tvSales);
            LPrice = (LinearLayout) view.findViewById(R.id.LPrice);
            tvUp = (TextView) view.findViewById(R.id.tvUp);
            tvDown = (TextView) view.findViewById(R.id.tvDown);
            tvPopular = (TextView) view.findViewById(R.id.tvPopular);

            final View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentSort = 3;
                    switch (v.getId()) {
                        case R.id.tvDefault:
                            currentIndex = 0;
                            currentSort = 3;
                            currentOrder = -1;
                            break;
                        case R.id.tvSales:
                            currentIndex = 1;
                            currentSort = 1;
                            currentOrder = -1;
                            break;
                        case R.id.LPrice:
                            currentIndex = 2;
                            currentSort = 2;
                            currentOrder = currentOrder == 1 ? 2 : 1;
                            break;
                        case R.id.tvPopular:
                            currentSort = 4;
                            currentIndex = 3;
                            currentOrder = -1;
                            break;
                    }
                    if (groupClickListener != null) {
                        groupClickListener.onFilterClick(currentSort, currentOrder);
                    }
                    notifyDataSetChanged();
                }
            };

            tvDefault.setOnClickListener(listener);
            tvSales.setOnClickListener(listener);
            LPrice.setOnClickListener(listener);
            tvPopular.setOnClickListener(listener);

            BaseFunc.setFont(tvUp);
            BaseFunc.setFont(tvDown);
            view.setTag(this);
        }
    }

    class ItemViewHolder {
        View viewGoodsOne;
        View viewGoodsTwo;

        public ItemViewHolder(View view) {
            viewGoodsOne = view.findViewById(R.id.viewGoodsOne);
            viewGoodsTwo = view.findViewById(R.id.viewGoodsTwo);
            view.setTag(this);
        }
    }

    private GroupClickListener groupClickListener;

    public void setListener(GroupClickListener listener) {
        this.groupClickListener = listener;
    }

    public interface GroupClickListener {
        void onFilterClick(int sort, int order);

        void onCollect(String brand_id, boolean hasCollected);
    }
}
