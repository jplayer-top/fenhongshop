package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyCartProduct;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/16-下午4:03.
 * 功能描述: 极速免税店 核对订单 商品
 */
public class DutyCartCheckHeaderViewHolder extends RecyclerView.ViewHolder {
    ImageView ivImage;
    TextView tvName, tvPrice, tvNum, tvActivityDesc;
    public View vLine;
    public TextView tvAddr;

    public DutyCartCheckHeaderViewHolder(View itemView) {
        super(itemView);
        ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
        tvNum = (TextView) itemView.findViewById(R.id.tvNum);
        tvAddr = (TextView) itemView.findViewById(R.id.tvAddr);
        vLine = itemView.findViewById(R.id.vLine);
        tvActivityDesc = (TextView) itemView.findViewById(R.id.tvActivityDesc);
    }


    public static DutyCartCheckHeaderViewHolder getHolder(Context context) {
        View view = View.inflate(context, R.layout.item_dutycartcheck_header, null);
        return new DutyCartCheckHeaderViewHolder(view);
    }

    public static DutyCartCheckHeaderViewHolder getHolder(Context context, int lineWidthResId) {
        View view = View.inflate(context, R.layout.item_dutycartcheck_header, null);
        DutyCartCheckHeaderViewHolder holder = new DutyCartCheckHeaderViewHolder(view);
        int height = context.getResources().getDimensionPixelOffset(lineWidthResId);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        holder.vLine.setLayoutParams(params);
        return holder;
    }

    public void refreshView(DutyCartProduct product) {
        if (product != null) {
            new FHImageViewUtil(ivImage).setImageURI(product.getProductImgUrl(), FHImageViewUtil.SHOWTYPE.DEFAULT);
            tvName.setText(product.getProductName());
            tvPrice.setText(product.getPriceRmb4Show());
            tvNum.setText(product.getProNumDesc());

            String activityDesc = product.getActivityDesc();
            if (!TextUtils.isEmpty(activityDesc)) {
                tvActivityDesc.setVisibility(View.VISIBLE);
                tvActivityDesc.setText(activityDesc);
            } else {
                tvActivityDesc.setVisibility(View.GONE);
            }
        }
    }
}
