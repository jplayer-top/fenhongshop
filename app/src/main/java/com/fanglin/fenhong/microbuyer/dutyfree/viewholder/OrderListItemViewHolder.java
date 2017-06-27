package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyOrderGoods;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/10-下午3:54.
 * 功能描述: 极速免税店 商品ViewHolder
 */
public class OrderListItemViewHolder extends RecyclerView.ViewHolder {
    ImageView ivImage;
    TextView tvName, tvPrice, tvNum;

    public OrderListItemViewHolder(View itemView) {
        super(itemView);
        ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
        tvNum = (TextView) itemView.findViewById(R.id.tvNum);
    }

    public static OrderListItemViewHolder getHolder(Context mContext) {
        View view = View.inflate(mContext, R.layout.item_orderlist_goods, null);
        return new OrderListItemViewHolder(view);
    }

    public void refreshView(DutyOrderGoods goods) {
        if (goods != null) {
            new FHImageViewUtil(ivImage).setImageURI(goods.getProductImgUrl(), FHImageViewUtil.SHOWTYPE.DEFAULT);
            tvName.setText(goods.getProductName());
            tvPrice.setText(goods.getPriceRmb4Show());
            tvNum.setText(goods.getProductNumDesc());
        }
    }
}
