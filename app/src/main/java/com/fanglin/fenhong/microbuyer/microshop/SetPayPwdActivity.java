package com.fanglin.fenhong.microbuyer.microshop;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
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
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.ReqSmsCode;
import com.fanglin.fenhong.microbuyer.base.model.SmsCodeInit;
import com.fanglin.fhlib.other.FHLib;
import com.fanglin.fhlib.other.FHLog;
import com.fanglin.fhui.FHHintDialog;
import com.fanglin.fhui.spotsdialog.SpotsDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 作者： Created by Plucky on 15-10-29.
 */
public class SetPayPwdActivity extends BaseFragmentActivityUI implements SmsCodeInit.SCIModellCallBack {
    @ViewInject(R.id.tv_phone)
    TextView tv_phone;
    @ViewInject(R.id.et_code)
    EditText et_code;
    @ViewInject(R.id.btnCode)
    TextView btnCode;
    @ViewInject(R.id.et_pwd)
    EditText et_pwd;
    @ViewInject(R.id.et_cfrm)
    EditText et_cfrm;

    @ViewInject(R.id.LImgCode)
    LinearLayout LImgCode;
    @ViewInject(R.id.etImgCode)
    EditText etImgCode;
    @ViewInject(R.id.imgCode)
    ImageView imgCode;

    private SpotsDialog sad;
    SmsCodeInit smsInitReq;//验证码初始化的请求
    ReqSmsCode reqSmsCode;//获取验证码的接口请求

    boolean hasCaptcha = false, canUseSmsCode = true;
    int countdown = 60;
    String captchaUrl;
    String account;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_setpay_pwd, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        initView();
    }

    private void initView() {
        setHeadTitle(R.string.title_paypwd);
        if (member == null) return;
        account = member.member_mobile;
        captchaUrl = BaseFunc.getCaptchaUrl(account);
        String fmt = getString(R.string.fmt_setpwd_phone);
        tv_phone.setText(String.format(fmt, account));

        /**
         * 初始化时不显示图形验证码、且只有当验证码初始完成之后 获取验证码按钮才可点击
         */
        refreshCaptcha();
        LImgCode.setVisibility(View.GONE);
        btnCode.setEnabled(false);

        smsInitReq = new SmsCodeInit();
        smsInitReq.setModelCallBack(this);
        smsInitReq.getData(account);

        reqSmsCode = new ReqSmsCode();
        reqSmsCode.setModelCallBack(this);

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
             * Aded By Plucky
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

    @OnClick(value = {R.id.btnCode, R.id.btn_submit, R.id.imgCode})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.btnCode:
                if (canUseSmsCode) {
                    getSmscode();
                } else {
                    showSmsCodenotAvailable();
                }
                break;
            case R.id.btn_submit:
                doSubmit();
                break;
            case R.id.imgCode:
                refreshCaptcha();
                break;
        }
    }

    private void doSubmit() {
        if (member == null) return;
        if (!FHLib.isMobileNO(member.member_mobile)) {
            BaseFunc.showMsg(mContext, getString(R.string.no_phoneNum));
            return;
        }

        if (et_code.length() < 4) {
            YoYo.with(Techniques.Shake).duration(700).playOn(et_code);
            BaseFunc.showMsg(mContext, getString(R.string.hint_code));
            return;
        }

        if (et_pwd.length() < 6) {
            YoYo.with(Techniques.Shake).duration(700).playOn(et_pwd);
            BaseFunc.showMsg(mContext, getString(R.string.hint_paypwd_limit));
            return;
        }

        if (et_cfrm.length() == 0) {
            YoYo.with(Techniques.Shake).duration(700).playOn(et_cfrm);
            BaseFunc.showMsg(mContext, getString(R.string.hint_cfrm_paypwd));
            return;
        }

        if (!TextUtils.equals(et_pwd.getText().toString(), et_cfrm.getText().toString())) {
            YoYo.with(Techniques.Shake).duration(700).playOn(et_pwd);
            YoYo.with(Techniques.Shake).duration(700).playOn(et_cfrm);
            BaseFunc.showMsg(mContext, getString(R.string.pwd_not_equal));
            return;
        }

        sad = BaseFunc.getLoadingDlg(mContext, getString(R.string.setpwd_requesting));
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {
                if (sad != null && isActive) {
                    sad.show();
                }
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (sad != null && isActive) {
                    sad.dismiss();
                }
                if (isSuccess) {
                    BaseFunc.showMsg(mContext, getString(R.string.paypwd_op_success));
                    finish();
                }
            }
        }).set_paypwd(member.member_id, member.token, et_pwd.getText().toString(), et_cfrm.getText().toString(), et_code.getText().toString(), member.member_mobile);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sad != null && isActive) {
            sad.dismiss();
            sad = null;
            isActive = false;
        }
    }

    /**
     * 刷新图形验证码
     */
    private void refreshCaptcha() {
        etImgCode.setText("");
        new FHImageViewUtil(imgCode).enableCache(false).setImageURI(captchaUrl, FHImageViewUtil.SHOWTYPE.DEFAULT_CAPTCHA);
    }

    boolean isActive = true;

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
    }


    private void showSmsCodenotAvailable() {
        FHHintDialog dialog = new FHHintDialog(mContext);
        dialog.setLeftBtn(null);
        dialog.setTvContent(getString(R.string.hint_sms_not_available));
        dialog.show();
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
