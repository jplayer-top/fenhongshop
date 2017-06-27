package com.fanglin.fenhong.microbuyer.microshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.BankCard;
import com.fanglin.fenhong.microbuyer.base.model.DeductMoney;
import com.fanglin.fhui.spotsdialog.SpotsDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-10-29.
 */
public class DepositeActivity extends BaseFragmentActivityUI implements BankCard.BCModelCallBack, DeductMoney.DMModelCallBack {
    private static int REQADD = 123;
    @ViewInject(R.id.LIcon)
    LinearLayout LIcon;
    @ViewInject(R.id.tv_icon)
    TextView tv_icon;
    @ViewInject(R.id.tv_cardinfo)
    TextView tv_cardinfo;
    @ViewInject(R.id.tv_avaliable)
    TextView tv_avaliable;
    @ViewInject(R.id.et_amount)
    EditText et_amount;
    @ViewInject(R.id.et_paypwd)
    EditText et_paypwd;
    LayoutBankCardList layoutBankCardList;
    DeductMoney deductMoney;
    SpotsDialog sad;
    @ViewInject(R.id.tv_desc)
    TextView tv_desc;
    boolean isActive = true;
    private BankCard request;
    private BankCard aCard;
    private List<BankCard> bankCards;
    private double avaliable = 0;
    private DecimalFormat df;
    private String fmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_deposite, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        initView();
    }

    private void initView() {
        setHeadTitle(R.string.deposit);
        df = new DecimalFormat("¥#0.00");
        fmt = getString(R.string.fmt_deposit_amount);
        BaseFunc.setFont(LIcon);

        tv_desc.setText(Html.fromHtml(getString(R.string.deposit_tips_iconfont)));
        BaseFunc.setFont(tv_desc);

        refreshAvaliable(0);
        refreshBankSelected(null);
        refreshBankSelected(null);
        if (member == null) return;
        layoutBankCardList = new LayoutBankCardList(mContext);
        layoutBankCardList.setCallBack(new LayoutBankCardList.LBCCallBack() {
            @Override
            public void onEdit(BankCard bc) {
                layoutBankCardList.dismiss();
                BaseFunc.gotoActivity4Result((Activity) mContext, EditBankCardActivity.class, new Gson().toJson(bc), REQADD);
            }

            @Override
            public void onAdd() {
                BaseFunc.gotoActivity4Result(DepositeActivity.this, EditBankCardActivity.class, null, REQADD);
            }

            @Override
            public void onSelect(BankCard bc) {
                refreshBankSelected(bc);
                if (member == null) return;
                if (request != null) {
                    /*点选变默认*/
                    request.if_default = "1";
                    request.card_id = bc.card_id;
                    request.bank_name = bc.bank_name;
                    request.bank_no = bc.bank_no;
                    request.bank_user = bc.bank_user;
                    request.id_card = bc.id_card;

                    request.update(member);
                }
            }
        });
        request = new BankCard();
        request.setModelCallBack(this);
        if (member == null) return;
        if (request != null) request.getList(member);
        deductMoney = new DeductMoney();
        deductMoney.setModelCallBack(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (member == null) return;
        if (deductMoney != null) deductMoney.getData(member);
        isActive = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQADD && resultCode == RESULT_OK && data != null) {
            BankCard card;
            try {
                card = new Gson().fromJson(data.getStringExtra("VAL"), BankCard.class);
            } catch (Exception e) {
                card = null;
            }
            if (card != null) {
                /** 如果是删除*/
                if (card.actionNum == 1) {
                    refreshBankSelected(null);
                } else {
                    refreshBankSelected(card);
                }
                if (member == null) return;
                if (request != null) request.getList(member);
            } else {
                refreshBankSelected(null);
            }
        }
    }

    private void refreshAvaliable(double money) {
        avaliable = money;
        tv_avaliable.setText(String.format(fmt, df.format(money)));
    }

    private void refreshBankSelected(BankCard bankCard) {
        aCard = bankCard;
        if (bankCard != null) {
            tv_icon.setText(BaseFunc.getBankIconByName(mContext, bankCard.bank_name));
            tv_cardinfo.setText(bankCard.getTitle());
        } else {
            tv_icon.setText(getString(R.string.if_why));
            tv_cardinfo.setText(getString(R.string.add_a_bankcard));
        }
    }

    /**银行卡代理方法*/
    @Override
    public void onError(String errcode) {
        switch (request.actionNum) {
            case 0:
                bankCards = null;
                refreshBankSelected(null);
                break;
            case 2:
                /** 点击设置为默认之后*/
                if (member == null) return;
                request.getList(member);
                break;
        }
    }

    @Override
    public void onList(List<BankCard> list) {
        bankCards = list;
        if (bankCards != null && bankCards.size() > 0) {
            if (aCard == null) {
                refreshBankSelected(bankCards.get(0));
            }
        }
    }

    @OnClick(value = {R.id.tv_paypwd, R.id.btn_submit, R.id.LIcon})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_paypwd:
                BaseFunc.gotoActivity(this, SetPayPwdActivity.class, null);
                break;
            case R.id.btn_submit:
                doSubmit();
                break;
            case R.id.LIcon:
                if (bankCards != null) {
                    layoutBankCardList.show(bankCards);
                } else {
                    BaseFunc.gotoActivity4Result(this, EditBankCardActivity.class, null, REQADD);
                }
                break;
        }
    }

    private void doSubmit() {
        if (member == null) return;
        if (aCard == null || TextUtils.isEmpty(aCard.card_id)) {
            YoYo.with(Techniques.Shake).duration(700).playOn(LIcon);
            BaseFunc.showMsg(mContext, getString(R.string.select_a_bankcard));
            return;
        }

        if (avaliable == 0) {
            YoYo.with(Techniques.Shake).duration(700).playOn(tv_avaliable);
            BaseFunc.showMsg(mContext, getString(R.string.tips_avaliable_money));
            return;
        }

        if (et_amount.length() == 0) {
            YoYo.with(Techniques.Shake).duration(700).playOn(et_amount);
            BaseFunc.showMsg(mContext, getString(R.string.tips_deduct_amount));
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(et_amount.getText().toString());
        } catch (Exception e) {
            amount = 0;
        }

        if (amount < 10) {
            YoYo.with(Techniques.Shake).duration(700).playOn(et_amount);
            BaseFunc.showMsg(mContext, getString(R.string.tips_deduct_money));
            return;
        }

        if (amount > avaliable) {
            YoYo.with(Techniques.Shake).duration(700).playOn(et_amount);
            String tips = String.format(getString(R.string.tips_deduct_more_than_avaliable), df.format(amount));
            BaseFunc.showMsg(mContext, tips);
            return;
        }

        if (et_paypwd.length() == 0) {
            YoYo.with(Techniques.Shake).duration(700).playOn(et_paypwd);
            BaseFunc.showMsg(mContext, getString(R.string.hint_deposit_pwd));
            return;
        }

        sad = BaseFunc.getLoadingDlg(mContext, getString(R.string.deposit_requesting));
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
                    BaseFunc.showMsg(mContext, getString(R.string.tips_cash_success));
                    if (deductMoney != null) deductMoney.getData(member);
                    et_amount.getText().clear();
                    et_paypwd.getText().clear();
                }
            }
        }).withdraw_cash(member.member_id, member.token, aCard.card_id, amount, et_paypwd.getText().toString());

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

    @Override
    public void onDMData(DeductMoney dMoney) {
        if (dMoney != null) {
            avaliable = dMoney.available_predeposit;
        } else {
            avaliable = 0;
        }
        refreshAvaliable(avaliable);
    }

    @Override
    public void onDMError(String errcode) {
        avaliable = 0;
        refreshAvaliable(avaliable);
    }
}
