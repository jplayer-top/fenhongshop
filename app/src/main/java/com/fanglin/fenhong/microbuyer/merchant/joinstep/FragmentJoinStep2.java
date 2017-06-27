package com.fanglin.fenhong.microbuyer.merchant.joinstep;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.imagebrowser.FileUtils;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragment;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.CompanyInfo;
import com.fanglin.fenhong.microbuyer.base.model.EditTipsEntity;
import com.fanglin.fhlib.other.FHLib;
import com.fanglin.fenhong.microbuyer.common.FHBrowserActivity;
import com.fanglin.fenhong.microbuyer.common.SelectAreaActivity;
import com.fanglin.fhlib.ypyun.UpyunUploader;
import com.fanglin.fhui.FHHintDialog;
import com.fanglin.fhui.spotsdialog.SpotsDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 作者： Created by Plucky on 15-9-30.
 * 供应商审核第一步--检查当前到达的步骤
 */
public class FragmentJoinStep2 extends BaseFragment implements CompanyInfo.CompanyInfoCallBack {
    View view;
    @ViewInject(R.id.tv_company)
    TextView tv_company;
    @ViewInject(R.id.tv_addrdtl)
    TextView tv_addrdtl;
    @ViewInject(R.id.tv_contact)
    TextView tv_contact;
    @ViewInject(R.id.tv_phone)
    TextView tv_phone;
    @ViewInject(R.id.tv_eml)
    TextView tv_eml;
    @ViewInject(R.id.iv_license)
    ImageView iv_license;
    @ViewInject(R.id.iv_legal)
    ImageView iv_legal;
    @ViewInject(R.id.tv_card)
    TextView tv_card;
    @ViewInject(R.id.tv_area)
    TextView tv_area;

    @ViewInject(R.id.Lcompany)
    LinearLayout Lcompany;
    @ViewInject(R.id.Larea)
    LinearLayout Larea;
    @ViewInject(R.id.LAddrdtl)
    LinearLayout LAddrdtl;
    @ViewInject(R.id.Lcontact)
    LinearLayout Lcontact;
    @ViewInject(R.id.Lphone)
    LinearLayout Lphone;
    @ViewInject(R.id.Leml)
    LinearLayout Leml;
    @ViewInject(R.id.Lcard)
    LinearLayout Lcard;
    @ViewInject(R.id.Llicence)
    LinearLayout Llicence;
    @ViewInject(R.id.Llegal)
    LinearLayout Llegal;

    @ViewInject(R.id.LHold)
    LinearLayout LHold;
    @ViewInject(R.id.ck)
    CheckBox ck;
    @ViewInject(R.id.LButton)
    LinearLayout LButton;

    CompanyInfo companyInfo;


    /* 要执行的操作
     * -1 无任何操作
     * 0 submit
     * 1 上传图片--营业执照 iv_license
     * 2 上传图片--身份证复印件 iv_legal
     * 3 选择地区
     * */ int updateField = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(act, R.layout.fragment_joinstep2, null);
        ViewUtils.inject(this, view);
        companyInfo = new CompanyInfo(act);
        companyInfo.setModelCallBack(this);
        FHLib.EnableViewGroup(LHold, false);
        LButton.setVisibility(View.GONE);
    }

    public void getData() {
        if (companyInfo != null) companyInfo.getData(member);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }


    @Override
    public void onSuccess(CompanyInfo info) {
        if (info != null) {
            tv_company.setText(info.company_name);
            tv_area.setText(info.company_address);
            tv_addrdtl.setText(info.company_address_detail);
            tv_contact.setText(info.contacts_name);
            tv_phone.setText(info.contacts_phone);
            tv_eml.setText(info.contacts_email);
            tv_card.setText(info.identity_code);

            if (info.business_licence_number_electronic != null && Patterns.WEB_URL.matcher(info.business_licence_number_electronic).matches()) {
                iv_license.setVisibility(View.VISIBLE);
                new FHImageViewUtil(iv_license).setImageURI(info.business_licence_number_electronic, FHImageViewUtil.SHOWTYPE.DEFAULT);
                iv_license.setTag(info.business_licence_number_electronic);// 为了判断有没有上传图片
            } else {
                iv_license.setVisibility(View.GONE);
            }

            if (info.identity_code_electronic != null && Patterns.WEB_URL.matcher(info.identity_code_electronic).matches()) {
                iv_legal.setVisibility(View.VISIBLE);
                new FHImageViewUtil(iv_legal).setImageURI(info.identity_code_electronic, FHImageViewUtil.SHOWTYPE.DEFAULT);
                iv_legal.setTag(info.identity_code_electronic);// 为了判断有没有上传图片
            } else {
                iv_legal.setVisibility(View.GONE);
            }

            if (TextUtils.equals("1", info.step)) {
                FHLib.EnableViewGroup(LHold, true);
                LButton.setVisibility(View.VISIBLE);
            } else {
                FHLib.EnableViewGroup(LHold, false);
                LButton.setVisibility(View.GONE);
            }
        } else {
            if (updateField == 0) {
                ((JoinStepActivity) act).changePage(2);
            }
            if (updateField == 1) {
                if (companyInfo.business_licence_number_electronic != null) {
                    iv_license.setVisibility(View.VISIBLE);
                    iv_license.setTag(companyInfo.business_licence_number_electronic);
                }
            }
            if (updateField == 2) {
                if (companyInfo.identity_code_electronic != null) {
                    iv_legal.setVisibility(View.VISIBLE);
                    iv_legal.setTag(companyInfo.identity_code_electronic);
                }
            }
            if (updateField == 3) {
                if (companyInfo.company_address != null) {
                    tv_area.setText(companyInfo.company_address);
                }
            }

            BaseFunc.showMsg(act, getString(R.string.op_success));
        }


    }

    @Override
    public void onError(String errcode) {

    }

    @OnClick(value = {R.id.Lcompany, R.id.Larea, R.id.LAddrdtl, R.id.Lcontact, R.id.Lphone, R.id.Leml, R.id.iv_upload1, R.id.iv_license, R.id.Lcard, R.id.iv_upload2, R.id.iv_legal, R.id.tv_policy, R.id.btn_next})
    public void onViewClick(View v) {
        String lbl = "";
        if (v instanceof LinearLayout)
            lbl = ((TextView) ((ViewGroup) v).getChildAt(0)).getText().toString();

        String lastClassName = this.getClass().getName();//记录从哪里来
        EditTipsEntity entity = new EditTipsEntity();
        entity.lastActivity = lastClassName;
        entity.isCompanyInfo = true;
        switch (v.getId()) {
            case R.id.Lcompany:
                entity.type = 1;
                entity.field = lbl;//要编辑的内容
                entity.content = tv_company.getText().toString();//已经输入的内容
                entity.tips = null;//tips
                BaseFunc.gotoActivity4Result(this, EditTipsAcivity.class, entity.getString(), entity.type);
                break;
            case R.id.Larea:
                BaseFunc.gotoActivity4Result(this, SelectAreaActivity.class, lastClassName, 0x100);
                break;
            case R.id.LAddrdtl:
                entity.type = 2;
                entity.field = lbl;//要编辑的内容
                entity.content = tv_addrdtl.getText().toString();//已经输入的内容
                entity.tips = null;//tips
                BaseFunc.gotoActivity4Result(this, EditTipsAcivity.class, entity.getString(), entity.type);
                break;
            case R.id.Lcontact:
                entity.type = 3;
                entity.field = lbl;//要编辑的内容
                entity.content = tv_contact.getText().toString();//已经输入的内容
                entity.tips = null;//tips
                BaseFunc.gotoActivity4Result(this, EditTipsAcivity.class, entity.getString(), entity.type);
                break;
            case R.id.Lphone:
                entity.type = 4;
                entity.field = lbl;//要编辑的内容
                entity.content = tv_phone.getText().toString();//已经输入的内容
                entity.tips = null;//tips
                BaseFunc.gotoActivity4Result(this, EditTipsAcivity.class, entity.getString(), entity.type);
                break;
            case R.id.Leml:
                entity.type = 5;
                entity.field = lbl;//要编辑的内容
                entity.content = tv_eml.getText().toString();//已经输入的内容
                entity.tips = null;//tips
                BaseFunc.gotoActivity4Result(this, EditTipsAcivity.class, entity.getString(), entity.type);
                break;
            case R.id.iv_upload1:
                BaseFunc.selectPictures(this, 0x101, true, 0, 0, 0, 0);
                break;
            case R.id.iv_license:
                if (iv_license.getTag() == null) return;
                FileUtils.BrowserOpenS(act, null, iv_license.getTag().toString());
                break;
            case R.id.Lcard:
                entity.type = 6;
                entity.field = lbl;//要编辑的内容
                entity.content = tv_card.getText().toString();//已经输入的内容
                entity.tips = null;//tips
                BaseFunc.gotoActivity4Result(this, EditTipsAcivity.class, entity.getString(), entity.type);
                break;
            case R.id.iv_upload2:
                BaseFunc.selectPictures(this, 0x102, true, 0, 0, 0, 0);
                break;
            case R.id.iv_legal:
                if (iv_legal.getTag() == null) return;
                FileUtils.BrowserOpenS(act, null, iv_legal.getTag().toString());
                break;
            case R.id.tv_policy:
                BaseFunc.gotoActivity(act, FHBrowserActivity.class, BaseVar.MERCHANTPOLICY);
                break;
            case R.id.btn_next:
                submit();
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case 0x100:
                try {
                    String[] pid = new Gson().fromJson(data.getStringExtra("VAL"), new TypeToken<String[]>() {
                    }.getType());
                    companyInfo.company_address = pid[3];
                    updateField = 3;
                    companyInfo.update(member, pid[0]);
                } catch (Exception e) {
                    //
                }
                break;
            case 0x101:
                try {
                    Uri uri = data.getParcelableExtra("VAL");
                    upload(uri, iv_license);
                } catch (Exception e) {
                    //
                }
                break;
            case 0x102:
                try {
                    Uri uri = data.getParcelableExtra("VAL");
                    upload(uri, iv_legal);
                } catch (Exception e) {
                    //
                }
                break;
            case 1:
                tv_company.setText(data.getStringExtra("VAL"));
                break;
            case 2:
                tv_addrdtl.setText(data.getStringExtra("VAL"));
                break;
            case 3:
                tv_contact.setText(data.getStringExtra("VAL"));
                break;
            case 4:
                tv_phone.setText(data.getStringExtra("VAL"));
                break;
            case 5:
                tv_eml.setText(data.getStringExtra("VAL"));
                break;
            case 6:
                tv_card.setText(data.getStringExtra("VAL"));
                break;
        }
    }

    private void upload(Uri uri, final ImageView sdv) {
        if (member == null) return;
        final SpotsDialog dlg = BaseFunc.getLoadingDlg(act, getString(R.string.picture_uploading));
        new UpyunUploader(member.member_id).setUploadFile(uri.getPath()).setUpYunCallBack(new UpyunUploader.UpYunCallBack() {
            @Override
            public void startLoading() {
                dlg.show();
            }

            @Override
            public void endLoading(boolean isSuccess, String data) {
                dlg.dismiss();
                if (isSuccess) {
                    new FHImageViewUtil(sdv).setImageURI(data, FHImageViewUtil.SHOWTYPE.DEFAULT);
                    if (sdv == iv_license) {
                        companyInfo.business_licence_number_electronic = data;
                        updateField = 1;
                    } else {
                        companyInfo.identity_code_electronic = data;
                        updateField = 2;
                    }
                    companyInfo.update(member, null);
                } else {
                    BaseFunc.showMsg(act, getString(R.string.op_error));
                }
            }
        }).upload();
    }

    private void submit() {
        if (member == null) return;
        if (!ck.isChecked()) {
            BaseFunc.showMsg(act, getString(R.string.policy_merchantin));
            return;
        }

        if (tv_company.getText().length() == 0) {
            BaseFunc.showMsg(act, (getString(R.string.merchantin_company) + getString(R.string.content_isEmpty)));
            YoYo.with(Techniques.Shake).duration(700).playOn(Lcompany);
            return;
        }

        if (tv_area.getText().length() == 0) {
            BaseFunc.showMsg(act, (getString(R.string.merchantin_addr) + getString(R.string.content_isEmpty)));
            YoYo.with(Techniques.Shake).duration(700).playOn(Larea);
            return;
        }

        if (tv_addrdtl.getText().length() == 0) {
            BaseFunc.showMsg(act, (getString(R.string.merchantin_addrdtl) + getString(R.string.content_isEmpty)));
            YoYo.with(Techniques.Shake).duration(700).playOn(LAddrdtl);
            return;
        }

        if (tv_contact.getText().length() == 0) {
            BaseFunc.showMsg(act, (getString(R.string.merchantin_contact) + getString(R.string.content_isEmpty)));
            YoYo.with(Techniques.Shake).duration(700).playOn(Lcontact);
            return;
        }

        if (tv_phone.getText().length() == 0) {
            BaseFunc.showMsg(act, (getString(R.string.merchantin_tel) + getString(R.string.content_isEmpty)));
            YoYo.with(Techniques.Shake).duration(700).playOn(Lphone);
            return;
        }

        if (tv_eml.getText().length() == 0) {
            BaseFunc.showMsg(act, (getString(R.string.merchantin_eml) + getString(R.string.content_isEmpty)));
            YoYo.with(Techniques.Shake).duration(700).playOn(Leml);
            return;
        }

        if (iv_license.getTag() == null) {
            BaseFunc.showMsg(act, (getString(R.string.tips_upload) + getString(R.string.merchantin_license)));
            YoYo.with(Techniques.Shake).duration(700).playOn(Llicence);
            return;
        }

        if (tv_card.getText().length() == 0) {
            BaseFunc.showMsg(act, (getString(R.string.merchantin_IDNO) + getString(R.string.content_isEmpty)));
            YoYo.with(Techniques.Shake).duration(700).playOn(Lcard);
            return;
        }

        if (iv_legal.getTag() == null) {
            BaseFunc.showMsg(act, (getString(R.string.tips_upload) + getString(R.string.merchantin_legal_idcard)));
            YoYo.with(Techniques.Shake).duration(700).playOn(Llegal);
            return;
        }

        final FHHintDialog fhd = new FHHintDialog(act);
        fhd.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
                updateField = 0;
                companyInfo.submit(member);
            }
        });
        fhd.setTvContent(getString(R.string.tips_of_submit));
        fhd.show();
    }


}
