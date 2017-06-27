package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.ActivityGoods;
import com.fanglin.fenhong.microbuyer.base.model.ActivityList;
import com.fanglin.fenhong.microbuyer.common.ActListDtlActivity;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/3/29-下午10:55.
 * 功能描述: 首页主题活动横向滑动商品适配器
 */
public class HomeNavRecycleGoodsAdapter extends RecyclerView.Adapter<HomeNavRecycleGoodsAdapter.ViewHolder> {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_MORE = 1;

    private Context mContext;
    private List<ActivityGoods> list;
    private ActivityList activityList;

    public HomeNavRecycleGoodsAdapter(Context context) {
        this.mContext = context;
    }

    public void setActivityList(ActivityList activityList) {
        this.activityList = activityList;
    }

    public void setList(List<ActivityGoods> list) {
        this.list = list;
    }

    public ActivityGoods getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_MORE;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView;
        if (viewType == TYPE_NORMAL) {
            convertView = View.inflate(mContext, R.layout.item_homenav_recycle_goods, null);
        } else {
            convertView = View.inflate(mContext, R.layout.item_homenav_recycle_lookmore, null);
        }
        return new ViewHolder(convertView, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_NORMAL) {
            new FHImageViewUtil(holder.ivGoodsImage).setImageURI(getItem(position).goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);
            holder.tvGoodsName.setText(getItem(position).goods_name);
            holder.tvGoodsPrice.setText(getItem(position).getGoodspriceDesc());
            holder.tvMarketPrice.setText(getItem(position).getMarketpriceDesc(true));
            holder.tvMarketPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.gotoGoodsDetail(mContext, getItem(position).goods_id, activityList == null ? null : activityList.resource_tags,null);
                }
            });
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activityList != null) {
                        if (BaseFunc.isValidUrl(activityList.activity_url)) {
                            BaseFunc.urlClick(mContext, activityList.activity_url);
                        } else {
                            BaseFunc.gotoActivity(mContext, ActListDtlActivity.class, activityList.activity_id);
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int c;
        if (list == null) {
            c = 1;
        } else {
            c = list.size() + 1;
        }
        return c;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivGoodsImage;
        TextView tvGoodsName, tvGoodsPrice, tvMarketPrice;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == TYPE_NORMAL) {
                ivGoodsImage = (ImageView) itemView.findViewById(R.id.ivGoodsImage);
                tvGoodsName = (TextView) itemView.findViewById(R.id.tvGoodsName);
                tvGoodsPrice = (TextView) itemView.findViewById(R.id.tvGoodsPrice);
                tvMarketPrice = (TextView) itemView.findViewById(R.id.tvMarketPrice);
            }
        }
    }
}
