package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.LinkGoods;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/16-下午1:06.
 * 功能描述:
 */
public class LinkGoodsAdapter extends BaseAdapter {

    private Context mContext;
    private List<LinkGoods> goodsList;
    private DecimalFormat priceFmt, commissionFmt;
    private String linkedStr;

    public LinkGoodsAdapter(Context mContext) {
        this.mContext = mContext;
        priceFmt = new DecimalFormat("#0.00");
        commissionFmt = new DecimalFormat("奖金¥#0.00");
    }

    public LinkGoodsAdapter(Context mContext, String linkedStr) {
        this.mContext = mContext;
        priceFmt = new DecimalFormat("#0.00");
        commissionFmt = new DecimalFormat("奖金¥#0.00");
        this.linkedStr = linkedStr;
    }

    public void setGoodsList(List<LinkGoods> goodsList) {
        this.goodsList = goodsList;
        notifyDataSetChanged();
    }

    public void addGoodsList(List<LinkGoods> list) {
        if (goodsList != null && goodsList.size() > 0) {
            if (list != null && list.size() > 0) {
                goodsList.addAll(list);
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public int getCount() {
        if (goodsList == null)
            return 0;
        return goodsList.size();
    }

    @Override
    public LinkGoods getItem(int position) {
        return goodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_linkgoods, null);
            new ItemViewHolder(convertView);
        }

        ItemViewHolder holder = (ItemViewHolder) convertView.getTag();
        new FHImageViewUtil(holder.imageView).setImageURI(getItem(position).getGoods_image(), FHImageViewUtil.SHOWTYPE.DEFAULT);
        holder.tvName.setText(getItem(position).getGoods_name());
        holder.tvPrice.setText(priceFmt.format(getItem(position).getGoods_price()));
        holder.tvCommission.setText(commissionFmt.format(getItem(position).getGoods_commission()));

        final boolean isBounded = getItem(position).isBounded();
        if (isBounded) {
            holder.btnAction.setSelected(false);
            if (TextUtils.isEmpty(linkedStr)) {
                holder.btnAction.setText(mContext.getString(R.string.lbl_linked));
            } else {
                holder.btnAction.setText(linkedStr);
            }

        } else {
            holder.btnAction.setSelected(true);
            holder.btnAction.setText(mContext.getString(R.string.lbl_link));
        }

        if (listener != null) {
            holder.btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isBounded) {
                        listener.onBound(position);
                    } else {
                        listener.onUnBound(position);
                    }
                }
            });
        }

        return convertView;
    }

    class ItemViewHolder {
        ImageView imageView;
        TextView tvName;
        TextView tvPrice;
        TextView tvCommission;
        Button btnAction;

        public ItemViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.imageView);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            tvCommission = (TextView) view.findViewById(R.id.tvCommission);
            btnAction = (Button) view.findViewById(R.id.btnAction);
            view.setTag(this);
        }
    }

    private LinkGoodsAdapterListener listener;

    public void setListener(LinkGoodsAdapterListener listener) {
        this.listener = listener;
    }

    public interface LinkGoodsAdapterListener {
        void onBound(int position);

        void onUnBound(int position);
    }
}
