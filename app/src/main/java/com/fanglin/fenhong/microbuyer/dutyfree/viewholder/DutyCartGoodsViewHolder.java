package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.DutyfreeBO;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.baseui.LayoutEditNum;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyCartEditResult;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyCartProduct;
import com.fanglin.fenhong.microbuyer.dutyfree.DFGoodsDetailActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.LayoutEditCartNum;
import com.google.gson.Gson;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/16-上午11:44.
 * 功能描述: 极速免税店购物车商品
 */
public class DutyCartGoodsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, LayoutEditNum.LayoutEditNumListener {

    ImageView ivCheck, ivImage;
    TextView tvName, tvPrice;
    TextView tvMinus, tvPlus;
    TextView tvNum;
    TextView tvDelete;
    TextView tvActivityDesc;//活动标签

    private DutyCartProduct product;
    private Member member;
    LayoutEditCartNum layoutEditCartNum;

    public DutyCartGoodsViewHolder(View itemView, Member member) {
        super(itemView);
        this.member = member;

        ivCheck = (ImageView) itemView.findViewById(R.id.ivCheck);
        ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
        tvMinus = (TextView) itemView.findViewById(R.id.tvMinus);
        tvNum = (TextView) itemView.findViewById(R.id.tvNum);
        tvPlus = (TextView) itemView.findViewById(R.id.tvPlus);

        tvDelete = (TextView) itemView.findViewById(R.id.tvDelete);
        tvActivityDesc = (TextView) itemView.findViewById(R.id.tvActivityDesc);

        ivCheck.setOnClickListener(this);
        tvMinus.setOnClickListener(this);
        tvPlus.setOnClickListener(this);

        layoutEditCartNum = new LayoutEditCartNum(itemView.getContext());
        layoutEditCartNum.setNumListener(this);

        tvNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product == null) return;
                layoutEditCartNum.show(product.getProduct_num());
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goodsListener != null) {
                    goodsListener.onDeleteCartGoods(product);
                }
            }
        });

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product != null) {
                    BaseFunc.gotoActivity(v.getContext(), DFGoodsDetailActivity.class, product.getProductId());
                }
            }
        };

        ivImage.setOnClickListener(clickListener);
        tvName.setOnClickListener(clickListener);
    }

    public static DutyCartGoodsViewHolder getHolder(Context context, Member member) {
        View view = View.inflate(context, R.layout.item_dutycart_goods, null);
        return new DutyCartGoodsViewHolder(view, member);
    }

    public void refreshView(DutyCartProduct product) {
        this.product = product;
        if (product != null) {
            ivCheck.setSelected(product.isSelected());
            new FHImageViewUtil(ivImage).setImageURI(product.getProductImgUrl(), FHImageViewUtil.SHOWTYPE.DEFAULT);
            tvName.setText(product.getProductName());
            tvNum.setText(product.getProNum4Cart());
            tvPrice.setText(product.getPriceRmb4Show());

            String activityDesc = product.getActivityDesc();
            if (!TextUtils.isEmpty(activityDesc)) {
                tvActivityDesc.setVisibility(View.VISIBLE);
                tvActivityDesc.setText(activityDesc);
            } else {
                tvActivityDesc.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onClick(final View v) {
        if (member == null) return;
        int select = product.getIs_selected();
        int num = product.getProduct_num();
        if (v.getId() == R.id.ivCheck) {
            select = product.isSelected() ? 0 : 1;
        }
        if (v.getId() == R.id.tvMinus) {
            num--;
            num = num < 1 ? 1 : num;
        }
        if (v.getId() == R.id.tvPlus) {
            num++;
        }
        final int finalNum = num;
        new DutyfreeBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {
                v.setEnabled(false);
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                v.setEnabled(true);
                if (isSuccess) {
                    if (v.getId() == R.id.ivCheck) {
                        product.setSelected(!product.isSelected());
                    } else {
                        product.setProductNum(finalNum);
                    }
                    DutyCartEditResult result;
                    try {
                        result = new Gson().fromJson(data, DutyCartEditResult.class);
                    } catch (Exception e) {
                        result = null;
                    }

                    if (goodsChangeListener != null) {
                        goodsChangeListener.onCartGoodsChange(result);
                    }
                }
            }
        }).editCart(member, product.getCartId(), num, select);
    }

    private CartGoodsDataChangeListener goodsChangeListener;

    public void setGoodsChangeListener(CartGoodsDataChangeListener goodsChangeListener) {
        this.goodsChangeListener = goodsChangeListener;
    }

    public interface CartGoodsDataChangeListener {
        void onCartGoodsChange(DutyCartEditResult result);
    }

    @Override
    public void onEditNum(final int num) {
        if (member == null || product == null) return;
        int select = product.getIs_selected();
        new DutyfreeBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    product.setProductNum(num);
                    DutyCartEditResult result;
                    try {
                        result = new Gson().fromJson(data, DutyCartEditResult.class);
                    } catch (Exception e) {
                        result = null;
                    }

                    if (goodsChangeListener != null) {
                        goodsChangeListener.onCartGoodsChange(result);
                    }
                }
            }
        }).editCart(member, product.getCartId(), num, select);
    }

    private DutyCartGoodsListener goodsListener;

    public void setGoodsListener(DutyCartGoodsListener goodsListener) {
        this.goodsListener = goodsListener;
    }

    public interface DutyCartGoodsListener {
        void onDeleteCartGoods(DutyCartProduct product);
    }
}
