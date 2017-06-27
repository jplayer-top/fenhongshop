package com.fanglin.fenhong.microbuyer.merchant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;

/**
 * 作者： Created by Plucky on 15-10-1.
 */
public class JoinViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_cls1;
    public TextView tv_cls2;
    public TextView tv_cls3;
    public TextView tv_op;

    public JoinViewHolder(View itemView) {
        super(itemView);
        tv_cls1 = (TextView) itemView.findViewById(R.id.tv_cls1);
        tv_cls2 = (TextView) itemView.findViewById(R.id.tv_cls2);
        tv_cls3 = (TextView) itemView.findViewById(R.id.tv_cls3);
        tv_op = (TextView) itemView.findViewById(R.id.tv_op);
    }
}