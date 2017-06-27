package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.ShopClass;
import com.fanglin.fenhong.microbuyer.base.model.ShopSettings;
import com.fanglin.fenhong.microbuyer.microshop.FancyShopListActivity;
import com.google.gson.Gson;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import java.util.List;

/**
 * 作者： Created by Plucky on 2016/1/25 10:39.
 * <p/>
 * 功能描述 发现页面Adapter
 */
public class SectionFindAdapter extends SectionedRecyclerViewAdapter<SectionFindAdapter.HeaderViewHolder, SectionFindAdapter.ItemViewHolder, RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER_ADDR = 0;
    public static final int TYPE_HEADER_NORMAL = 1;

    private List<ShopClass> list;
    private Context mContext;
    FrameLayout.LayoutParams imageParams;

    public SectionFindAdapter(Context c) {
        this.mContext = c;
        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        int h = 300 * screenWidth / 720;
        imageParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
    }

    public void setList(List<ShopClass> list) {
        this.list = list;
    }

    private ShopSettings shopSettings;

    public void setShopSettings(ShopSettings shopSettings) {
        this.shopSettings = shopSettings;
        notifyDataSetChanged();
    }

    /**
     * 分区内容个数
     *
     * @param section 分区
     * @return int
     */
    @Override
    protected int getItemCountForSection(int section) {
        if (section == 0) {
            return 0;
        } else {
            return getSubClassCountOfSection(section);
        }
    }

    /**
     * 分区数
     *
     * @return int
     */
    @Override
    protected int getSectionCount() {
        return 1 + getListCount();
    }

    public int getListCount() {
        if (list == null) return 0;
        return list.size();
    }

    /**
     * 获取店铺子分类个数
     *
     * @param section 分区
     * @return int
     */
    private int getSubClassCountOfSection(int section) {
        if (list != null) {
            List<ShopClass> subclass = getHeaderEntity(section).subclass;
            if (subclass != null) {
                return subclass.size();
            }
        }
        return 0;
    }

    /**
     * 获取店铺分类
     *
     * @param section 分区
     * @return ShopClass
     */
    private ShopClass getHeaderEntity(int section) {
        return list.get(section - 1);
    }

    /**
     * 获取店铺子分类
     *
     * @param section  分区
     * @param position 位置
     * @return ShopClass
     */
    private ShopClass getItemEntity(int section, int position) {
        return getHeaderEntity(section).subclass.get(position);
    }

    @Override
    protected int getSectionHeaderViewType(int section) {
        if (section == 0) return TYPE_HEADER_ADDR;
        return TYPE_HEADER_NORMAL;
    }

    /**
     * 如果自定义了头部的ViewType要记得实现该方法
     *
     * @param viewType int
     * @return boolean
     */
    @Override
    protected boolean isSectionHeaderViewType(int viewType) {
        return viewType == TYPE_HEADER_ADDR || viewType == TYPE_HEADER_NORMAL;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_HEADER_ADDR) {
            view = View.inflate(mContext, R.layout.layout_find_header, null);
        } else {
            view = View.inflate(mContext, R.layout.item_section_label, null);
        }

        return new HeaderViewHolder(view, viewType);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_mshop_subcls, null);
        return new ItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(HeaderViewHolder holder, int section) {
        int viewType = getSectionHeaderViewType(section);
        if (viewType == TYPE_HEADER_ADDR) {
            //暂时没有数据处理
            if (shopSettings != null) {
                new FHImageViewUtil(holder.imageTopBg).setImageURI(shopSettings.getShop_banner_pic(), FHImageViewUtil.SHOWTYPE.BGFIND);
                holder.imageTopBg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (BaseFunc.isValidUrl(shopSettings.getShop_banner_url())) {
                            BaseFunc.urlClick(mContext,shopSettings.getShop_banner_url());
                        }
                    }
                });
            }
        } else {
            holder.tv_header.setText(getHeaderEntity(section).sc_name);
        }
    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(ItemViewHolder holder, int section, int position) {
        final ShopClass sc = getItemEntity(section, position);
        holder.tv_title.setText(sc.sc_name);
        holder.tv_subtitle.setText(sc.sc_name);

        new FHImageViewUtil(holder.sdv).setImageURI(sc.sc_pic, FHImageViewUtil.SHOWTYPE.DEFAULT);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFunc.gotoActivity(mContext, FancyShopListActivity.class, new Gson().toJson(sc));
            }
        });
    }

    /**
     * 分区头部视图
     */
    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_header;
        public LinearLayout LHeaderTop;
        ImageView imageTopBg;

        public HeaderViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == TYPE_HEADER_ADDR) {
                imageTopBg = (ImageView) itemView.findViewById(R.id.imageTopBg);
                imageTopBg.setLayoutParams(imageParams);
            } else {
                tv_header = (TextView) itemView.findViewById(R.id.tv_header);
                LHeaderTop = (LinearLayout) itemView.findViewById(R.id.LHeaderTop);
            }
        }


    }

    /**
     * 分区Row
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView sdv;
        public TextView tv_title;
        public TextView tv_subtitle;

        public ItemViewHolder(View itemView) {
            super(itemView);
            sdv = (ImageView) itemView.findViewById(R.id.sdv);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_subtitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
        }
    }
}
