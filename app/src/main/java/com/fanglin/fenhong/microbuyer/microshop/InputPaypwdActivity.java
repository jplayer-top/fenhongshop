package com.fanglin.fenhong.microbuyer.microshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 作者： Created by Plucky on 2015/12/1.
 * 输入支付密码页面
 */
public class InputPaypwdActivity extends BaseFragmentActivityUI {

    @ViewInject(R.id.et_amount)
    EditText et_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_inputpaypwd, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        initView();
    }

    private void initView() {
        setHeadTitle(R.string.title_pay_pwd);
    }

    @OnClick(value = {R.id.btn_submit, R.id.tv_paypwd})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                doSubmit();
                break;
            case R.id.tv_paypwd:
                BaseFunc.gotoActivity(this, SetPayPwdActivity.class, null);
                break;
        }
    }

    private void doSubmit() {
        if (et_amount.length() < 6) {
            YoYo.with(Techniques.Shake).duration(700).playOn(et_amount);
            BaseFunc.showMsg(mContext, getString(R.string.hint_paypwd_limit));
            return;
        }

        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    Intent intent = new Intent();
                    intent.putExtra("VAL", et_amount.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }).check_paypwd(member, et_amount.getText().toString());
    }
}
