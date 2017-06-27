package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyAddress;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/16-下午9:06.
 * 功能描述: 极速免税店 地址列表
 */
public class DutyAddrListViewHolder extends RecyclerView.ViewHolder {

    TextView tvName;
    TextView tvAddr, tvEdit;
    View vLine;

    public DutyAddrListViewHolder(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvAddr = (TextView) itemView.findViewById(R.id.tvAddr);
        tvEdit = (TextView) itemView.findViewById(R.id.tvEdit);
        vLine = itemView.findViewById(R.id.vLine);
    }

    public static DutyAddrListViewHolder getHolder(Context context) {
        View view = View.inflate(context, R.layout.item_dutyaddress, null);
        return new DutyAddrListViewHolder(view);
    }

    public void refreshView(final DutyAddress address) {
        if (address != null) {
            String nameDesc = address.getNameAndPhone();
            tvName.setText(nameDesc);
            tvAddr.setText(address.getWholeAddrDesc());
            tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.gotoDutyNewAddress(v.getContext(), null, address);
                }
            });
            tvName.setSelected(address.isSelected());
        }

    }
}
