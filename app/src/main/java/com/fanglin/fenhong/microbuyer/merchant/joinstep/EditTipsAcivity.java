package com.fanglin.fenhong.microbuyer.merchant.joinstep;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.CompanyInfo;
import com.fanglin.fenhong.microbuyer.base.model.EditTipsEntity;
import com.fanglin.fenhong.microbuyer.base.model.StoreInfo;
import com.fanglin.fhlib.other.FHLib;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 作者： Created by Plucky on 15-9-30.
 */
public class EditTipsAcivity extends BaseFragmentActivityUI {

    @ViewInject(R.id.tv_tips)
    TextView tv_tips;
    @ViewInject(R.id.ed_content)
    EditText ed_content;

    EditTipsEntity entity;
    CompanyInfo companyInfo;
    StoreInfo storeInfo;
    Class lastActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_edit_tips, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);

        try {
            String val = getIntent().getStringExtra("VAL");
            entity = new Gson().fromJson(val, EditTipsEntity.class);
            lastActivity = Class.forName(entity.lastActivity);
        } catch (Exception e) {
            entity = null;
        }

        if (entity == null) {
            BaseFunc.showMsg(mContext, getString(R.string.invalid_data));
            finish();
            return;
        }
        parseIntent();
    }


    private void parseIntent() {
        if (entity.isCompanyInfo) {
            /** 表示更新公司信息*/
            companyInfo = new CompanyInfo(mContext);
            companyInfo.setModelCallBack(new CompanyInfo.CompanyInfoCallBack() {
                @Override
                public void onSuccess(CompanyInfo info) {
                    BaseFunc.showMsg(mContext, getString(R.string.op_success));
                    if (lastActivity != null) {
                        Intent i = new Intent(mContext, lastActivity);
                        i.putExtra("VAL", ed_content.getText().toString());
                        setResult(RESULT_OK, i);
                    }
                    finish();
                }

                @Override
                public void onError(String errcode) {

                }
            });
        } else {
            storeInfo = new StoreInfo();
            storeInfo.setModelCallBack(new StoreInfo.StoreInfoModeCallBack() {
                @Override
                public void onSuccess(StoreInfo info) {
                    BaseFunc.showMsg(mContext, getString(R.string.op_success));
                    if (lastActivity != null) {
                        Intent i = new Intent(mContext, lastActivity);
                        i.putExtra("VAL", ed_content.getText().toString());
                        setResult(RESULT_OK, i);
                    }
                    finish();
                }

                @Override
                public void onError(String errcode) {

                }
            });
        }

        enableIvMore(R.string.save);
        setHeadTitle(getString(R.string.merchantin_tips_title_prefix) + entity.field);

        ed_content.setHint(getString(R.string.merchantin_tips_edit_hint) + entity.field);
        ed_content.setText(entity.content);
        if (TextUtils.isEmpty(entity.tips)) {
            tv_tips.setVisibility(View.GONE);
        } else {
            tv_tips.setVisibility(View.VISIBLE);
            tv_tips.setText(entity.tips);
        }
        ed_content.setInputType(InputType.TYPE_CLASS_TEXT);
        switch (entity.type) {
            case 1://编辑公司名称

                break;
            case 2://编辑公司详细地址

                break;
            case 3://编辑公司联系人

                break;
            case 4://编辑公司手机号码
                ed_content.setInputType(InputType.TYPE_CLASS_PHONE);
                InputFilter[] filter = {new InputFilter.LengthFilter(11)};
                ed_content.setFilters(filter);
                break;
            case 5://编辑公司邮件
                ed_content.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case 6://编辑公司身份证
                InputFilter[] filters = {new InputFilter.LengthFilter(18)};
                ed_content.setFilters(filters);
                break;
            case 7:
                break;
            case 8:
                break;
        }
    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();
        if (ed_content.getText().length() == 0) {
            YoYo.with(Techniques.Shake).duration(700).playOn(ed_content);
            BaseFunc.showMsg(mContext, ed_content.getHint().toString());
            return;
        }
        switch (entity.type) {
            case 1:
                companyInfo.company_name = ed_content.getText().toString();
                break;
            case 2:
                companyInfo.company_address_detail = ed_content.getText().toString();
                break;
            case 3:
                companyInfo.contacts_name = ed_content.getText().toString();
                break;
            case 4:
                companyInfo.contacts_phone = ed_content.getText().toString();
                if (!FHLib.isMobileNO(companyInfo.contacts_phone)) {
                    YoYo.with(Techniques.Shake).duration(700).playOn(ed_content);
                    BaseFunc.showMsg(mContext, entity.field + getString(R.string.invalid_format));
                    return;
                }
                break;
            case 5:
                companyInfo.contacts_email = ed_content.getText().toString();
                if (!FHLib.isEmail(companyInfo.contacts_email)) {
                    YoYo.with(Techniques.Shake).duration(700).playOn(ed_content);
                    BaseFunc.showMsg(mContext, entity.field + getString(R.string.invalid_format));
                    return;
                }
                break;
            case 6:
                companyInfo.identity_code = ed_content.getText().toString();
                if (companyInfo.identity_code.length() < 15) {
                    YoYo.with(Techniques.Shake).duration(700).playOn(ed_content);
                    BaseFunc.showMsg(mContext, entity.field + getString(R.string.invalid_format));
                    return;
                }
                break;
            case 7:
                storeInfo.seller_name = ed_content.getText().toString();
                break;
            case 8:
                storeInfo.store_name = ed_content.getText().toString();
                break;
        }

        if (entity.isCompanyInfo) {
            companyInfo.update(member, null);
        } else {
            storeInfo.update(member);
        }

    }
}
