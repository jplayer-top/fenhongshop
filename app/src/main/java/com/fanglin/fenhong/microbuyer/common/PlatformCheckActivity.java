package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 作者： Created by Plucky on 2015/11/27.
 */
public class PlatformCheckActivity extends BaseFragmentActivityUI {

    @ViewInject(R.id.tv_apply_desc)
    TextView tv_apply_desc;
    @ViewInject(R.id.tv_icon_error)
    TextView tv_icon_error;

    int refundType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_platform_check, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);

        try {
            refundType = getIntent().getIntExtra("TYPE", 1);
        } catch (Exception e) {
            refundType = 1;
        }
        initView();

    }

    private void initView() {
        /*如果是退货*/
        if (refundType == 2) {
            setHeadTitle(R.string.title_return_goods_1);
            tv_apply_desc.setText(getString(R.string.plat_check_goods));
        } else {
            setHeadTitle(R.string.title_refund);
            tv_apply_desc.setText(getString(R.string.plat_check_money));
        }
        BaseFunc.setFont(tv_icon_error);
    }
}
