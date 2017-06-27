package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.BrandMessage;
import com.fanglin.fenhong.microbuyer.dutyfree.DutyBrandAllActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.DutyfreeBrandDtlActivity;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/14-下午1:00.
 * 功能描述:极速免税店 品牌首页 品牌ViewHolder
 */
public class DutyBrandViewHolder extends RecyclerView.ViewHolder {

    ImageView ivImage;
    LinearLayout.LayoutParams params;
    String brandId;

    boolean isLast;

    public DutyBrandViewHolder(final View itemView) {
        super(itemView);
        ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        final Context mContext = itemView.getContext();
        int w = BaseFunc.getDisplayMetrics(mContext).widthPixels;
        w = (w - mContext.getResources().getDimensionPixelOffset(R.dimen.dp_of_46)) / 2;
        int h = w * 75 / 165;
        params = new LinearLayout.LayoutParams(w, h);
        ivImage.setLayoutParams(params);
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLast) {
                    BaseFunc.gotoActivity(itemView.getContext(), DutyBrandAllActivity.class, brandId);
                } else {
                    BaseFunc.gotoActivity(mContext, DutyfreeBrandDtlActivity.class, brandId);
                }
            }
        });
    }

    public static DutyBrandViewHolder getHolder(Context context) {
        View view = View.inflate(context, R.layout.item_dutyfree_brand, null);
        return new DutyBrandViewHolder(view);
    }

    public void refreshView(int index, List<BrandMessage> brands) {
        if (brands == null) return;
        BrandMessage brandMessage = brands.get(index);
        brandId = brandMessage.getBrand_id();
        int count = brands.size();

        isLast = index == count - 1;

        boolean isFirstLine = index < 2;
        int line = (count % 2 == 0) ? count / 2 : count / 2 + 1;
        boolean isLastLine = index > (line - 1) * 2 - 1;
        boolean isFirstCol = index % 2 == 0;

        if (isFirstLine) {
            if (isFirstCol) {
                itemView.setBackgroundResource(R.drawable.brand_top_left);
            } else {
                itemView.setBackgroundResource(R.drawable.brand_top_right);
            }
        } else if (isLastLine) {
            if (isFirstCol) {
                itemView.setBackgroundResource(R.drawable.brand_bottom_left);
            } else {
                itemView.setBackgroundResource(R.drawable.brand_bottom_right);
            }
        } else {
            if (isFirstCol) {
                itemView.setBackgroundResource(R.drawable.brand_mid_left);
            } else {
                itemView.setBackgroundResource(R.drawable.brand_mid_right);
            }
        }

        new FHImageViewUtil(ivImage).setImageURI(brandMessage.getBrand_mainpage_pic(), FHImageViewUtil.SHOWTYPE.NEWHOME_BRAND);
    }
}
