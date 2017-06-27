package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyOrder;
import com.fanglin.fenhong.microbuyer.dutyfree.LayoutFreightDesc;
import com.fanglin.fenhong.microbuyer.dutyfree.SellerQrcodeActivity;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/11-上午11:36.
 * 功能描述: 极速免税店 订单详情页 底部视图
 */
public class OrderDetailBottomViewHolder extends RecyclerView.ViewHolder {
    TextView tvGoodsMoney, tvDiscountTip, tvDiscountMoney, tvTax;
    TextView tvPlatform, tvSeller;
    LinearLayout LPromotion;
    LinearLayout LImport;
    TextView tvImportLabel, tvImportDesc, tvImportMoney;

    //余额使用
    LinearLayout LBalance;
    TextView tvBalanceLabel, tvBalanceDesc, tvBalanceMoney;

    //运费相关
    LinearLayout LFreight;
    TextView tvFreight, tvWeight;
    ImageView ivFreight;

    //限时优惠
    LinearLayout LItem;
    TextView tvItemLabel, tvItemDesc, tvItemMoney;

    private LayoutFreightDesc layoutFreightDesc;

    public OrderDetailBottomViewHolder(View itemView) {
        super(itemView);
        tvGoodsMoney = (TextView) itemView.findViewById(R.id.tvGoodsMoney);
        tvDiscountTip = (TextView) itemView.findViewById(R.id.tvDiscountTip);
        tvDiscountMoney = (TextView) itemView.findViewById(R.id.tvDiscountMoney);
        tvTax = (TextView) itemView.findViewById(R.id.tvTax);
        tvPlatform = (TextView) itemView.findViewById(R.id.tvPlatform);
        tvSeller = (TextView) itemView.findViewById(R.id.tvSeller);
        LPromotion = (LinearLayout) itemView.findViewById(R.id.LPromotion);

        LImport = (LinearLayout) itemView.findViewById(R.id.LImport);
        tvImportLabel = (TextView) itemView.findViewById(R.id.tvImportLabel);
        tvImportDesc = (TextView) itemView.findViewById(R.id.tvImportDesc);
        tvImportMoney = (TextView) itemView.findViewById(R.id.tvImportMoney);

        LBalance = (LinearLayout) itemView.findViewById(R.id.LBalance);
        tvBalanceLabel = (TextView) itemView.findViewById(R.id.tvBalanceLabel);
        tvBalanceDesc = (TextView) itemView.findViewById(R.id.tvBalanceDesc);
        tvBalanceMoney = (TextView) itemView.findViewById(R.id.tvBalanceMoney);

        LFreight = (LinearLayout) itemView.findViewById(R.id.LFreight);
        tvFreight = (TextView) itemView.findViewById(R.id.tvFreight);
        tvWeight = (TextView) itemView.findViewById(R.id.tvWeight);
        ivFreight = (ImageView) itemView.findViewById(R.id.ivFreight);

        //限时优惠
        LItem = (LinearLayout) itemView.findViewById(R.id.LItem);
        tvItemLabel = (TextView) itemView.findViewById(R.id.tvItemLabel);
        tvItemDesc = (TextView) itemView.findViewById(R.id.tvItemDesc);
        tvItemMoney = (TextView) itemView.findViewById(R.id.tvItemMoney);

        layoutFreightDesc = new LayoutFreightDesc(itemView.getContext());

        LFreight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ivFreight.getVisibility() == View.VISIBLE) {
                    layoutFreightDesc.show();
                }
            }
        });
    }

    public static OrderDetailBottomViewHolder getHolder(Context mContext) {
        View view = View.inflate(mContext, R.layout.item_orderdtl_bottom, null);
        return new OrderDetailBottomViewHolder(view);
    }

    public void refreshView(final DutyOrder order) {
        if (order != null) {
            tvGoodsMoney.setText(order.getOriginalPriceDesc());
            tvFreight.setText(order.getFreightSimpleDesc());
            tvTax.setText(order.getTaxes());
            tvPlatform.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(order.getKefu_qq())) {
                        BaseFunc.startQQChat(v.getContext(), order.getKefu_qq());
                    }
                }
            });
            tvSeller.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(order.getStore_qrcode())) {
                        BaseFunc.gotoActivity(v.getContext(), SellerQrcodeActivity.class, order.getStore_qrcode());
                        //BaseFunc.gotoActivity(v.getContext(), FHBrowserActivity.class, order.getStore_baidusales());
                    } else {
                        BaseFunc.showMsg(v.getContext(), "即将开放");
                    }

                }
            });

            if (order.showPromotion()) {
                tvDiscountTip.setText(order.getGoodsPromotionDesc());
                tvDiscountMoney.setText(order.getGoodsProAmount());
                LPromotion.setVisibility(View.VISIBLE);
            } else {
                LPromotion.setVisibility(View.GONE);
            }

            if (order.showImport()) {
                LImport.setVisibility(View.VISIBLE);
                tvImportLabel.setText(order.getImportLabel());
                tvImportDesc.setText(order.getImportDesc());
                tvImportMoney.setText(order.getImportMoney());
            } else {
                LImport.setVisibility(View.GONE);
            }

            if (order.showBalance()) {
                LBalance.setVisibility(View.VISIBLE);
                tvBalanceLabel.setText(order.getBalanceLabel());
                tvBalanceDesc.setText(order.getBalanceDesc());
                tvBalanceMoney.setText(order.getBalanceMoney());
            } else {
                LBalance.setVisibility(View.GONE);
            }

            // 运费相关
            tvFreight.setText(order.getFreightDescHtml());
            tvWeight.setText(order.getFreightWeightHtml());
            if (order.showFreightIntro()) {
                ivFreight.setVisibility(View.VISIBLE);
                layoutFreightDesc.setTitle(order.getGoodsFreightTitle());
                layoutFreightDesc.refreshView(order.getFreightDescHtml(), order.getFreightIntroHtml());
            } else {
                ivFreight.setVisibility(View.GONE);
            }

            if (order.isItemVisible()) {
                LItem.setVisibility(View.VISIBLE);
                tvItemLabel.setText(order.getItemLabel());
                tvItemDesc.setText(order.getItemDesc());
                tvItemMoney.setText(order.getItemMoney());
            } else {
                LItem.setVisibility(View.GONE);
            }
        }
    }
}
