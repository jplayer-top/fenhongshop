package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyOrderGoods;
import com.fanglin.fenhong.microbuyer.dutyfree.DFGoodsDetailActivity;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/11-上午11:46.
 * 功能描述: 极速免税店 订单详情 商品
 */
public class OrderDetailGoodsViewHolder extends RecyclerView.ViewHolder {
    ImageView ivImage;
    TextView tvName, tvPrice, tvNum;
    TextView tvActivityDesc;

    private String goodsId;

    public OrderDetailGoodsViewHolder(View itemView) {
        super(itemView);
        ivImage = (ImageView) itemView.findViewById(R.id.ivImage);

        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
        tvNum = (TextView) itemView.findViewById(R.id.tvNum);

        tvActivityDesc = (TextView) itemView.findViewById(R.id.tvActivityDesc);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFunc.gotoActivity(v.getContext(), DFGoodsDetailActivity.class, goodsId);
            }
        });
    }

    public static OrderDetailGoodsViewHolder getHolder(Context mContext) {
        View view = View.inflate(mContext, R.layout.item_orderdtl_header, null);
        return new OrderDetailGoodsViewHolder(view);
    }

    public void refreshView(DutyOrderGoods goods) {
        if (goods != null) {
            goodsId = goods.getProductId();
            new FHImageViewUtil(ivImage).setImageURI(goods.getProductImgUrl(), FHImageViewUtil.SHOWTYPE.DEFAULT);
            tvName.setText(goods.getProductName());
            tvPrice.setText(goods.getPriceRmb4Show());
            tvNum.setText(goods.getProductNumDesc());

            String activityDesc = goods.getActivityDesc();
            if (!TextUtils.isEmpty(activityDesc)) {
                tvActivityDesc.setVisibility(View.VISIBLE);
                tvActivityDesc.setText(activityDesc);
            } else {
                tvActivityDesc.setVisibility(View.GONE);
            }
        }
    }
}
