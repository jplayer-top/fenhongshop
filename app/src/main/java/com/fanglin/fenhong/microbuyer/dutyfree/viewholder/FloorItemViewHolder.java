package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/17-下午5:50.
 * 功能描述:楼层ItemViewHolder
 */
public class FloorItemViewHolder extends RecyclerView.ViewHolder {

    public TextView tvName;
    public View vLine;

    public FloorItemViewHolder(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        vLine = itemView.findViewById(R.id.vLine);
    }

    public static FloorItemViewHolder getHolder(Context context) {
        View view = View.inflate(context, R.layout.item_dutyfree_floor, null);
        return new FloorItemViewHolder(view);
    }

}
