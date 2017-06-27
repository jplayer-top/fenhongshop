package com.fanglin.fenhong.microbuyer.buyer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.event.FHLoginEvent;
import com.fanglin.fenhong.microbuyer.common.RegisterActivity;
import com.fanglin.fhlib.other.FHLib;
import com.fanglin.fhui.FHHintDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import de.greenrobot.event.EventBus;

/**
 * 作者： Created by Plucky on 2015/9/6.
 * modify by lizhixin on 2016/03/18
 */
public class LoginActivity extends BaseFragmentActivity implements View.OnFocusChangeListener, TextWatcher, TextView.OnEditorActionListener {

    public static final int LOGIN_REQ = 0x0001;

    @ViewInject(R.id.LEdit)
    LinearLayout LEdit;
    @ViewInject(R.id.etAccount)
    EditText etAccount;
    @ViewInject(R.id.btnSubimt)
    Button btnSubimt;
    @ViewInject(R.id.ivClear)
    ImageView ivClear;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (etAccount.length() > 0) {
            btnSubimt.setEnabled(true);
            ivClear.setVisibility(View.VISIBLE);
        } else {
            btnSubimt.setEnabled(false);
            ivClear.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        LEdit.setSelected(hasFocus);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);
        initView();
    }

    private void initView() {
        etAccount.addTextChangedListener(this);
        etAccount.setOnFocusChangeListener(this);
        ivClear.setVisibility(View.INVISIBLE);

        etAccount.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etAccount.setOnEditorActionListener(this);

        EventBus.getDefault().post(new FHLoginEvent());
    }

    @OnClick(value = {R.id.btnSubimt, R.id.ivClose, R.id.ivClear})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubimt:
                login();
                break;
            case R.id.ivClose:
                hideSoft();
                finish();
                break;
            case R.id.ivClear:
                etAccount.getText().clear();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQ && resultCode == RESULT_OK) {
            //从快速登录页面返回
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (etAccount.length() > 0) {
            login();
        }
        return false;
    }

    private void login() {
        final String checking = getString(R.string.login_check_account);
        if (TextUtils.equals(btnSubimt.getText().toString(), checking)) return;

        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {
                btnSubimt.setText(checking);
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {

                //该用户存在则去登录
                if (TextUtils.equals("1", data)) {
                    Intent intent = new Intent(mContext, LoginActivityWithAccount.class);
                    intent.putExtra("ACCOUNT", etAccount.getText().toString());
                    startActivityForResult(intent, LOGIN_REQ);
                }

                //不存在去注册
                if (TextUtils.equals("0", data)) {
                    accountNotExist(etAccount.getText().toString());
                }

                btnSubimt.setText(getString(R.string.login_signin));
            }
        }).if_account_exists(etAccount.getText().toString());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void hideSoft() {
        InputMethodManager im = BaseFunc.getInputMethodManager(this);
        if (im.isActive()) {
            im.hideSoftInputFromWindow(etAccount.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideSoft();
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnSubimt.setEnabled(etAccount.length() > 0);
    }

    private void accountNotExist(String account) {
        if (FHLib.isMobileNO(account)) {
            BaseFunc.gotoActivity4Result(this, RegisterActivity.class, account, LOGIN_REQ);
        } else {
            FHHintDialog hintDialog = new FHHintDialog(mContext);
            hintDialog.setLeftBtn(null);
            hintDialog.setTvContent(getString(R.string.account_not_exist));
            hintDialog.show();
        }
    }
}
