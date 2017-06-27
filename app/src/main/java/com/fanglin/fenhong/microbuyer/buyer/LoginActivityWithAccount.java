package com.fanglin.fenhong.microbuyer.buyer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fhlib.other.FHLib;
import com.fanglin.fhui.FHHintDialog;
import com.fanglin.fhui.spotsdialog.SpotsDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 作者： Created by Plucky on 2015/9/6.
 * modify by lizhixin on 2016/03/18
 */
public class LoginActivityWithAccount extends BaseFragmentActivityUI implements TextWatcher, View.OnFocusChangeListener, APIUtil.FHAPICallBack, FHHintDialog.FHHintListener {

    @ViewInject(R.id.tvAccount)
    TextView tvAccount;
    @ViewInject(R.id.etPassword)
    EditText etPassword;
    @ViewInject(R.id.btnSubimt)
    Button btnSubimt;
    @ViewInject(R.id.LEdit)
    LinearLayout LEdit;
    @ViewInject(R.id.ivClear)
    ImageView ivClear;

    private String account;

    SpotsDialog spotsDialog;
    boolean isActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_login_with_account, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        account = getIntent().getStringExtra("ACCOUNT");
        initView();
    }

    private void initView() {
        setHeadTitle(R.string.login);
        tvAccount.setText(account);
        etPassword.addTextChangedListener(this);
        etPassword.setOnFocusChangeListener(this);
        etPassword.requestFocus();
        ivClear.setVisibility(View.INVISIBLE);
        btnSubimt.setEnabled(false);

        spotsDialog = BaseFunc.getLoadingDlg(mContext, getString(R.string.logining));
    }

    @OnClick(value = {R.id.tvForget, R.id.btnSubimt, R.id.ivClear})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tvForget:
                forgetPassword();
                break;
            case R.id.btnSubimt:
                login();
                break;
            case R.id.ivClear:
                etPassword.getText().clear();
                break;
        }
    }

    @Override
    protected void onResume() {
        skipChk = true;
        super.onResume();
        isActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (spotsDialog != null) {
            spotsDialog = null;
            isActive = false;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        LEdit.setSelected(hasFocus);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (etPassword.length() > 5) {
            btnSubimt.setEnabled(true);
            ivClear.setVisibility(View.VISIBLE);
        } else {
            btnSubimt.setEnabled(false);
            ivClear.setVisibility(View.GONE);
        }
    }

    private void login() {
        new BaseBO().setCallBack(this).login(account, etPassword.getText().toString());
    }

    @Override
    public void onStart(String data) {
        if (spotsDialog != null && isActive) {
            spotsDialog.show();
        }
    }

    @Override
    public void onEnd(boolean isSuccess, String data) {
        if (spotsDialog != null && isActive) {
            spotsDialog.dismiss();
        }
        if (isSuccess) {
            BaseFunc.recordMemberinfo(fhApp, this, data);
            setResult(RESULT_OK);
            finish();
        } else {
            errPassword();
        }
    }

    private void forgetPassword() {
        if (FHLib.isMobileNO(account)) {
            BaseFunc.gotoRetrievePwdActivity(this,account,LoginActivity.LOGIN_REQ);
        } else {
            FHHintDialog dialog = new FHHintDialog(mContext);
            dialog.setTvContent(getString(R.string.checkpassword_login));
            dialog.setLeftBtn(null);
            dialog.setTvRight(getString(R.string.confirm));
            dialog.show();
        }
    }

    private void errPassword() {
        if (!isActive) return;
        FHHintDialog dialog = new FHHintDialog(mContext);
        dialog.setTvContent(getString(R.string.account_pwd_error));
        dialog.setTvLeft(getString(R.string.input_pwd_again));
        dialog.setTvRight(getString(R.string.i_forget_the_pwd));
        dialog.setHintListener(this);
        dialog.show();
    }

    @Override
    public void onLeftClick() {
        etPassword.getText().clear();
    }

    @Override
    public void onRightClick() {
        forgetPassword();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LoginActivity.LOGIN_REQ && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
