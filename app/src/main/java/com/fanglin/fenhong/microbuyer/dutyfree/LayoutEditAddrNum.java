package com.fanglin.fenhong.microbuyer.dutyfree;

import android.content.Context;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.LayoutEditNum;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/12/3-下午2:09.
 * 功能描述: 编辑关联地址数量
 */
public class LayoutEditAddrNum extends LayoutEditNum {

    private int curNum, totalNum;
    private String cartId;

    public LayoutEditAddrNum(Context mContext) {
        super(mContext);
    }

    public void setCartIdAndTotal(String cartId, int totalNum) {
        this.cartId = cartId;
        this.totalNum = totalNum;
    }

    public void show(int curNum) {
        this.curNum = curNum;
        etNum.setText(String.valueOf(curNum));
        super.show();
    }

    @Override
    public void onPlus() {
        String numStr = etNum.getText().toString();
        int num = BaseFunc.isInteger(numStr) ? Integer.valueOf(numStr) : 1;
        int oldNum = curNum;
        int dx = num - oldNum;

        int leftNum = CartCheckCache.getLeftNumOfCartId(cartId, totalNum);
        if (leftNum == 0) {
            BaseFunc.showMsg(etNum.getContext(), "无可再分配商品数量");
            return;
        }
        //增加数量
        if (dx > leftNum) {
            BaseFunc.showMsg(etNum.getContext(), "最多能分配" + (oldNum + leftNum) + "件");
            return;
        } else {
            super.onPlus();
        }
    }

    @Override
    public void onSubmit() {
        String numStr = etNum.getText().toString();
        int num = BaseFunc.isInteger(numStr) ? Integer.valueOf(numStr) : 1;
        int oldNum = curNum;
        int dx = num - oldNum;

        int leftNum = CartCheckCache.getLeftNumOfCartId(cartId, totalNum);

        //增加数量
        if (dx > leftNum) {
            BaseFunc.showMsg(etNum.getContext(), "最多能分配" + (oldNum + leftNum) + "件");
            return;
        }
        num = num < 1 ? 1 : num;
        if (numListener != null) {
            dismiss();
            numListener.onEditNum(num);
        }
    }
}
