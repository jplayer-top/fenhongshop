package com.fanglin.fenhong.microbuyer.microshop;

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
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.BankCard;
import com.fanglin.fhui.FHHintDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

/**
 * 作者： Created by Plucky on 15-11-1.
 * 新建或编辑银行卡
 */
public class EditBankCardActivity extends BaseFragmentActivityUI implements BankCard.BCModelCallBack {
    @ViewInject(R.id.et_bankuser)
    EditText et_bankuser;
    @ViewInject(R.id.et_idcard)
    EditText et_idcard;
    @ViewInject(R.id.LIcon)
    LinearLayout LIcon;
    @ViewInject(R.id.tv_icon)
    TextView tv_icon;
    @ViewInject(R.id.tv_cardinfo)
    TextView tv_cardinfo;
    @ViewInject(R.id.et_cardno)
    EditText et_cardno;
    LayoutSelectaBank layoutSelectaBank;
    @ViewInject(R.id.tv_desc)
    TextView tv_desc;
    private BankCard adata;
    private BankCard request;
    private String bankname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_edit_bankcard, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        try {
            adata = new Gson().fromJson(getIntent().getStringExtra("VAL"), BankCard.class);
        } catch (Exception e) {
            adata = null;
        }
        initView();
    }

    private void initView() {

        tv_desc.setText(Html.fromHtml(getString(R.string.deposit_tips_iconfont)));
        BaseFunc.setFont(tv_desc);

        BaseFunc.setFont(LIcon);
        layoutSelectaBank = new LayoutSelectaBank(mContext, null);
        layoutSelectaBank.setCallBack(new LayoutSelectaBank.onBankSelected() {
            @Override
            public void onSelected(String bankname, int index) {
                refreshBank(bankname);
                if (index == 0) {//如果是支付宝的话
                    et_cardno.setHint(getString(R.string.hint_alipay));
                } else {
                    et_cardno.setHint(getString(R.string.hint_bankcardnum));
                }
            }
        });
        if (adata != null) {
            et_bankuser.setText(adata.bank_user);
            et_idcard.setText(adata.id_card);
            refreshBank(adata.bank_name);
            et_cardno.setText(adata.bank_no);
            enableTvMore(R.string.delete, false);
            setHeadTitle(R.string.edit_bankcard);
        } else {
            refreshBank(null);
            setHeadTitle(R.string.new_bankcard);
        }

        request = new BankCard();
        request.setModelCallBack(this);
    }

    private void refreshBank(String bankname) {
        this.bankname = bankname;
        if (bankname != null) {
            tv_cardinfo.setText(bankname);
            tv_icon.setText(BaseFunc.getBankIconByName(mContext, bankname));

        } else {
            tv_cardinfo.setText(getString(R.string.select_a_bank));
            tv_icon.setText(getString(R.string.if_why));
        }
    }

    private void onSubmit() {
        if (member == null) return;
        if (et_bankuser.length() == 0) {
            BaseFunc.showMsg(mContext, getString(R.string.tips_bankuser));
            YoYo.with(Techniques.Shake).duration(700).playOn(et_bankuser);
            return;
        }
        request.bank_user = et_bankuser.getText().toString();

        if (et_idcard.length() < 15) {
            BaseFunc.showMsg(mContext, getString(R.string.tips_idcard));
            YoYo.with(Techniques.Shake).duration(700).playOn(et_idcard);
            return;
        }
        request.id_card = et_idcard.getText().toString();

        if (bankname == null) {
            BaseFunc.showMsg(mContext, getString(R.string.select_a_bank));
            YoYo.with(Techniques.Shake).duration(700).playOn(LIcon);
            return;
        }
        request.bank_name = bankname;

        if (et_cardno.length() < 5) {
            //如果是支付宝的话
            if (BaseFunc.getBandIndex(bankname) == 0) {
                BaseFunc.showMsg(mContext, getString(R.string.tips_alipay));
            } else {
                BaseFunc.showMsg(mContext, getString(R.string.tips_bankcardnum));
            }
            YoYo.with(Techniques.Shake).duration(700).playOn(et_cardno);
            return;
        }
        request.bank_no = et_cardno.getText().toString();
        if (adata != null) {
            request.card_id = adata.card_id;
        } else {
            request.if_default = "1";
        }

        request.update(member);
    }

    @OnClick(value = {R.id.btn_submit, R.id.LIcon})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                onSubmit();
                break;
            case R.id.LIcon:
                layoutSelectaBank.show();
                break;
        }
    }

    /* 银行卡增删改代理*/
    @Override
    public void onError(String errcode) {
        if (TextUtils.equals("0", errcode)) {
            BaseFunc.showMsg(mContext, getString(R.string.op_success));
            if (request.actionNum != 0) {
                Intent i = new Intent();
                request.setModelCallBack(null);
                request.setCallBack(null);
                i.putExtra("VAL", new Gson().toJson(request));
                setResult(RESULT_OK, i);
            }
            finish();
        }
    }

    @Override
    public void onList(List<BankCard> list) {

    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();
        if (member == null || adata == null) return;

        final FHHintDialog fhd = new FHHintDialog(EditBankCardActivity.this);
        fhd.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
                request.delete(member, adata.card_id);
            }
        });
        fhd.setTvContent(getString(R.string.tips_del_bankcard));
        fhd.show();
    }
}
