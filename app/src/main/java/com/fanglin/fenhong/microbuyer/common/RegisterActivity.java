package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fenhong.microbuyer.base.model.RegisterReq;
import com.fanglin.fenhong.microbuyer.base.model.ReqSmsCode;
import com.fanglin.fenhong.microbuyer.base.model.SmsCodeInit;
import com.fanglin.fhlib.other.FHLog;
import com.fanglin.fhui.FHHintDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/5/20-下午4:06.
 * 功能描述:注册接口
 */
public class RegisterActivity extends BaseFragmentActivityUI implements SmsCodeInit.SCIModellCallBack, RegisterReq.RRModelCallBack {

    @ViewInject(R.id.tvPhone)
    TextView tvPhone;
    @ViewInject(R.id.LImgCode)
    LinearLayout LImgCode;
    @ViewInject(R.id.etImgCode)
    EditText etImgCode;
    @ViewInject(R.id.imgCode)
    ImageView imgCode;
    @ViewInject(R.id.etCode)
    EditText etCode;
    @ViewInject(R.id.btnCode)
    TextView btnCode;
    @ViewInject(R.id.etPassword)
    EditText etPassword;
    @ViewInject(R.id.ivEyes)
    ImageView ivEyes;
    @ViewInject(R.id.ivReceive)
    ImageView ivReceive;
    @ViewInject(R.id.btnSubimt)
    Button btnSubimt;

    String account;

    SmsCodeInit smsInitReq;//验证码初始化的请求
    ReqSmsCode reqSmsCode;//获取验证码的接口请求
    RegisterReq registerReq;//注册请求接口

    boolean hasCaptcha = false, canUseSmsCode = true;
    int countdown = 60;
    String captchaUrl;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_register, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        account = getIntent().getStringExtra("VAL");
        captchaUrl = BaseFunc.getCaptchaUrl(account);
        initView();
    }

    @Override
    protected void onResume() {
        skipChk = true;
        super.onResume();
    }

    private void initView() {
        setHeadTitle(getString(R.string.register));
        tvPhone.setText(account);
        ivReceive.setSelected(true);

        /**
         * 初始化时不显示图形验证码、且只有当验证码初始完成之后 获取验证码按钮才可点击
         */
        refreshCaptcha();
        LImgCode.setVisibility(View.GONE);
        btnCode.setEnabled(false);

        //用于设定确定按钮可不可点
        etPassword.addTextChangedListener(tw);
        etCode.addTextChangedListener(tw);
        etImgCode.addTextChangedListener(tw);
        btnSubimt.setEnabled(false);

        smsInitReq = new SmsCodeInit();
        smsInitReq.setModelCallBack(this);
        smsInitReq.getData(account);

        reqSmsCode = new ReqSmsCode();
        reqSmsCode.setModelCallBack(this);

        registerReq = new RegisterReq();
        registerReq.setModelCallBack(this);

    }


    /**
     * 获取验证码、验证码初始化接口的回调
     *
     * @param fromSmsCode 是否是通过获取验证码得到的数据
     * @param data        验证码初始化数据
     */
    @Override
    public void onData(boolean fromSmsCode, SmsCodeInit data) {
        if (data != null) {
            hasCaptcha = data.isShowCaptcha();
            canUseSmsCode = data.isSmsAvailable();
            countdown = data.getCountdown();
            if (data.getLast_countdown() > 0) {
                resetCounter(data.getLast_countdown());
                startCounter();
            } else {
                btnCode.setEnabled(true);
            }
        } else {
            /**
             * 如果获取验证码时 输入的图形验证码是错误的
             * 则获取验证码的接口会返回错误的信息 此时的result为null 但是
             * 数据不能被重置 倒计时还是上一次的倒计时时间 验证码可不可用、是否需要显示图形验证码
             * 仍然保留内存的数据
             * Added By Plucky
             */
            if (fromSmsCode) {
                //如果验证码获取失败（data==null）的话
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                    btnCode.setEnabled(true);
                    btnCode.setText(getString(R.string.get_code_again));
                }
            } else {
                btnCode.setEnabled(true);
                hasCaptcha = false;
                canUseSmsCode = true;
                countdown = 60;
            }
        }

        //如果是获取验证码 则给出提示
        if (fromSmsCode && data != null) {
            BaseFunc.showMsg(mContext, getString(R.string.get_phonecode_success));
        }

        if (!fromSmsCode) {
            //如果是验证码初始化过来的
            if (hasCaptcha) {
                LImgCode.setVisibility(View.VISIBLE);
            } else {
                LImgCode.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 刷新图形验证码
     */
    private void refreshCaptcha() {
        etImgCode.setText("");
        new FHImageViewUtil(imgCode).enableCache(false).setImageURI(captchaUrl, FHImageViewUtil.SHOWTYPE.DEFAULT_CAPTCHA);
    }

    /**
     * 注册成功的回调
     *
     * @param member 会员信息
     */
    @Override
    public void onData(Member member) {
        if (member != null) {
            String memberStr = new Gson().toJson(member);
            BaseFunc.recordMemberinfo(fhApp, this, memberStr);
            BaseFunc.showSelfToast(mContext,getString(R.string.register_success));
            setResult(RESULT_OK);
            finish();
        }

        btnSubimt.setText(getString(R.string.register));
    }

    private TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            refreshButtonStatus();

        }
    };


    /**
     * 改变注册按钮的状态
     */
    private void refreshButtonStatus() {
        if (LImgCode.getVisibility() == View.VISIBLE) {
            if (etPassword.length() >= 6 && etCode.length() > 3 && etImgCode.length() > 3 && ivReceive.isSelected()) {
                btnSubimt.setEnabled(true);
            } else {
                btnSubimt.setEnabled(false);
            }
        } else {
            if (etPassword.length() >= 6 && etCode.length() > 3 && ivReceive.isSelected()) {
                btnSubimt.setEnabled(true);
            } else {
                btnSubimt.setEnabled(false);
            }
        }
    }


    /**
     * 获取手机验证码
     */
    public void getSmscode() {
        if (reqSmsCode == null) return;

        /**
         * 如果getSmsCode（每次返回下次的状态）
         * 返回了这次需要图形验证码 则显示出来
         */
        if (hasCaptcha) {
            LImgCode.setVisibility(View.VISIBLE);
        } else {
            LImgCode.setVisibility(View.GONE);
        }

        if (hasCaptcha && etImgCode.length() < 4) {
            YoYo.with(Techniques.Shake).duration(700).playOn(etImgCode);
            BaseFunc.showSelfToast(mContext, getString(R.string.hint_invalid_captcha));
            return;
        }

        /**
         * 有图形验证码 则进行图形验证码的接口验证
         * 否则直接范松验证码
         */

        if (hasCaptcha) {
            checkCaptcha(account, etImgCode.getText().toString());
        } else {
            resetCounter(countdown);
            startCounter();
            reqSmsCode.getSmsCode(account, null);
        }

    }

    @OnClick(value = {R.id.ivEyes, R.id.btnCode, R.id.btnSubimt,
            R.id.ivReceive, R.id.tvReceive, R.id.tvPolicy, R.id.imgCode})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ivEyes:
                ivEyes.setSelected(!ivEyes.isSelected());
                if (ivEyes.isSelected()) {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.btnCode:
                if (canUseSmsCode) {
                    getSmscode();
                } else {
                    showSmsCodenotAvailable();
                }
                break;
            case R.id.btnSubimt:
                register();
                break;
            case R.id.ivReceive:
            case R.id.tvReceive:
                ivReceive.setSelected(!ivReceive.isSelected());
                refreshButtonStatus();
                break;
            case R.id.tvPolicy:
                BaseFunc.gotoActivity(mContext, FHBrowserActivity.class, BaseVar.API_USER_AGREEMENT);
                break;
            case R.id.imgCode:
                refreshCaptcha();
                break;
        }
    }


    private void showSmsCodenotAvailable() {
        FHHintDialog dialog = new FHHintDialog(mContext);
        dialog.setLeftBtn(null);
        dialog.setTvContent(getString(R.string.hint_sms_not_available));
        dialog.show();
    }

    private void register() {
        if (registerReq == null) return;

        String isRegistering = getString(R.string.registering);
        if (TextUtils.equals(isRegistering, btnSubimt.getText())) return;

        btnSubimt.setText(isRegistering);
        registerReq.register(account, etCode.getText().toString(), etPassword.getText().toString());
    }


    /**
     * 重置倒计时
     */
    private void resetCounter(int mcountdown) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(mcountdown * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                FHLog.d("Plucky", "tick:" + millisUntilFinished);
                btnCode.setText(BaseFunc.getSecondStrOfMillis(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                btnCode.setEnabled(true);
                btnCode.setText(getString(R.string.get_code_again));
            }
        };
    }

    /**
     * 开始计时器
     */
    private void startCounter() {
        if (countDownTimer != null) {
            btnCode.setEnabled(false);
            countDownTimer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }


    private void checkCaptcha(final String mobile, final String captcha) {
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    /**
                     * 图形验证码验证完成 再进行手机验证码的请求
                     */

                    resetCounter(countdown);
                    startCounter();
                    reqSmsCode.getSmsCode(mobile, captcha);
                }
            }
        }).checkCaptcha(mobile, captcha);
    }
}
