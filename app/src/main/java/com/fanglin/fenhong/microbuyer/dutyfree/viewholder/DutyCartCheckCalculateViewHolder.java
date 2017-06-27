package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyCartCheck;
import com.fanglin.fenhong.microbuyer.common.FHBrowserActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.LayoutFreightDesc;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/16-下午4:27.
 * 功能描述: 极速免税店 核对订单 订单核算
 */
public class DutyCartCheckCalculateViewHolder extends RecyclerView.ViewHolder {

    TextView tvMoney, tvDiscountDesc, tvDiscountMoney, tvTax, tvEdit;
    public ImageView ivCheck;
    TextView tvConfirm;
    LinearLayout LPromotion;
    LinearLayout LImport;
    TextView tvImportLabel, tvImportDesc, tvImportMoney;

    //余额使用
    LinearLayout LBalance, LBalanceClick;
    TextView tvBalanceLabel, tvBalanceDesc, tvBalanceMoney;
    ImageView ivBalanceCheck;
    View vBalance;

    LinearLayout LFreight;
    TextView tvFreight, tvWeight;
    ImageView ivFreight;

    //限时优惠
    LinearLayout LItem;
    TextView tvItemLabel, tvItemDesc, tvItemMoney;

    private LayoutFreightDesc layoutFreightDesc;
  //  LinearLayout llRelevance, llAddressShow;
    private static Context mContext;

    public DutyCartCheckCalculateViewHolder(View itemView) {
        super(itemView);
        tvMoney = (TextView) itemView.findViewById(R.id.tvMoney);
        tvDiscountDesc = (TextView) itemView.findViewById(R.id.tvDiscountDesc);
        tvDiscountMoney = (TextView) itemView.findViewById(R.id.tvDiscountMoney);
        tvTax = (TextView) itemView.findViewById(R.id.tvTax);
        ivCheck = (ImageView) itemView.findViewById(R.id.ivCheck);
        tvConfirm = (TextView) itemView.findViewById(R.id.tvConfirm);
        LPromotion = (LinearLayout) itemView.findViewById(R.id.LPromotion);

        LImport = (LinearLayout) itemView.findViewById(R.id.LImport);
        tvImportLabel = (TextView) itemView.findViewById(R.id.tvImportLabel);
        tvImportDesc = (TextView) itemView.findViewById(R.id.tvImportDesc);
        tvImportMoney = (TextView) itemView.findViewById(R.id.tvImportMoney);

        LBalance = (LinearLayout) itemView.findViewById(R.id.LBalance);
        LBalanceClick = (LinearLayout) itemView.findViewById(R.id.LBalanceClick);
        tvBalanceLabel = (TextView) itemView.findViewById(R.id.tvBalanceLabel);
        tvBalanceDesc = (TextView) itemView.findViewById(R.id.tvBalanceDesc);
        tvBalanceMoney = (TextView) itemView.findViewById(R.id.tvBalanceMoney);
        ivBalanceCheck = (ImageView) itemView.findViewById(R.id.ivBalanceCheck);
        vBalance = itemView.findViewById(R.id.vBalance);

        LFreight = (LinearLayout) itemView.findViewById(R.id.LFreight);
        tvFreight = (TextView) itemView.findViewById(R.id.tvFreight);
        tvWeight = (TextView) itemView.findViewById(R.id.tvWeight);
        ivFreight = (ImageView) itemView.findViewById(R.id.ivFreight);

//        //Obl-change
//        llRelevance = (LinearLayout) itemView.findViewById(R.id.llRelevance);
//        llAddressShow = (LinearLayout) itemView.findViewById(R.id.llAddressShow);
        tvEdit = (TextView) itemView.findViewById(R.id.tvEdit);

        //限时优惠
        LItem = (LinearLayout) itemView.findViewById(R.id.LItem);
        tvItemLabel = (TextView) itemView.findViewById(R.id.tvItemLabel);
        tvItemDesc = (TextView) itemView.findViewById(R.id.tvItemDesc);
        tvItemMoney = (TextView) itemView.findViewById(R.id.tvItemMoney);

        LBalanceClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calculateListener != null) {
                    calculateListener.onUserBalanceClick();
                }
            }
        });

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

    public static DutyCartCheckCalculateViewHolder getHolder(Context context) {
        mContext = context;
        View view = View.inflate(context, R.layout.item_dutycartcheck_calculate, null);
        return new DutyCartCheckCalculateViewHolder(view);
    }

    public void refreshView(final DutyCartCheck cartCheck) {
        if (cartCheck != null) {
            tvMoney.setText(cartCheck.getGoodsAmountDesc());
            tvTax.setText(cartCheck.getTaxes());
            tvConfirm.setText(cartCheck.getAgreement());
            ivCheck.setSelected(cartCheck.isHasChecked());
            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.gotoActivity(v.getContext(), FHBrowserActivity.class, BaseVar.URL_DUTYFREE_TRUST);
                }
            });
//            if (event == null) {
//                //Obl-change
//                llRelevance.setVisibility(View.VISIBLE);
//                llRelevance.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        BaseFunc.gotoActivity(mContext, DutyAddrListActivity.class, "");
//                        //   BaseFunc.gotoActivity(mContext, DutyfreeBindAddrActivity.class, product.getCartId());
//                    }
//                });
//                llAddressShow.setVisibility(View.INVISIBLE);
//            } else {
//                llAddressShow.setVisibility(View.VISIBLE);
//                llRelevance.setVisibility(View.INVISIBLE);
//
//            }

            if (cartCheck.showPromotion()) {
                tvDiscountDesc.setText(cartCheck.getGoodsPromotionDesc());
                tvDiscountMoney.setText(cartCheck.getGoodsProAmount());
                LPromotion.setVisibility(View.VISIBLE);
            } else {
                LPromotion.setVisibility(View.GONE);
            }

            if (cartCheck.showImport()) {
                LImport.setVisibility(View.VISIBLE);
                tvImportLabel.setText(cartCheck.getImportLabel());
                tvImportDesc.setText(cartCheck.getImportDesc());
                tvImportMoney.setText(cartCheck.getImportMoney());
            } else {
                LImport.setVisibility(View.GONE);
            }

            if (cartCheck.showBalance()) {
                LBalance.setVisibility(View.VISIBLE);
                vBalance.setVisibility(View.VISIBLE);
                tvBalanceLabel.setText(cartCheck.getBalanceLabel());
                tvBalanceDesc.setText(cartCheck.getBalanceDesc());
                tvBalanceMoney.setText(cartCheck.getBalanceMoney());
                ivBalanceCheck.setSelected(cartCheck.getIs_selected() == 1);
            } else {
                LBalance.setVisibility(View.GONE);
                vBalance.setVisibility(View.GONE);
            }

            // 运费相关
            tvFreight.setText(cartCheck.getFreightDescHtml());
            tvWeight.setText(cartCheck.getFreightWeightHtml());
            if (cartCheck.showFreightIntro()) {
                ivFreight.setVisibility(View.VISIBLE);
                layoutFreightDesc.setTitle(cartCheck.getGoodsFreightTitle());
                layoutFreightDesc.refreshView(cartCheck.getFreightDescHtml(), cartCheck.getFreightIntroHtml());
            } else {
                ivFreight.setVisibility(View.GONE);
            }

            if (cartCheck.isItemVisible()) {
                LItem.setVisibility(View.VISIBLE);
                tvItemLabel.setText(cartCheck.getItemLabel());
                tvItemDesc.setText(cartCheck.getItemDesc());
                tvItemMoney.setText(cartCheck.getItemMoney());
            } else {
                LItem.setVisibility(View.GONE);
            }
        }
    }

    private DutyCartCheckCalculateListener calculateListener;

    public void setCalculateListener(DutyCartCheckCalculateListener calculateListener) {
        this.calculateListener = calculateListener;
    }

    public interface DutyCartCheckCalculateListener {
        void onUserBalanceClick();

    }

}
