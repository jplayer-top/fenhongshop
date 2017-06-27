package com.fanglin.fenhong.microbuyer.dutyfree;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.DutyfreeBO;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyAddress;
import com.fanglin.fenhong.microbuyer.common.SelectAreaActivity;
import com.fanglin.fhlib.other.FHLib;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/17-上午9:40.
 * 功能描述: 极速免税店 新建地址
 */
public class DutyNewAddrsActivity extends BaseFragmentActivityUI {

    @ViewInject(R.id.etName)
    EditText etName;
    @ViewInject(R.id.etMobile)
    EditText etMobile;
    @ViewInject(R.id.tvArea)
    TextView tvArea;
    @ViewInject(R.id.etAddress)
    EditText etAddress;
    @ViewInject(R.id.tvSubmit)
    TextView tvSubmit;

    DutyAddress address;
    String cartID;
    //省市区
    String[] areaIds = new String[]{"", "", ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        redTop = true;
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_dutyfree_newaddrs, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        cartID = getIntent().getStringExtra("CARTID");
        try {
            String val = getIntent().getStringExtra("ADDRESS");
            address = new Gson().fromJson(val, DutyAddress.class);
        } catch (Exception e) {
            address = null;
        }
        initView();
    }

    private void initView() {
        if (address == null) {
            tvHead.setText("新建收货地址");
        } else {
            tvHead.setText("编辑收货地址");
            etName.setText(address.getTrue_name());
            etMobile.setText(address.getMobile());
            tvArea.setText(address.getArea_info());
            etAddress.setText(address.getAddress());
        }
    }

    @OnClick(value = {R.id.tvSubmit, R.id.tvArea})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tvSubmit:
                if (member == null) return;
                if (address == null) {
                    doAdd();
                } else {
                    doUpdate();
                }
                break;
            case R.id.tvArea:
                BaseFunc.gotoActivity4Result(this, SelectAreaActivity.class, this.getClass().getName(), 250);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        if (resultCode == RESULT_OK && requestCode == 250) {
            try {
                String[] pid = new Gson().fromJson(data.getStringExtra("VAL"), new TypeToken<String[]>() {
                }.getType());
                tvArea.setText(pid[3]);
                areaIds[0] = pid[0];
                areaIds[1] = pid[1];
                areaIds[2] = pid[2];

                if (address != null) {
                    address.setProvince_id(areaIds[0]);
                    address.setCity_id(areaIds[1]);
                    address.setArea_id(areaIds[2]);
                }
            } catch (Exception e) {
                areaIds = new String[]{"", "", ""};
            }
        }
    }

    private void doAdd() {
        String name = etName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            BaseFunc.showMsg(mContext, getString(R.string.hint_receiver));
            return;
        }
        String mobile = etMobile.getText().toString();
        if (TextUtils.isEmpty(mobile) || !FHLib.isMobileNO(mobile)) {
            BaseFunc.showMsg(mContext, getString(R.string.tips_input_phoneNum));
            return;
        }
        String area = tvArea.getText().toString();
        if (TextUtils.isEmpty(area)) {
            BaseFunc.showMsg(mContext, getString(R.string.hint_area_info));
            return;
        }
        String addrs = etAddress.getText().toString();
        if (TextUtils.isEmpty(addrs)) {
            BaseFunc.showMsg(mContext, "请输入详细地址!");
            return;
        }
        final DutyAddress addr = new DutyAddress();
        addr.setTrue_name(name);
        addr.setMobile(mobile);
        addr.setProvince_id(areaIds[0]);
        addr.setCity_id(areaIds[1]);
        addr.setArea_id(areaIds[2]);
        addr.setArea_info(area);
        addr.setAddress(addrs);
        new DutyfreeBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    if (!TextUtils.isEmpty(cartID)) {
                        addr.setAddress_id(data);
                        CartCheckCache.bindAddrOfGoods(addr, cartID);
                    }
                    finish();
                }
            }
        }).addAddr(member, addr);
    }

    private void doUpdate() {
        if (address == null) return;
        String name = etName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            BaseFunc.showMsg(mContext, getString(R.string.hint_receiver));
            return;
        }
        String mobile = etMobile.getText().toString();
        if (TextUtils.isEmpty(mobile) || !FHLib.isMobileNO(mobile)) {
            BaseFunc.showMsg(mContext, getString(R.string.tips_input_phoneNum));
            return;
        }
        String area = tvArea.getText().toString();
        if (TextUtils.isEmpty(area)) {
            BaseFunc.showMsg(mContext, getString(R.string.hint_area_info));
            return;
        }
        String addrs = etAddress.getText().toString();
        if (TextUtils.isEmpty(addrs)) {
            BaseFunc.showMsg(mContext, "请输入详细地址!");
            return;
        }

        address.setTrue_name(name);
        address.setMobile(mobile);
        address.setArea_info(area);
        address.setAddress(addrs);

        new DutyfreeBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    finish();
                }
            }
        }).editAddr(member, address);
    }
}
