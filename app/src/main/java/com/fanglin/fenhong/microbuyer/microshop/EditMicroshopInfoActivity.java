package com.fanglin.fenhong.microbuyer.microshop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.fanglin.fenhong.microbuyer.base.baseui.imagebrowser.FileUtils;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.event.MicroshopInfoEvent;
import com.fanglin.fenhong.microbuyer.base.model.MicroshopInfo;
import com.fanglin.fhlib.ypyun.UpyunUploader;
import com.fanglin.fhui.CircleImageView;
import com.fanglin.fhui.FHHintDialog;
import com.fanglin.fhui.spotsdialog.SpotsDialog;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import de.greenrobot.event.EventBus;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/6-下午4:02.
 * 功能描述:编辑微店信息页面
 */
public class EditMicroshopInfoActivity extends BaseFragmentActivityUI implements MicroshopInfo.MSIModelCallBack, APIUtil.FHAPICallBack, TextWatcher {
    @ViewInject(R.id.circleImageView)
    CircleImageView circleImageView;
    @ViewInject(R.id.tvAccount)
    TextView tvAccount;
    @ViewInject(R.id.LName)
    LinearLayout LName;
    @ViewInject(R.id.etName)
    EditText etName;
    @ViewInject(R.id.LScope)
    LinearLayout LScope;
    @ViewInject(R.id.etScope)
    EditText etScope;
    @ViewInject(R.id.tvHeadIcon)
    TextView tvHeadIcon;

    MicroshopInfo microshopInfoRequset;
    MicroshopInfo microshopInfoData;
    String shopLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_edit_microshop_info, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        initView();
    }

    private void initView() {
        tvHeadIcon.setTypeface(iconfont);
        setHeadTitle(R.string.microshop_info);

        microshopInfoRequset = new MicroshopInfo();
        microshopInfoRequset.setModelCallBack(this);
        /**
         * 获取微店信息
         */
        microshopInfoRequset.getData(member_id);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void onMSIData(MicroshopInfo microshopInfo) {
        microshopInfoData = microshopInfo;
        if (microshopInfo != null) {
            shopLogo = microshopInfo.getShop_logo();
            enableTvMore(R.string.save, false);
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
            tvAccount.setText(member != null ? member.member_name : member_id);
            etName.setText(microshopInfo.getShop_name());
            etScope.setText(microshopInfo.getShop_scope());
            new FHImageViewUtil(circleImageView).setImageURI(shopLogo, FHImageViewUtil.SHOWTYPE.AVATAR);

            etName.addTextChangedListener(this);
            etScope.addTextChangedListener(this);
        } else {
            refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();

        if (member == null) {
            BaseFunc.gotoLogin(mContext);
            return;
        }

        if (etName.length() < 2) {
            YoYo.with(Techniques.Shake).duration(700).playOn(LName);
            BaseFunc.showMsg(mContext, getString(R.string.microshopname_less_than_2));
            return;
        }

        if (etScope.length() < 2) {
            YoYo.with(Techniques.Shake).duration(700).playOn(LScope);
            BaseFunc.showMsg(mContext, getString(R.string.microshopscope_less_than_2));
            return;
        }

        if (microshopInfoData != null) {
            microshopInfoData.setShop_logo(shopLogo);
            microshopInfoData.setShop_name(etName.getText().toString());
            microshopInfoData.setShop_scope(etScope.getText().toString());
        }

        new BaseBO().setCallBack(this).setMicroshopInfo(member.member_id,
                member.token, etName.getText().toString(), etScope.getText().toString(), shopLogo);

    }

    @Override
    public void onStart(String data) {

    }

    @Override
    public void onEnd(boolean isSuccess, String data) {
        if (isSuccess) {
            //通知Fragment微店信息获取到了
            EventBus.getDefault().post(new MicroshopInfoEvent(microshopInfoData));
            BaseFunc.showMsg(mContext, getString(R.string.microshop_info_edit_success));
            finish();
        }
    }

    private void doCrop() {
        if (member == null) return;
        BaseFunc.selectPictures(this, 0x001, true, 800, 800, 1, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case 0x001:
                try {
                    Uri uri = data.getParcelableExtra("VAL");
                    upload(uri);
                } catch (Exception e) {
                    //
                }
                break;
        }
    }

    private void upload(Uri uri) {
        if (member == null) return;
        final SpotsDialog dlg = BaseFunc.getLoadingDlg(this, getString(R.string.picture_uploading));
        new UpyunUploader(member.member_id).setUploadFile(uri.getPath()).setUpYunCallBack(new UpyunUploader.UpYunCallBack() {
            @Override
            public void startLoading() {
                dlg.show();
            }

            @Override
            public void endLoading(boolean isSuccess, String data) {
                dlg.dismiss();
                if (isSuccess) {
                    shopLogo = data;
                    new FHImageViewUtil(circleImageView).setImageURI(shopLogo, FHImageViewUtil.SHOWTYPE.AVATAR);
                    hasEdit = true;
                }
            }
        }).upload();
    }

    @OnClick(value = {R.id.LAvatar, R.id.circleImageView})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.LAvatar:
                doCrop();
                break;
            case R.id.circleImageView:
                if (BaseFunc.isValidUrl(shopLogo)) {
                    FileUtils.BrowserOpenS(this, null, shopLogo);
                } else {
                    doCrop();
                }
                break;
        }
    }


    @Override
    public void onBackClick() {
        if (hasEdit) {
            confirmTips();
        } else {
            super.onBackClick();
        }
    }

    @Override
    public void onBackPressed() {
        if (hasEdit) {
            confirmTips();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        hasEdit = true;
        if (etName.length() < 2) {
            etName.setTextColor(getResources().getColor(R.color.fh_red));
        } else {
            etName.setTextColor(getResources().getColor(R.color.color_33));
        }

        if (etScope.length() < 2) {
            etScope.setTextColor(getResources().getColor(R.color.fh_red));
        } else {
            etScope.setTextColor(getResources().getColor(R.color.color_33));
        }
    }

    private boolean hasEdit = false;

    private void confirmTips() {
        FHHintDialog fhHintDialog = new FHHintDialog(mContext);
        fhHintDialog.setTvContent(getString(R.string.microshop_info_edit_tips));
        fhHintDialog.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                finish();
            }
        });
        fhHintDialog.show();
    }
}
