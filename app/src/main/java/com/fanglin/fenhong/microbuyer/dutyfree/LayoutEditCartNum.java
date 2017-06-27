package com.fanglin.fenhong.microbuyer.dutyfree;

import android.content.Context;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.LayoutEditNum;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/12/3-下午3:02.
 * 功能描述: 更新购物车数量弹框
 */
public class LayoutEditCartNum extends LayoutEditNum {

    public LayoutEditCartNum(Context mContext) {
        super(mContext);
    }

    public void show(int num) {
        etNum.setText(String.valueOf(num));
        super.show();
    }

    @Override
    public void onSubmit() {
        String numStr = etNum.getText().toString();
        int num = BaseFunc.isInteger(numStr) ? Integer.valueOf(numStr) : 1;
        num = num < 1 ? 1 : num;
        if (numListener != null) {
            dismiss();
            numListener.onEditNum(num);
        }
    }
}
