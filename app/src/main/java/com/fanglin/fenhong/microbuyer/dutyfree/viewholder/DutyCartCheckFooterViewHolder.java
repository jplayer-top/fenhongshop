package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.fanglin.fenhong.microbuyer.R;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/16-下午4:14.
 * 功能描述: 极速免税店 核对订单Footer
 */
public class DutyCartCheckFooterViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivImage;

    public DutyCartCheckFooterViewHolder(View itemView) {
        super(itemView);
        ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
    }

    public static DutyCartCheckFooterViewHolder getHolder(Context context) {
        View view = View.inflate(context, R.layout.item_dutycartcheck_footer, null);
        return new DutyCartCheckFooterViewHolder(view);
    }

    public void refreshView(boolean isExpanded) {
        itemView.setSelected(isExpanded);
    }
}
