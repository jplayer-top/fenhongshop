package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreeConsume;
import com.fanglin.fenhong.microbuyer.dutyfree.DutyfreeConsumeDetailActivity;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/12/8-上午10:24.
 * 功能描述: 极速免税 消费列表ITEM
 */
public class DutyConsumeListViewHolder extends RecyclerView.ViewHolder {

    View vLeft;
    ImageView ivIcon;
    TextView tvTitle, tvTime, tvMoney;

    private String consumeId;

    public DutyConsumeListViewHolder(View itemView) {
        super(itemView);
        vLeft = itemView.findViewById(R.id.vLeft);
        ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);

        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        tvMoney = (TextView) itemView.findViewById(R.id.tvMoney);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFunc.gotoActivity(v.getContext(), DutyfreeConsumeDetailActivity.class, consumeId);
            }
        });
    }

    public static DutyConsumeListViewHolder getHolder(Context mContext) {
        View view = View.inflate(mContext, R.layout.item_dutyfree_consume_list, null);
        return new DutyConsumeListViewHolder(view);
    }

    public void refreshView(DutyfreeConsume consume) {
        if (consume != null) {
            consumeId = consume.getConsumeId();

            new FHImageViewUtil(ivIcon).setImageURI(consume.getConsumeIcon(), FHImageViewUtil.SHOWTYPE.DEFAULT);
            tvTitle.setText(consume.getConsumeName());
            tvTime.setText(consume.getCreatetime());
            tvMoney.setText(consume.getConsumeMoneyDesc());

            GradientDrawable drawable = (GradientDrawable) vLeft.getBackground();
            drawable.setColor(consume.getColor());
        }

    }
}
