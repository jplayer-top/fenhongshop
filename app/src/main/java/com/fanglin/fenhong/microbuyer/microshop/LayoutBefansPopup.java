package com.fanglin.fenhong.microbuyer.microshop;

import android.view.View;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/9/1-上午10:02.
 * 功能描述:
 * 1、支付成功后，恭喜成为粉丝弹窗
 */
public class LayoutBefansPopup extends LayoutPopup {


    public LayoutBefansPopup(BaseFragmentActivity activity) {
        super(activity);
    }

    @Override
    public void onSubmit(View v) {
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                dismiss();
                BaseFunc.gotoHome(activity, 4);
            }
        }).applyShoper(activity.member);
    }
}
