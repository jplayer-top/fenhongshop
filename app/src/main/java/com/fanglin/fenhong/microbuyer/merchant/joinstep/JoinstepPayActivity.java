package com.fanglin.fenhong.microbuyer.merchant.joinstep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 作者： Created by Plucky on 15-9-30.
 */
public class JoinstepPayActivity extends BaseFragmentActivityUI {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_joinstep_pay, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        setHeadTitle(R.string.merchantinpay);
    }

    @OnClick(value = {R.id.Loffline})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.Loffline:
                BaseFunc.gotoActivity4Result(this, JoinstepPayofflineActivity.class, null, 0x001);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case 0x001:
                Intent i = new Intent(mContext, JoinStepActivity.class);
                setResult(RESULT_OK, i);
                finish();
                break;
        }
    }
}
