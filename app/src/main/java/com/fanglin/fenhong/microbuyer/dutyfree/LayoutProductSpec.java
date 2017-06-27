package com.fanglin.fenhong.microbuyer.dutyfree;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.ProductDetail;
import com.fanglin.fhui.FHDialog;

/**
 * 极速免税的规格选择
 * Created by Plucky on 2016/11/10.
 */
public class LayoutProductSpec implements View.OnClickListener {
    FHDialog dlg;
    ImageView ivImage;
    TextView tvPrice, tvSpec, tvClose;
    TextView tvLeft, tvRight;
    RecyclerView recyclerView;
    TextView tvMinus, tvBuyNum, tvPlus;

    int buyNum = 1;
    ProductDetail detail;

    public LayoutProductSpec(Context mContext) {
        View v = View.inflate(mContext, R.layout.layout_product_spec, null);
        v.findViewById(R.id.tvClose).setOnClickListener(this);

        v.findViewById(R.id.tv5).setOnClickListener(this);
        v.findViewById(R.id.tv10).setOnClickListener(this);
        v.findViewById(R.id.tv20).setOnClickListener(this);
        v.findViewById(R.id.tv30).setOnClickListener(this);
        v.findViewById(R.id.tv50).setOnClickListener(this);
        v.findViewById(R.id.tv100).setOnClickListener(this);

        int h = BaseFunc.getDisplayMetrics(mContext).heightPixels * 4 / 7;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
        v.setLayoutParams(params);
        ivImage = (ImageView) v.findViewById(R.id.ivImage);
        tvPrice = (TextView) v.findViewById(R.id.tvPrice);
        tvSpec = (TextView) v.findViewById(R.id.tvSpec);
        tvClose = (TextView) v.findViewById(R.id.tvClose);
        tvLeft = (TextView) v.findViewById(R.id.tvLeft);
        tvRight = (TextView) v.findViewById(R.id.tvRight);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        tvMinus = (TextView) v.findViewById(R.id.tvMinus);
        tvBuyNum = (TextView) v.findViewById(R.id.tvBuyNum);
        tvPlus = (TextView) v.findViewById(R.id.tvPlus);

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvMinus.setOnClickListener(this);
        tvPlus.setOnClickListener(this);

        tvSpec.setVisibility(View.INVISIBLE);
        BaseFunc.setFont(tvClose);

        dlg = new FHDialog(mContext);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setBotView(v, 0);
    }

    public void refreshView(ProductDetail detail) {
        this.detail = detail;
        if (detail == null) return;

        if (detail.showItem()) {
            tvPrice.setText(detail.getItemPrice());
        } else {
            tvPrice.setText(detail.getPriceRmb4Show());
        }

        new FHImageViewUtil(ivImage).setImageURI(detail.getProductImgUrl(), FHImageViewUtil.SHOWTYPE.DEFAULT);
    }

    /**
     * @param index 0 购物车 1 立即购买 2 两个按钮都显示
     */
    public void show(int index) {
        if (detail == null) return;
        if (index == 0) {
            tvLeft.setVisibility(View.VISIBLE);
            tvRight.setVisibility(View.GONE);
        } else if (index == 1) {
            tvLeft.setVisibility(View.GONE);
            tvRight.setVisibility(View.VISIBLE);
        } else {
            tvLeft.setVisibility(View.VISIBLE);
            tvRight.setVisibility(View.VISIBLE);
        }
        dlg.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvClose:
                dlg.dismiss();
                break;
            case R.id.tvLeft:
                if (dutySpecCallback != null) {
                    dutySpecCallback.onCart(detail.getProductId(), buyNum);
                }
                break;
            case R.id.tvRight:
                if (dutySpecCallback != null) {
                    dutySpecCallback.onBuy(detail.getProductId(), buyNum);
                }
                break;
            case R.id.tvMinus:
                buyNum--;
                buyNum = buyNum < 1 ? 1 : buyNum;
                tvBuyNum.setText(String.valueOf(buyNum));
                break;
            case R.id.tvPlus:
                buyNum++;
                tvBuyNum.setText(String.valueOf(buyNum));
                break;
            case R.id.tv5:
                buyNum = 5;
                tvBuyNum.setText(String.valueOf(buyNum));
                break;
            case R.id.tv10:
                buyNum = 10;
                tvBuyNum.setText(String.valueOf(buyNum));
                break;
            case R.id.tv20:
                buyNum = 20;
                tvBuyNum.setText(String.valueOf(buyNum));
                break;
            case R.id.tv30:
                buyNum = 30;
                tvBuyNum.setText(String.valueOf(buyNum));
                break;
            case R.id.tv50:
                buyNum = 50;
                tvBuyNum.setText(String.valueOf(buyNum));
                break;
            case R.id.tv100:
                buyNum = 100;
                tvBuyNum.setText(String.valueOf(buyNum));
                break;
        }
    }

    private DutySpecCallback dutySpecCallback;

    public void setDutySpecCallback(DutySpecCallback dutySpecCallback) {
        this.dutySpecCallback = dutySpecCallback;
    }

    public interface DutySpecCallback {
        void onBuy(String goodsId, int buyNum);

        void onCart(String goodsId, int buyNum);
    }
}
