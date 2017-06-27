package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/17-下午7:58.
 * 功能描述: 楼层分割条
 */
public class DutyfloorHeaderViewHolder extends RecyclerView.ViewHolder {

    TextView tvName;

    public DutyfloorHeaderViewHolder(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
    }

    public static DutyfloorHeaderViewHolder getHolder(Context context) {
        View view = View.inflate(context, R.layout.item_dutyfloor_header, null);
        return new DutyfloorHeaderViewHolder(view);
    }

    public void refreshView(String title) {
        tvName.setText(title);
    }
}
