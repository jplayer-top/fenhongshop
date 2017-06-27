package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyAddress;

/**
 * author:Created by Oblivion on 2017/2/7.
 * 功能描述:
 */
public class DutyCartCheckAddAddressHolder extends RecyclerView.ViewHolder {

    public LinearLayout llAddressShow, llArrow;
    public LinearLayout llRelevance;
    public TextView tvAddressDesc, tvAddressName;

    public DutyCartCheckAddAddressHolder(View itemView) {
        super(itemView);
        llAddressShow = (LinearLayout) itemView.findViewById(R.id.llAddressShow);
        llRelevance = (LinearLayout) itemView.findViewById(R.id.llRelevance);
        llArrow = (LinearLayout) itemView.findViewById(R.id.llArrow);
        tvAddressDesc = (TextView) itemView.findViewById(R.id.tvAddressDesc);
        tvAddressName = (TextView) itemView.findViewById(R.id.tvAddressName);
    }

    public static DutyCartCheckAddAddressHolder getHolder(Context context) {
        View itemView = View.inflate(context, R.layout.relevance, null);

        return new DutyCartCheckAddAddressHolder(itemView);
    }

    public void refreshView(boolean isShow, DutyAddress dutyAddress) {
        if (isShow) {
            llRelevance.setVisibility(View.INVISIBLE);
            llAddressShow.setVisibility(View.VISIBLE);
            llArrow.setVisibility(View.VISIBLE);
            if (dutyAddress != null) {
                tvAddressDesc.setText(dutyAddress.getArea_info() + dutyAddress.getAddress());
                tvAddressName.setText(dutyAddress.getNameAndPhone());
            }
        } else {
            llRelevance.setVisibility(View.VISIBLE);
            llAddressShow.setVisibility(View.INVISIBLE);
            llArrow.setVisibility(View.INVISIBLE);
        }
    }
}
