package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanglin.fenhong.microbuyer.R;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/14-下午1:14.
 * 功能描述: RecyclerView中的分割线
 */
public class SpliterViewHolder extends RecyclerView.ViewHolder {

    public View vSpliter;

    public SpliterViewHolder(View itemView) {
        super(itemView);
        vSpliter = itemView.findViewById(R.id.vSpliter);
    }

    public static SpliterViewHolder getHolder(Context context) {
        View view = View.inflate(context, R.layout.item_spliter, null);
        return new SpliterViewHolder(view);
    }

    public static SpliterViewHolder getHolder(Context context, int lineWidthResId) {
        View view = View.inflate(context, R.layout.item_spliter, null);
        SpliterViewHolder holder = new SpliterViewHolder(view);
        int height = context.getResources().getDimensionPixelOffset(lineWidthResId);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        holder.vSpliter.setLayoutParams(params);
        return holder;
    }

}
