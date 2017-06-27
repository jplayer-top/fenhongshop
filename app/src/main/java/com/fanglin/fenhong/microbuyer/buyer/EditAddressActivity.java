package com.fanglin.fenhong.microbuyer.buyer;

import android.content.Intent;
import android.os.Bundle;
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
import com.fanglin.fenhong.microbuyer.base.model.Address;
import com.fanglin.fenhong.microbuyer.common.SelectAreaActivity;
import com.fanglin.fhlib.other.FHLib;
import com.fanglin.fhui.FHHintDialog;
import com.fanglin.fhui.spotsdialog.SpotsDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by Plucky on 2015/9/21.
 * 编辑收货地址
 */
public class EditAddressActivity extends BaseFragmentActivityUI {

    @ViewInject(R.id.et_name)
    EditText et_name;
    @ViewInject(R.id.LName)
    LinearLayout LName;
    @ViewInject(R.id.et_mob_phone)
    EditText et_mob_phone;
    @ViewInject(R.id.LPhone)
    LinearLayout LPhone;
    @ViewInject(R.id.et_cert_name)
    EditText et_cert_name;
    @ViewInject(R.id.et_cert_num)
    EditText et_cert_num;
    @ViewInject(R.id.tv_areainfo)
    TextView tv_areainfo;
    @ViewInject(R.id.et_address)
    EditText et_address;
    @ViewInject(R.id.LAddr)
    LinearLayout LAddr;
    @ViewInject(R.id.LArea)
    LinearLayout LArea;
    @ViewInject(R.id.LCertName)
    LinearLayout LCertName;
    @ViewInject(R.id.LCertNO)
    LinearLayout LCertNO;

    @ViewInject(R.id.LDefault)
    LinearLayout LDefault;
    @ViewInject(R.id.tv_check)
    TextView tv_check;
    Class requestAddrActivity;
    private Address address;
    private String address_id;
    private String area_id, city_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_editaddress, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);

        /** 此段有待优化*/
        if (getIntent().hasExtra("EDIT")) {
            //解析来源  新建地址
            try {
                requestAddrActivity = Class.forName(getIntent().getStringExtra("EDIT"));
            } catch (Exception e) {
                requestAddrActivity = null;
            }
        } else {
            //解析来源  新建地址
            try {
                requestAddrActivity = Class.forName(getIntent().getStringExtra("VAL"));
            } catch (Exception e) {
                requestAddrActivity = null;
            }
        }

        /** 解析Address*/
        try {
            address = new Gson().fromJson(getIntent().getStringExtra("VAL"), Address.class);
        } catch (Exception e) {
            address = null;
        }


        initView();
    }

    private void initView() {
        BaseFunc.setFont(LArea);
        BaseFunc.setFont(LDefault);

        if (address != null) {
            setHeadTitle(R.string.edit_address);
            enableTvMore(R.string.delete, false);

            et_name.setText(address.name);
            et_mob_phone.setText(address.mob_phone);
            et_cert_name.setText(address.cert_name);
            et_cert_num.setText(address.cert_num);
            tv_areainfo.setText(address.area_info);
            et_address.setText(address.address);
            address_id = address.address_id;
            area_id = address.area_id;
            city_id = address.city_id;
            if (TextUtils.equals("1", address.is_default)) {
                tv_check.setVisibility(View.VISIBLE);
            } else {
                tv_check.setVisibility(View.GONE);
            }
        } else {
            tv_check.setVisibility(View.GONE);
            setHeadTitle(R.string.insert_address);
        }
    }

    @OnClick(value = {R.id.LArea, R.id.tv_save, R.id.LDefault})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.LArea:
                BaseFunc.gotoActivity4Result(this, SelectAreaActivity.class, this.getClass().getName(), 250);
                break;
            case R.id.tv_save:
                doSave();
                break;
            case R.id.LDefault:
                if (tv_check.getVisibility() == View.VISIBLE) {
                    return;
                }
                tv_check.setVisibility(View.VISIBLE);
                setDefault();
                break;
        }
    }

    private Address isValid() {
        if (et_name.length() == 0) {
            YoYo.with(Techniques.Shake).duration(700).playOn(LName);
            BaseFunc.showMsg(mContext, getString(R.string.hint_receiver));
            return null;
        }
        if (et_mob_phone.length() == 0 || !FHLib.isMobileNO(et_mob_phone.getText().toString())) {
            YoYo.with(Techniques.Shake).duration(700).playOn(LPhone);
            BaseFunc.showMsg(mContext, getString(R.string.tips_input_phoneNum));
            return null;
        }

        if (tv_areainfo.length() == 0) {
            YoYo.with(Techniques.Shake).duration(700).playOn(LArea);
            BaseFunc.showMsg(mContext, getString(R.string.hint_area_info));
            return null;
        }

        if (et_address.length() == 0) {
            YoYo.with(Techniques.Shake).duration(700).playOn(LAddr);
            BaseFunc.showMsg(mContext, getString(R.string.hint_area_info));
            return null;
        }

        /** 如果填了真实信息则需要对信息进行校验*/
        if (et_cert_name.length() > 0) {
            if (!TextUtils.equals(et_name.getText().toString(), et_cert_name.getText().toString())) {
                YoYo.with(Techniques.Shake).duration(700).playOn(LName);
                YoYo.with(Techniques.Shake).duration(700).playOn(LCertName);
                BaseFunc.showMsg(mContext, getString(R.string.hint_name_not_equal));
                return null;
            }

            if (et_cert_num.length() == 0) {
                YoYo.with(Techniques.Shake).duration(700).playOn(LCertNO);
                BaseFunc.showMsg(mContext, getString(R.string.hint_certno_empty));
                return null;
            }
        }

        /** 身份证如果输入，则要保证在18位*/
        if (et_cert_num.length() > 0) {
            if (et_cert_num.length() < 18) {
                YoYo.with(Techniques.Shake).duration(700).playOn(LCertNO);
                BaseFunc.showMsg(mContext, getString(R.string.hint_certno_invalid));
                return null;
            }
        }

        Address addr = new Address();
        addr.name = et_name.getText().toString();
        addr.mob_phone = et_mob_phone.getText().toString();
        addr.cert_name = et_cert_name.getText().toString();
        addr.cert_num = et_cert_num.getText().toString();
        addr.address = et_address.getText().toString();
        addr.area_info = tv_areainfo.getText().toString();

        addr.mobile = addr.mob_phone;//蛋疼的后端参数设计

        if (address != null) {
            addr.address_id = address.address_id;
            addr.area_id = area_id;
            addr.city_id = city_id;
            addr.is_default = tv_check.getVisibility() == View.VISIBLE ? "1" : "0";
        } else {
            addr.is_default = "1";
            addr.area_id = area_id;
            addr.city_id = city_id;
        }
        return addr;
    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();
        confirmDelete();
    }

    private void doSave() {
        if (member == null) return;
        final Address addr = isValid();
        if (addr == null) return;
        final SpotsDialog sd = BaseFunc.getLoadingDlg(mContext, getString(R.string.updating));
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {
                sd.show();
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                sd.dismiss();
                if (isSuccess) {
                    BaseFunc.showMsg(mContext, getString(R.string.update_success));
                    if (requestAddrActivity != null) {
                        Intent i = new Intent(mContext, requestAddrActivity);
                        i.putExtra("VAL", new Gson().toJson(addr));
                        setResult(RESULT_OK, i);
                        finish();
                    } else {
                        finish();
                    }
                }
            }
        }).set_address(member.member_id, member.token, addr);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        if (resultCode == RESULT_OK && requestCode == 250) {
            try {
                String[] pid = new Gson().fromJson(data.getStringExtra("VAL"), new TypeToken<String[]>() {
                }.getType());
                tv_areainfo.setText(pid[3]);
                area_id = pid[2];
                city_id = pid[1];

            } catch (Exception e) {
                area_id = null;
                city_id = null;
            }
        }
    }

    private void confirmDelete() {
        final FHHintDialog fhd = new FHHintDialog(EditAddressActivity.this);
        fhd.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
                doDelete();
            }
        });
        fhd.setTvContent(getString(R.string.hint_delete));
        fhd.show();
    }

    private void doDelete() {
        if (member == null) return;
        final SpotsDialog sd = BaseFunc.getLoadingDlg(mContext, getString(R.string.deleting));
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {
                sd.show();
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                sd.dismiss();
                if (isSuccess) {
                    BaseFunc.showMsg(mContext, getString(R.string.delete_success));
                    finish();
                }
            }
        }).del_address(address_id, member.member_id, member.token);
    }

    private void setDefault() {
        if (member == null) return;
        if (address_id == null) return;
        final Address addr = new Address();
        addr.address_id = address_id;
        addr.is_default = "1";
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    //成功
                    tv_check.setVisibility(View.VISIBLE);
                    address.is_default = "1";
                    BaseFunc.showMsg(mContext, getString(R.string.op_success));
                }
            }
        }).set_address(member.member_id, member.token, addr);
    }
}
