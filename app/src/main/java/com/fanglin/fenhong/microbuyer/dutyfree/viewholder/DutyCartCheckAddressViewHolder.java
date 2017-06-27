package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyAddress;
import com.fanglin.fenhong.microbuyer.dutyfree.CartCheckCache;
import com.fanglin.fenhong.microbuyer.base.baseui.LayoutEditNum;
import com.fanglin.fenhong.microbuyer.dutyfree.LayoutEditAddrNum;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/16-下午4:19.
 * 功能描述: 急速免税店 核对购物车 关联地址
 */
public class DutyCartCheckAddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, LayoutEditNum.LayoutEditNumListener {

    public TextView tvDelete;
    private TextView tvName, tvAddr;
    private TextView tvMinus, tvPlus;
    private TextView tvNum;
    public View vLine;
    private int totalNum;
    private DutyAddress address;

    private LayoutEditAddrNum inputDialog;

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public DutyCartCheckAddressViewHolder(View itemView) {
        super(itemView);
        tvDelete = (TextView) itemView.findViewById(R.id.tvDelete);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvAddr = (TextView) itemView.findViewById(R.id.tvAddr);
        tvMinus = (TextView) itemView.findViewById(R.id.tvMinus);
        tvNum = (TextView) itemView.findViewById(R.id.tvNum);
        tvPlus = (TextView) itemView.findViewById(R.id.tvPlus);
        vLine = itemView.findViewById(R.id.vLine);

        tvMinus.setOnClickListener(this);
        tvPlus.setOnClickListener(this);
        inputDialog = new LayoutEditAddrNum(itemView.getContext());
        inputDialog.setNumListener(this);
        tvNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address == null) return;
                inputDialog.setCartIdAndTotal(address.getCartID(), totalNum);
                inputDialog.show(address.getSelectNum());
            }
        });
    }

    public static DutyCartCheckAddressViewHolder getHolder(Context context) {
        View view = View.inflate(context, R.layout.item_dutycartcheck_address, null);
        return new DutyCartCheckAddressViewHolder(view);
    }

    public void refreshView(final DutyAddress address) {
        this.address = address;
        if (address != null) {
            tvName.setText(address.getNameAndPhone());
            tvAddr.setText(address.getWholeAddrDesc());
            tvNum.setText(address.getSelectNumDesc());
        }
    }

    @Override
    public void onClick(View v) {
        if (address == null) return;
        int num = address.getSelectNum();
        switch (v.getId()) {
            case R.id.tvMinus:
                num--;
                num = num < 1 ? 1 : num;
                break;
            case R.id.tvPlus:
                int leftNum = CartCheckCache.getLeftNumOfCartId(address.getCartID(), totalNum);
                if (leftNum > 0) {
                    num++;
                } else {
                    BaseFunc.showMsg(v.getContext(), "无再可分配数量");
                }
                break;
        }

        address.setSelectNum(num);
        CartCheckCache.updateBindedAddress(address);
        tvNum.setText(String.valueOf(num));
    }

    @Override
    public void onEditNum(int num) {
        address.setSelectNum(num);
        CartCheckCache.updateBindedAddress(address);
        tvNum.setText(String.valueOf(num));
    }
}
