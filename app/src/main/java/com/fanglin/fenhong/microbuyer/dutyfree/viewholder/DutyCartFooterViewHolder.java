package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyCart;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/16-上午11:49.
 * 功能描述: 急速免税店购物车Footer
 */
public class DutyCartFooterViewHolder extends RecyclerView.ViewHolder {
    private TextView tvPrice;
    private LinearLayout LFreight;
    private TextView tvTax, tvFreight;
    private TextView tvDiscountDesc, tvDiscountMoney;
    private LinearLayout LPromotion;
    //进口商品的显示
    private LinearLayout LImport;
    private TextView tvImportLabel, tvImportDesc, tvImportMoney;
    //余额使用
    private LinearLayout LBalance;
    private TextView tvBalanceLabel, tvBalanceDesc, tvBalanceMoney;
    //限时优惠
    private LinearLayout LItem;
    private TextView tvItemLabel, tvItemDesc, tvItemMoney;

    public DutyCartFooterViewHolder(View itemView) {
        super(itemView);
        tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
        tvTax = (TextView) itemView.findViewById(R.id.tvTax);
        LFreight = (LinearLayout) itemView.findViewById(R.id.LFreight);
        tvFreight = (TextView) itemView.findViewById(R.id.tvFreight);
        tvDiscountDesc = (TextView) itemView.findViewById(R.id.tvDiscountDesc);
        tvDiscountMoney = (TextView) itemView.findViewById(R.id.tvDiscountMoney);
        LPromotion = (LinearLayout) itemView.findViewById(R.id.LPromotion);
        //进口商品显示
        LImport = (LinearLayout) itemView.findViewById(R.id.LImport);
        tvImportLabel = (TextView) itemView.findViewById(R.id.tvImportLabel);
        tvImportDesc = (TextView) itemView.findViewById(R.id.tvImportDesc);
        tvImportMoney = (TextView) itemView.findViewById(R.id.tvImportMoney);

        LBalance = (LinearLayout) itemView.findViewById(R.id.LBalance);
        tvBalanceLabel = (TextView) itemView.findViewById(R.id.tvBalanceLabel);
        tvBalanceDesc = (TextView) itemView.findViewById(R.id.tvBalanceDesc);
        tvBalanceMoney = (TextView) itemView.findViewById(R.id.tvBalanceMoney);

        LItem = (LinearLayout) itemView.findViewById(R.id.LItem);
        tvItemLabel = (TextView) itemView.findViewById(R.id.tvItemLabel);
        tvItemDesc = (TextView) itemView.findViewById(R.id.tvItemDesc);
        tvItemMoney = (TextView) itemView.findViewById(R.id.tvItemMoney);
    }

    public static DutyCartFooterViewHolder getHolder(Context context) {
        View view = View.inflate(context, R.layout.item_dutycart_footer, null);
        return new DutyCartFooterViewHolder(view);
    }

    public void refreshView(DutyCart cart) {
        if (cart != null) {
            tvPrice.setText(cart.getGoodsAllPrice());
            tvTax.setText(cart.getTaxes());

            if (TextUtils.isEmpty(cart.getFreight())) {
                LFreight.setVisibility(View.GONE);
            } else {
                LFreight.setVisibility(View.VISIBLE);
                tvFreight.setText(cart.getFreight());
            }

            //VIP 活动显示
            if (cart.showPromotion()) {
                LPromotion.setVisibility(View.VISIBLE);
                tvDiscountDesc.setText(cart.getGoodsPromotionDesc());
                tvDiscountMoney.setText(cart.getGoodsPromotionAmount());
            } else {
                LPromotion.setVisibility(View.GONE);
            }

            if (cart.showImport()) {
                LImport.setVisibility(View.VISIBLE);
                tvImportLabel.setText(cart.getImportLabel());
                tvImportDesc.setText(cart.getImportDesc());
                tvImportMoney.setText(cart.getImportMoney());
            } else {
                LImport.setVisibility(View.GONE);
            }

            if (cart.showBalance()) {
                LBalance.setVisibility(View.VISIBLE);
                tvBalanceLabel.setText(cart.getBalanceLabel());
                tvBalanceDesc.setText(cart.getBalanceDesc());
                tvBalanceMoney.setText(cart.getBalanceMoney());
            } else {
                LBalance.setVisibility(View.GONE);
            }

            if (cart.isItemVisible()) {
                LItem.setVisibility(View.VISIBLE);
                tvItemLabel.setText(cart.getItemLabel());
                tvItemDesc.setText(cart.getItemDesc());
                tvItemMoney.setText(cart.getItemMoney());
            } else {
                LItem.setVisibility(View.GONE);
            }
        }
    }
}
