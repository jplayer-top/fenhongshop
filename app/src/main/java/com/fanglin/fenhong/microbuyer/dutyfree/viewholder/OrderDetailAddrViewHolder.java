package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyWaybill;
import com.fanglin.fenhong.microbuyer.dutyfree.listener.DutyBillActionListener;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/11-上午11:55.
 * 功能描述: 急速免税店 订单详情 地址
 */
public class OrderDetailAddrViewHolder extends RecyclerView.ViewHolder {
    LinearLayout LAction;
    TextView tvLeft, tvRight;
    TextView tvName, tvPhone, tvNum;
    TextView tvAddr;
    View vLine;

    private DutyWaybill bill;

    public OrderDetailAddrViewHolder(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvPhone = (TextView) itemView.findViewById(R.id.tvPhone);
        tvNum = (TextView) itemView.findViewById(R.id.tvNum);
        tvAddr = (TextView) itemView.findViewById(R.id.tvAddr);
        vLine = itemView.findViewById(R.id.vLine);
        LAction = (LinearLayout) itemView.findViewById(R.id.LAction);
        tvLeft = (TextView) itemView.findViewById(R.id.tvLeft);
        tvRight = (TextView) itemView.findViewById(R.id.tvRight);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof TextView) {
                    String billStates = ((TextView) v).getText().toString();
                    if (TextUtils.equals(billStates, DutyBillActionListener.SUB_DELIVERY)) {
                        listener.onDelivery(bill);
                    } else {
                        listener.onReceive(bill);
                    }
                }
            }
        };
        tvLeft.setOnClickListener(clickListener);
        tvRight.setOnClickListener(clickListener);
    }

    public static OrderDetailAddrViewHolder getHolder(Context mContext) {
        View view = View.inflate(mContext, R.layout.item_orderdtl_addr, null);
        return new OrderDetailAddrViewHolder(view);
    }

    private DutyBillActionListener listener;

    public void setListener(DutyBillActionListener listener) {
        this.listener = listener;
    }

    public void refreshView(final DutyWaybill bill) {
        this.bill = bill;
        if (bill != null) {
            tvName.setText(bill.getTrue_name());
            tvPhone.setText(bill.getTel_phone());
            tvNum.setText(bill.getAllProductNumDesc());
            tvAddr.setText(bill.getAddress());
            if (listener != null) {
                String[] billStates = bill.getBillButtonDesc();
                String leftStr = billStates[0], rightStr = billStates[1];
                tvLeft.setText(leftStr);
                tvRight.setText(rightStr);

                if (TextUtils.isEmpty(leftStr) && TextUtils.isEmpty(rightStr)) {
                    LAction.setVisibility(View.GONE);
                } else {
                    LAction.setVisibility(View.VISIBLE);

                    if (!TextUtils.isEmpty(leftStr)) {
                        tvLeft.setVisibility(View.VISIBLE);
                    } else {
                        tvLeft.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(rightStr)) {
                        tvRight.setVisibility(View.VISIBLE);
                    } else {
                        tvRight.setVisibility(View.GONE);
                    }
                }
            } else {
                LAction.setVisibility(View.GONE);
            }
        }
    }
}
