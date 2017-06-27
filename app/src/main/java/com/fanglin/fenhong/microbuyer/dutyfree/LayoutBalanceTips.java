package com.fanglin.fenhong.microbuyer.dutyfree;

import android.view.View;

import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.microshop.LayoutPopup;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/12/9-下午3:43.
 * 功能描述: 使用余额时的弹窗提示
 */
public class LayoutBalanceTips extends LayoutPopup {

    public LayoutBalanceTips(BaseFragmentActivity activity) {
        super(activity);
    }

    @Override
    public void onSubmitWithUrl() {
        super.onSubmitWithUrl();
        activity.finish();
    }

    @Override
    public void onSubmit(View v) {
        dismiss();
    }
}
