package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.BrandMessage;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/24-下午1:08.
 * 功能描述: 极速免税 品牌详情页
 */
public class DutyBrandDtlTopViewHolder extends RecyclerView.ViewHolder implements ViewTreeObserver.OnGlobalLayoutListener, View.OnClickListener {

    ImageView ivBanner;
    TextView tvName;
    ImageView ivImage, ivNation;
    TextView tvNation, tvNum;
    TextView tvDesc;

    boolean isExpanded;
    BrandMessage brand;

    public DutyBrandDtlTopViewHolder(View itemView) {
        super(itemView);
        ivBanner = (ImageView) itemView.findViewById(R.id.ivBanner);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        ivImage = (ImageView) itemView.findViewById(R.id.ivImage);

        ivNation = (ImageView) itemView.findViewById(R.id.ivNation);

        tvNation = (TextView) itemView.findViewById(R.id.tvNation);
        tvNum = (TextView) itemView.findViewById(R.id.tvNum);
        tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
        tvDesc.getViewTreeObserver().addOnGlobalLayoutListener(this);
        tvDesc.setOnClickListener(this);
    }

    public static View getView(Context mContext) {
        return View.inflate(mContext, R.layout.item_dutybrandtl_top, null);
    }

    public static DutyBrandDtlTopViewHolder getHolder(Context mContext) {
        View view = getView(mContext);
        return new DutyBrandDtlTopViewHolder(view);
    }

    public static DutyBrandDtlTopViewHolder getHolderByView(View view) {
        return new DutyBrandDtlTopViewHolder(view);
    }

    public void refreshView(BrandMessage brandMessage) {
        brand = brandMessage;
        if (brand != null) {
            new FHImageViewUtil(ivBanner).setImageURI(brand.getBrand_page_pic(), FHImageViewUtil.SHOWTYPE.GRP_BANNER);
            tvName.setText(brand.getBrand_name());
            new FHImageViewUtil(ivImage).setImageURI(brand.getBrand_pic(), FHImageViewUtil.SHOWTYPE.DEFAULT);

            new FHImageViewUtil(ivNation).setImageURI(brand.getCountry_pic(), FHImageViewUtil.SHOWTYPE.BANNER);
            tvNation.setText(brand.getCountry_name());
            tvNum.setText(brand.getGoods_storage());
            if (TextUtils.isEmpty(brand.getBrand_intro())) {
                tvDesc.setVisibility(View.GONE);
            } else {
                tvDesc.setVisibility(View.VISIBLE);
                tvDesc.setText(brand.getBrand_intro());
            }

            if (isExpanded) {
                tvDesc.setMaxLines(brand.maxLines);
                tvDesc.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.icon_triangle_gray);
            } else {
                tvDesc.setMaxLines(2);
                if (brand.maxLines < 3) {
                    tvDesc.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    tvDesc.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.icon_triangle_gray_down);
                }
            }
        }
    }

    @Override
    public void onGlobalLayout() {
        if (brand == null) return;
        //获取玩初始值就可以
        if (brand.maxLines > 0) return;
        brand.maxLines = tvDesc.getLineCount();
        if (changeListener != null && brand.maxLines > 2) {
            changeListener.onTopHeightChange();
        }
    }

    @Override
    public void onClick(View v) {
        //如果行数小于2 则不需要折叠
        if (brand.maxLines < 3) return;
        isExpanded = !isExpanded;
        if (changeListener != null) {
            changeListener.onTopHeightChange();
        }
    }

    private BrandTopChangeListener changeListener;

    public void setChangeListener(BrandTopChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public interface BrandTopChangeListener {
        void onTopHeightChange();
    }

}
