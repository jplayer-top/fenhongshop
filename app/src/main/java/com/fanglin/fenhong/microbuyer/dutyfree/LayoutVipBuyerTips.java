package com.fanglin.fenhong.microbuyer.dutyfree;

import android.view.View;

import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.microshop.LayoutPopup;

/**
 * 青岛芳林信息
 * Created by Plucky on 2016/12/27.
 * 功能描述: 极速免税店商品详情页 购买成为VIP买手的弹窗
 */

public class LayoutVipBuyerTips extends LayoutPopup {

    public LayoutVipBuyerTips(BaseFragmentActivity activity) {
        super(activity);
    }

    @Override
    public void onSubmit(View v) {
        dismiss();
        if (vipBuyerListener != null) {
            vipBuyerListener.onVipSubmit();
        }
    }

    @Override
    public void onCancel(View v) {
        dismiss();
        if (vipBuyerListener != null) {
            vipBuyerListener.onVipCancel();
        }
    }

    private LayoutVipBuyerListener vipBuyerListener;

    public void setVipBuyerListener(LayoutVipBuyerListener vipBuyerListener) {
        this.vipBuyerListener = vipBuyerListener;
    }

    public interface LayoutVipBuyerListener {
        void onVipSubmit();

        void onVipCancel();
    }
}
