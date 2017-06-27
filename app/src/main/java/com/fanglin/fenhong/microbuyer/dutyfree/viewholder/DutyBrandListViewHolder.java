package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.BrandMessage;
import com.fanglin.fenhong.microbuyer.dutyfree.DutyfreeBrandDtlActivity;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/17-上午10:14.
 * 功能描述: 极速免税店 品牌ViewHolder
 */
public class DutyBrandListViewHolder extends RecyclerView.ViewHolder {

    ImageView ivImage;
    TextView tvName;

    public DutyBrandListViewHolder(View itemView) {
        super(itemView);
        ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        tvName = (TextView) itemView.findViewById(R.id.tvName);

        itemView.setTag(this);
    }

    public static View getView(Context context) {
        return View.inflate(context, R.layout.item_dutyfree_brandlist, null);
    }

    /**
     * 复用Item
     *
     * @param view View
     */
    public static void recyclerViewHolder(View view) {
        new DutyBrandListViewHolder(view);
    }

    public static DutyBrandListViewHolder getHolder(Context context) {
        View view = getView(context);
        return new DutyBrandListViewHolder(view);
    }

    public void refreshView(final BrandMessage brandMessage) {
        if (brandMessage != null) {
            new FHImageViewUtil(ivImage).setImageURI(brandMessage.getBrand_pic(), FHImageViewUtil.SHOWTYPE.DEFAULT);
            tvName.setText(brandMessage.getBrand_name());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.gotoActivity(v.getContext(), DutyfreeBrandDtlActivity.class, brandMessage.getBrand_id());
                }
            });
        }
    }
}
