package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fenhong.microbuyer.base.model.MicroshopInfo;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.base.model.ShopGoods;
import com.fanglin.fenhong.microbuyer.microshop.EditMicroshopInfoActivity;
import com.fanglin.fhui.CircleImageView;
import com.hb.views.PinnedSectionListView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/3-下午4:02.
 * 功能描述:新版微店首页带沾粘效果适配器
 */
public class PinnedMicroshopAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_PINNED = 1;
    private static final int TYPE_ITEM = 2;

    private Context mContext;
    private List<ShopGoods> goodsList;
    private DecimalFormat priceFmt, commissionFmt;
    MicroshopInfo microshopInfo;
    private String showName;

    public PinnedMicroshopAdapter(Context context) {
        this.mContext = context;
        priceFmt = new DecimalFormat("¥#0.00");
        commissionFmt = new DecimalFormat("奖金 ¥#0.00");
    }

    public void setList(List<ShopGoods> goodses) {
        this.goodsList = goodses;
        notifyDataSetChanged();
    }

    public void setMicroShop(MicroshopInfo microshopInfo) {
        this.microshopInfo = microshopInfo;
        showName = handleName();
    }

    public void addList(List<ShopGoods> goodses) {
        if (goodses == null) return;
        if (this.goodsList != null && this.goodsList.size() > 0) {
            this.goodsList.addAll(goodses);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == 1) {
            return TYPE_PINNED;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == TYPE_PINNED;
    }

    @Override
    public int getCount() {
        if (goodsList == null) return 2;
        return goodsList.size() + 2;
    }

    @Override
    public ShopGoods getItem(int position) {
        return goodsList.get(position - 2);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (convertView == null) {
            if (viewType == TYPE_HEADER) {
                convertView = View.inflate(mContext, R.layout.item_microshop_header, null);
                new HeaderViewHolder(convertView);
            } else if (viewType == TYPE_PINNED) {
                convertView = View.inflate(mContext, R.layout.item_microshop_pinned_section, null);
                new PinnedViewHolder(convertView);
            } else {
                convertView = View.inflate(mContext, R.layout.item_microshop_item, null);
                new ItemViewHolder(convertView);
            }
        }

        if (viewType == TYPE_HEADER) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) convertView.getTag();
            headerViewHolder.tvName.setText(showName);
            headerViewHolder.tvDesc.setText(microshopInfo != null ? microshopInfo.getShop_scope() : "");
            new FHImageViewUtil(headerViewHolder.circleImageView).setImageURI(microshopInfo != null ? microshopInfo.getShop_logo() : null, FHImageViewUtil.SHOWTYPE.AVATAR);

            /**
             * 点击进入编辑店铺信息
             */
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.gotoActivity(mContext, EditMicroshopInfoActivity.class, null);
                }
            });
        } else if (viewType == TYPE_PINNED) {
            PinnedViewHolder pinnedViewHolder = (PinnedViewHolder) convertView.getTag();
            pinnedViewHolder.tvName.setText(mContext.getString(R.string.goods_i_shared));
        } else {
            ItemViewHolder itemViewHolder = (ItemViewHolder) convertView.getTag();
            new FHImageViewUtil(itemViewHolder.ivGoodsImage).setImageURI(getItem(position).getGoods_image(), FHImageViewUtil.SHOWTYPE.DEFAULT);
            new FHImageViewUtil(itemViewHolder.ivFlag).setImageURI(getItem(position).getNation_flag(), FHImageViewUtil.SHOWTYPE.DEFAULT);
            String fmtNationAndFlag = String.format(mContext.getString(R.string.nation_and_flag), getItem(position).getNation_name(), getItem(position).getGoods_name());
            itemViewHolder.tvTitle.setText(BaseFunc.fromHtml(fmtNationAndFlag));
            itemViewHolder.tvPrice.setText(priceFmt.format(getItem(position).getGoods_price()));
            itemViewHolder.tvCommission.setText(commissionFmt.format(getItem(position).getGoods_commission()));

            itemViewHolder.tvWechat.setSelected(getItem(position).isSelected(ShopGoods.GSHARE.WECHAT));
            itemViewHolder.tvMoments.setSelected(getItem(position).isSelected(ShopGoods.GSHARE.MOMENTS));
            itemViewHolder.tvQzone.setSelected(getItem(position).isSelected(ShopGoods.GSHARE.QZONE));
            itemViewHolder.tvQq.setSelected(getItem(position).isSelected(ShopGoods.GSHARE.QQ));
            itemViewHolder.tvWeibo.setSelected(getItem(position).isSelected(ShopGoods.GSHARE.WEIBO));

            itemViewHolder.tvStorage.setText(String.format(mContext.getString(R.string.fmt_storage), getItem(position).getGoods_storage()));
            itemViewHolder.tvClick.setText(String.format(mContext.getString(R.string.fmt_click), getItem(position).getGoods_click()));
            itemViewHolder.tvConllect.setText(String.format(mContext.getString(R.string.fmt_conllect), getItem(position).getGoods_collect()));
            itemViewHolder.tvSales.setText(String.format(mContext.getString(R.string.fmt_sales), getItem(position).getGoods_salenum()));

            /**
             * 点击进入商品详情
             */
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.gotoGoodsDetail(mContext, getItem(position).getGoods_id(), null, null);
                }
            });

            /**
             * 分享按钮
             * 分享标题:商品名  分享内容:分享描述(为空则为商品名)
             * 只能分享单张图片
             */
            itemViewHolder.tvShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareData shareData = new ShareData();
                    String goodsName = getItem(position).getGoods_name();
                    String goodsDesc = getItem(position).getGoods_desc();
                    shareData.title = goodsName;
                    shareData.content = !TextUtils.isEmpty(goodsDesc) ? goodsDesc : goodsName;
                    shareData.url = String.format(BaseVar.SHARE_GOODS_DTL, getItem(position).getGoods_id());
                    shareData.imgs = getItem(position).getGoods_image();
                    ShareData.fhShare((BaseFragmentActivity) mContext, shareData, null);
                }
            });
        }
        return convertView;
    }

    class ItemViewHolder {
        ImageView ivGoodsImage, ivFlag;
        TextView tvTitle;
        TextView tvPrice, tvCommission;
        TextView tvWechat, tvMoments, tvQzone, tvQq, tvWeibo, tvShare;
        TextView tvStorage, tvClick, tvConllect, tvSales;

        public ItemViewHolder(View view) {
            ivGoodsImage = (ImageView) view.findViewById(R.id.ivGoodsImage);
            ivFlag = (ImageView) view.findViewById(R.id.ivFlag);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            tvCommission = (TextView) view.findViewById(R.id.tvCommission);
            tvWechat = (TextView) view.findViewById(R.id.tvWechat);
            tvMoments = (TextView) view.findViewById(R.id.tvMoments);
            tvQzone = (TextView) view.findViewById(R.id.tvQzone);
            tvQq = (TextView) view.findViewById(R.id.tvQq);
            tvWeibo = (TextView) view.findViewById(R.id.tvWeibo);
            tvShare = (TextView) view.findViewById(R.id.tvShare);
            tvStorage = (TextView) view.findViewById(R.id.tvStorage);
            tvClick = (TextView) view.findViewById(R.id.tvClick);
            tvConllect = (TextView) view.findViewById(R.id.tvConllect);
            tvSales = (TextView) view.findViewById(R.id.tvSales);

            BaseFunc.setFont(tvWechat);
            BaseFunc.setFont(tvMoments);
            BaseFunc.setFont(tvQzone);
            BaseFunc.setFont(tvQq);
            BaseFunc.setFont(tvWeibo);

            view.setTag(this);
        }
    }

    class PinnedViewHolder {
        TextView tvName;

        public PinnedViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.tvName);
            view.setTag(this);
        }
    }

    class HeaderViewHolder {
        CircleImageView circleImageView;
        TextView tvName, tvDesc;

        public HeaderViewHolder(View view) {
            circleImageView = (CircleImageView) view.findViewById(R.id.circleImageView);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvDesc = (TextView) view.findViewById(R.id.tvDesc);
            view.setTag(this);
        }
    }

    private String handleName() {
        String res = microshopInfo != null ? microshopInfo.getShop_name() : "";
        if (TextUtils.isEmpty(res)) {
            BaseFragmentActivity activity = (BaseFragmentActivity) mContext;
            if (activity != null) {
                Member member = activity.member;
                if (member != null) {
                    if (!TextUtils.isEmpty(member.member_nickname)) {
                        res = member.member_nickname;
                    } else {
                        res = member.member_name;
                    }
                }
            }
        }
        return res;
    }
}
